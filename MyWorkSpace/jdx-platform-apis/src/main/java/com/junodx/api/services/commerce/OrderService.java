package com.junodx.api.services.commerce;

import com.amazonaws.services.rds.model.Timezone;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.configuration.AwsConfiguration;
import com.junodx.api.connectors.aws.sns.SnsMessageResponse;
import com.junodx.api.connectors.lims.elements.client.ElementsClient;
import com.junodx.api.connectors.lims.elements.entities.*;
import com.junodx.api.connectors.lims.services.ElementsService;
import com.junodx.api.connectors.messaging.SnsMessageHandler;
import com.junodx.api.connectors.messaging.payloads.EntityPayload;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.connectors.stripe.services.StripeService;
import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.commerce.OrderErrorCodes;
import com.junodx.api.controllers.commerce.payloads.ActivationPayload;
import com.junodx.api.controllers.commerce.payloads.ActivationResponsePayload;
import com.junodx.api.controllers.commerce.payloads.OrderCountsPayload;
import com.junodx.api.controllers.commerce.types.OrderSortType;
import com.junodx.api.controllers.exceptions.JunoErrorCodes;
import com.junodx.api.controllers.lab.LaboratoryErrorCodes;
import com.junodx.api.controllers.payloads.OrderLineItemInfo;
import com.junodx.api.controllers.payloads.SalesforceLineItemUpdateInfo;
import com.junodx.api.controllers.payloads.SalesforceOrderUpdateAccountInfo;
import com.junodx.api.controllers.payloads.SalesforceRecordChanged;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.models.auth.Preferences;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.*;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.models.commerce.types.DiscountType;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.commerce.types.OrderType;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.*;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.laboratory.*;
import com.junodx.api.models.laboratory.types.CustomerActionRequestType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.models.patient.Consent;
import com.junodx.api.models.patient.MedicalDetails;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.models.patient.Vital;
import com.junodx.api.models.patient.types.ConsentType;
import com.junodx.api.models.patient.types.VitalType;
import com.junodx.api.models.payment.PaymentProcessorProvider;
import com.junodx.api.models.payment.Transaction;
import com.junodx.api.models.payment.types.PaymentInstrumentType;
import com.junodx.api.models.payment.types.TransactionType;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.repositories.commerce.TransactionRepository;
import com.junodx.api.repositories.fulfillment.ShippingCarrierRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.inventory.InventoryService;
import com.junodx.api.services.lab.KitService;
import com.junodx.api.services.lab.LaboratoryOrderService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.lab.TestRunService;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.services.providers.PracticeService;
import com.junodx.api.services.providers.ProviderService;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService extends ServiceBase {

    @Value("${jdx.connectors.aws.orderTopic}")
    private String orderTopic;

    @Value("${jdx.connectors.aws.sns.disabled}")
    private boolean snsDisabled;

    @Autowired
    private AwsConfiguration awsConfigurationProperties;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ElementsService elementsService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private TestRunService testRunService;

    @Autowired
    private LaboratoryOrderService laboratoryOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private MailService mailService;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private PaymentProcessorProviderService paymentProcessorProviderService;

    @Autowired
    private FulfillmentProviderService fulfillmentProviderService;

    @Autowired
    private KitService kitService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    ShippingCarrierRepository shippingCarrierRepository;

    @Autowired
    OrderLineItemRepository orderLineitemRepository;



    @Value("${stripe.api.key:sk_test_51JuPUpGkZuPExSA9kA5tftxzN3NuNpOMs4q1cEUQo97ee4lcvhC2nTgPKRldqBNeU1WwyYplJzntpdGtsgzQ4Kzh00bSfpMZ2b}")
    private String apiKey;

    @Value("${stripe.webhook.secret.endpoint}")
    private String endPointSecret;

    //private static final String endPointSecret = "whsec_wZE07JFQ8CwDmKGaPvuySDqDduQHhOY9";

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private ObjectMapper mapper;

    private CommerceMapStructMapper commerceMapStructMapper;

    private UserMapStructMapper userMapStructMapper;

    public OrderService(CommerceMapStructMapper commerceMapStructMapper, UserMapStructMapper userMapStructMapper){
        this.commerceMapStructMapper = commerceMapStructMapper;
        this.userMapStructMapper = userMapStructMapper;
        mapper = new ObjectMapper();
    }

    public Page<Order> findAll(String[] includes, UserDetailsImpl user, Pageable pageable) throws JdxServiceException {
        try {
            return orderRepository.findAll(pageable);
        } catch (Exception e){
            throw new JdxServiceException("Cannot get all orders");
        }
    }

    public Optional<Order> findOneByOrderId(String id, String[] includes, UserDetailsImpl user){
        return orderRepository.findById(id);
        //commerceMapStructMapper.orderToOrderDto(order.get()));

        //return Optional.empty();
        //return Optional.of(generatedFakeOrder(includes, id, user));
    }

    public Optional<Order> findByLineItem(OrderLineItem item) throws JdxServiceException {
        try {
            return orderRepository.findOrderByLineItems(item);
        } catch(Exception e){
            throw new JdxServiceException("Cannot get order by line item");
        }
    }

    public List<Order> findOrdersByCustomer(String customerId) throws JdxServiceException {
        try {
            return orderRepository.findOrdersByCustomer_IdOrderByOrderedAt(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot find orders for customer " + customerId + " : " + e.getMessage());
        }
    }

    public void processStripeWebhookEvents(String signatureHeader, String payLoad) {
        Event event = null;
        try {
            logger.info("processing webhook: constructing event");
            event = Webhook.constructEvent(payLoad, signatureHeader, endPointSecret);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot construct event for the paymentIntent payload.");
        }
        try {
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                throw new JdxServiceException("Error while deserializing paymentIntent payload");
            }
            String eventType = event.getType();
            logger.info("processing webhook event {}",eventType);
            switch (eventType) {
                case "payment_intent.succeeded":
                    createOrder(stripeObject);
                    break;
                case "charge.refunded":
                    cancelOrder(stripeObject);
                    break;
                default:
                    logger.error("Could not process webhook event. Invalid event.");
                    throw new JdxServiceException("Invalid event type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Error while processing paymentIntent payload");
        }
    }

    private void createOrder(StripeObject stripeObject) throws JdxServiceException {
        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        if (paymentIntent.getClientSecret().isBlank()) {
            throw new JdxServiceException("client secret can not be null.");
        }

        User systemUser = userService.getSystemUser();
        UserDetailsImpl updater = UserDetailsImpl.build(systemUser);
        logger.info("processing webhook event to save order");
        saveOneFromCheckout(paymentIntent.getClientSecret(), paymentIntent, updater);
    }

    private void cancelOrder(StripeObject stripeObject) {
        //get the order from the system.
        Charge charge = (Charge) stripeObject;
        String paymentIntentId = charge.getPaymentIntent();
        if (paymentIntentId.isBlank()) {
            logger.error("Missing paymentIntent in the Webhook refund payload");
            throw new JdxServiceException("Processing refund failed. paymentIntent id can not be null or empty");
        }
        logger.info("processing webhook found paymentIntent {}", paymentIntentId);
        List<Transaction> transactions = transactionRepository.findByExternalTransactionId(paymentIntentId);
        if (transactions == null || transactions.isEmpty()) {
            logger.error("Could not found paymentIntent {} in the system.", paymentIntentId);
            throw new JdxServiceException("Processing refund failed. Could not found payment intent id");
        }
        logger.info("Found {} transactions for the paymentIntent", transactions.size());
        Order order = transactions.get(0).getOrder();
        logger.info("processing to refund for the order {}", order.getId());
        //check if it is already refunded.
        if (order.getCurrentStatus() == OrderStatusType.REFUNDED) {
            logger.error("Order {} is already marked as Refunded", order.getId());
            throw new JdxServiceException("Processing refund failed. Order is already marked as Refunded.");
        }
        //create a transaction and add it to the order
        Transaction transaction = new Transaction();
        transaction.setTransactionJson(charge.toJson());
        transaction.setExternalTransactionId(charge.getId());
        transaction.setPaymentInstrumentType(PaymentInstrumentType.CREDIT_CARD);
        transaction.setCreatedAt(Calendar.getInstance());
        transaction.setType(TransactionType.CARD_REFUNDED);
        transaction.setOrder(order);
        //TODO: set payment processor to the transaction
        order.addTransaction(transaction);
        order = updateOrderStatus(order, OrderStatusType.REFUNDED);
        /*
        OrderStatus orderStatus = new OrderStatus();
        //update the order status to refunded
        orderStatus.setOrder(order);
        orderStatus.setCurrent(true);
        orderStatus.setStatusType(OrderStatusType.REFUNDED);
        orderStatus.setUpdatedAt(Calendar.getInstance());
        order.addOrderStatus(orderStatus);
         */
        logger.info("processing to send sns message to CANCEL the order");
        sendOrderStatus(order, EventType.CANCEL);
        logger.info("order {} refunded successfully", order.getId());
    }


    public Page<Order> search(Optional<Boolean> requiresShipment,
                              Optional<Boolean> requiresRedraw,
                              Optional<Boolean> resultsAvailable,
                              Optional<Boolean> isOpen,
                              Optional<OrderStatusType> status,
                              Optional<Boolean> withInsurance,
                              Optional<String> patientId,
                              Optional<String> firstName,
                              Optional<String> lastName,
                              Optional<String> email,
                              Optional<String> xifinId,
                              Optional<String> stripeId,
                              Optional<Boolean> customerActivated,
                              Optional<String> containsProductId,
                              Optional<Boolean> containsTests,
                                   Optional<String> orderNumber,
                                   Optional<String> orderId,
                                   Optional<String> kitCode,
                                   Optional<String> sampleNumber,
                                   Optional<OrderSortType> sortBy,
                                   Optional<SortType> sortDirection,
                                   Optional<Boolean> condensed,
                                   Optional<Calendar> after,
                                   Pageable pageable) {
        Boolean oIsOpen = null;
        OrderStatusType oOrderStatusType = null;
        String oPatientId = null;
        String oFirstName = null;
        String oLastName = null;
        String oEmail = null;
        String oXifinId = null;
        String oStripeId = null;
        String oContainsProductId = null;
        Boolean oRequiresShipment = null;
        Boolean oRequiresRedraw = null;
        Boolean oResultsAvailable = null;
        Boolean oCustomerActivated = null;
        Boolean oContainsTests = null;
        Boolean oWithInsurance = null;
        String oOrderNumber = null;
        String oOrderId = null;
        String oKitCode = null;
        String oSampleNumber = null;
        Boolean oCondensed = null;
        Calendar oAfter = null;
        OrderSortType oSort = null;
        SortType oSortType = null;

        if (requiresShipment.isPresent()) oRequiresShipment = requiresShipment.get();
        if (requiresRedraw.isPresent()) oRequiresRedraw = requiresRedraw.get();
        if (resultsAvailable.isPresent()) oResultsAvailable = resultsAvailable.get();
        if (patientId.isPresent()) oPatientId = patientId.get();
        if (isOpen.isPresent()) oIsOpen = isOpen.get();
        if (withInsurance.isPresent()) oWithInsurance = withInsurance.get();
        if (customerActivated.isPresent()) oCustomerActivated = customerActivated.get();
        if (containsProductId.isPresent()) oContainsProductId = containsProductId.get();
        if (containsTests.isPresent()) oContainsTests = containsTests.get();
        if (orderNumber.isPresent()) oOrderNumber = orderNumber.get();
        if (orderId.isPresent()) oOrderId = orderId.get();
        if (kitCode.isPresent()) oKitCode = kitCode.get();
        if (sampleNumber.isPresent()) oSampleNumber = sampleNumber.get();
        if (sortBy.isPresent()) oSort= sortBy.get();
        if (sortDirection.isPresent()) oSortType = sortDirection.get();
        if (after.isPresent()) oAfter = after.get();

        if(oKitCode != null || oSampleNumber != null)
            return orderRepository.searchOnKitCodeOrSampleNumber(oKitCode, oSampleNumber, pageable);
        else
            return orderRepository.searchOrderByCreatedAtDESC(oPatientId, oFirstName, oLastName, oEmail, oXifinId, oStripeId, oAfter, oRequiresShipment, oRequiresRedraw, oResultsAvailable, oIsOpen, oWithInsurance, oCustomerActivated, oOrderNumber, oOrderId, pageable);
        /*
        if (sortBy.isPresent() && sortDirection.isPresent()) {
            switch (sortBy.get()) {
                case estimatedToBeAvailableAt:
                    if (sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByEstimatedToBeAvailableAtASC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByEstimatedToBeAvailableAtDSC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
                case firstAvailableAt:
                    if (sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByFirstAvailableAtASC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByFirstAvailableAtDSC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
                default:
                    if (sortDirection.get().equals(SortType.ASC))
                        return testReportRepository.searchOrderByCompletedAtASC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
                    else
                        return testReportRepository.searchOrderByCompletedAtDSC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);
            }
        } else
            return testReportRepository.searchOrderByCompletedAtDSC(oType, oSignedOut, oPatientId, oSampleNumber, oBatchRunid, oLabId, oLabOrderId, oIsAvailable, oApproved, oReport, oAfter, pageable);

         */
    }

    public Order saveOneFromCheckout(String token, PaymentIntent payment, UserDetailsImpl updater) throws JdxServiceException {
        try {
            Order order = new Order();
            Optional<Checkout> checkout = checkoutService.find(Optional.of(token));
            if (checkout.isEmpty())
                throw new JdxServiceException("Cannot find checkout session from provided token");

            //TODO expand this to allow for multiple purchases
            Optional<InventoryItem> inventory = null;
            if(checkout.get().getItems() != null && checkout.get().getItems().size() == 1 && checkout.get().getItems().get(0).getProductId() != null) {
                inventory = inventoryService.get(checkout.get().getItems().get(0).getProductId());
                if(inventory.isEmpty())
                    throw new JdxServiceException("Cannot create checkout as inventory for the product cannot be checked");

                if(inventory.get().getAvailableUnits() == 0)
                    throw new JdxServiceException("Cannot create checkout, there is no inventory remaining for this product");
            }


            //TODO need an algo for handling different labs for different tests, round robin, etc.
            //Look up laboratory
            Optional<Laboratory> laboratory = laboratoryService.getDefaultLaboratory();
            if (laboratory.isEmpty())
                throw new JdxServiceException("Cannot find laboratory to assign to order");

            Optional<PaymentProcessorProvider> paymentProcessorProvider = paymentProcessorProviderService.getPaymentProcessorProviderByName(checkout.get().getProcessor().name());
          //  if(paymentProcessorProvider.isEmpty())
          //      throw new JdxServiceException("Cannot find payment processor to associate to this order.");

            if (checkout.get().getTotalAmount() != payment.getAmount().floatValue())
                logger.info("Checkout " + checkout.get().getId() + " amount and payment amount not equal, using payment details");

            if (!checkout.get().getCurrency().equals(payment.getCurrency()))
                logger.info("Checkout " + checkout.get().getId() + " currency and payment currency not the same, using payment details");

            //Find the customer first, if an existing grab them, otherwise let's create one
            //TODO need a better way of handling the clientId ownership here
            Optional<User> lookupUser = null;

            if(checkout.get().getCustomer().getId() != null)
                lookupUser = userService.findOne(checkout.get().getCustomer().getId());
            else
                lookupUser = userService.findOneByEmailAndClientId(checkout.get().getCustomer().getEmail(), checkout.get().getClientId());

            User customer = null;
            if (lookupUser.isPresent()) {
                customer = lookupUser.get();
                if(checkout.get().getCustomer().getDateOfBirth() != null)
                    customer.setDateOfBirth(checkout.get().getCustomer().getDateOfBirth());

                if(customer.getStripeCustomerId() == null)
                    customer.setStripeCustomerId(checkout.get().getCustomer().getStripeCustomerId());

                PatientDetails patientDetails = customer.getPatientDetails();
                if(patientDetails != null) {
                    MedicalDetails medicalDetails = patientDetails.getMedicalDetails();
                    if (checkout.get().getConception() != null) {
                        medicalDetails.setConceptionDate(checkout.get().getConception());
                        medicalDetails.setPregnant(true);
                    }
                } else {
                    patientDetails = new PatientDetails();
                    MedicalDetails medicalDetails = new MedicalDetails();
                    medicalDetails.setNoBloodTransfusion(checkout.get().isAgreeNoTransplantNorTransfusion());
                    medicalDetails.setNoOrganTransplant(checkout.get().isAgreeNoTransplantNorTransfusion());

                    if (checkout.get().getConception() != null) {
                        medicalDetails.setPregnant(true);
                        medicalDetails.setConceptionDate(checkout.get().getConception());
                        medicalDetails.setPatientDetails(patientDetails);
                    }
                    patientDetails.setMedicalDetails(medicalDetails);
                    customer.setPatientDetails(patientDetails);
                }

                if (customer.getPreferences() == null) {
                    Preferences prefs = new Preferences();
                    prefs.setUser(customer);
                    customer.setPreferences(prefs);
                }

                if(checkout.get().getFetalSexResultsPreferences() != null)
                    customer.getPreferences().setFstPreferences(checkout.get().getFetalSexResultsPreferences());

                if(checkout.get().getCustomer().getShippingAddress() != null)
                    customer.setPrimaryAddress(checkout.get().getCustomer().getShippingAddress());

                if(checkout.get().getCustomer().getPhone() != null)
                    customer.setPrimaryPhone(new Phone(checkout.get().getCustomer().getPhone()));


                customer.setLastOrderedAt(Calendar.getInstance());
                customer = userService.update(customer, updater);
            } else {

                //New customer/patient scenario
                customer = new User();
                customer.setEmail(checkout.get().getCustomer().getEmail());
                customer.setFirstName(checkout.get().getCustomer().getFirstName());
                customer.setLastName(checkout.get().getCustomer().getLastName());
                customer.setUserType(UserType.STANDARD);
                customer.setStatus(UserStatus.PROVISIONAL);
                customer.setClientId(checkout.get().getClientId());
                customer.setDateOfBirth(checkout.get().getCustomer().getDateOfBirth());
                customer.setLastOrderedAt(Calendar.getInstance());
                customer.setPrimaryAddress(checkout.get().getCustomer().getShippingAddress());
                customer.setPrimaryPhone(new Phone(checkout.get().getCustomer().getPhone()));
                customer.setActivated(false);
                if(customer.getStripeCustomerId() == null)
                    customer.setStripeCustomerId(checkout.get().getCustomer().getStripeCustomerId());

                PatientDetails patientDetails = new PatientDetails();
                MedicalDetails medicalDetails = new MedicalDetails();
                medicalDetails.setNoBloodTransfusion(checkout.get().isAgreeNoTransplantNorTransfusion());
                medicalDetails.setNoOrganTransplant(checkout.get().isAgreeNoTransplantNorTransfusion());
                if (checkout.get().getConception() != null) {
                    medicalDetails.setPregnant(true);
                    medicalDetails.setConceptionDate(checkout.get().getConception());
                    medicalDetails.setPatientDetails(patientDetails);
                }
                patientDetails.setMedicalDetails(medicalDetails);
                customer.setPatientDetails(patientDetails);

                if (customer.getPreferences() == null) {
                    Preferences prefs = new Preferences();
                    prefs.setUser(customer);
                    customer.setPreferences(prefs);
                }

                if(checkout.get().getFetalSexResultsPreferences() != null)
                    customer.getPreferences().setFstPreferences(checkout.get().getFetalSexResultsPreferences());

//TODO need to check in on this code and align with the checkout service checkout create method to see where to create
// the new customer - if here, and the customer is saved, then need to add a new customer/patient record to the customer/patient queue
                //This is not a full customer create action since the user still needs to register an account and get grants
                //customer = userService.createDefaultAuthorities(customer);

                //Save this user - but ensure that they're not activated yet
                try {
                    customer = userService.save(customer, updater);
                } catch (Exception e) {
                    customer = userService.update(customer, updater);
                }
                //update SNS with new customer - or has the customer been updated already?
            }

            order.setCustomer(customer);
            order.setCheckoutId(checkout.get().getId());
            order.setOrderNumber(checkout.get().getOrderNumber());
            order.setOpen(true);

            //Set the order creation to NOW
            order.setOrderedAt(Calendar.getInstance());

            //Set order details from the payment, log any differences between the checkout and payment
            order.setAmount(payment.getAmount().floatValue());
            order.setCurrency(Currency.getInstance(payment.getCurrency().toUpperCase(Locale.ROOT)));

            //Add this transaction from the payment intent
            Transaction transaction = new Transaction();
            transaction.setTransactionJson(payment.toJson());
            transaction.setExternalTransactionId(payment.getId());

            transaction.setPaymentInstrumentType(PaymentInstrumentType.CREDIT_CARD);

            /*
            if (payment.getPaymentMethod().equals(PaymentMethodType.card))
                transaction.setPaymentInstrumentType(PaymentInstrumentType.CREDIT_CARD);
            if (payment.getPaymentMethod().equals(PaymentMethodType.apple_pay))
                transaction.setPaymentInstrumentType(PaymentInstrumentType.APPLE_PAY);
            if (payment.getPaymentMethod().equals(PaymentMethodType.google_pay))
                transaction.setPaymentInstrumentType(PaymentInstrumentType.GOOGLE_PAY);

             */
            transaction.setCreatedAt(Calendar.getInstance());


            if (paymentProcessorProvider.isPresent())
                transaction.setProcessor(paymentProcessorProvider.get());
            transaction.setType(TransactionType.CARD_CHARGED);
            transaction.setOrder(order);
            order.addTransaction(transaction);

            //Set order service preferences
            if(checkout.get().getServiceOptions() != null) {
                order.setServiceOptions(checkout.get().getServiceOptions());
            }

            //Create an order status and add to the order
            order = updateOrderStatus(order, OrderStatusType.CREATED);
            order.setRequiresShipment(true);

            //Look up products from checkout line items to get details to create each line item
            List<CheckoutLineItem> items = checkout.get().getItems();
            List<Product> products = productService.getAllByIds(items.stream().map(x -> x.getProductId()).collect(Collectors.toList()));
            if (products != null && products.size() > 0) {
                for (CheckoutLineItem item : items) {
                    Optional<Product> oProduct = products.stream().filter(x -> x.getId().equals(item.getProductId())).findFirst();
                    if (oProduct.isPresent()) {
                        Product product = oProduct.get();
                        OrderLineItem lineItem = new OrderLineItem();
                        order.addLineItem(lineItem);
                        lineItem.setOrder(order);
                        lineItem.setSku(product.getSku());
                        lineItem.setProductName(product.getName());
                        lineItem.setProductId(product.getId());
                        lineItem.setProductImageUrl(product.getProductImageUrlThumbnail());
                        order.setPriceBookId(product.getSalesforcePriceBookId());
                        lineItem.setPriceBookEntryId(product.getSalesforcePriceBookEntryId());
                        lineItem.setRequiresShipping(true);
                        lineItem.setInOfficeCollected(false);
                        lineItem.setDirectlyProvided(false);
                        lineItem.setDescription(product.getShortDescription());
                        lineItem.setType(product.getType());
                        lineItem.setAmount(product.getPrice());
                        lineItem.setQuantity(item.getQuantity());
                        lineItem.setRequiresShipping(item.isShipped());
                        //order.setRequiresShipment(item.isShipped());

                        //Set the lab for this lineItem
                        LaboratoryOrder laboratoryOrder = new LaboratoryOrder();
                        laboratoryOrder.setLab(laboratory.get());
                        laboratoryOrder.setOrderLineItem(lineItem);
                        laboratoryOrder.setParentOrder(order);
                        laboratoryOrder.setOrderType(OrderType.SELF_CREATED);
                        laboratoryOrder.setPatient(customer);
                        laboratoryOrder.setReportConfiguration(product.getReportConfiguration());
                        laboratoryOrder.setMeta(buildMeta(updater));

                        //Set the consents, should be informed consent and terms of service consent
                        if (checkout.get().isAgreedToInformedConsent()) {
                            Consent informedConsent = new Consent();
                            informedConsent.setLaboratoryOrder(laboratoryOrder);
                            informedConsent.setType(ConsentType.MEDICAL);
                            informedConsent.setApprovalDate(Calendar.getInstance());
                            informedConsent.setFormName("Informed Consent");
                            informedConsent.setApproval(true);
                            informedConsent.setPatient(customer);
                            laboratoryOrder.setPatientConsent(informedConsent);
                        }
                        if (checkout.get().isAgreedToTerms()) {
                            Consent termsOfServiceConsent = new Consent();
                            termsOfServiceConsent.setLaboratoryOrder(laboratoryOrder);
                            termsOfServiceConsent.setType(ConsentType.DISCLOSURE);
                            termsOfServiceConsent.setApprovalDate(Calendar.getInstance());
                            termsOfServiceConsent.setFormName("Terms of Service");
                            termsOfServiceConsent.setApproval(true);
                            termsOfServiceConsent.setPatient(customer);
                            laboratoryOrder.setPatientConsent(termsOfServiceConsent);
                        }

                        //Get the approving provider here
                        if (product.isRequiresProviderApproval()) {
                            ProviderApproval approval = new ProviderApproval();
                            approval.setApproved(false);
                            approval.setRequiresApproval(true);
                            if (checkout.get().getApprovingProvider() == null && !checkout.get().isApprovingProviderDesignated()) {
                                Optional<Practice> practice = practiceService.getDefaultPractice();
                                if(practice.isEmpty())
                                    throw new JdxServiceException("No default practice is available to assign to order.");
                                Optional<Provider> provider = providerService.getDefaultProviderForPractice(practice.get().getId());
                                if(provider.isEmpty())
                                    throw new JdxServiceException("No default provider to assign to Order");
                                approval.setApprovingProvider(provider.get());
                            } else
                                approval.setApprovingProvider(checkout.get().getApprovingProvider());

                            laboratoryOrder.setProviderApproval(approval);
                            order.setApprovingProviderName(approval.getApprovingProvider().getName());
                            order.setRequiresProviderApproval(true);

                        } else {
                            ProviderApproval approval = new ProviderApproval();
                            approval.setRequiresApproval(false);
                            approval.setApproved(false);
                            approval.setApprovingProvider(null);
                            laboratoryOrder.setProviderApproval(approval);
                            order.setRequiresProviderApproval(false);
                        }

                        User u = new User();
                        u.setFirstName(checkout.get().getCustomer().getFirstName());
                        u.setLastName(checkout.get().getCustomer().getLastName());
                        u.setPrimaryPhone(new Phone(checkout.get().getCustomer().getPhone()));
                        u.setPrimaryAddress(checkout.get().getCustomer().getShippingAddress());

                        Fulfillment fulfillment = createNewFulfillment(product, u, checkout.get().getCustomer().getShippingAddress(), laboratory.get());
                        fulfillment.setMeta(buildMeta(updater));

                        /*
                        //Generate and link TRF here (or do this afterwards if it wasn't delivered already)

                        //Find which fulfillment providers match by country and by state and assign that to each line item
                        List<FulfillmentProvider> fulfillmentProviders = product.getFulfillmentProviders();
                        FulfillmentProvider selectedProvider = null;
                        for (FulfillmentProvider provider : fulfillmentProviders) {
                            String countriesString = provider.getCoveredCountries();
                            if (countriesString != null) {
                                String[] countries = countriesString.split(",");
                                for (String a : countries) {
                                    a.stripLeading().stripTrailing();
                                    if (a.equals(checkout.get().getCustomer().getShippingAddress().getCountry())) {
                                        String coveredStatesString = provider.getCoveredStates();
                                        String[] states = coveredStatesString.split(",");

                                        //TODO refactor this into a search query/lookup against the states array for efficiency
                                        for (String s : states) {
                                            s.stripLeading().stripTrailing();
                                            if (s.equals(checkout.get().getCustomer().getShippingAddress().getState())) {
                                                selectedProvider = provider;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //Attach fulfillment details to the order
                        Fulfillment fulfillment = new Fulfillment();

                        if (selectedProvider != null) {
                            logger.info("found fulfillment provider " + selectedProvider.getName() + " to fulfill order.");
                            fulfillment.setFulfillmentProvider(selectedProvider);
                        }
                        else {
                            Optional<FulfillmentProvider> defaultProvider = fulfillmentProviderService.getDefaultProvider();
                            if(defaultProvider.isPresent()) {
                                logger.info("Using default provider to fulfill order");
                                fulfillment.setFulfillmentProvider(defaultProvider.get());
                            }
                            else
                                throw new JdxServiceException("Cannot find default provider to set order to");
                        }

                        ShippingDetails shippingDetails = new ShippingDetails();
                        shippingDetails.setFulfillment(fulfillment);

                        //Set the ship to address of the customer from the checkout object
                        ShippingTarget shippingTo = new ShippingTarget();
                        shippingTo.setAddress(checkout.get().getCustomer().getShippingAddress());
                        shippingTo.setRecipientName(checkout.get().getCustomer().getFirstName() + " " + checkout.get().getCustomer().getLastName());
                        shippingTo.setPhone(new Phone(checkout.get().getCustomer().getPhone()));
                        shippingDetails.setToAddress(shippingTo);

                        //Set the return address to the laboratory selected above
                        ShippingTarget returnTo = new ShippingTarget();
                        returnTo.setAddress(laboratory.get().getLocation());
                        returnTo.setRecipientName("Juno Diagnostics Laboratory");
                        returnTo.setPhone(laboratory.get().getContact());
                        shippingDetails.setReturnAddress(returnTo);

                        ShippingStatus status = new ShippingStatus();
                        status.setShippingDetails(shippingDetails);
                        status.setStatusTimestamp(Calendar.getInstance());
                        status.setCurrent(true);
                        status.setToCustomer(true);
                        status.setStatus(ShippingStatusType.AWAITING_ASSEMBLY);

                        shippingDetails.addShippingStatus(status);
                        fulfillment.setShippingDetails(shippingDetails);
                        */

                        //Add this new fulfillment object to the line item
                        fulfillment.setOrderLineItem(lineItem);
                        lineItem.addFulfillment(fulfillment);

                        laboratoryOrder.setOrderLineItem(lineItem);
                        lineItem.setLaboratoryOrderDetails(laboratoryOrder);
                    }
                }
            }
            else
                throw new JdxServiceException("Cannot find product or products associated with this order");

       //TODO re-add this once ready to test inventory handling
            //decrement the count of available units
       //     inventory.get().setAvailableUnits(inventory.get().getAvailableUnits()-1);
       //     inventoryService.save(inventory.get(), updater);

            order.setMeta(buildMeta(updater));

            //Save the order in the DB
            order = orderRepository.save(order);

            //Send an order creation email
            sendOrderCreateEmail(order);

            //Send to SNS
            sendOrderStatus(order, EventType.CREATE);


            //If the order was completed, remove the checkout record
            checkoutService.delete(checkout.get().getId());

            return order;

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create order from checkout: " + e.getMessage());
        }
    }

    public Fulfillment createNewFulfillment(Product product, User customer, Address shippingAddress, Laboratory laboratory) throws JdxServiceException {
        //Find which fulfillment providers match by country and by state and assign that to each line item
        List<FulfillmentProvider> fulfillmentProviders = product.getFulfillmentProviders();
        FulfillmentProvider selectedProvider = null;
        for (FulfillmentProvider provider : fulfillmentProviders) {
            String countriesString = provider.getCoveredCountries();
            if (countriesString != null) {
                String[] countries = countriesString.split(",");
                for (String a : countries) {
                    a.stripLeading().stripTrailing();
                    if (a.equals(shippingAddress.getCountry())) {
                        String coveredStatesString = provider.getCoveredStates();
                        String[] states = coveredStatesString.split(",");

                        //TODO refactor this into a search query/lookup against the states array for efficiency
                        for (String s : states) {
                            s.stripLeading().stripTrailing();
                            if (s.equals(shippingAddress.getState())) {
                                selectedProvider = provider;
                                break;
                            }
                        }
                    }
                }
            }
        }

        //Attach fulfillment details to the order
        Fulfillment fulfillment = new Fulfillment();

        if (selectedProvider != null) {
            logger.info("found fulfillment provider " + selectedProvider.getName() + " to fulfill order.");
            fulfillment.setFulfillmentProvider(selectedProvider);
        }
        else {
            Optional<FulfillmentProvider> defaultProvider = fulfillmentProviderService.getDefaultProvider();
            if(defaultProvider.isPresent()) {
                logger.info("Using default provider to fulfill order");
                fulfillment.setFulfillmentProvider(defaultProvider.get());
            }
            else
                throw new JdxServiceException("Cannot find default provider to set order to");
        }

        fulfillment.setEstimatedToShipAt(getEstimatedShipmentTime());

        ShippingDetails shippingDetails = new ShippingDetails();
        shippingDetails.setFulfillment(fulfillment);

        //Set the ship to address of the customer from the checkout object
        ShippingTarget shippingTo = new ShippingTarget();
        shippingTo.setAddress(shippingAddress);
        shippingTo.setRecipientName(customer.getFirstName() + " " + customer.getLastName());
        shippingTo.setPhone(customer.getPrimaryPhone());
        shippingDetails.setToAddress(shippingTo);

        //Set the return address to the laboratory selected above
        ShippingTarget returnTo = new ShippingTarget();
        returnTo.setAddress(laboratory.getLocation());
        returnTo.setRecipientName("Juno Diagnostics Laboratory");
        returnTo.setPhone(laboratory.getContact());
        shippingDetails.setReturnAddress(returnTo);

        ShippingStatus status = new ShippingStatus();
        status.setShippingDetails(shippingDetails);
        status.setStatusTimestamp(Calendar.getInstance());
        status.setCurrent(true);
        status.setToCustomer(true);
        status.setStatus(ShippingStatusType.AWAITING_ASSEMBLY);

        shippingDetails.addShippingStatus(status);
        fulfillment.setShippingDetails(shippingDetails);

        return fulfillment;
    }

    public Calendar getEstimatedShipmentTime() {
        //Compute estimated shipment time
        Calendar nowPst = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        Calendar noon = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        noon.set(Calendar.HOUR_OF_DAY, 12);
        //If the order is before noon on a business day

        //Is this order before noon?
        if (nowPst.getTimeInMillis() <= noon.getTimeInMillis()) {
            //Is this a business day?
            if (nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                return nowPst;
            }
        } else {
            if(nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || nowPst.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                nowPst.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            } else {
                nowPst.set(Calendar.DAY_OF_WEEK, nowPst.get(Calendar.DAY_OF_WEEK) + 1);
            }
        }
        return nowPst;
    }

    public Order saveOne(Order order, UserDetailsImpl updater) throws JdxServiceException {
        try {
            //Handle all of the dependent objects and save those
            if (order != null) {
                User user = order.getCustomer();
                order.setOrderedAt(Calendar.getInstance());
                Optional<User> lookup = userService.findOne(user.getId());
                if (lookup.isPresent())
                    order.setCustomer(lookup.get());
                else
                    throw new JdxServiceException("Patient with id " + user.getId() + " does not exist already");

                List<OrderLineItem> items = order.getLineItems();
                for (OrderLineItem i : items) {
                    List<Kit> kits = new ArrayList<>();
                    LaboratoryOrder laboratoryOrder = i.getLaboratoryOrderDetails();
                    if (laboratoryOrder != null) {
                        laboratoryOrder.setMeta(buildMeta(updater));
                        laboratoryOrder.setOrderLineItem(i);
                        laboratoryOrder.setParentOrder(order);
                        laboratoryOrder.setPatient(lookup.get());
                        laboratoryOrder.setMeta(buildMeta(updater));
                        Laboratory lab = laboratoryOrder.getLab();
                        if (lab != null && lab.getId() != null) {
                            Optional<Laboratory> oLab = laboratoryService.getLaboratory(lab.getId());
                            if (oLab.isPresent())
                                laboratoryOrder.setLab(oLab.get());
                            else
                                throw new JdxServiceException("No Lab exists for ID " + lab.getId());

                        }

                        Consent consent = laboratoryOrder.getPatientConsent();
                        if (consent != null)
                            consent.setPatient(lookup.get());
                        ProviderApproval approval = laboratoryOrder.getProviderApproval();
                        Provider approvingProvider = approval.getApprovingProvider();
                        if (approvingProvider != null && approvingProvider.getId() != null) {
                            Optional<Provider> oProvider = providerService.getProvider(approvingProvider.getId(), new String[]{});
                            if (oProvider.isPresent())
                                approval.setApprovingProvider(oProvider.get());
                            else
                                throw new JdxServiceException("For order requiring approval, provider at Id " + approvingProvider.getId() + " does not exist already");
                        }

                        //Compile a list of kits to match up from the DB - to be used for TestRuns and fulfillment if present
                        List<TestRun> testRuns = laboratoryOrder.getTestRuns();
                        if (testRuns != null && testRuns.size() > 0) {
                            for (TestRun t : testRuns) {
                                t.setLaboratoryOrder(laboratoryOrder);
                                for (LaboratoryStatus status : t.getStatus())
                                    status.setCreatedBy(updater.getEmail());
                                if (t.getKit() != null) {
                                    if (!kits.stream().anyMatch(x -> x.getId().equals(t.getKit().getId())))
                                        kits.add(t.getKit());
                                }
                            }
                            logger.info("Kit Ids to look up " + mapper.writeValueAsString(kits.stream().map(x -> x.getId()).collect(Collectors.toList())));

                            kits = kitService.findKitsByIds(kits.stream().map(x -> x.getId()).collect(Collectors.toList()));
                            logger.info("Kits assocaited with test runs in this order " + mapper.writeValueAsString(kits));

                            //Now go back and set the Kit for each TestRun
                            for (TestRun t : testRuns) {
                                Optional<Kit> k = kits.stream().filter(x -> x.getId().equals(t.getKit().getId())).findFirst();
                                if (k.isPresent()) {
                                    if (k.get().isUnusable())
                                        throw new JdxServiceException("Cannot assign an unusable kit to a lab order");
                                    t.setKit(k.get());
                                    k.get().setAssigned(true);
                                }
                            }
                        }

                        if(i.getFulfillments() != null) {
                            for (Fulfillment fulfillment : i.getFulfillments()) {
                                if(fulfillment.getKit() != null && fulfillment.getKit().getId() != null) {
                                    Optional<Kit> kit = kitService.getKit(fulfillment.getKit().getId());
                                    if (kit.isPresent())
                                        fulfillment.setKit(kit.get());
                                    fulfillment.setOrderLineItem(i);
                                }
                            }
                        }
                    }
                }

                order.setMeta(buildMeta(updater));
                //TODO need a more efficient stored proc here instead of multiple DB writes

                Order o =  orderRepository.save(order);

                if(o != null)
                    sendOrderStatus(o, EventType.CREATE);

                return o;
            }

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot create order " + e.getMessage());
        }

        return null;
    }


    //public Order saveAll(List<Order> orders){
    //    return orderRepository.saveAll(orders);
    //}

    //TODO need to do the entire update flow here
    public Order update(Order order, UserDetailsImpl updater) throws JdxServiceException {
        Order updateOrder = new Order();
        if(order != null){
            return updateOrder;
        }

        throw new JdxServiceException("Cannot update order");

    }

    //For each kit assigned to an order, we need to set let is stay, but set it to unusable
    public void deleteOrder(Order order){
        orderRepository.delete(order);
    }

    public Order updateWithLimsDetails(String orderId, String orderLineItemid, ElementsOrder limsOrder, UserDetailsImpl updater) throws JdxServiceException {
        return null;
    }

    //Criteria
    //1.)Does this kit have an id and exist already, if so, is it attached to this order and does it require updating?
    //2.)Order does not already have an active kit/test run that isn't yet complete
    //3.)If order already has an active test run and the inbound kit code is already assigned to this order, then update the current kit if able
    //4.)Kit code is not already in-use anywhere else
    //5.)Sample number is not already in-use anywhere else
    //6.)Sleeve number is not already in-use anywhere else
    public Order updateWithKitDetails(String orderId, String orderLineItemId, Kit inboundKit, String fulfillmentId, UserDetailsImpl updater) throws JdxServiceException {
        try {
            boolean updateCurrentKit = false;
            boolean assignExistingKitToOrder = false;
            Kit existingKit = null;
            TestRun updateTestRun = null;

            if (orderId == null || orderLineItemId == null || inboundKit == null)
                throw new JdxServiceException(OrderErrorCodes.INSUFFICIENT_INFORMATION_TO_PROCESS_ORDER_UPDATE.code,
                        OrderErrorCodes.INSUFFICIENT_INFORMATION_TO_PROCESS_ORDER_UPDATE.statusCode,
                        OrderErrorCodes.INSUFFICIENT_INFORMATION_TO_PROCESS_ORDER_UPDATE.message,
                        "Order ID, lineItem ID are null or the kit to update is null when attempting to assign or update an assigned kit with this order");

            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isEmpty())
                throw new JdxServiceException(JunoErrorCodes.RESOURCE_NOT_FOUND.code,
                        JunoErrorCodes.RESOURCE_NOT_FOUND.statusCode,
                        JunoErrorCodes.CLIENT_ID_NOT_PROVIDED.message,
                        "Cannot update order for orderId " + orderId + " since order cannot be found");

            if(!order.get().isOpen())
                throw new JdxServiceException(OrderErrorCodes.ORDER_IS_CLOSED.code,
                        OrderErrorCodes.ORDER_IS_CLOSED.statusCode,
                        OrderErrorCodes.ORDER_IS_CLOSED.message,
                        "An attempt to assign or update a kit to a closed order has been rejected. Reopen the order before attempting this kit modification");

            Optional<OrderLineItem> lineItem = order.get().getLineItems().stream().filter(x -> x.getId().equals(orderLineItemId)).findFirst();
            if (lineItem.isEmpty())
                throw new JdxServiceException(JunoErrorCodes.RESOURCE_NOT_FOUND.code,
                        JunoErrorCodes.RESOURCE_NOT_FOUND.statusCode,
                        JunoErrorCodes.RESOURCE_NOT_FOUND.message,
                        "Cannot find line item associated with order " + orderId);

            LaboratoryOrder labOrder = lineItem.get().getLaboratoryOrderDetails();
            if (labOrder == null)
                throw new JdxServiceException(JunoErrorCodes.RESOURCE_NOT_FOUND.code,
                        JunoErrorCodes.RESOURCE_NOT_FOUND.statusCode,
                        JunoErrorCodes.RESOURCE_NOT_FOUND.message,
                        "Cannot update orderLineItem since the lab order was never properly created");

            Optional<TestRun> foundRun = null;

            //Find an existing testrun with a kit that matches the kit.id, if provided
            if(inboundKit.getId() != null)
                foundRun = labOrder.getTestRuns().stream().filter(x -> x.getKit().getId().equals(inboundKit.getId())).findFirst();
            else
                foundRun = Optional.empty();

            //If there is a test run associated to this kit, but it has already been completed, then fail
            if (foundRun.isPresent() && foundRun.get().isCompleted())
                throw new JdxServiceException(OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.code,
                        OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.statusCode,
                        OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.message,
                        "Cannot update this kit as it is assigned to a test run that is now completed");
            //If the test run is not complete yet, however the kit has already been delivered to the patient, shipped or handed off or in the lab already
            else if(foundRun.isPresent() && foundRun.get().isBeyondKitAssignment())
                throw new JdxServiceException(OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.code,
                        OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.statusCode,
                        OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.message,
                        "Cannot update this kit as it is assigned to a test run where the kit has already been delivered to the patient");
            //Otherwise if we have a test run, even if it has a kit assigned to it now, the new kit will supersede it
            else if(foundRun.isPresent()) {
                updateTestRun = foundRun.get();
                existingKit = foundRun.get().getKit();
                updateCurrentKit = true;
            }
            //If the user is attempting to update an existing kit but the test run can't be found, let's do some fancy checking
            else if(foundRun.isEmpty()) {
                //cannot assign a new kit and create a new testrun until previous run is closed
                if(labOrder.getTestRuns().stream().filter(x -> !x.isCompleted()).findAny().isPresent())
                    throw new JdxServiceException(OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.code,
                            OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.statusCode,
                            OrderErrorCodes.INVALID_TEST_RUN_STATE_WHEN_UPDATING_KIT.message,
                            "Cannot assign this existing kit to a new testrun for this order since an existing testRun is still incomplete");

                //At this point, we don't have a testRun associated with the kit, the LabOrder has no other open testRuns so a new one can be created and assigned

                Optional<Kit> foundKit = kitService.getKit(inboundKit.getId());
                if (foundKit.isPresent()){
                    //If the kit that is the client is attempting to assign to this order is already assigned, then reject
                    if (foundKit.get().isAssigned())
                        throw new JdxServiceException(LaboratoryErrorCodes.KIT_IS_ALREADY_ASSIGNED.code,
                                LaboratoryErrorCodes.KIT_IS_ALREADY_ASSIGNED.statusCode,
                                LaboratoryErrorCodes.KIT_IS_ALREADY_ASSIGNED.message,
                                "The kit that is in the request exists already and is already assigned, but not to this order so it cannot be assigned here.");
                    else {
                        assignExistingKitToOrder = true;
                        existingKit = foundKit.get();
                    }
                }
            }

            //Now we can apply the changes accordingly

            Order updatedOrder = null;

            //Update the current kit assigned to the Order

            //Can't do this updating on kit.code until LIMS can support decoupled updates of kit barcode and external_barcode when we update, otherwise we can only allow sampleNumber and sleeveNumber updates after the kit is created
            //Let's do some kit updating if we're just modifying an existing kit's information

            if (updateTestRun != null) {

                Kit kit = updateTestRun.getKit();
                kit = kitService.updateKit(inboundKit, updater);

                if (kit != null) {
                    //Update the testrun lab status
                    testRunService.updateTestRunLaboratoryStatus(foundRun.get(), LaboratoryStatusType.KIT_CODES_UPDATED);

                    //TODO
                    //Need to do some stuff with LIMS to inform them of the kit changes
                    //LIMS.updatekit.here.please

                    //Update the order details too
                    updateOrderStatus(order.get(), OrderStatusType.KIT_ASSIGNED);
                    order.get().setMeta(updateMeta(order.get().getMeta(), updater));
                    updatedOrder = orderRepository.save(order.get());
                }
            }

            //Assign an existing kit to this order
            else if(assignExistingKitToOrder && existingKit != null){
                assignExistingKitToOrder(order.get(), lineItem.get(), existingKit, fulfillmentId, updater);
                updatedOrder = order.get();
            }

            //Create a new kit and assign to this order
            else {
                //If we have any other testRuns which are not yet completed, we can't assign a new kit
                if (labOrder.getTestRuns().stream().anyMatch(x -> !x.isCompleted()))
                    throw new JdxServiceException("Cannot create a new test run until the previous one is completed");

                //If any other kit exists using the new code, sampleNumber or sleeveNumber, then fail
                List<Kit> foundKits = null;
                if(inboundKit.getPsdSleeveNumber() != null)
                    foundKits = kitService.findKitsByKitCodeOrSampleNumberOrSleeveNumber(inboundKit.getCode(), inboundKit.getSampleNumber(), inboundKit.getPsdSleeveNumber());
                else
                    foundKits = kitService.findKitsByKitCodeOrSampleNumber(inboundKit.getCode(), inboundKit.getSampleNumber());

                if (foundKits != null && !foundKits.isEmpty())
                    throw new JdxServiceException("Cannot assign this kit as one or more of the codes are already in use with another kit");

                Kit kit = kitService.saveKit(inboundKit, updater);

                //Assign this new kit to a new test run and fulfillment for this line item
                assignNewKitToOrder(order.get(), lineItem.get(), kit, fulfillmentId, updater);

                updateOrderStatus(order.get(), OrderStatusType.KIT_ASSIGNED);

                updatedOrder = orderRepository.save(order.get());
            }
            if (updatedOrder == null)
                throw new JdxServiceException(OrderErrorCodes.ORDER_UPDATE_FAILURE.code,
                        OrderErrorCodes.ORDER_UPDATE_FAILURE.statusCode, OrderErrorCodes.ORDER_UPDATE_FAILURE.message,
                        "Failed to update the kit for the order");

            //Send SNS message
            sendOrderStatus(updatedOrder, EventType.UPDATE);

            return updatedOrder;

        } catch (JdxServiceException e){
            e.printStackTrace();
            throw e;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update order with kit information " + e.getMessage());
        }
    }

    public void assignNewKitToOrder(Order order, OrderLineItem lineItem, Kit kit, String fulfillmentId, UserDetailsImpl updater) throws JdxServiceException {
        LaboratoryOrder labOrder = lineItem.getLaboratoryOrderDetails();
        if(labOrder == null)
            throw new JdxServiceException("Cannot assign new kit since the Lab order is not present");

        TestRun newRun = new TestRun();
        newRun.setKit(kit);
        kit.setTestRun(newRun);
        newRun.setLaboratoryOrder(labOrder);
        newRun.setType(TestRunType.STANDARD);
        LaboratoryStatus status = new LaboratoryStatus();
        status.setTestRun(newRun);
        status.setCurrent(true);
        status.setCreatedAt(Calendar.getInstance());
        status.setCreatedBy(updater.getUserId());
        status.setStatus(LaboratoryStatusType.KIT_ASSIGNED);
        newRun.addStatus(status);
        newRun.setReportConfiguration(labOrder.getReportConfiguration());
        labOrder.addTestRun(newRun);

        //If this is a redraw order, then set the new testRun to redraw and close out the parent order
        if(order.isRequiresRedraw()) {
            newRun.setRedraw(true);
            order.setRequiresRedraw(false);
        }

        //TODO figure out how to attach kit to fulfillment object in question - how do we pick the right fulfillment entity - just grab the first one?
        Optional<Fulfillment> fulfillment = lineItem.getFulfillments().stream().filter(x->x.getId().equals(fulfillmentId)).findFirst();
        Fulfillment oFulfillment = null;
        //If no fulfillment, then let's create one here
        if(fulfillment.isEmpty()) {
            Optional<Product> product = productService.get(lineItem.getProductId());
            if(product.isEmpty())
                throw new JdxServiceException("No fulfillment found for and cannot create one due to an invalid product Id");
            List<Laboratory> labProviders = product.get().getLaboratoryProviders();
            if(labProviders == null || labProviders.size() < 1)
                throw new JdxServiceException("No fulfillment found and cannot create one due to no laboratory provider set for product");

            //Just grab the first lab provider for now
            Laboratory laboratory = labProviders.get(0);
            oFulfillment = createNewFulfillment(product.get(), order.getCustomer(), order.getCustomer().getPrimaryAddress(), laboratory);
            lineItem.addFulfillment(oFulfillment);
        } else
            oFulfillment = fulfillment.get();

        oFulfillment.setKit(kit);
        oFulfillment.setMeta(buildMeta(updater));

        //Notify lims about this new kit. Maybe we should wait to inform LIMS about this order until
        ElementsOrder limsOrder = updateLimsWithOrder(order, lineItem, newRun, kit, updater);

        if(limsOrder == null)
            throw new JdxServiceException("Failed to send order to lims");

        kit.setAddedToLimsAt(Calendar.getInstance());
        kit.setLimsId(String.valueOf(limsOrder.getKit().getId()));

        kit.setAssigned(true);
        kit.setMeta(updateMeta(kit.getMeta(), updater));
        kitService.updateKit(kit, updater);
    }

    public void assignExistingKitToOrder(Order order, OrderLineItem lineItem, Kit kit, String fulfillmentId, UserDetailsImpl updater) throws JdxServiceException {
        LaboratoryOrder labOrder = lineItem.getLaboratoryOrderDetails();
        if(labOrder == null)
            throw new JdxServiceException("Cannot assign new kit since the Lab order is not present");

        TestRun newRun = new TestRun();
        newRun.setKit(kit);
        kit.setTestRun(newRun);
        newRun.setLaboratoryOrder(labOrder);
        newRun.setType(TestRunType.STANDARD);
        LaboratoryStatus status = new LaboratoryStatus();
        status.setTestRun(newRun);
        status.setCurrent(true);
        status.setCreatedAt(Calendar.getInstance());
        status.setCreatedBy(updater.getUserId());
        status.setStatus(LaboratoryStatusType.KIT_ASSIGNED);
        newRun.addStatus(status);
        newRun.setReportConfiguration(labOrder.getReportConfiguration());
        labOrder.addTestRun(newRun);

        //TODO figure out how to attach kit to fulfillment object in question - how do we pick the right fulfillment entity - just grab the first one?
        Optional<Fulfillment> fulfillment = lineItem.getFulfillments().stream().filter(x->x.getId().equals(fulfillmentId)).findFirst();
        Fulfillment oFulfillment = null;
        //If no fulfillment, then let's create one here
        if(fulfillment.isEmpty()) {
            Optional<Product> product = productService.get(lineItem.getProductId());
            if(product.isEmpty())
                throw new JdxServiceException("No fulfillment found for and cannot create one due to an invalid product Id");
            List<Laboratory> labProviders = product.get().getLaboratoryProviders();
            if(labProviders == null || labProviders.size() < 1)
                throw new JdxServiceException("No fulfillment found and cannot create one due to no laboratory provider set for product");

            //Just grab the first lab provider for now
            Laboratory laboratory = labProviders.get(0);
            oFulfillment = createNewFulfillment(product.get(), order.getCustomer(), order.getCustomer().getPrimaryAddress(), laboratory);
            lineItem.addFulfillment(oFulfillment);
        } else
            oFulfillment = fulfillment.get();

        oFulfillment.setKit(kit);
        oFulfillment.setMeta(buildMeta(updater));

        //Notify lims about this new kit. Maybe we should wait to inform LIMS about this order until
        //ElementsOrder limsOrder = updateLimsWithOrder(order, lineItem, newRun, kit, updater);
        //Need a means to update the order in lims

       // if(limsOrder == null)
       //     throw new JdxServiceException("Failed to send order to lims");

        //kit.setAddedToLimsAt(Calendar.getInstance());
        //kit.setLimsId(String.valueOf(limsOrder.getKit().getId()));

        kit.setAssigned(true);
        kit.setMeta(updateMeta(kit.getMeta(), updater));
        kitService.updateKit(kit, updater);
    }

    public Order updateOrderWithNewStatus(String orderId, OrderStatus newStatus, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (orderId == null)
                throw new JdxServiceException("Cannot update order status since the orderId is not provided");

            if (newStatus == null)
                throw new JdxServiceException("Cannot update order status with a new status to set it to");

            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isEmpty())
                throw new JdxServiceException("Cannot update order status since the order is not found");

            newStatus.setOrder(order.get());
            newStatus.setUpdatedAt(Calendar.getInstance());
            newStatus.setCurrent(true);

            //Cheat here and update the order to closed if any of the closure-worthy states are attained
            switch (newStatus.getStatusType()){
                case REFUNDED:
                    order.get().setOpen(false);
                    break;
                case RESULTS_VIEWED:
                    order.get().setOpen(false);
                    break;
                case CLOSED:
                    order.get().setOpen(false);
                    break;
                case CANCELED:
                    order.get().setOpen(false);
                    break;
                default:
                    break;
            }

            if(!order.get().getOrderStatusHistory().stream().anyMatch(x->x.getStatusType().equals(newStatus.getStatusType()))) {

                order.get().addOrderStatus(newStatus);
                order.get().setMeta(updateMeta(order.get().getMeta(), updater));

                Order o = orderRepository.save(order.get());
                if (o != null)
                    sendOrderStatus(o, EventType.UPDATE);

                return o;
            }

            return order.get();


        } catch (Exception e){
            throw new JdxServiceException("Cannot update order status: " + e.getMessage());
        }
    }

    public Order closeOrder(String orderId, UserDetailsImpl updater) throws JdxServiceException {
        return null;
    }

    public Order cancelOrder(String orderId, UserDetailsImpl updater) throws JdxServiceException {
        return null;
    }

    public Order updateWithRedraw(String orderId, String orderLineItemId, String runId, Boolean isCreate, Boolean isApprove, UserDetailsImpl updater) throws JdxServiceException {
        try {

            if (orderId == null || orderLineItemId == null)
                throw new JdxServiceException("Cannot set order to redraw since orderId or lineItemId is not present in the request");

            if (runId == null)
                throw new JdxServiceException("Do not have test run information to use to create a new redraw request");

            if(isCreate != null && isApprove != null){
                if(isCreate == true && isApprove == true)
                    throw new JdxServiceException("Cannot both create and approve a redraw request at the same time");
            }

            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isEmpty())
                throw new JdxServiceException("Cannot find order to create a redraw request against");

            if(order.get().isRequiresRedraw()) {
                order.get().setOpen(true);

                //If we have an existing requiresRedraw, if we're trying to approve it, then continue, otherwise do not create a new one
                Optional<CustomerActionRequest> foundRequest = order.get().getCustomerActionRequests().stream().filter(x->x.getCustomerActionRequestType().equals(CustomerActionRequestType.REDRAW) && !x.getApproved()).findAny();
                if(foundRequest.isPresent()) {
                    if(isCreate) {
                        logger.info("Cannot create a new redraw request since one is already open and awaiting approval");
                        return order.get();
                    } else if(isApprove) {
                        foundRequest.get().setApproved(true);
                        foundRequest.get().setApprovalDate(Calendar.getInstance());

                        //Create a new fulfillment for the lineItem in this order
                        Optional<OrderLineItem> lineItem = order.get().getLineItems().stream().filter(x -> x.getId().equals(orderLineItemId)).findFirst();
                        if (lineItem.isEmpty())
                            throw new JdxServiceException("Cannot find line item associated with this order to create a redraw request for");

                        if(lineItem.get().getProductId() != null) {
                            Optional<Product> product = productService.get(lineItem.get().getProductId());
                            if (product.isEmpty())
                                throw new JdxServiceException("Cannot finish creating a new fulfillment for approved redraw request as product is missing");

                            //TODO refactor this when we support >1 laboratory
                            Optional<Laboratory> laboratory = laboratoryService.getDefaultLaboratory();
                            if (laboratory.isEmpty())
                                throw new JdxServiceException("Cannot find laboratory to assign to order");

                            for(TestRun t : lineItem.get().getLaboratoryOrderDetails().getTestRuns())
                                t.setCompleted(true);


                            //close out any active fulfillments attached to this line item, if still open
                            for(Fulfillment f : lineItem.get().getFulfillments())
                                f.setCompleted(true);

                            //TODO this is where a new fulfillment should be added, why is it not showing up?
                            lineItem.get().addFulfillment(createNewFulfillment(product.get(), order.get().getCustomer(), order.get().getCustomer().getPrimaryAddress(), laboratory.get()));

                            order.get().setRequiresShipment(true);

                            updateOrderStatus(order.get(), OrderStatusType.REDRAW_APPROVED);
                        }

                        //order.requiredRedraw is not reset to false until the redraw has been fulfilled
                    }
                } else
                    throw new JdxServiceException("Order is in an invalid state, requiresRedraw is true by there is is not customer action request associated to it");
            } else {
                //Create a new customer action request and set the order to requiresRedraw mode
                Optional<OrderLineItem> lineItem = order.get().getLineItems().stream().filter(x -> x.getId().equals(orderLineItemId)).findFirst();
                if (lineItem.isEmpty())
                    throw new JdxServiceException("Cannot find line item associated with this order to create a redraw request for");

                LaboratoryOrder laboratoryOrder = lineItem.get().getLaboratoryOrderDetails();
                if (laboratoryOrder == null)
                    throw new JdxServiceException("There was an issue obtaining laboratory order details for this redraw request");

                Optional<TestRun> testRun = laboratoryOrder.getTestRuns().stream().filter(x -> x.getId().equals(runId)).findFirst();
                if (testRun.isEmpty())
                    throw new JdxServiceException("Could not validate that the test run information provided is found in this order");

                testRun.get().setRedraw(true);
                if(testRun.get().getReport() != null ) {
                    testRun.get().getReport().setReportable(false);
                    testRun.get().getReport().setAvailable(false);
                }
                testRun.get().setCompleted(true);

                LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
                laboratoryStatus.setStatus(LaboratoryStatusType.REDRAW);
                laboratoryStatus.setCurrent(true);
                laboratoryStatus.setCreatedAt(Calendar.getInstance());
                laboratoryStatus.setCreatedBy(updater.getEmail());
                laboratoryStatus.setTestRun(testRun.get());

                testRun.get().addStatus(laboratoryStatus);

                laboratoryOrder.setReportableTestRunId(null);
                laboratoryOrder.setReportableTestReportId(null);
                laboratoryOrder.setEstArrivalInLab(null);
                laboratoryOrder.setMeta(updateMeta(laboratoryOrder.getMeta(), updater));

                CustomerActionRequest request = new CustomerActionRequest();
                request.setActive(true);
                request.setCustomerActionRequestType(CustomerActionRequestType.REDRAW);
                Calendar sevenDaysOut = Calendar.getInstance();
                sevenDaysOut.add(Calendar.DATE, 7);
                request.setResolveByDate(sevenDaysOut);
                request.setOrder(order.get());
                request.setLineItemId(lineItem.get().getId());
                request.setApproved(false);
                request.setMeta(buildMeta(updater));

                order.get().addCustomerActionRequest(request);

                order.get().setRequiresRedraw(true);
                order.get().setResultsAvailable(false);
                order.get().setOpen(true);

                updateOrderStatus(order.get(), OrderStatusType.REDRAW_REQUESTED);
            }

            Order o = orderRepository.save(order.get());

            if (o != null)
                sendOrderStatus(o, EventType.UPDATE);

            return o;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create a new redraw request for order");
        }
    }

    public Order updateWithRetest(String orderId, String orderLineItemid, String kitId, UserDetailsImpl updater){
        return null;
    }

    public Order updateWithRefund(String orderId, String orderLineItemId, Boolean isCreate, Boolean isApprove, UserDetailsImpl updater) throws JdxServiceException {
        try {

            if (orderId == null || orderLineItemId == null)
                throw new JdxServiceException("Cannot set order to refund since orderId or lineItemId is not present in the request");

            if (isCreate != null && isApprove != null) {
                if (isCreate == true && isApprove == true)
                    throw new JdxServiceException("Cannot both create and approve a refund request at the same time");
            }

            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isEmpty())
                throw new JdxServiceException("Cannot find order to create a refund request against");

            if(isCreate) {
                updateOrderStatus(order.get(), OrderStatusType.REFUND_PROCESSING);
                /*
                if(!order.get().getCurrentStatus().equals(OrderStatusType.REFUND_PROCESSING)) {
                    OrderStatus refundStatus = new OrderStatus();
                    refundStatus.setStatusType(OrderStatusType.REFUND_PROCESSING);
                    refundStatus.setOrder(order.get());
                    refundStatus.setUpdatedAt(Calendar.getInstance());
                    refundStatus.setCurrent(true);

                    order.get().addOrderStatus(refundStatus);
                }
                 */
            } else if(isApprove){
                //Submit refund request to stripe here, process if successful
                Transaction intent = order.get().getChargeTransaction();;
                if(intent == null)
                    throw new JdxServiceException("Cannot issue refund as the payment intent associated with the card change transaction cannot be found");

                Transaction refund = stripeService.issueRefund(intent.getExternalTransactionId());
                if(refund == null)
                    throw new JdxServiceException("Cannot refund charge with payment processor");

                refund.setOrder(order.get());

                updateOrderStatus(order.get(), OrderStatusType.REFUNDED);
                /*
                OrderStatus refundStatus = new OrderStatus();
                refundStatus.setStatusType(OrderStatusType.REFUNDED);
                refundStatus.setOrder(order.get());
                refundStatus.setUpdatedAt(Calendar.getInstance());
                refundStatus.setCurrent(true);

                order.get().addOrderStatus(refundStatus);

                 */

                order.get().addTransaction(refund);
                order.get().setMeta(updateMeta(order.get().getMeta(), updater));

                //TODO do other refund related activities here to ensure that any lab processing is closed out
            } else
                throw new JdxServiceException("Cannot process refund update as neither create nor approve is true");

            return orderRepository.save(order.get());

        } catch (Exception e) {
            throw new JdxServiceException("Cannot create refund request: " + e.getMessage());
        }
    }

    public Order updateOrderFromLabReceipt(String sampleNumber, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (sampleNumber == null)
                throw new JdxServiceException("Cannot update order from receipt as kitId is not provided");

            Optional<Kit> kit = kitService.findKitBySampleNumber(sampleNumber);
            if(kit.isEmpty())
                throw new JdxServiceException("Cannot find kit associated with sample");

            //Find order associated to this sample
            Optional<TestRun> testRun = testRunService.getTestRunForKitId(kit.get().getId());
            if (testRun.isEmpty())
                throw new JdxServiceException("Cannot find test run associated with kit");

            Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRunId(testRun.get().getId());
            if (labOrder.isEmpty())
                throw new JdxServiceException("Cannot find lab order associated with kit");

            if (labOrder.get().getParentOrderId() == null)
                throw new JdxServiceException("Cannot find order, id or kit information not provided");

            Optional<Order> order = orderRepository.findById(labOrder.get().getParentOrderId());
            if (order.isEmpty())
                throw new JdxServiceException("Cannot find order to update from lab receipt");

            List<OrderLineItem> lineItems = order.get().getLineItems();
            if (lineItems == null || lineItems.size() == 0)
                throw new JdxServiceException("Cannot find any lineitems for order to update from lab");

            if(!testRun.get().getStatus().stream().filter(x->x.getStatus().equals(LaboratoryStatusType.RECEIVED)).findAny().isPresent()) {
                LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
                laboratoryStatus.setStatus(LaboratoryStatusType.RECEIVED);
                laboratoryStatus.setCurrent(true);
                laboratoryStatus.setCreatedAt(Calendar.getInstance());
                laboratoryStatus.setCreatedBy(updater.getEmail());

                laboratoryStatus.setTestRun(testRun.get());
                testRun.get().addStatus(laboratoryStatus);
            }

            for (OrderLineItem item : lineItems) {
                Optional<Fulfillment> fulfillment = item.getFulfillments().stream().filter(x -> x.getKit().getId().equals(kit.get().getId())).findAny();
                if (fulfillment.isPresent()) {
                    if(!fulfillment.get().getShippingDetails().getShippingStatus().stream().filter(x->x.getStatus().equals(ShippingStatusType.RETURN_ARRIVED)).findAny().isPresent()) {
                        fulfillment.get().setCompleted(true);
                        ShippingStatus shippingStatus = new ShippingStatus();
                        shippingStatus.setStatus(ShippingStatusType.RETURN_ARRIVED);
                        shippingStatus.setStatusTimestamp(Calendar.getInstance());
                        shippingStatus.setCurrent(true);
                        shippingStatus.setToCustomer(false);

                        shippingStatus.setShippingDetails(fulfillment.get().getShippingDetails());
                        fulfillment.get().getShippingDetails().addShippingStatus(shippingStatus);
                        fulfillment.get().setMeta(updateMeta(fulfillment.get().getMeta(), updater));
                    }

                    updateOrderStatus(order.get(), OrderStatusType.RECEIVED);
                    /*
                    if(!order.get().getOrderStatusHistory().stream().filter(x->x.getStatusType().equals(OrderStatusType.RECEIVED)).findAny().isPresent()) {
                        OrderStatus orderStatus = new OrderStatus();
                        orderStatus.setStatusType(OrderStatusType.RECEIVED);
                        orderStatus.setCurrent(true);
                        orderStatus.setUpdatedAt(Calendar.getInstance());
                        orderStatus.setOrder(order.get());
                        order.get().addOrderStatus(orderStatus);
                    }

                     */

                    order.get().setMeta(updateMeta(order.get().getMeta(), updater));

                    Order o = orderRepository.save(order.get());

                    if(o != null)
                        sendOrderStatus(o, EventType.UPDATE);

                    return o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot process update from lab receipt: " + e.getMessage());
        }

        throw new JdxServiceException("Cannot update order for sample received by the lab");
    }

    public Order updateOrderFromLabProcessingWithReport(String reportId, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (reportId == null)
                throw new JdxServiceException("Cannot update order from receipt as kitId is not provided");

            //Optional<Kit> kit = kitService.findKitBySampleNumber(sampleNumber);
            //if(kit.isEmpty())
            //    throw new JdxServiceException("Cannot find kit associated with sample");

            //Find order associated to this sample
            Optional<TestRun> testRun = testRunService.findTestRunByLimsReportId(reportId);
            if (testRun.isEmpty())
                throw new JdxServiceException("Cannot find test run associated with kit");

            Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRunId(testRun.get().getId());
            if (labOrder.isEmpty())
                throw new JdxServiceException("Cannot find lab order associated with kit");

            if (labOrder.get().getParentOrderId() == null)
                throw new JdxServiceException("Cannot find order, id or kit information not provided");

            Optional<Order> order = orderRepository.findById(labOrder.get().getParentOrderId());
            if (order.isEmpty())
                throw new JdxServiceException("Cannot find order to update from lab receipt");

            List<OrderLineItem> lineItems = order.get().getLineItems();
            if (lineItems == null || lineItems.size() == 0)
                throw new JdxServiceException("Cannot find any lineitems for order to update from lab");

            if(!testRun.get().getStatus().stream().filter(x->x.getStatus().equals(LaboratoryStatusType.LAB_PROCESSING)).findAny().isPresent()) {
                LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
                laboratoryStatus.setStatus(LaboratoryStatusType.LAB_PROCESSING);
                laboratoryStatus.setCurrent(true);
                laboratoryStatus.setCreatedAt(Calendar.getInstance());
                laboratoryStatus.setCreatedBy(updater.getEmail());

                laboratoryStatus.setTestRun(testRun.get());
                testRun.get().addStatus(laboratoryStatus);
            }

            updateOrderStatus(order.get(),OrderStatusType.LABORATORY_PROCESSING);
            /*
            if(!order.get().getOrderStatusHistory().stream().filter(x->x.getStatusType().equals(OrderStatusType.LABORATORY_PROCESSING)).findAny().isPresent()) {
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setStatusType(OrderStatusType.LABORATORY_PROCESSING);
                orderStatus.setCurrent(true);
                orderStatus.setUpdatedAt(Calendar.getInstance());
                orderStatus.setOrder(order.get());
                order.get().addOrderStatus(orderStatus);
            }

             */

            order.get().setMeta(updateMeta(order.get().getMeta(), updater));

            Order o = orderRepository.save(order.get());

            if(o != null)
                sendOrderStatus(o, EventType.UPDATE);

            return o;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot process update from lab receipt: " + e.getMessage());
        }
    }

    public Order updateOrderFromLabProcessingWithSample(String sampleNumber, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (sampleNumber == null)
                throw new JdxServiceException("Cannot update order from receipt as kitId is not provided");

            Optional<Kit> kit = kitService.findKitBySampleNumber(sampleNumber);
            if(kit.isEmpty())
                throw new JdxServiceException("Cannot find kit associated with sample");

            //Find order associated to this sample
            Optional<TestRun> testRun = testRunService.getTestRunForKitId(kit.get().getId());
            if (testRun.isEmpty())
                throw new JdxServiceException("Cannot find test run associated with kit");

            Optional<LaboratoryOrder> labOrder = laboratoryOrderService.findLaboratoryOrderByTestRunId(testRun.get().getId());
            if (labOrder.isEmpty())
                throw new JdxServiceException("Cannot find lab order associated with kit");

            if (labOrder.get().getParentOrderId() == null)
                throw new JdxServiceException("Cannot find order, id or kit information not provided");

            Optional<Order> order = orderRepository.findById(labOrder.get().getParentOrderId());
            if (order.isEmpty())
                throw new JdxServiceException("Cannot find order to update from lab receipt");

            List<OrderLineItem> lineItems = order.get().getLineItems();
            if (lineItems == null || lineItems.size() == 0)
                throw new JdxServiceException("Cannot find any lineitems for order to update from lab");

            if(!testRun.get().getStatus().stream().filter(x->x.getStatus().equals(LaboratoryStatusType.LAB_PROCESSING)).findAny().isPresent()) {
                LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
                laboratoryStatus.setStatus(LaboratoryStatusType.LAB_PROCESSING);
                laboratoryStatus.setCurrent(true);
                laboratoryStatus.setCreatedAt(Calendar.getInstance());
                laboratoryStatus.setCreatedBy(updater.getEmail());

                laboratoryStatus.setTestRun(testRun.get());
                testRun.get().addStatus(laboratoryStatus);
            }

            updateOrderStatus(order.get(), OrderStatusType.LABORATORY_PROCESSING);
            /*
            if(order.get().getOrderStatusHistory().stream().filter(x->x.getStatusType().equals(OrderStatusType.LABORATORY_PROCESSING)).findAny().isPresent()) {
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setStatusType(OrderStatusType.LABORATORY_PROCESSING);
                orderStatus.setCurrent(true);
                orderStatus.setUpdatedAt(Calendar.getInstance());
                orderStatus.setOrder(order.get());
                order.get().addOrderStatus(orderStatus);
            }

             */

            order.get().setMeta(updateMeta(order.get().getMeta(), updater));

            Order o = orderRepository.save(order.get());

            if(o != null)
                sendOrderStatus(o, EventType.UPDATE);

            return o;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot process update from lab receipt: " + e.getMessage());
        }
    }

    public Order updateOrderFromShipping(Order order, String lineItemId, UserDetailsImpl updater) throws JdxServiceException {
        boolean kitAdded = false;
        boolean shipmentCreated = false;
        boolean kitShipped = false;

        if(order == null)
            throw new JdxServiceException("Cannot update order as the provided details are missing or corrupt.");

        /**
         * 1. Update all fulfillments that have been created
         * 2. For each kit assigned, create a new test run in the laboratoryOrder
         * 3. If a fulfillment has a kit and the labOrder has a kit, check that they match or whether its a new kit shipment
         */

        try {
            Optional<Order> foundOrder = orderRepository.findById(order.getId());
            if (foundOrder.isEmpty())
                throw new JdxServiceException("Cannot find order to update at id: " + order.getId());

            //Find the matching lineItem
            Optional<OrderLineItem> foundItem = foundOrder.get().getLineItems().stream().filter(x -> x.getId().equals(lineItemId)).findFirst();
            Optional<OrderLineItem> updateItem = order.getLineItems().stream().filter(x -> x.getId().equals(lineItemId)).findFirst();

            if (foundItem.isEmpty())
                throw new JdxServiceException("Cannot complete order update as there is a line item mismatch for " + lineItemId);

            if(updateItem.isEmpty())
                throw new JdxServiceException("Cannot update shipping for order since the order update does not contain the identified line item to change");

            Fulfillment fulfillmentUpdateData = null;
            if (updateItem.get().getFulfillments().size() == 1)
                fulfillmentUpdateData = updateItem.get().getFulfillments().get(0);
            else if (updateItem.get().getFulfillments().size() > 1)
                throw new JdxServiceException("Can't add two or more incomplete fulfillment data sets to the same line item.");
            else
                throw new JdxServiceException("No fulfillment data to add to line item " + updateItem.get().getId());

            if (fulfillmentUpdateData.getShippingDetails() != null && fulfillmentUpdateData.getShippingDetails().getToMethod() != null && fulfillmentUpdateData.getShippingDetails().getToMethod().isShipped())
                kitShipped = true;

            //Does this fulfillment object already exist for this lineItem, if so, we'll need to update that record
            Fulfillment finalFulfillmentUpdateData = fulfillmentUpdateData;
            Optional<Fulfillment> foundFulfillment = foundItem.get().getFulfillments().stream().filter(x -> x.getId().equals(finalFulfillmentUpdateData.getId())).findFirst();

            //If no existing fulfillment data, then this fulfillment object is new, so attach to the line item
            if (foundFulfillment.isEmpty()) {
                foundItem.get().getFulfillments().add(fulfillmentUpdateData);
                fulfillmentUpdateData.setOrderLineItem(foundItem.get());
                fulfillmentUpdateData.setMeta(buildMeta(updater));
            } else { //otherwise, merge with the existing fulfillment data because something was updated
                if (fulfillmentUpdateData.getKit() != null && fulfillmentUpdateData.getKit().getId() != null) {
                    Optional<Kit> foundKit = kitService.getKit(foundFulfillment.get().getKit().getId());
                    if (foundKit.isPresent())
                        foundFulfillment.get().setKit(foundKit.get());
                }
                //Update the shipping order id if different
                if (fulfillmentUpdateData.getFulfillmentOrderId() != null && !fulfillmentUpdateData.getFulfillmentOrderId().equals(foundFulfillment.get().getFulfillmentOrderId()))
                    foundFulfillment.get().setFulfillmentOrderId(fulfillmentUpdateData.getFulfillmentOrderId());

                //Update the shipping provider, if different
                if (fulfillmentUpdateData.getFulfillmentProvider() != null && fulfillmentUpdateData.getFulfillmentProvider() != foundFulfillment.get().getFulfillmentProvider())
                    foundFulfillment.get().setFulfillmentProvider(fulfillmentUpdateData.getFulfillmentProvider());

                //Update shipping details
                if (fulfillmentUpdateData.getShippingDetails() != null) {
                    ShippingMethod toMethod = fulfillmentUpdateData.getShippingDetails().getToMethod();
                    if (toMethod != null) {
                        toMethod.setShippingDetails(foundFulfillment.get().getShippingDetails());
                        foundFulfillment.get().getShippingDetails().setToMethod(toMethod);
                    }
                    ShippingMethod returnMethod = fulfillmentUpdateData.getShippingDetails().getReturnMethod();
                    if (returnMethod != null) {
                        returnMethod.setShippingDetails(foundFulfillment.get().getShippingDetails());
                        foundFulfillment.get().getShippingDetails().setReturnMethod(returnMethod);
                    }
                    if(fulfillmentUpdateData.getShippingDetails().getToAddress() != null && !fulfillmentUpdateData.getShippingDetails().getToAddress().equals(foundFulfillment.get().getShippingDetails().getToAddress())){
                        foundFulfillment.get().getShippingDetails().setToAddress(fulfillmentUpdateData.getShippingDetails().getToAddress());
                    }


                    foundFulfillment.get().setShippingDetails(fulfillmentUpdateData.getShippingDetails());
                }
                foundFulfillment.get().setMeta(updateMeta(foundFulfillment.get().getMeta(), updater));
            }

            //Update test run and kit data
            /*
            if (fulfillmentUpdateData.getKit() != null) {
                kitAdded = true;
                foundFulfillment.get().setKit(fulfillmentUpdateData.getKit());
                if (fulfillmentUpdateData.isRedraw())
                    foundItem.get().setLaboratoryOrderDetails(laboratoryOrderService.updateTestRunWithKit(foundItem.get().getLaboratoryOrderDetails(), foundFulfillment.get().getKit(), TestRunType.STANDARD, false, true));
                else
                    foundItem.get().setLaboratoryOrderDetails(laboratoryOrderService.updateTestRunWithKit(foundItem.get().getLaboratoryOrderDetails(), foundFulfillment.get().getKit(), TestRunType.STANDARD, false, false));
            }

             */
            //TODO need to test for the scenario where the shipment is created and shipped but

            if (kitShipped) {
                logger.info("ABout to update kit shipped");
                order = updateOrderStatus(foundOrder.get(), OrderStatusType.KIT_SHIPPED);
            }
            else if (kitAdded)
                order = updateOrderStatus(foundOrder.get(), OrderStatusType.KIT_ASSIGNED);
            else if (shipmentCreated)
                order = updateOrderStatus(foundOrder.get(), OrderStatusType.SHIPMENT_ORDER_CREATED);
            else
                order = updateOrderStatus(foundOrder.get(), OrderStatusType.KIT_AWAITING_SHIPPING);

            //Now that the shipment is created or fulfilled, the order no longer requires shipping
            order.setRequiresShipment(false);

            order.setMeta(updateMeta(order.getMeta(), updater));

            sendKitPreparedForShipping(order, foundFulfillment.get());

            return orderRepository.save(order);

        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update order with shipping details: " + e.getMessage());
        }
    }

    protected Order updateOrderStatus(Order order, OrderStatusType type) {
        OrderStatus status = new OrderStatus();
        if(order == null)
            return order;

        if(order.getOrderStatusHistory() == null || order.getOrderStatusHistory().size() == 0 || !order.getOrderStatusHistory().stream().filter(x->x.getStatusType().equals(type)).findAny().isPresent()) {
            status.setStatusType(type);
            status.setOrder(order);
            status.setCurrent(true);
            status.setUpdatedAt(Calendar.getInstance());

            order.addOrderStatus(status);
        } else
            logger.info("Already have a status of type: " + type);

        return order;
    }

    @Async
    public void sendOrderStatus(Order o, EventType eventType) throws JdxServiceException {
        try {
            //Send SNS a message that this order was created
            //TODO is there a non-blocking way to do this?
            if (o != null && !snsDisabled) {
                EntityPayload msg = new EntityPayload();
                msg.setEntity(o);
                msg.setEvent(eventType);
                msg.setEventTs(Calendar.getInstance());
                msg.setStatus(o.getCurrentStatus().name());

            logger.info("Sending Order update to SNS: " + o.getId());

            Ops_sendOrderCreateEmail(msg);
                SnsMessageResponse response = SnsMessageHandler.sendSnsMessage(orderTopic, msg);
                if(response.getResponse().sequenceNumber() == null)
                    throw new JdxServiceException("Failed to send SNS message");
            } else
                logger.info("Updated to SNS disabled for event of type: " + eventType );
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot send SNS message for order change: " + e.getMessage());
        }
    }

    protected ElementsOrder updateLimsWithOrder(Order o, OrderLineItem item, TestRun run, Kit kit, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if(o == null || item == null || kit == null)
                throw new JdxServiceException("Cannot update lims with invalid data");

            ElementsOrder elementsOrder = new ElementsOrder();
            ElementsKit elementsKit = new ElementsKit();
            elementsKit.setBarcode(kit.getCode());
            elementsKit.setExternalBarcode(kit.getCode());
            elementsKit.setExternalId(kit.getId());
            elementsKit.setCreateShipment(false);
            elementsKit.setCollectionType(CollectionType.Blood);
            elementsOrder.setKit(elementsKit);

            LaboratoryOrder labOrder = item.getLaboratoryOrderDetails();
            if(labOrder == null)
                throw new JdxServiceException("Cannot find Lab Order for this order update to LIMS");

            User patient = labOrder.getPatient();
            if(patient == null)
                throw new JdxServiceException("Cannot find patient associated with order to update in LIMS");

            ElementsPatient elementsPatient = new ElementsPatient();
            ElementsLocation elementsLocation = new ElementsLocation();
            String limsId = patient.getLimsContactId();
            if(limsId != null)
                elementsPatient.setExternalId(limsId);

            elementsPatient.setEmail(patient.getEmail());
            elementsPatient.setFirstName(patient.getFirstName());
            elementsPatient.setLastName(patient.getLastName());
            elementsPatient.setBirthDate(patient.getDateOfBirth());
            elementsPatient.setExternalId(patient.getId());
            elementsOrder.setPatient(elementsPatient);


            elementsLocation.setAddressOne(patient.getPrimaryAddress().getStreet());
            elementsLocation.setCity(patient.getPrimaryAddress().getCity());
            elementsLocation.setState(patient.getPrimaryAddress().getState());
            elementsLocation.setZip(patient.getPrimaryAddress().getPostalCode());
            elementsLocation.setCountry(patient.getPrimaryAddress().getCountry());
            elementsOrder.setPatientLocation(elementsLocation);

            ProviderApproval approvalInfo = labOrder.getProviderApproval();
            ElementsClinic clinic = new ElementsClinic();;
            ElementsPractitioner practitioner = new ElementsPractitioner();
            if(approvalInfo != null && approvalInfo.isRequiresApproval()) {
                Provider approvingProvider = approvalInfo.getApprovingProvider();
                Practice practice = null;
                if(approvingProvider != null) {
                    practice = practiceService.getPractice(approvingProvider.getPracticeId(), new String[0]);
                } else {
                    Optional<Practice> defaultPractice = practiceService.getDefaultPractice();
                    if (defaultPractice.isPresent())
                        practice = defaultPractice.get();
                    else
                        throw new JdxServiceException("Cannot set the clinic for this order as no practice was set and default practice is not available");

                    Optional<Provider> defaultProvider = providerService.getDefaultProviderForPractice(practice.getId());
                    if(defaultProvider.isPresent())
                        approvingProvider = defaultProvider.get();
                    else
                        throw new JdxServiceException("Approving provider not available for this order and default provider not set");

                    labOrder = laboratoryOrderService.saveLaboratoryOrder(labOrder, updater);
                }

                if (practice != null) {
                    if (practice.getLimsId() != null)
                        clinic.setId(Integer.valueOf(practice.getLimsId()));
                    else
                        throw new JdxServiceException("Cannot set the clinic for the order as the practice cannot be found");
                } else {
                    throw new JdxServiceException("Cannot set the clinic for the order as the practice cannot be found");
                }

                if (approvingProvider.getLimsId() != null)
                    practitioner.setId(approvingProvider.getLimsId());
                else
                    throw new JdxServiceException("Provider must exist and be registered in LIMS before creating an order");

                elementsOrder.setClinic(clinic);
                elementsOrder.setPractitioner(practitioner);
            } else { //set the default clinic and practicioner
                //Optional<Practice> defaultPractice = practiceService.getDefaultPractice();
                //if(defaultPractice.isEmpty())
                //    throw new JdxServiceException("No default practice available to send to LIMS");

                Optional<Provider> defaultProvider = providerService.getDefaultProvider();
                if(defaultProvider.isEmpty())
                    throw new JdxServiceException("No default provider available to send to LIMS");

               // if(defaultPractice.get().getLimsId() != null)
               //     clinic.setId(Integer.valueOf(defaultPractice.get().getLimsId()));
               // else
               //     throw new JdxServiceException("No LIMS ID set for default practice");
                elementsOrder.setClinic(clinic);
                practitioner.setFirstName(defaultProvider.get().getFirstName());
                practitioner.setLastName(defaultProvider.get().getLastName());
                practitioner.setNpi(defaultProvider.get().getNpi());

                elementsOrder.setPractitioner(practitioner);
            }

            Optional<Product> product = productService.get(item.getProductId());
            if(product.isEmpty())
                throw new JdxServiceException("Cannot find product to lookup report type for when trying to write to LIMS");

            if(product.get().getLimsReportId() == null)
                throw new JdxServiceException("LIMS report ID not set for product associated with this line item: " + product.get().getId());

            ElementsReport report = new ElementsReport();
            report.setReportTypeId(product.get().getLimsReportId());
            if(run != null)
                report.setExternalId(run.getLimsReportId());

            elementsOrder.setReport(report);

            List<String> consents = new ArrayList<>();
            //if(item.getLaboratoryOrderDetails().getPatientConsent().getFormName() != null)
            //    consents.add(item.getLaboratoryOrderDetails().getPatientConsent().getFormName());
            //else
            //    throw new JdxServiceException("Cannot provide LIMS with consents as they cannot be found in the order");

            consents.add("general");

            elementsOrder.setConsents(consents);

            return elementsService.createOrder(elementsOrder);

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot update lims with order details");
        }
    }

    public ActivationResponsePayload activateKit(ActivationPayload request) throws JdxServiceException {
        try {
            if (request == null)
                throw new JdxServiceException("Cannot activate kit as request payload is empty");

            UserDetailsImpl systemUser = UserDetailsImpl.build(userService.getSystemUser());
            if(systemUser == null)
                throw new JdxServiceException("Cannot obtain system user to activate kit and order");

            //TODO need to refactor this and find a more efficient way of looking up this order
            //Optional<Order> order = orderRepository.findOrderByCustomer_LastNameAndCustomer_DateOfBirth(request.getLastName(), request.getDob());
            List<Order> orders = orderRepository.findOrderByCustomer_LastNameOrderByOrderedAtDesc(request.getLastName());
            if (orders == null || orders.size() == 0)
                throw new JdxServiceException("Cannot find order for " + request.getLastName() + " with dob " + mapper.writeValueAsString(request.getDob()));

            boolean activatedKit = false;
            for(int l = 0; l < orders.size() && !activatedKit; l++) {
                Order order = orders.get(l);

                if (order.getCustomer().getDateOfBirth() != null && order.getCustomer().getDateOfBirth().equals(request.getDob())) {
                    if (order.getLineItems() != null) {
                        for (int i = 0; i < order.getLineItems().size() && !activatedKit; i++) {
                            OrderLineItem item = order.getLineItems().get(i);
                            if (item.getFulfillments() != null) {

                                for (int j = 0; j < item.getFulfillments().size() && !activatedKit; j++) {
                                    Fulfillment fulfillment = item.getFulfillments().get(j);
                                    Kit kit = fulfillment.getKit();

                               logger.info("Kit from fulfillment is " + fulfillment.getId() + " and from request " + request.getKitCode());

                                    if (kit != null && kit.getCode() != null && kit.getCode().equals(request.getKitCode())) {
                                        if (fulfillment.isCompleted())
                                            throw new JdxServiceException("Cannot activate kit for this order as it is already completed");

                                        kit.setActivated(true);
                                        kit.setMeta(updateMeta(kit.getMeta(), systemUser));
                                        activatedKit = true;

                                        //Update shipping status to arrived if not already
                                        if (!fulfillment.getShippingDetails().hasShippingStatusOfType(ShippingStatusType.ARRIVED)) {
                                            ShippingStatus newShippingStatus = new ShippingStatus();
                                            newShippingStatus.setStatus(ShippingStatusType.ARRIVED);
                                            newShippingStatus.setShippingDetails(fulfillment.getShippingDetails());
                                            newShippingStatus.setCurrent(true);
                                            newShippingStatus.setStatusTimestamp(Calendar.getInstance());
                                            newShippingStatus.setToCustomer(false);
                                            fulfillment.getShippingDetails().addShippingStatus(newShippingStatus);

                                            fulfillment.setMeta(updateMeta(fulfillment.getMeta(), systemUser));
                                        }

                                        LaboratoryOrder laboratoryOrder = item.getLaboratoryOrderDetails();
                                        boolean foundKitInLabOrder = false;
                                        if (laboratoryOrder.getTestRuns() != null) {
                                            for (TestRun run : laboratoryOrder.getTestRuns()) {
                                                Kit k = run.getKit();
                                                if (k.getCode() != null && k.getCode().equals(request.getKitCode())) {
                                                    foundKitInLabOrder = true;

                                                    if(!run.getStatus().stream().filter(x->x.getStatus().equals(LaboratoryStatusType.KIT_ACTIVATED)).findAny().isPresent())  {
                                                        LaboratoryStatus status = new LaboratoryStatus();
                                                        status.setStatus(LaboratoryStatusType.KIT_ACTIVATED);
                                                        status.setCurrent(true);
                                                        status.setCreatedAt(Calendar.getInstance());
                                                        status.setCreatedBy(systemUser.getEmail());
                                                        status.setTestRun(run);
                                                        run.addStatus(status);
                                                    }
                                                }
                                            }
                                            if (!foundKitInLabOrder)
                                                laboratoryOrder = laboratoryOrderService.updateTestRunWithKit(laboratoryOrder, kit, TestRunType.STANDARD, false, false);
                                        }

                                        laboratoryOrder.setMeta(updateMeta(laboratoryOrder.getMeta(), systemUser));

                                        /*
                                        if(!order.getOrderStatusHistory().stream().filter(x->x.getStatusType().equals(OrderStatusType.KIT_ACTIVATED)).findAny().isPresent()) {
                                            OrderStatus newStatus = new OrderStatus();
                                            newStatus.setStatusType(OrderStatusType.KIT_ACTIVATED);
                                            newStatus.setOrder(order);
                                            newStatus.setUpdatedAt(Calendar.getInstance());
                                            newStatus.setCurrent(true);
                                            order.addOrderStatus(newStatus);
                                        }
                                         */

                                        order = updateOrderStatus(order, OrderStatusType.KIT_ACTIVATED);

                                        sendOrderStatus(order, EventType.UPDATE);

                                        order.setMeta(updateMeta(order.getMeta(), systemUser));

                                        order = orderRepository.save(order);

                                        sendKitActivatedEmail(order);

                                        ActivationResponsePayload response = new ActivationResponsePayload();
                                        response.setCustomerId(order.getCustomer().getId());
                                        response.setCustomerEmail(order.getCustomer().getEmail());
                                        response.setCustomerFirstName(order.getCustomer().getFirstName());
                                        response.setCustomerLastName(order.getCustomer().getLastName());
                                        response.setCustomerActivated(order.getCustomer().isActivated());
                                        response.setOrderId(order.getId());
                                        response.setLineItemId(item.getId());
                                        response.setFulfillmentId(fulfillment.getId());
                                        response.setProductId(item.getProductId());
                                        response.setProductName(item.getProductName());

                                        return response;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(!activatedKit)
                throw new JdxServiceException("Unable to activate kit");
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot activate kit: " + e.getMessage());
        }
        throw new JdxServiceException("Unable to activate kit");
    }

    public void sendOrderCreateEmail(Order order) throws JdxServiceException {
        if(order != null && order.getLineItems().size() > 0) {
            String content = "Hi " + order.getCustomer().getFirstName() + ", we've received your order for " + order.getLineItems().get(0).getProductName() + " at " + order.getOrderedAt() + " \n";
            content += "Your order number is: " + order.getOrderNumber();
            if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - We've received your order!", content, false, false))
                throw new JdxServiceException("Cannot send order confirmation email");
        }
    }

    public void Ops_sendOrderCreateEmail(EntityPayload order) throws JdxServiceException {
        try {
            if (order != null) {
                String content = mapper.writeValueAsString(order);
                if (!mailService.sendEmail("jon@junodx.com", "JunoOps - dev - New SNS message!", content, false, false))
                    throw new JdxServiceException("Cannot send order ops email");
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot send ops email for order");
        }
    }

    public void sendKitActivatedEmail(Order order) throws JdxServiceException {
        if(order != null && order.getLineItems().size() > 0) {
            String content = "Hi " + order.getCustomer().getFirstName() + ", your kit is now activated for " + order.getLineItems().get(0).getProductName() +  "\n";
            content += "If you haven't already, please obtain your sample with the included instructions or click here for additional help";
            if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - You have activate your kit", content, false, false))
                throw new JdxServiceException("Cannot send kit activation email");
        }
    }

    public void sendKitPreparedForShipping(Order order, Fulfillment fulfillment) throws JdxServiceException {
        if(order != null && order.getLineItems().size() > 0) {
            String content = "Hi " + order.getCustomer().getFirstName() + ", your kit " + order.getLineItems().get(0).getProductName() +  " has been prepared to ship. it will be on its way soon.\n";
            content += "It is estimated to arrive by " + fulfillment.getShippingDetails().getToMethod().getEta();
            if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - Your kit is ready for shipping", content, false, false))
                throw new JdxServiceException("Cannot send kit shipment email");
        }
    }

    public void sendOrderHasShippedEmail(Order order, Fulfillment fulfillment) throws JdxServiceException {
        if(order != null && order.getLineItems().size() > 0) {
            String content = "Hi " + order.getCustomer().getFirstName() + ", your kit is now on its way for " + order.getLineItems().get(0).getProductName() +  "\n";
            content += "It is estimated to arrive by " + fulfillment.getShippingDetails().getToMethod().getEta();
            if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - Your kit has shipped", content, false, false))
                throw new JdxServiceException("Cannot send kit shipment email");
        }
    }

  public void updateOrderFromSalesforce(List<SalesforceRecordChanged> recordsChanged, List<SalesforceOrderUpdateAccountInfo> orderUpdateAccountList, UserDetailsImpl userContext) {
    int orderUpdate = 0;
    for (SalesforceOrderUpdateAccountInfo orderInfo : orderUpdateAccountList) {
      if (orderInfo.getId().isBlank()) {
        logger.error("user id is null");
        continue;
      }
      String orderId = orderInfo.getOrderId();
      Optional<Order> orderOptional = orderRepository.findById(orderId);
      if (orderOptional.isPresent() != true) {
        logger.error("order not found with the id {} ", orderInfo.getId());
        continue;
      }
      Order order = orderOptional.get();
      if (!orderId.isBlank() && !order.getId().equals(orderId)) {
        logger.error("Salesforce Id and Order Id is not Matching.");
        logger.error("Salesforce Id is ::  " + orderId);
        logger.error("Order Id is::  " + order.getId());
        continue;
      }
      String accountId = orderInfo.getAccountId();
      boolean updated = false;
      if (!accountId.isBlank() && !order.getCustomer().getFirstName().equals(accountId)) {
        order.getCustomer().setFirstName(accountId);
        updated = true;
      }
      String activatedById = orderInfo.getActivatedById();
      if (!activatedById.isBlank() && !order.getCustomer().getUsername().equals(activatedById)) {
        order.getCustomer().setUsername(activatedById);
        updated = true;
      }
      if (order.getShippingOrderId() != null) {
        String carrierAddress1 = orderInfo.getCarrierAddress1();
        Optional<ShippingCarrier> shippingCarrierOptional = shippingCarrierRepository.findById(order.getShippingOrderId());
        ShippingCarrier shippingCarrier = shippingCarrierOptional.get();
        if (!carrierAddress1.isBlank() && !shippingCarrier.getContactAddress().getName().equals(carrierAddress1)) {
          shippingCarrier.getContactAddress().setName(carrierAddress1);
          updated = true;
        }
        String carrierAddress2 = orderInfo.getCarrierAddress2();
        if (!carrierAddress2.isBlank() && !shippingCarrier.getContactAddress().getStreet().equals(carrierAddress2)) {
          shippingCarrier.getContactAddress().setStreet(carrierAddress2);
          updated = true;
        }
        String carrierCity = orderInfo.getCarrierAddress2();
        if (!carrierCity.isBlank() && !shippingCarrier.getContactAddress().getCity().equals(carrierCity)) {
          shippingCarrier.getContactAddress().setCity(carrierCity);
          updated = true;
        }
        String carrierCountry = orderInfo.getCarrierCountry();
        if (!carrierCountry.isBlank() && !shippingCarrier.getContactAddress().getCountry().equals(carrierCountry)) {
          shippingCarrier.getContactAddress().setCountry(carrierCountry);
          updated = true;
        }
        String carrierName = orderInfo.getCarrierName();
        if (!carrierName.isBlank() && !shippingCarrier.getName().equals(carrierName)) {
          shippingCarrier.setName(carrierName);
          updated = true;
        }
        String carrierPhone = orderInfo.getCarrierPhone();
        if (!carrierPhone.isBlank() && !shippingCarrier.getContactPhone().getPhoneNumber().equals(carrierPhone)) {
          shippingCarrier.getContactPhone().setPhoneNumber(carrierPhone);
          updated = true;
        }
        String carrierState = orderInfo.getCarrierName();
        if (!carrierState.isBlank() && !shippingCarrier.getContactAddress().getState().equals(carrierState)) {
          shippingCarrier.getContactAddress().setState(carrierState);
          updated = true;
        }
        String carrierZip = orderInfo.getCarrierPhone();
        if (!carrierZip.isBlank() && !shippingCarrier.getContactAddress().getPostalCode().equals(carrierZip)) {
          shippingCarrier.getContactAddress().setPostalCode(carrierZip);
          updated = true;
        }
      }
      String cartId = orderInfo.getCartId();
      if (!cartId.isBlank() && !order.getCartId().equals(cartId)) {
        order.setCartId(cartId);
        updated = true;
      }
      String checkoutId = orderInfo.getCheckoutId();
      if (!checkoutId.isBlank() && !order.getCheckoutId().equals(checkoutId)) {
        order.setCheckoutId(checkoutId);
        updated = true;
      }
     /* String clinicianNotes = orderInfo.getClinicianNotes();
      if (!clinicianNotes.isBlank() && !order.getCustomer().getPatientDetails().getNotes().contains(clinicianNotes)) {
        Note notes = new Note();
        notes.setMedicalNoteId(clinicianNotes);
        order.getCustomer().getPatientDetails().getNotes().add(notes);
        updated = true;
      }
      String createdById = orderInfo.getCreatedById();
      if (!createdById.isBlank() && !order.getMeta().getCreatedById().equals(createdById)) {
        logger.info("Created By Id is Not Changeable.");
        logger.info("SF Sent createdById" + createdById);
        logger.info("Order Object Containing createdById" + order.getMeta().getCreatedById());
      }*/
      String currency = orderInfo.getCurrency();
      if (!currency.isBlank() && !order.getCurrency().equals(currency)) {
        order.setCurrency(Currency.getInstance(currency));
        updated = true;
      }
    /*  String description = orderInfo.getDescription();
      if (!description.isBlank()) {
        if (StringUtils.isNotBlank(order.getNotes()) && !order.getNotes().equals(description))
          order.setNotes(description);
        if (StringUtils.isBlank(order.getNotes()))
          order.setNotes(description);
        updated = true;
      }*/
      String discountAmount = orderInfo.getDiscountAmount();
      if (!discountAmount.isBlank() && order.getDiscount().getAmountDiscounted() != Float.parseFloat(discountAmount)) {
        order.getDiscount().setAmountDiscounted(Float.parseFloat(discountAmount));
        updated = true;
      }
      String discountApplied = orderInfo.getDiscountApplied();
      if (!discountApplied.isBlank() && order.getDiscount().isDiscountApplied() != Boolean.valueOf(discountApplied)) {
        order.getDiscount().setDiscountApplied(Boolean.valueOf(discountApplied));
        updated = true;
      }
      String discountMode = orderInfo.getDiscountMode();
      if (!discountMode.isBlank() && !order.getDiscount().getMode().toString().equals(discountMode)) {
        order.getDiscount().setMode(Enum.valueOf(DiscountMode.class, discountMode));
        updated = true;
      }
      String discountType = orderInfo.getDiscountType();
      if (!discountType.isBlank() && order.getDiscount().getType().toString().equals(discountType)) {
        order.getDiscount().setType(Enum.valueOf(DiscountType.class, discountType));
        updated = true;
      }
      String gestationalAge = orderInfo.getGestationalAge();
      if (!gestationalAge.isBlank() && order.getCustomer().getPatientDetails().getMedicalDetails().getGestationalAge() != Float.parseFloat(gestationalAge)) {
        order.getCustomer().getPatientDetails().getMedicalDetails().setGestationalAge(Float.parseFloat(gestationalAge));
        updated = true;
      }
      String insuranceBillingOrderId = orderInfo.getInsuranceBillingOrderId();
      if (!insuranceBillingOrderId.isBlank()) {
        if (StringUtils.isNotBlank(order.getInsuranceBillingOrderId()) && order.getInsuranceBillingOrderId().equals(insuranceBillingOrderId))
          order.setInsuranceBillingOrderId(insuranceBillingOrderId);
        if (StringUtils.isBlank(order.getInsuranceBillingOrderId()))
          order.setInsuranceBillingOrderId(insuranceBillingOrderId);
        updated = true;
      }
    /*  String lastModifiedById = orderInfo.getLastModifiedById();
      if (!lastModifiedById.isBlank() && !order.getMeta().getLastModifiedById().equals(lastModifiedById)) {
        order.getMeta().setLastModifiedById(lastModifiedById);
        updated = true;
      }*/
      String totalAmount = orderInfo.getTotalAmount();
      if (!totalAmount.isBlank() && order.getAmount() != Float.parseFloat(totalAmount)) {
        order.setAmount(Float.parseFloat(totalAmount));
        updated = true;
      }
      try {
        String orderedAt = orderInfo.getOrderedAt();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = sdf.parse(orderedAt);
        cal.setTime(date);
        if (!orderedAt.isBlank() && !order.getOrderedAt().equals(orderedAt)) {
          order.setOrderedAt(cal);
          updated = true;
        }
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      String isPregnant = orderInfo.getIsPregnant();
      if (!isPregnant.isBlank() && order.getCustomer().getPatientDetails().getMedicalDetails().isPregnant() != Boolean.valueOf(isPregnant)) {
        order.getCustomer().getPatientDetails().getMedicalDetails().setPregnant(Boolean.valueOf(isPregnant));
        updated = true;
      }
      String priceBook2Id = orderInfo.getPriceBook2Id();
      if (!priceBook2Id.isBlank()) {
        if (StringUtils.isNotBlank(order.getPriceBookId()) && !order.getPriceBookId().equals(priceBook2Id))
          order.setPriceBookId(priceBook2Id);
        if (StringUtils.isBlank(order.getPriceBookId()))
          order.setPriceBookId(priceBook2Id);
        updated = true;
      }
      String KitId = orderInfo.getKitId();
      String shipping = orderInfo.getShipping();
      if (!shipping.isBlank() && !KitId.isBlank()) {
        for (OrderLineItem lineItem : order.getLineItems()) {

          for (Fulfillment fulfillment : lineItem.getFulfillments()) {
            Kit kit = fulfillment.getKit();
            if (kit.getId().equals(KitId)) {
              fulfillment.getShippingDetails().getShippingTransactionDetails().setAmount(Float.parseFloat(shipping));
              updated = true;
            }
          }
        }
      }
      String shippingAddress = orderInfo.getShippingAddress();
      com.junodx.api.models.core.Address addr = new com.junodx.api.models.core.Address();
      for (String s : shippingAddress.split(",")) {
        String[] e = s.split(":");
        switch (e[0]) {
          case "address_person_name":
            addr.setName(e[1]);
            break;
          case "address_street":
            addr.setName(e[1]);
            break;
          case "address_city":
            addr.setName(e[1]);
            break;
          case "address_state":
            addr.setName(e[1]);
            break;
          case "address_postalCode":
            addr.setName(e[1]);
            break;
          case "primaryMailingAddress":
            addr.setName(e[1]);
            break;
          case "is_resdential":
            addr.setName(e[1]);
        }
      }
      if (!shippingAddress.isBlank() && !KitId.isBlank()) {
        for (OrderLineItem lineItem : order.getLineItems()) {
          for (Fulfillment fulfillment : lineItem.getFulfillments()) {
            Kit kit = fulfillment.getKit();
            if (kit.getId().equals(KitId)) {
              fulfillment.getShippingDetails().getToAddress().setAddress(addr);
              updated = true;
            }
          }
        }
      }
      String shippingOrderId = orderInfo.getShippingOrderId(); //need to set fulfillment order id.
      if (!shippingOrderId.isBlank() && !KitId.isBlank()) {
        for (OrderLineItem lineItem : order.getLineItems()) {
          for (Fulfillment fulfillment : lineItem.getFulfillments()) {
            Kit kit = fulfillment.getKit();
            if (kit.getId().equals(KitId)) {
              fulfillment.setFulfillmentOrderId(shippingOrderId);
              updated = true;
            }
          }
        }
      }
      String status = orderInfo.getStatus();
      if (!status.isBlank() && order.getOrderStatusHistory().stream().anyMatch(OrderStatus::isCurrent)) {
        OrderStatus newOrderStatus = new OrderStatus();
        for (OrderStatus orderStatus : order.getOrderStatusHistory()) {
          if (orderStatus.isCurrent() && !orderStatus.getStatusType().equals(status)) {
            orderStatus.setCurrent(false);
            newOrderStatus.setStatusType(Enum.valueOf(OrderStatusType.class, status));
            newOrderStatus.setCurrent(true);
            newOrderStatus.setOrder(order);
            updated = true;
            break;
          }
        }
        order.getOrderStatusHistory().add(newOrderStatus);
      }
      String subTotal = orderInfo.getSubTotal();
      if (!subTotal.isBlank() && order.getSubTotal() != Float.parseFloat(subTotal)) {
        order.setSubTotal(Float.parseFloat(subTotal));
        updated = true;
      }
      String threeOrMore = orderInfo.getThreeOrMore();
      if (!threeOrMore.isBlank() && order.getCustomer().getPatientDetails().getMedicalDetails().isThreeOrMoreFetuses() != Boolean.valueOf(threeOrMore)) {
        order.getCustomer().getPatientDetails().getMedicalDetails().setThreeOrMoreFetuses(Boolean.valueOf(threeOrMore));
        updated = true;
      }
      String weight = orderInfo.getWeight();
      if (!weight.isBlank() && order.getCustomer().getPatientDetails().getMedicalDetails().getVitals().contains(VitalType.WEIGHT)) {
        List<Vital> vitalList = order.getCustomer().getPatientDetails().getMedicalDetails().getVitals();
        for (Vital vital : vitalList) {
          if (vital.getType().name().equals(VitalType.WEIGHT.name())) {
            vital.setValue(weight);
          }
        }
      }
      String taxSF = orderInfo.getTax();
      if (!taxSF.isBlank()) {
        if (order.getTax() != null && order.getTax().getAmount() != Float.parseFloat(taxSF))
          order.getTax().setAmount(Float.parseFloat(taxSF));
        if (order.getTax() == null) {
          Tax tax = new Tax();
          tax.setAmount(Float.parseFloat(taxSF));
          order.setTax(tax);
        }
        updated = true;
      }

      OrderLineItemInfo lineItems = orderInfo.getLineItems();
      updateLineItemInfoInOrder(lineItems.getRecords(), userContext);

      if (updated) {
        logger.info("updating user {} ", orderInfo.getId());
        Meta.setMeta(order.getMeta(), userContext);
//        orderRepository.save(order);
        orderUpdate++;
      }
    }
  }

  private void updateLineItemInfoInOrder(List<SalesforceLineItemUpdateInfo> lineItemsInfoList, UserDetailsImpl userContext) {
    for (SalesforceLineItemUpdateInfo lineItemInfo : lineItemsInfoList) {
      if (lineItemInfo.getLineItemId().isBlank()) {
        logger.error("LineItem id is null");
        continue;
      }
      String orderId = lineItemInfo.getOrderId();
      String lineItemId = lineItemInfo.getLineItemId();

      Optional<Order> orderOptional = orderRepository.findById(orderId);
      if (orderOptional.isPresent() != true) {
        logger.error("order not found with the id {} ", orderId);
        continue;
      }
      Order order = orderOptional.get();
      if (!orderId.isBlank() && !order.getId().equals(orderId)) {
        logger.error("Salesforce Id and Order Id is not Matching.");
        logger.error("Salesforce Id is ::  " + orderId);
        logger.error("Order Id is::  " + order.getId());
        continue;
      }
      OrderLineItem lineItem = orderLineitemRepository.findOrderLineItemByIdAndOrder_id(lineItemId, orderId);
      String amount = lineItemInfo.getAmount();
      if (!amount.isBlank() && lineItem.getAmount() != Float.parseFloat(amount)) {
        lineItem.setAmount(Float.parseFloat(amount));
      }
      String amountDiscounted = lineItemInfo.getAmountDiscounted();
      if (!amountDiscounted.isBlank()) {
        if (order.getDiscount() != null && order.getDiscount().getAmountDiscounted() != Float.parseFloat(amountDiscounted))
          order.getDiscount().setAmountDiscounted(Float.parseFloat(amountDiscounted));
      }
      String availableQuantity = lineItemInfo.getAvailableQuantity();
      if (!availableQuantity.isBlank() && lineItem.getQuantity() != Integer.parseInt(availableQuantity)) {
        lineItem.setQuantity(Integer.parseInt(availableQuantity));
      }
     /* String clinicaianNotes = lineItemInfo.getClinicianNotes();
      if(!clinicaianNotes.isBlank()){
        if(lineItem.getLaboratoryOrderDetails() != null && lineItem.getLaboratoryOrderDetails().getNotes().equals(clinicaianNotes))
          lineItem.getLaboratoryOrderDetails().setNotes(clinicaianNotes);
      }*/
      String consentApproval = lineItemInfo.getConsentApproval();
      String createdById = lineItemInfo.getCreatedById();
      String isDirectlyProvided = lineItemInfo.getIsDirectlyProvided();
      if (!isDirectlyProvided.isBlank() && lineItem.isDirectlyProvided() != Boolean.valueOf(isDirectlyProvided)) {
        lineItem.setDirectlyProvided(Boolean.valueOf(isDirectlyProvided));
      }
      String isInOfficeCollected = lineItemInfo.getIsInOfficeCollected();
      if (!isInOfficeCollected.isBlank() && lineItem.isInOfficeCollected() != Boolean.valueOf(isInOfficeCollected)) {
        lineItem.setInOfficeCollected(Boolean.valueOf(isInOfficeCollected));
      }
      String insurenceEstimatedCoveredAmount = lineItemInfo.getInsurenceEstimatedCoveredAmount();
      if (!insurenceEstimatedCoveredAmount.isBlank() && lineItem.getInsuranceEstimatedCoveredAmount() != Float.parseFloat(insurenceEstimatedCoveredAmount)) {
        lineItem.setInsuranceEstimatedCoveredAmount(Float.parseFloat(insurenceEstimatedCoveredAmount));
      }
      String description = lineItemInfo.getDescription();
      if (!description.isBlank() && lineItem.getDescription().equals(description)) {
        lineItem.setDescription(description);
      }
      String orderItemNumber = lineItemInfo.getOrderItemNumber();
      if (!orderItemNumber.isBlank() && lineItem.getProductId().equals(orderItemNumber)) {
        lineItem.setProductId(orderItemNumber);
      }
      String quantity = lineItemInfo.getQuantity();
      if (!quantity.isBlank() && lineItem.getQuantity() != Integer.parseInt(quantity)) {
        lineItem.setQuantity(Integer.parseInt(quantity));
      }
      String referralType = lineItemInfo.getReferralType();
      if (!referralType.isBlank() && lineItem.getLaboratoryOrderDetails().getOrderType().equals(OrderType.valueOf(referralType))) {
        lineItem.getLaboratoryOrderDetails().setOrderType(OrderType.valueOf(referralType));
      }
      String requiresShipping = lineItemInfo.getRequiresShipping();
      if (!requiresShipping.isBlank() && lineItem.isRequiresShipping() != Boolean.valueOf(requiresShipping)) {
        lineItem.setRequiresShipping(Boolean.valueOf(requiresShipping));
      }
      String SKU = lineItemInfo.getSKU();
      if (!SKU.isBlank() && lineItem.getSku().equals(SKU)) {
        lineItem.setSku(SKU);
      }
      logger.info("updating OrderLineItem {} ", lineItem.getId());
      Meta.setMeta(order.getMeta(), userContext);
      Meta.setMeta(lineItem.getMeta(), userContext);
    }
  }

  public OrderCountsPayload getCounts(Calendar after) {
        OrderCountsPayload payload = new OrderCountsPayload();
        payload.setRequiresShippingCount(orderRepository.countOfRequiresShipment(after));
        payload.setRequiresRedrawCount(orderRepository.countOfRequiresRedraw(after));

        return payload;
  }
}
