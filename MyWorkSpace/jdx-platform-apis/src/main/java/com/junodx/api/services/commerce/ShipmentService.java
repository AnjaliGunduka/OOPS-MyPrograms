package com.junodx.api.services.commerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.connectors.shippo.payloads.MultipleRatesPayload;
import com.junodx.api.connectors.shippo.payloads.OrderShipmentPayload;
import com.junodx.api.connectors.shippo.payloads.ShippoAddressForm;
import com.junodx.api.connectors.shippo.payloads.ShippoCreateLabelRequest;
import com.junodx.api.connectors.shippo.payloads.ShippoParcel;
import com.junodx.api.connectors.shippo.payloads.ShippoShipmentPayload;
import com.junodx.api.connectors.shippo.payloads.ShippoStatusPayload;
import com.junodx.api.connectors.shippo.payloads.ShippoTrackingStatus;
import com.junodx.api.connectors.shippo.payloads.UpdateCustomerToAddress;
import com.junodx.api.dto.models.fulfillment.MultipleShippingLabelsDto;
import com.junodx.api.dto.models.fulfillment.ShipmentLabelDto;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.ShippingCarrier;
import com.junodx.api.models.fulfillment.ShippingMethod;
import com.junodx.api.models.fulfillment.ShippingStatus;
import com.junodx.api.models.fulfillment.ShippingTarget;
import com.junodx.api.models.fulfillment.ShippingTransaction;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;
import com.junodx.api.repositories.commerce.OrderRepository;
import com.junodx.api.repositories.fulfillment.FulfillmentProviderRepository;
import com.junodx.api.repositories.fulfillment.FulfillmentRepository;
import com.junodx.api.repositories.fulfillment.ShippingCarrierRepository;
import com.junodx.api.repositories.fulfillment.ShippingDetailsRepository;
import com.junodx.api.repositories.fulfillment.ShippingMethodRepository;
import com.junodx.api.repositories.fulfillment.ShippingStatusRepository;
import com.junodx.api.repositories.fulfillment.ShippingTargetRepository;
import com.junodx.api.repositories.fulfillment.ShippingTransactionRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserServiceImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.mail.MailService;
import com.junodx.api.util.DateUtil;
import com.shippo.Shippo;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import com.shippo.model.Shipment;
import com.shippo.model.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ShipmentService extends ServiceBase  {

  @Value("${shippo.api.key:shippo_test_bb41542ecd8d5563afaf39427a82d14985e15dcc}")
  private String apiKey;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  OrderService orderService;

  @Autowired
  ProductService productService;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  private MailService mailService;

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  FulfillmentProviderRepository fulfillmentProviderRepository;

  @Autowired
  ShippingCarrierRepository shippingCarrierRepository;

  @Autowired
  ShippingMethodRepository shippingMethodRepository;

  @Autowired
  ShippingDetailsRepository shippingDetailsRepository;

  @Autowired
  ShippingStatusRepository shippingStatusRepository;

  @Autowired
  ShippingTargetRepository shippingTargetRepository;

  @Autowired
  FulfillmentRepository fulfillmentRepository;

  @Autowired
  ShippingTransactionRepository shippingTransactionRepository;

  private static Logger logger = LoggerFactory.getLogger(ShipmentService.class);

  private ObjectMapper mapper;

  public ShipmentService(){
    mapper = new ObjectMapper();
  }

  public MultipleRatesPayload createShipmentOptionsForOrder(OrderShipmentPayload request) throws JdxServiceException {
    Shippo.setApiKey(apiKey);
    Shippo.setApiVersion("2018-02-08");
    try {

      Optional<Order> order = orderRepository.findById(request.getOrderId());
      if (!order.isPresent()) {
        logger.error("Could not find order with the order id {}", request.getOrderId());
        throw new JdxServiceException("Invalid order id.");
      }

      logger.info("order to update shipping for " + mapper.writeValueAsString(order.get()));

      if (request.getLineItemId() == null) {
        logger.info("No line item available for shipment label request");
        throw new JdxServiceException("No line items available for label creation");
      }

      Optional<OrderLineItem> item = order.get().getLineItems().stream().filter(x -> x.getId().equals(request.getLineItemId())).findFirst();
      OrderLineItem lineItem = null;
      if (item.isPresent())
        lineItem = item.get();
      else
        throw new JdxServiceException("Cannot find line item to update");

      //Find an unfulfilled
      Optional<Fulfillment> unfulfilled = lineItem.getFulfillments().stream().filter(x -> x.getId().equals(request.getFulfillmentId())).findFirst();
      if (unfulfilled.isEmpty()) {
        logger.info("No fuflfillments set for one or more line items in this order. Set fulfillment options before creating a shipment");
        throw new JdxServiceException("Cannot find a Fulfillment for line item " + lineItem);
      }

      Optional<Product> product = productService.get(lineItem.getProductId());
      if(product.isEmpty())
        throw new JdxServiceException("Cannot find product for line item");

      ShippoShipmentPayload toPayload = new ShippoShipmentPayload();
      ShippoShipmentPayload returnPayload = new ShippoShipmentPayload();

      List<ShippoParcel> parcels = new ArrayList<>();
      parcels.add(getShippoParcelFromProduct(product.get()));

      toPayload.setAddressTo(getShippoTargetAddress(order.get().getCustomer().getFirstName() + " " + order.get().getCustomer().getLastName(), order.get().getCustomer().getEmail(), unfulfilled.get().getShippingDetails().getToAddress().getAddress(), unfulfilled.get().getShippingDetails().getToAddress().getPhone()));
      toPayload.setAddressFrom(getShippoTargetAddress(unfulfilled.get().getFulfillmentProvider().getName(), "support@junodx.com", unfulfilled.get().getFulfillmentProvider().getShipFromAddress(), unfulfilled.get().getFulfillmentProvider().getShipFromContactNumber()));
      toPayload.setAddressReturn(getShippoTargetAddress(unfulfilled.get().getShippingDetails().getReturnAddress().getRecipientName(), "support@junodx.com", unfulfilled.get().getShippingDetails().getReturnAddress().getAddress(), unfulfilled.get().getShippingDetails().getReturnAddress().getPhone()));
      toPayload.setAsync(false);

      toPayload.setParcels(parcels);

      returnPayload.setAddressTo(getShippoTargetAddress(order.get().getCustomer().getFirstName() + " " + order.get().getCustomer().getLastName(), order.get().getCustomer().getEmail(), unfulfilled.get().getShippingDetails().getToAddress().getAddress(), unfulfilled.get().getShippingDetails().getToAddress().getPhone()));
      returnPayload.setAddressFrom(getShippoTargetAddress(unfulfilled.get().getShippingDetails().getReturnAddress().getRecipientName(), "support@junodx.com", unfulfilled.get().getFulfillmentProvider().getShipFromAddress(), unfulfilled.get().getFulfillmentProvider().getShipFromContactNumber()));
      returnPayload.setAsync(false);
      returnPayload.setParcels(parcels);

      MultipleRatesPayload responsePayload = new MultipleRatesPayload();

      responsePayload.setToRates(createShipmentAndGetOptions(toPayload, false));
      responsePayload.setReturnRates(createShipmentAndGetOptions(returnPayload, true));

      return responsePayload;

    } catch (Exception e){
      e.printStackTrace();
      throw new JdxServiceException("Cannot look up shipment options for order");
    }
  }

  public java.util.List<com.shippo.model.Rate> createShipmentAndGetOptions(ShippoShipmentPayload payload, boolean isReturn) throws JdxServiceException {
    try {
      Shippo.setApiKey(apiKey);
      Shippo.setApiVersion("2018-02-08");

      Map<String, Object> shipmentMap = objectMapper.convertValue(payload, new TypeReference<Map<String, Object>>() {
      });
      if(isReturn) {
        logger.info("Setting shipment as a return");
        HashMap<String, Object> extraMap = new HashMap<String, Object>();
        extraMap.put("is_return", true);
        shipmentMap.put("extra", extraMap);
      }

      try {
        logger.info(objectMapper.writeValueAsString(shipmentMap));
      } catch (Exception e) {
        logger.error("An Error has occurred while creating the shipment.");
        e.printStackTrace();
        throw new JdxServiceException("An Error has occurred while processing to create shipment.");
      }

      Shipment shipment = Shipment.create(shipmentMap);
      logger.info("Shipment info: " + mapper.writeValueAsString(shipment));

      if (shipment == null) {
        logger.error("Rates for the shipment is null or empty");
        throw new JdxServiceException("Could not get the rates for the shipment");
      }
      logger.info("shipment: " + objectMapper.writeValueAsString(shipment.getRates()));

      return shipment.getRates();
    } catch (Exception e) {
      e.printStackTrace();
      throw new JdxServiceException("Could not get the rates for the shipment");
    }
  }

  public MultipleShippingLabelsDto saveShippingDetails(ShippoCreateLabelRequest request, UserDetailsImpl updater) throws APIException, AuthenticationException, InvalidRequestException, APIConnectionException, JsonProcessingException {
    //if (request.getAddressFrom() == null || request.getAddressTo() == null) {
    //  logger.error("From address or To address is null or empty");
    //  throw new JdxServiceException("Shipping From address or To address can not be null or empty");
   // }
    Shippo.setApiKey(apiKey);
    Shippo.setApiVersion("2018-02-08");
    MultipleShippingLabelsDto multipleLabels = new MultipleShippingLabelsDto();

    Transaction transactionToCustomer = null;
    Transaction transactionReturnToLab = null;

    if(request.getObjectIdToShipment() != null) {
      Map<String, Object> transParams = new HashMap<>();
      transParams.put("rate", request.getObjectIdToShipment());
      transParams.put("label_file_type", "PNG");
      transParams.put("async", request.isAsync());
      transactionToCustomer = Transaction.create(transParams);
    }

    if(request.isIncludeReturnLabel() && request.getObjectIdReturnShipment() != null){
      Map<String, Object> returnTransParams = new HashMap<>();
      returnTransParams.put("rate", request.getObjectIdReturnShipment());
      returnTransParams.put("label_file_type", "PNG");
      returnTransParams.put("async", request.isAsync());
      transactionReturnToLab = Transaction.create(returnTransParams);
    }


    if ((transactionToCustomer != null && transactionToCustomer.getStatus().equals("SUCCESS"))
      || (transactionReturnToLab != null && transactionReturnToLab.getStatus().equals("SUCCESS"))) {
      Optional<Order> order = orderRepository.findById(request.getOrderId());
      if (!order.isPresent()) {
        logger.error("Could not find order with the order id {}", request.getOrderId());
        throw new JdxServiceException("Invalid order id.");
      }

      logger.info("order to update shipping for " + mapper.writeValueAsString(order.get()));

      Meta meta = new Meta();
      meta.setCreatedAt(DateUtil.nowCalendar());
      meta.setLastModifiedAt(DateUtil.nowCalendar());

      logger.info("creating line item");
      if(request.getLineItemId() == null) {
        logger.info("No line item available for shipment label request");
        throw new JdxServiceException("No line items available for label creation");
      }

      Optional<OrderLineItem> item = order.get().getLineItems().stream().filter(x -> x.getId().equals(request.getLineItemId())).findFirst();
      OrderLineItem lineItem = null;
      if (item.isPresent())
        lineItem = item.get();
      else
        throw new JdxServiceException("Cannot find line item to update");

      //Find an unfulfilled
      Optional<Fulfillment> unfulfilled = lineItem.getFulfillments().stream().filter(x -> x.getId().equals(request.getFulfillmentId())).findFirst();
      if (unfulfilled.isEmpty()) {
        logger.info("No fuflfillments set for one or more line items in this order. Set fulfillment options before creating a shipment");
        throw new JdxServiceException("Cannot find a Fulfillment for line item " + lineItem);
      }

      if (unfulfilled.get().getShippingDetails() == null) {
        logger.info("Shipping details aren't available in fulfillment and is required before creating a shipment");
        throw new JdxServiceException("Shipping details not available for shipment");
      }

      logger.info("creating fulfillment: " + mapper.writeValueAsString(transactionToCustomer));

      if(request.getObjectIdToShipment() != null)
        unfulfilled.get().setFulfillmentOrderId(transactionToCustomer.getObjectId());

      unfulfilled.get().setMeta(updateMeta(unfulfilled.get().getMeta(), updater));

      Optional<ShippingCarrier> carrier = null;

      if(request.getObjectIdToShipment() != null) {
        if (unfulfilled.get().getShippingDetails().getToMethod() == null) {
          ShippingMethod toMethod = new ShippingMethod();
          unfulfilled.get().getShippingDetails().setToMethod(toMethod);
          toMethod.setShippingDetails(unfulfilled.get().getShippingDetails());
          toMethod.setShipped(false);
        }
        if (transactionToCustomer.getTrackingNumber() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setTrackingCode(transactionToCustomer.getTrackingNumber().toString());

        if (transactionToCustomer.getTrackingUrlProvider() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setTrackingUrl(transactionToCustomer.getTrackingUrlProvider().toString());

        if (transactionToCustomer.getLabelUrl() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setLabelUrl((String) transactionToCustomer.getLabelUrl());

        if (transactionToCustomer.getObjectId() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setLabelId(transactionToCustomer.getObjectId());


        if (request.getCarrierTo() != null) {
          String uppercaseCarrier = request.getCarrierTo().toUpperCase(Locale.ROOT);
          carrier = shippingCarrierRepository.findShippingCarrierByName(uppercaseCarrier);
          if (carrier != null && carrier.isPresent()) {
            unfulfilled.get().getShippingDetails().getToMethod().setCarrier(carrier.get());
          }
        }

        if (request.getTypeTo() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setType(request.getTypeTo());

        if (request.getDeliveryDescriptionTo() != null)
          unfulfilled.get().getShippingDetails().getToMethod().setDeliveryDescription(request.getDeliveryDescriptionTo());


        ShipmentLabelDto dto = new ShipmentLabelDto();
        dto = getLabelDto(unfulfilled.get().getShippingDetails().getToMethod(), order.get().getId(), lineItem.getId(), "", transactionToCustomer.getStatus());
        multipleLabels.setToLabel(dto);
      }

      if (transactionReturnToLab != null) {
        if (unfulfilled.get().getShippingDetails().getReturnMethod() == null) {
          ShippingMethod returnMethod = new ShippingMethod();
          unfulfilled.get().getShippingDetails().setReturnMethod(returnMethod);
          returnMethod.setShippingDetails(unfulfilled.get().getShippingDetails());
          returnMethod.setLabelUrl((String) transactionReturnToLab.getLabelUrl());
          returnMethod.setShipped(false);
          returnMethod.setReturn(true);
        }
        if (transactionReturnToLab.getTrackingNumber() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setTrackingCode(transactionReturnToLab.getTrackingNumber().toString());

        if (transactionReturnToLab.getTrackingUrlProvider() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setTrackingUrl(transactionReturnToLab.getTrackingUrlProvider().toString());

        if(transactionReturnToLab.getLabelUrl() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setLabelUrl((String) transactionReturnToLab.getLabelUrl());

        if(transactionReturnToLab.getObjectId() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setLabelId(transactionReturnToLab.getObjectId());

        if(request.getCarrierReturn() != null){
          if(!request.getCarrierTo().equals(request.getCarrierReturn()) && carrier == null) {
            String uppercaseCarrier = request.getCarrierReturn().toUpperCase(Locale.ROOT);
            carrier = shippingCarrierRepository.findShippingCarrierByName(uppercaseCarrier);
          }
          if(carrier != null && carrier.isPresent())
            unfulfilled.get().getShippingDetails().getReturnMethod().setCarrier(carrier.get());
        }

        if(request.getTypeReturn() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setType(request.getTypeReturn());

        if(request.getDeliveryDescriptionReturn() != null)
          unfulfilled.get().getShippingDetails().getReturnMethod().setDeliveryDescription(request.getDeliveryDescriptionReturn());

        ShipmentLabelDto returnDto = getLabelDto(unfulfilled.get().getShippingDetails().getReturnMethod(), order.get().getId(), lineItem.getId(), "", transactionReturnToLab.getStatus());
        multipleLabels.setReturnLabel(returnDto);
      }

      ShippingTransaction shippingTransaction = new ShippingTransaction();
      shippingTransaction.setTransactionDate(DateUtil.nowCalendar());
      shippingTransaction.setAmount(Float.valueOf(request.getAmount()));
      if (request.getCurrency() != null)
        shippingTransaction.setCurrency(Currency.getInstance(request.getCurrency()));
      else
        shippingTransaction.setCurrency(Currency.getInstance("USD"));
      shippingTransaction.setShippingDetails(unfulfilled.get().getShippingDetails());
      unfulfilled.get().getShippingDetails().setShippingTransactionDetails(shippingTransaction);

      ShippingStatus shippingStatus = new ShippingStatus();
      shippingStatus.setCurrent(true);
      shippingStatus.setStatus(ShippingStatusType.LABEL_CREATED);
      shippingStatus.setStatusTimestamp(DateUtil.nowCalendar());
      shippingStatus.setShippingDetails(unfulfilled.get().getShippingDetails());
      unfulfilled.get().getShippingDetails().addShippingStatus(shippingStatus);

      orderService.updateOrderFromShipping(order.get(), lineItem.getId(), updater);

    } else {
      logger.error("An Error has occured while generating the label. Messages : {}", transactionToCustomer.getMessages());
      throw new JdxServiceException("An Error has occured while processing to create label.");
    }
    return multipleLabels;
  }

  public MultipleShippingLabelsDto getLabelObjects(String fulfillmentId, boolean bothLabels)
          throws APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
    Shippo.setApiKey(apiKey);
    Shippo.setApiVersion("2018-02-08");

    Optional<Fulfillment> fulfillment = fulfillmentRepository.findById(fulfillmentId);
    if (!fulfillment.isPresent()) {
      logger.error("Could not find fulfillment to retrieve labels for {}", fulfillmentId);
      throw new JdxServiceException("Invalid fulfillment id.");
    }

    if (fulfillment.get().getShippingDetails() == null) {
      logger.error("could not find shipping details {} ", fulfillmentId);
      throw new JdxServiceException("Invalid fulfillment id. order details not found.");
    }
    if (fulfillment.get().getShippingDetails().getToMethod() == null) {
      logger.error("could not find shipping method to the customer for the fulfillment {} ", fulfillmentId);
      throw new JdxServiceException("Invalid fulfillment id. To method deatils not found.");
    }
    if(bothLabels) {
      if (fulfillment.get().getShippingDetails().getReturnMethod() == null) {
        logger.error("could not find shipping method to the customer for the fulfillment {} ", fulfillmentId);
        throw new JdxServiceException("Invalid fulfillment id. Return method details found.");
      }
    }

    MultipleShippingLabelsDto multipleShippingLabelsDto = new MultipleShippingLabelsDto();

    if (!fulfillment.get().getShippingDetails().getToMethod().getLabelId().isBlank()) {
      Transaction transaction = Transaction.retrieve(fulfillment.get().getShippingDetails().getToMethod().getLabelId());
      if (transaction == null) {
        logger.error("Could not find label {} for shipment", fulfillment.get().getShippingDetails().getToMethod().getLabelId());
        throw new JdxServiceException("Invalid label id.");
      }

      ShipmentLabelDto toLabel = new ShipmentLabelDto();
      toLabel.setLabelUrl(transaction.getLabelUrl().toString());
      toLabel.setTrackingUrl(transaction.getTrackingUrlProvider().toString());
      toLabel.setTrackingNumber(transaction.getTrackingNumber().toString());
      toLabel.setLabelId(transaction.getObjectId());
      multipleShippingLabelsDto.setToLabel(toLabel);
    }

    if (bothLabels && !fulfillment.get().getShippingDetails().getReturnMethod().getLabelId().isBlank()) {
      Transaction transaction = Transaction.retrieve(fulfillment.get().getShippingDetails().getReturnMethod().getLabelId());
      if (transaction == null) {
        logger.error("Could not find return label {} for shipment", fulfillment.get().getShippingDetails().getReturnMethod().getLabelId());
        throw new JdxServiceException("Invalid label id for return label.");
      }

      ShipmentLabelDto returnLabel = new ShipmentLabelDto();
      returnLabel.setLabelUrl(transaction.getLabelUrl().toString());
      returnLabel.setTrackingUrl(transaction.getTrackingUrlProvider().toString());
      returnLabel.setTrackingNumber(transaction.getTrackingNumber().toString());
      returnLabel.setLabelId(transaction.getObjectId());
      multipleShippingLabelsDto.setReturnLabel(returnLabel);
    }

    return multipleShippingLabelsDto;
  }

  public Order updateCustomerShipmentAddress(UpdateCustomerToAddress payload, UserDetailsImpl updater) throws JdxServiceException {
    try {
      if (payload == null)
        throw new JdxServiceException("Cannot process customer address update, request data not found");

      Optional<Order> order = orderService.findOneByOrderId(payload.getOrderId(), new String[]{}, updater);
      if (order.isEmpty())
        throw new JdxServiceException("Cannot find order to update address for");

      Optional<OrderLineItem> lineItem = order.get().getLineItems().stream().filter(x -> x.getId().equals(payload.getLineItemId())).findFirst();
      if (lineItem.isEmpty())
        throw new JdxServiceException("Cannot find line item to update");

      Optional<Fulfillment> fulfillment = lineItem.get().getFulfillments().stream().filter(x -> x.getId().equals(payload.getFulfillmentId())).findFirst();
      if (fulfillment.isEmpty())
        throw new JdxServiceException("Cannot find fulfillment to update address for");

      if (payload.getToAddress() != null) {
        fulfillment.get().getShippingDetails().getToAddress().setAddress(payload.getToAddress());

        //Update the user's primary address at the same time
        order.get().getCustomer().setPrimaryAddress(payload.getToAddress());
      }

      if (payload.getName() != null)
        fulfillment.get().getShippingDetails().getToAddress().setRecipientName(payload.getName());

      if (payload.getPhone() != null)
        fulfillment.get().getShippingDetails().getToAddress().setPhone(payload.getPhone());

      fulfillment.get().setMeta(updateMeta(fulfillment.get().getMeta(), updater));
      order.get().setMeta(updateMeta(order.get().getMeta(), updater));

      Order o = orderRepository.save(order.get());
      if (o != null) {
        orderService.sendOrderStatus(o, EventType.UPDATE);
        userService.sendUserStatus(o.getCustomer(), EventType.UPDATE);
      }

      return o;
    } catch (Exception e){
      throw new JdxServiceException("Cannot update order address: " + e.getMessage());
    }
    //return orderService.updateOrderFromShipping(order.get(), payload.getLineItemId(), updater);
  }

  public Fulfillment updateShippingStatusForOrder(String orderId, String lineItemId, String fulFillmentId, boolean isReturnShipment, UserDetailsImpl updater) throws JdxServiceException {
    try {
      if (orderId == null)
        throw new JdxServiceException("Cannot update shipping status as selected order is not provided");

      Optional<Order> foundOrder = orderService.findOneByOrderId(orderId, new String[]{}, updater);
      if (foundOrder.isEmpty())
        throw new JdxServiceException("Cannot find order to update " + orderId);

      Optional<OrderLineItem> lineItem = foundOrder.get().getLineItems().stream().filter(x -> x.getId().equals(lineItemId)).findFirst();
      if (lineItem.isEmpty())
        throw new JdxServiceException("Cannot find line item to update for order " + lineItemId);

      Optional<Fulfillment> fulfillment = lineItem.get().getFulfillments().stream().filter(x -> x.getId().equals(fulFillmentId)).findFirst();
      if (fulfillment.isEmpty())
        throw new JdxServiceException("Cannot find line item to update for order " + fulFillmentId);

      if (isReturnShipment) {
        logger.info("Return shipment: " + mapper.writeValueAsString(fulfillment.get()));
        if (fulfillment.get().getShippingDetails() != null && fulfillment.get().getShippingDetails().getReturnMethod() != null) {
          logger.info("Shipment details is not null and neither is return method");
          fulfillment.get().getShippingDetails().getReturnMethod().setShipped(true);
          Calendar now = Calendar.getInstance();
          if (fulfillment.get().getShippingDetails().getReturnMethod().getType() != null) {
            logger.info("getReturnType is not null");
            switch (fulfillment.get().getShippingDetails().getReturnMethod().getType()) {
              case TWO_DAY:
                now.add(Calendar.DAY_OF_MONTH, 2);
                break;
              case GROUND:
                now.add(Calendar.DAY_OF_MONTH, 4);
                break;
              case FIRST_CLASS:
                now.add(Calendar.DAY_OF_MONTH, 2);
                break;
              case OVERNIGHT:
                now.add(Calendar.DAY_OF_MONTH, 1);
                break;
              default:
                now.add(Calendar.DAY_OF_MONTH, 3);
                break;
            }
          } else
            now.add(Calendar.DAY_OF_MONTH, 3);

          fulfillment.get().getShippingDetails().getReturnMethod().setEta(now);
          logger.info("Setting status type");

          orderService.updateOrderStatus(foundOrder.get(), OrderStatusType.RETURN_SHIPPED);

          ShippingStatus returnStatus = new ShippingStatus();
          returnStatus.setStatus(ShippingStatusType.RETURN_SHIPPED);
          returnStatus.setStatusTimestamp(Calendar.getInstance());
          returnStatus.setToCustomer(false);
          returnStatus.setCurrent(true);
          returnStatus.setShippingDetails(fulfillment.get().getShippingDetails());

          fulfillment.get().getShippingDetails().setMeta(updateMeta(fulfillment.get().getShippingDetails().getMeta(), updater));

          fulfillment.get().getShippingDetails().addShippingStatus(returnStatus);
        }
      } else {

        if (fulfillment.get().getShippingDetails() != null && fulfillment.get().getShippingDetails().getToMethod() != null) {
          fulfillment.get().getShippingDetails().getToMethod().setShipped(true);
          Calendar now = Calendar.getInstance();
          if (fulfillment.get().getShippingDetails().getToMethod().getType() != null) {
            switch (fulfillment.get().getShippingDetails().getReturnMethod().getType()) {
              case TWO_DAY:
                now.add(Calendar.DAY_OF_MONTH, 2);
                break;
              case GROUND:
                now.add(Calendar.DAY_OF_MONTH, 4);
                break;
              case FIRST_CLASS:
                now.add(Calendar.DAY_OF_MONTH, 2);
                break;
              case OVERNIGHT:
                now.add(Calendar.DAY_OF_MONTH, 1);
                break;
              default:
                now.add(Calendar.DAY_OF_MONTH, 3);
                break;
            }
          } else
            now.add(Calendar.DAY_OF_MONTH, 3);

          fulfillment.get().getShippingDetails().getToMethod().setEta(now);

          orderService.updateOrderStatus(foundOrder.get(), OrderStatusType.KIT_SHIPPED);

          ShippingStatus toStatus = new ShippingStatus();
          toStatus.setStatus(ShippingStatusType.SHIPPED);
          toStatus.setStatusTimestamp(Calendar.getInstance());
          toStatus.setToCustomer(true);
          toStatus.setCurrent(true);
          toStatus.setShippingDetails(fulfillment.get().getShippingDetails());

          fulfillment.get().getShippingDetails().setMeta(updateMeta(fulfillment.get().getShippingDetails().getMeta(), updater));

          fulfillment.get().getShippingDetails().addShippingStatus(toStatus);
        }
      }

      fulfillment.get().setMeta(updateMeta(fulfillment.get().getMeta(), updater));

      foundOrder.get().setMeta(updateMeta(foundOrder.get().getMeta(), updater));

      orderRepository.save(foundOrder.get());
      orderService.sendOrderStatus(foundOrder.get(), EventType.UPDATE);

      return fulfillment.get();
    } catch (Exception e){
      e.printStackTrace();
      throw new JdxServiceException("Cannot update shipment status for order");
    }
  }

  private ShipmentLabelDto getLabelDto(ShippingMethod shippingMethod, String orderId, String lineItemId, String eta, String status) {
    ShipmentLabelDto dto = new ShipmentLabelDto();
    dto.setLabelUrl(shippingMethod.getLabelUrl());
    dto.setLineItemIds(Arrays.asList(lineItemId));
    dto.setOrderId(orderId);
    dto.setShippingSuccessStatus(shippingMethod.isShipped());
    dto.setTrackingNumber(shippingMethod.getTrackingCode());
    dto.setTrackingUrl(shippingMethod.getTrackingUrl());
    dto.setLabelId(shippingMethod.getLabelId());
    dto.setEta(eta);
    dto.setTrackingStatus(status);
    return dto;
  }

  private ShippingTarget getTargetAddress(ShippoAddressForm addressForm) {
    Address address = new Address();
    address.setCity(addressForm.getCity());
    address.setCountry(addressForm.getCountry());
    address.setName(addressForm.getName());
    address.setPostalCode(addressForm.getZip());
    address.setState(addressForm.getState());
    address.setStreet(addressForm.getStreet1());
    Phone fromPhone = new Phone();
    fromPhone.setPhoneNumber(addressForm.getPhone());
    ShippingTarget targetAddress = new ShippingTarget();
    targetAddress.setAddress(address);
    targetAddress.setPhone(fromPhone);
    return targetAddress;
  }

  private ShippoAddressForm getShippoTargetAddress(String name, String email, Address target, Phone phone) {
    ShippoAddressForm address = new ShippoAddressForm();
    address.setName(name);
    address.setStreet1(target.getStreet());
    address.setCity(target.getCity());
    address.setState(target.getState());
    if(target.getCountry() == null || target.getCountry().equals("United States") || target.getCountry().equals("US"))
      address.setCountry("US");
    address.setPhone(phone.getPhoneNumber());
    address.setZip(target.getPostalCode());
    address.setEmail(email);

    return address;
  }

  private ShippoParcel getShippoParcelFromProduct(Product product) {
    ShippoParcel parcel = new ShippoParcel();
    parcel.setHeight(Float.valueOf(product.getDimensions().getHeight()).toString());
    parcel.setWidth(Float.valueOf(product.getDimensions().getWidth()).toString());
    parcel.setLength(Float.valueOf(product.getDimensions().getLength()).toString());
    parcel.setWeight(Float.valueOf(product.getDimensions().getWeight()).toString());
    parcel.setDistanceUnit(product.getDimensions().getLengthUnits().name());
    parcel.setMassUnit(product.getDimensions().getWeightUnits().name());

    return parcel;
  }


  private ShippingMethod getShippingMethod(
          Transaction transaction, ShippoCreateLabelRequest request, UserDetailsImpl userContext) {
    ShippingMethod shippingMethod = new ShippingMethod();
    if (transaction.getTrackingUrlProvider() != null) {
      shippingMethod.setTrackingUrl(transaction.getTrackingUrlProvider().toString());
    }
    if (transaction.getTrackingNumber() != null) {
      shippingMethod.setTrackingCode(transaction.getTrackingNumber().toString());
    }
    ShippingCarrier shippingCarrier = new ShippingCarrier();

    shippingCarrier.setName(request.getCarrierTo());
    shippingCarrier.setMeta(updateMeta(shippingCarrier.getMeta(), userContext));
    if (transaction.getLabelUrl() != null) {
      shippingMethod.setLabelUrl(transaction.getLabelUrl().toString());
    }
    shippingCarrierRepository.save(shippingCarrier);
    shippingMethod.setCarrier(shippingCarrier);
    return shippingMethod;
  }

  public void sendShippedEmail(Order order) throws JdxServiceException {
    if(order != null && order.getLineItems().size() > 0) {
      String content = "Hi " + order.getCustomer().getFirstName() + ", your order for" + order.getLineItems().get(0).getProductName() + " has shipped \n";
      content += "Your order number is: " + order.getOrderNumber();
      if(!mailService.sendEmail(order.getCustomer().getEmail(), "Junodx - Your order has shipped!", content, false, false))
        throw new JdxServiceException("Cannot send kit shipment email");
    }
  }

  public void updateShipmentStatusFromShippo(ShippoStatusPayload payload) {
    if (StringUtils.isBlank(payload.getData().getTrackingNumber())) {
      logger.error("Tracking Number from the payload is null");
      throw new JdxServiceException("Tracking Number from the payload is null");
    }
    try {
      logger.info("processing to update shipping status");
      Optional<ShippingMethod> shippingMethod = shippingMethodRepository.findByTrackingCode(payload.getData().getTrackingNumber());
      if (!shippingMethod.isPresent()) {
        logger.error("Could not find Shipping method details for tracking number {}", payload.getData().getTrackingNumber());
        throw new JdxServiceException("Could not find Shipping method details for tracking number " + payload.getData().getTrackingNumber());
      }
      logger.info("Found shipping method {} for the tracking number {}", shippingMethod.get().getId(), payload.getData().getTrackingNumber());
      Fulfillment fulfillment = shippingMethod.get().getShippingDetails().getFulfillment();
      boolean isReturn = shippingMethod.get().isReturn();
      ShippingStatusType shippingStatusType = null;
      OrderStatusType orderStatusType = null;
      ShippoTrackingStatus statusRequest = payload.getData().getTrackingStatus();

      switch (statusRequest.getStatus()) {
        case "PRE_TRANSIT":
          if (StringUtils.isNotBlank(statusRequest.getSubStatus())) {
            String subStatus = statusRequest.getSubStatus();
            if (subStatus.equals("out_for_delivery")) {
              shippingStatusType = isReturn ? ShippingStatusType.RETURN_ARRIVING_TODAY : ShippingStatusType.ARRIVING_TODAY;
              orderStatusType = isReturn ? OrderStatusType.RETURN_SHIPPED : OrderStatusType.KIT_SHIPPED;
            } else if (subStatus.equals("address_issue") || subStatus.equals("contact_carrier")
                    || subStatus.equals("contact_carrier") || subStatus.equals("delayed")
                    || subStatus.equals("location_inaccessible") || subStatus.equals("notice_left")
                    || subStatus.equals("package_damaged") || subStatus.equals("package_held")) {
              shippingStatusType = isReturn ? ShippingStatusType.RETURN_DELAYED : ShippingStatusType.DELAYED;
              orderStatusType = isReturn ? OrderStatusType.RETURN_SHIPPED : OrderStatusType.KIT_SHIPPED;
            } else {
              shippingStatusType = isReturn ? ShippingStatusType.RETURN_SHIPPED : ShippingStatusType.EN_ROUTE_TO_DESTINATION;
              orderStatusType = isReturn ? OrderStatusType.RETURN_SHIPPED : OrderStatusType.KIT_SHIPPED;
            }
          }
          break;
        case "DELIVERED":
          shippingStatusType = isReturn ? ShippingStatusType.RETURN_ARRIVED : ShippingStatusType.ARRIVED;
          orderStatusType = OrderStatusType.ARRIVED;
          break;
        case "RETURNED":
        case "FAILURE":
          shippingStatusType = isReturn ? ShippingStatusType.DELAYED : ShippingStatusType.RETURN_DELAYED;
          orderStatusType = isReturn ? OrderStatusType.RETURN_SHIPPED : OrderStatusType.KIT_SHIPPED;
          break;
        //default:
          //shippingStatusType = isReturn ? ShippingStatusType.RETURN_SHIPPED : ShippingStatusType.EN_ROUTE_TO_DESTINATION;
          //orderStatusType = isReturn ? OrderStatusType.KIT_RETURN_SHIPPED : OrderStatusType.KIT_SHIPPED;
      }

      //save the shipping status if the current is different.
      if (shippingStatusType != null && shippingStatusType != fulfillment.getShippingDetails().getCurrentStatus()) {
        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setCurrent(true);
        shippingStatus.setStatus(shippingStatusType);
        shippingStatus.setStatusTimestamp(DateUtil.nowCalendar());
        shippingStatus.setShippingDetails(fulfillment.getShippingDetails());
        fulfillment.getShippingDetails().addShippingStatus(shippingStatus);
      }
      //save order status if the current status is different
      Order order = fulfillment.getOrderLineItem().getOrder();
      if (orderStatusType != null && orderStatusType != order.getCurrentStatus()) {
        OrderStatus newOrderStatus = new OrderStatus();
        newOrderStatus.setStatusType(orderStatusType);
        newOrderStatus.setOrder(order);
        newOrderStatus.setCurrent(true);
        newOrderStatus.setUpdatedAt(Calendar.getInstance());
        order.addOrderStatus(newOrderStatus);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new JdxServiceException("Cannot process to update the shipment");
    }
  }
}
