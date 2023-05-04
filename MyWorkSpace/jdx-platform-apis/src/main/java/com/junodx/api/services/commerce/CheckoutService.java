package com.junodx.api.services.commerce;

import com.amazonaws.event.DeliveryMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.configuration.AwsConfiguration;
import com.junodx.api.connectors.commerce.stripe.StripeClient;
import com.junodx.api.controllers.commerce.CheckoutErrorCodes;
import com.junodx.api.controllers.commerce.InventoryErrorCodes;
import com.junodx.api.controllers.exceptions.JunoErrorCodes;
import com.junodx.api.controllers.payloads.CheckoutRequestPayload;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.commerce.PaymentIntentDto;
import com.junodx.api.dto.models.commerce.PendingOrderDto;
import com.junodx.api.dto.models.commerce.PendingTransactionDto;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.*;
import com.junodx.api.models.commerce.types.PaymentMethodType;
import com.junodx.api.models.commerce.types.PaymentProcessingType;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.ZipCode;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.commerce.CheckoutRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.exceptions.*;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.inventory.InventoryService;
import com.junodx.api.services.lab.KitService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.providers.PracticeService;
import com.junodx.api.services.providers.ProviderService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CheckoutService extends ServiceBase {

    @Autowired
    private AwsConfiguration awsConfigurationProperties;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private UserService userService;

    private UserMapStructMapper userMapStructMapper;

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private ObjectMapper mapper;

    public CheckoutService(UserMapStructMapper userMapStructMapper) {
        mapper = new ObjectMapper();
        this.userMapStructMapper = userMapStructMapper;
    }

    //Used for anonymous user access
    public Checkout create(CheckoutRequestPayload pendingCheckout, boolean anonymous) throws JdxServiceException {
        User systemUser = userService.getSystemUser();
        UserDetailsImpl defaultUser = UserDetailsImpl.build(systemUser);

        return create(pendingCheckout, anonymous, defaultUser);
    }

    public Checkout create(CheckoutRequestPayload pendingCheckout, boolean anonymous, UserDetailsImpl user) {
        try {
            Checkout checkout = new Checkout();
            String userClientId = null;
            if (pendingCheckout == null)
                throw new JdxServiceException("Cannot process checkout request as the checkout object was not provided.");

            if (pendingCheckout.getCustomer() == null)
                throw new JdxServiceException(CheckoutErrorCodes.CUSTOMER_NOT_PROVIDED.code, CheckoutErrorCodes.CUSTOMER_NOT_PROVIDED.statusCode, CheckoutErrorCodes.CUSTOMER_NOT_PROVIDED.message, "Cannot create checkout for the user as the customer's data was not provided in the checkout request payload");

            if(pendingCheckout.getCustomer().getShippingAddress() == null)
                throw new JdxServiceException(CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.code, CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.statusCode, CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.message, "Shipping address not provided in checkout request");
            else if(pendingCheckout.getCustomer().getShippingAddress().getState() == null || pendingCheckout.getCustomer().getShippingAddress().getPostalCode() == null)
                throw new JdxServiceException(CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.code, CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.statusCode, CheckoutErrorCodes.SHIPPING_ADDRESS_NOT_PROVIDED.message, "State or zip code not provided in checkout request");

            //TODO refactor this for a cart with multiple items
            if(pendingCheckout.getItems() == null || pendingCheckout.getItems().size() != 1)
                throw new JdxServiceException(CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.code, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.statusCode, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.message,"Items provided in the checkout were either not provided at all or greater than the number of allowed items at this time.");

            if(pendingCheckout.getItems().get(0).getProductId() == null)
                throw new JdxServiceException(CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.code, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.statusCode, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.message, "No product ID provided for item in checkout.");

            //Let's check for product inventory availability again here to make sure that there is enough stock to fulfill before committing
            //Let's check for availability in the user's shipping region again
            ProductAvailablity availablity = null;
            try {
                availablity = productService.getProductAvailabilityWithRegion(pendingCheckout.getItems().get(0).getProductId(), new State(pendingCheckout.getCustomer().getShippingAddress().getState()), new ZipCode(pendingCheckout.getCustomer().getShippingAddress().getPostalCode()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new JdxServiceException(InventoryErrorCodes.CANNOT_FIND_AVAILABILITY_ERROR.code, InventoryErrorCodes.CANNOT_FIND_AVAILABILITY_ERROR.statusCode, InventoryErrorCodes.CANNOT_FIND_AVAILABILITY_ERROR.message, "Unable to determine if the product is available in the provided region");
            }

            if (availablity != null) {
                if (availablity.getOutOfRegion())
                    throw new JdxServiceException(InventoryErrorCodes.PRODUCT_NOT_AVAILABLE_IN_REGION.code, InventoryErrorCodes.PRODUCT_NOT_AVAILABLE_IN_REGION.statusCode, InventoryErrorCodes.PRODUCT_NOT_AVAILABLE_IN_REGION.message, "This product is not yet available in the provided region");
                if (availablity.getSoldOut())
                    throw new JdxServiceException(InventoryErrorCodes.PRODUCT_SOLD_OUT.code, InventoryErrorCodes.PRODUCT_SOLD_OUT.statusCode, InventoryErrorCodes.PRODUCT_SOLD_OUT.message, "This product is currently sold out.");
            }

            //If the checkout creation is anonymous, then the name and dob data must be present as it won't be found in the DB
            if(anonymous) {
                if(pendingCheckout.getCustomer().getFirstName() == null || pendingCheckout.getCustomer().getFirstName().equals(""))
                    throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "First name must be present for an anonymous checkout");
                if(pendingCheckout.getCustomer().getLastName() == null || pendingCheckout.getCustomer().getLastName().equals(""))
                    throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Last name must be present for an anonymous checkout");
                if(pendingCheckout.getCustomer().getDateOfBirth() == null || pendingCheckout.getCustomer().getDateOfBirth().equals(""))
                    throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Date of birth must be present for an anonymous checkout");
                if(pendingCheckout.getCustomer().getEmail() == null || pendingCheckout.getCustomer().getEmail().equals(""))
                    throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code,
                            CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode,
                            CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message,
                            "Email address was not provided and is required for an anonymous checkout");

                checkout.setLoggedInSession(false);
                if(pendingCheckout.getClientId() == null)
                    throw new JdxServiceException(JunoErrorCodes.CLIENT_ID_NOT_PROVIDED.code, JunoErrorCodes.CLIENT_ID_NOT_PROVIDED.statusCode, JunoErrorCodes.CLIENT_ID_NOT_PROVIDED.message, "Cannot perform an anonymous checkout with the clientId specified");
                else
                    userClientId = pendingCheckout.getClientId();
            } else {
                checkout.setLoggedInSession(true);
                if(pendingCheckout.getClientId() == null)
                    userClientId = user.getClientId();
                else
                    userClientId = pendingCheckout.getClientId();
            }

            if (pendingCheckout.getCustomer().getEmail() != null) {
                Optional<User> customer = userService.findOneByEmailAndClientId(pendingCheckout.getCustomer().getEmail(), userClientId);
                if (customer.isPresent()) {
                    //If the user is trying to buy anonymously but for an existing email address, then they will be rejected.
                    //There is no guarantee that the new anonymous user intended to use that email and therefore we don't want to put results to a different person
                    if(anonymous) {
                        throw new JdxServiceException(CheckoutErrorCodes.CANNOT_CREATE_CHECKOUT_FOR_EXISTING_UNAUTHENTICATED_USER.code,
                                CheckoutErrorCodes.CANNOT_CREATE_CHECKOUT_FOR_EXISTING_UNAUTHENTICATED_USER.statusCode,
                                CheckoutErrorCodes.CANNOT_CREATE_CHECKOUT_FOR_EXISTING_UNAUTHENTICATED_USER.message,
                                "Cannot create anonymous checkout for an existing email address. User must log in first.");
                    }

                    UserOrderDto foundCustomer = userMapStructMapper.userToUserOrderDto(customer.get());
                    //check if the customer is already having a checkout
                    Optional<Checkout> foundCheckout = checkoutRepository.findByCustomerCustomerId(foundCustomer.getId());
                    if (foundCheckout.isPresent()) {
                        //throw new JdxServiceException("Cannot create new checkout. Checkout already exists for the user.");
                        return foundCheckout.get();
                    }
                    //Update customer details here
                    if(pendingCheckout.getCustomer().getDateOfBirth() != null && pendingCheckout.getCustomer().getDateOfBirth() != foundCustomer.getDateOfBirth())
                        foundCustomer.setDateOfBirth(pendingCheckout.getCustomer().getDateOfBirth());
                    if(pendingCheckout.getCustomer().getPhone() != null && !pendingCheckout.getCustomer().getPhone().equals(foundCustomer.getPhone()))
                        foundCustomer.setPhone(pendingCheckout.getCustomer().getPhone());
                    if(pendingCheckout.getCustomer().getShippingAddress() != null && !pendingCheckout.getCustomer().getShippingAddress().equals(foundCustomer.getShippingAddress()))
                        foundCustomer.setShippingAddress(pendingCheckout.getCustomer().getShippingAddress());

                    checkout.setCustomer(foundCustomer);
                }
            }

            if(checkout.getCustomer() == null)
                checkout.setCustomer(pendingCheckout.getCustomer());

            //Ensure that we have vital information here with either an anonymous or looked up user
            if(checkout.getCustomer().getFirstName() == null || checkout.getCustomer().getFirstName().equals(""))
                throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "First name must be present for a checkout");
            if(checkout.getCustomer().getLastName() == null || checkout.getCustomer().getLastName().equals(""))
                throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Last name must be present for an anonymous checkout");
            if(checkout.getCustomer().getDateOfBirth() == null)
                throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Date of birth must be present for an anonymous checkout");
            if(checkout.getCustomer().getShippingAddress() == null)
                throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Shipping address must be present for a checkout");

            //If this is a user without a stripe account, create them in Stripe
            if (checkout.getCustomer().getStripeCustomerId() == null) {
                StripeClient stripeClient = new StripeClient();
                Customer stripeCustomer = stripeClient.createCustomer(pendingCheckout.getCustomer().getEmail());
                checkout.getCustomer().setStripeCustomerId(stripeCustomer.getId());
            }

            if(pendingCheckout.getClientId() != null)
                checkout.setClientId(pendingCheckout.getClientId());
            else
                checkout.setClientId(user.getClientId());

            checkout.setOrderNumber(checkout.generateOrderNumber());
            checkout.setPaymentMethod(pendingCheckout.getPaymentMethod());
            checkout.setWithInsurance(pendingCheckout.isWithInsurance());
            checkout.setProcessor(pendingCheckout.getProcessor());
            checkout.setTotalAmount(pendingCheckout.getTotalAmount());
            checkout.setItems(pendingCheckout.getItems());
            checkout.setCurrency(pendingCheckout.getCurrency());
            //checkout.set
            if(pendingCheckout.getProvider() != null){
                checkout.setApprovingProviderDesignated(true);
                Optional<Provider> approvingProvider = providerService.getProvider(pendingCheckout.getProvider().getId(), new String[]{});
                if(approvingProvider.isPresent())
                    checkout.setApprovingProvider(approvingProvider.get());
                else
                    checkout.setApprovingProvider(getDefaultProvider());
            } else
                checkout.setApprovingProvider(getDefaultProvider());

            if(checkout.getItems() != null && checkout.getItems().size() > 0){
                for(CheckoutLineItem item : checkout.getItems())
                    item.setCheckout(checkout);
            } else
                throw new JdxServiceException(CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.code, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.statusCode, CheckoutErrorCodes.INVALID_NUMBER_OF_CHECKOUT_ITEMS.message,"Items provided in the checkout were either not provided at all or greater than the number of allowed items at this time.");

            if(pendingCheckout.getServiceOptions() != null){
                checkout.getServiceOptions().setSelfCollected(pendingCheckout.getServiceOptions().isSelfCollected());
                checkout.getServiceOptions().setAssistedSampleCollection(pendingCheckout.getServiceOptions().isAssistedSampleCollection());
            }

            if(pendingCheckout.getFetalSexResultsPreferences() != null){
                checkout.getFetalSexResultsPreferences().setGenderTerms(pendingCheckout.getFetalSexResultsPreferences().getGenderTerms());
                checkout.getFetalSexResultsPreferences().setGenderFanfare(pendingCheckout.getFetalSexResultsPreferences().isGenderFanfare());
                checkout.getFetalSexResultsPreferences().setGenderDelegated(pendingCheckout.getFetalSexResultsPreferences().isGenderDelegated());
                checkout.getFetalSexResultsPreferences().setFstResultsDelegatedEmail(pendingCheckout.getFetalSexResultsPreferences().getFstResultsDelegatedEmail());
            }

            if (pendingCheckout.getMedicalDetails() != null) {
                checkout.setLmpDate(pendingCheckout.getMedicalDetails().getLmpDate());
                checkout.setAgreedToTerms(pendingCheckout.getMedicalDetails().isAgreedToTerms());
                checkout.setAgreedToInformedConsent(pendingCheckout.getMedicalDetails().isConsented());
                checkout.setAgreeNoTransplantNorTransfusion(pendingCheckout.getMedicalDetails().isNoTransplantNorTransfusion());
                Calendar conceptionDate = Calendar.getInstance();
                if (pendingCheckout.getMedicalDetails().getLmpDate() != null)
                    conceptionDate = pendingCheckout.getMedicalDetails().getLmpDate();
                else if (pendingCheckout.getMedicalDetails().isOverTenWeeks())
                    conceptionDate.add(Calendar.WEEK_OF_YEAR, -10);
                else if (pendingCheckout.getMedicalDetails().isOverSevenWeeks())
                    conceptionDate.add(Calendar.WEEK_OF_YEAR, -7);
                else
                    conceptionDate = null;

                if(conceptionDate == null)
                    throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Cannot compute conception date and therefore cannot create checkout");

                checkout.setConception(conceptionDate);
            } else
                throw new JdxServiceException(CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.code, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.statusCode, CheckoutErrorCodes.INSUFFICIENT_CUSTOMER_INFORMATION_PROVIDED.message, "Medical details and consent are not provided, cannot proceed with checkout");


            if (pendingCheckout.getProcessor().equals(PaymentProcessingType.STRIPE)) {
                PaymentIntentDto intent = createPaymentIntent(checkout);
                if (intent != null) {
                    checkout.setToken(intent.getClientSecret());
                    checkout.getCustomer().setStripeCustomerId(intent.getCustomerId());
                }
            }

            checkout.setMeta(buildMeta(user));

            return checkoutRepository.save(checkout);
        } catch (JdxServiceException e){
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            throw new JdxServiceException(e.getMessage());
        }
    }

    public Optional<Checkout> get(String id){
        return checkoutRepository.findById(id);
    }

    public Optional<Checkout> find(Optional<String> token){
        if(token.isPresent())
            return checkoutRepository.findCheckoutByToken(token.get());

        return Optional.empty();
    }

    public Checkout update(Checkout checkout, UserDetailsImpl user){
        Optional<Checkout> update = checkoutRepository.findById(checkout.getId());
        if(update.isPresent()){
            if(checkout.getCustomer() != null) update.get().setCustomer(checkout.getCustomer());
            if(checkout.getConception() != null) update.get().setConception(checkout.getConception());
            if(checkout.getToken() != null) update.get().setToken(checkout.getToken());
            if(checkout.getItems() != null) update.get().setItems(checkout.getItems());
            if(checkout.getCurrency() != null) update.get().setCurrency(checkout.getCurrency());
            if(checkout.getProcessor() != null) update.get().setProcessor(checkout.getProcessor());
            if(checkout.getPaymentMethod() != null) update.get().setPaymentMethod(checkout.getPaymentMethod());
            if(checkout.getTotalAmount() > 0.0f) update.get().setTotalAmount(checkout.getTotalAmount());
            update.get().setAgreedToInformedConsent(checkout.isAgreedToInformedConsent());
            update.get().setAgreedToTerms(checkout.isAgreedToTerms());

            update.get().setMeta(updateMeta(update.get().getMeta(), user));

            return checkoutRepository.save(update.get());
        }

        return null;
    }

    protected PaymentIntentDto createPaymentIntent(Checkout checkout) throws JdxServiceException {
        try {
            if(checkout == null)
                throw new JdxServiceException("Order not specified properly");

            if(checkout.getProcessor().equals(PaymentProcessingType.STRIPE)) {
                StripeClient stripe = new StripeClient();

                if(checkout.getCustomer() == null )
                    throw new JdxServiceException("Cannot create a checkout since the Customer wasn't created");

                Customer stripeCustomer = null;

                //TODO this can get really inefficient to have to call Stripe at least twice in the same API session
                //Determine if we need to create a new stripe customer or have an existing ID linked for the current buying customer
                if(checkout.getCustomer().getStripeCustomerId() == null) {
                    throw new JdxServiceException("Cannot create customer in Stripe since email is not provided.");
                } else {
                    stripeCustomer = new Customer();
                    stripeCustomer.setId(checkout.getCustomer().getStripeCustomerId());
                }

                PaymentIntent paymentIntent = stripe.createPaymentIntent(((Float) checkout.getTotalAmount()).longValue(), Currency.getInstance(checkout.getCurrency()), checkout.getPaymentMethod(), stripeCustomer);

                PaymentIntentDto paymentIntentDto = new PaymentIntentDto();

                if (paymentIntent != null) {
                    paymentIntentDto.setId(paymentIntent.getId());
                    paymentIntentDto.setAmount(paymentIntent.getAmount());
                    paymentIntentDto.setCaptureMethod(paymentIntent.getCaptureMethod());
                    paymentIntentDto.setCreatedAt(paymentIntent.getCreated());
                    paymentIntentDto.setClientSecret(paymentIntent.getClientSecret());
                    paymentIntentDto.setCurrency(paymentIntent.getCurrency());
                    paymentIntentDto.setConfirmationMethod(paymentIntent.getConfirmationMethod());
                    paymentIntentDto.setObject(paymentIntent.getObject());
                    paymentIntentDto.setLiveMode(paymentIntent.getLivemode());
                    paymentIntentDto.setPaymentMethodTypes(paymentIntent.getPaymentMethodTypes());
                    paymentIntentDto.setStatus(paymentIntent.getStatus());
                    if(stripeCustomer == null && checkout.getCustomer().getStripeCustomerId() != null)
                        paymentIntentDto.setCustomerId(checkout.getCustomer().getStripeCustomerId());
                    else
                        paymentIntentDto.setCustomerId(stripeCustomer.getId());
                }

                return paymentIntentDto;
            } else
                throw new JdxServiceException("Cannot determine payment processor to create payment intent from");

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create payment intent " + e.getMessage());
        }
    }

    public void delete(String checkoutId) throws JdxServiceException  {
        try {
            Optional<Checkout> delete = checkoutRepository.findById(checkoutId);
            if (delete.isPresent())
                checkoutRepository.delete(delete.get());
        } catch(Exception e){
            throw e;
        }
    }

    protected Provider getDefaultProvider() {
        Optional<Practice> defaultPractice = practiceService.getDefaultPractice();
        if (defaultPractice.isPresent()) {
            Optional<Provider> defaultProvider = providerService.getDefaultProviderForPractice(defaultPractice.get().getId());
            if (defaultProvider.isPresent())
                return defaultProvider.get();
        }
        return null;
    }
}
