package com.junodx.api.models.commerce;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.models.commerce.types.DiscountType;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.commerce.types.OrderType;
import com.junodx.api.models.commerce.types.TaxType;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.Signout;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.KitType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.models.providers.DataBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.junodx.api.connectors.lims.elements.entities.CollectionType;
import com.junodx.api.connectors.lims.elements.entities.ElementsClinic;
import com.junodx.api.connectors.lims.elements.entities.ElementsKit;
import com.junodx.api.connectors.lims.elements.entities.ElementsLocation;
import com.junodx.api.connectors.lims.elements.entities.ElementsOrder;
import com.junodx.api.connectors.lims.elements.entities.ElementsPatient;
import com.junodx.api.connectors.lims.elements.entities.ElementsPractitioner;
import com.junodx.api.connectors.lims.elements.entities.ElementsReport;
import com.junodx.api.connectors.lims.elements.entities.ElementsShipment;
import com.junodx.api.controllers.commerce.actions.OrderKitAssignPayload;
import com.junodx.api.controllers.commerce.actions.OrderUpdateActions;
import com.junodx.api.controllers.commerce.payloads.ActivationPayload;
import com.junodx.api.controllers.commerce.payloads.ActivationResponsePayload;
import com.junodx.api.controllers.commerce.payloads.LabReceiptPayload;
import com.junodx.api.controllers.commerce.payloads.RedrawRequestPayload;
import com.junodx.api.controllers.commerce.payloads.RefundRequestPayload;
import com.junodx.api.controllers.payloads.OrderUpdateRequest;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.commerce.OrderBatchDto;
import com.junodx.api.dto.models.commerce.OrderLineItemBatchDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.models.commerce.types.DiscountType;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.commerce.types.OrderType;
import com.junodx.api.models.commerce.types.PaymentMethodType;
import com.junodx.api.models.commerce.types.PaymentProcessingType;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.commerce.types.ResultsConfigurationTemplate;
import com.junodx.api.models.commerce.types.TaxType;
import com.junodx.api.models.core.LengthUnit;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.WeightUnit;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.fulfillment.ShippingCarrier;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.ShippingMethod;
import com.junodx.api.models.fulfillment.ShippingStatus;
import com.junodx.api.models.fulfillment.ShippingTarget;
import com.junodx.api.models.fulfillment.ShippingTransaction;
import com.junodx.api.models.fulfillment.types.ShippingDeliveryType;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.laboratory.CustomerActionRequest;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.Signout;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ConfidenceIndexType;
import com.junodx.api.models.laboratory.types.CustomerActionRequestType;
import com.junodx.api.models.laboratory.types.KitType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportConfigurationCounts;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.models.patient.Consent;
import com.junodx.api.models.patient.types.ConsentType;
import com.junodx.api.models.payment.PaymentProcessorProvider;
import com.junodx.api.models.payment.Transaction;
import com.junodx.api.models.payment.types.PaymentInstrumentType;
import com.junodx.api.models.payment.types.TransactionType;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.models.providers.MedicalLicense;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.models.providers.Specialty;
import com.junodx.api.models.providers.types.SpecialtyType;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.lab.TestRunService;

public class DataBuilderorderExample {
	public static User mockUser = User.createDummyUser();

	public static User mockUser() {

		User u = new User();
		u.setUsername("General_test_user");
		u.setFirstName("Juno_test");
		u.setLastName("User");
		u.setEmail("no-email@junodx.com");
		u.setUserType(UserType.TEST);
		u.setDateOfBirth("1997-12-15");
		u.setPatientDetails(DataBuilder.mockPatientDetails());
		u.setLimsContactId("78788");
		u.setId("1L");
		u.setActivated(true);
		u.setStatus(UserStatus.ACTIVATED);
		u.setActivationTs(Calendar.getInstance());
		u.setPrimaryPhone(DataBuilder.getMockPhone());
		u.setBillingAddress(DataBuilder.getMockAddress());
		u.setPreferences(DataBuilder.getMockPreferences());
		return u;

	}
	public static List<OrderLineItem> OrderlineItems= new ArrayList<>();

	public static Order mockOrder() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(mockOrderLineItem());
		order.setLineItems(OrderlineItems);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		
		return order;
	}
	
	public static List<OrderLineItem> OrderlineItemsDiffful= new ArrayList<>();

	public static Order mockOrderDiffful() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(mockOrderLineItem());
		order.setLineItems(OrderlineItemsDiffful);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		
		return order;
	}
	
	public static List<OrderLineItem> OrderlineItemEmpty= new ArrayList<>();
	public static List<OrderStatus> orderStatusHistorys = new ArrayList<>();
	public static Order mockOrderEmptyFulfillments() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistorys);
		// lineItems.add(mockOrderLineItem());
		order.setLineItems(OrderlineItemEmpty);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		
		return order;
	}
	public static Order mockOrders() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItems);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemKit = new ArrayList<>();
	public static Order mockOrderLabEmpty() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemOrderLabEmpty);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}
	
	public static List<OrderLineItem> lineItemProduct = new ArrayList<>();
	public static Order mockOrdermProduct() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemProduct);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(true);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}
	
	public static List<OrderLineItem> lineItemassignExistingKit = new ArrayList<>();
	public static Order mockOrderassignExistingKit() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemassignExistingKit);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(true);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}
	public static List<OrderLineItem> lineItemassignExistingKits = new ArrayList<>();
	public static Order mockOrderassignExistingKits() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemassignExistingKits);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(true);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}
	public static List<OrderLineItem> lineItemProducts = new ArrayList<>();
	public static Order mockOrdermProducts() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemProducts);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(true);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}
	public static List<OrderLineItem> lineItemlimOrder = new ArrayList<>();
	public static Order mockOrderlimOrder() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemlimOrder);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(true);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemOrderLabEmpty = new ArrayList<>();
	

	public static Order mockOrderKit() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemKit);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static Order mockOrderKitCompleted() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemsKitCompleted);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemsBeyondKitAssignment = new ArrayList<>();

	public static Order mockOrderBeyondKitAssignment() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemsBeyondKitAssignment);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemsTestRunEmpty = new ArrayList<>();

	public static Order mockOrderTestRunEmpty() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemsTestRunEmpty);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemsTestRunEmptys = new ArrayList<>();

	public static Order mockOrderTestRunEmptys() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemsTestRunEmptys);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static List<OrderLineItem> lineItemsKitCompleted = new ArrayList<>();
	public static List<OrderLineItem> lineItemsEmptyLab = new ArrayList<>();

	public static Order mockOrdersEmptyLab() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItemsEmptyLab);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static Order mockOrderslineItemsEmpty() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		// order.setLineItems(lineItems);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(true);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static Order mockOrderOpen() {
		Order order = new Order();
		order.setId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		order.setOrderNumber("1234");
		order.setAmount(349.99f);
		order.setSubTotal(349.99f);
		order.setTotalShipping(5.99f);
		order.setTotalTax(0.0f);
		order.setWithInsurance(false);
		order.setCurrency(Currency.getInstance("USD"));
		order.setCustomer(mockUser());
		order.setCheckoutId("63d1a928-05b7-45b8-9a5b-fdc85b736cef");
		order.setCartId("afd66f9d-84c0-4aa7-82e8-8b7fb41b42dd");
		order.setNotes("notes");
		order.setDiscount(mockDiscount());
		order.setTax(mockTax());
		// orderStatusHistory.add(mockOrderStatus());
		order.setOrderStatusHistory(orderStatusHistory);
		// lineItems.add(orderLineItem);
		order.setLineItems(lineItems);
		order.setOrderedAt(Calendar.getInstance());
		order.setRequiresShipment(false);
		order.setResultsAvailable(false);
		order.setRequiresRedraw(false);
		order.setOpen(false);
		order.setRequiresProviderApproval(false);
		// order.setApprovingProviderName(null);
		order.setMeta(DataBuilder.getMockMeta());
		order.setShippingOrderId("3456");
		order.setInsuranceBillingOrderId("89878");
		order.setPriceBookId("989877");
		// order.setAmountDue(0);
		// order.setAmountPaid(0);
		// order.setCrmOrderId(null);
		// order.setCrmContactId(null);
		// customerActionRequests.add(mockCustomerActionRequest());
		// order.setCustomerActionRequests(customerActionRequests);
		// order.setTransactions(transactions);
		return order;
	}

	public static Discount mockDiscount() {
		Discount discount = new Discount();
		discount.setType(DiscountType.PROMO_CODE);
		discount.setMode(DiscountMode.AMOUNT);
		discount.setDiscountApplied(true);
		discount.setAmountDiscounted(25f);
		return discount;
	}

	public static Tax mockTax() {
		Tax tax = new Tax();
		tax.setAmount(876f);
		tax.setJurisdiction("jurisdiction");
		tax.setRate(876f);
		tax.setType(TaxType.STATE);
		return tax;
	}

	public static OrderStatus mockOrderStatuss() {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setCurrent(false);
		orderStatus.setId(2L);
		orderStatus.setOrder(mockOrders());
		orderStatus.setStatusType(OrderStatusType.CREATED);
		orderStatus.setUpdatedAt(Calendar.getInstance());
		return orderStatus;
	}

	public static List<OrderStatus> orderStatusHistory = new ArrayList<>();

	public static List<OrderLineItem> lineItems = new ArrayList<>();

	public static ShippingCarrier mockShippingCarrier() {
		ShippingCarrier shippingCarrier = new ShippingCarrier();
		shippingCarrier.setContactAddress(DataBuilder.getMockAddress());
		shippingCarrier.setContactPhone(DataBuilder.getMockPhone());
		shippingCarrier.setId("1L");
		shippingCarrier.setMeta(DataBuilder.getMockMeta());
		shippingCarrier.setName("jhon");
		return shippingCarrier;
	}

	public static List<ShippingCarrier> carriers = new ArrayList<>();

	public static FulfillmentProvider mockFulfillmentProvider() {
		FulfillmentProvider fulfillmentProvider = new FulfillmentProvider();
		carriers.add(mockShippingCarrier());
		fulfillmentProvider.setCarriers(carriers);
		fulfillmentProvider.setCoveredCountries("CA");
		fulfillmentProvider.setCoveredStates("INDIA");
		fulfillmentProvider.setDefaultCarrierId("");
		fulfillmentProvider.setDefaultProvider(true);
		fulfillmentProvider.setEmail("no-email@junodx.com");
		fulfillmentProvider.setId("1L");
		fulfillmentProvider.setLabPortalAssigned(true);
		fulfillmentProvider.setMeta(DataBuilder.getMockMeta());
		fulfillmentProvider.setName("jhon");
		fulfillmentProvider.setShipFromAddress(DataBuilder.getMockAddress());
		fulfillmentProvider.setShipFromContactNumber(DataBuilder.getMockPhone());
		fulfillmentProvider.setSubProviderLogoUrl("https://somewhere.ons3.com/finditpath.df3");
		fulfillmentProvider.setSubProviderName("jhon");
		return fulfillmentProvider;
	}
	public static List<Fulfillment> fulfillmentShippings = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemshipping() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentShippings);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}
	
	public static List<Fulfillment> fulfillmentShippingsss = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemshippingsss() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentShippingsss);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}
	
	public static List<Fulfillment> fulfillmentShippingss = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemshippings() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentShippingss);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderDiffful());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}
	public static List<Fulfillment> fulfillment1= new ArrayList<>();
	public static OrderLineItem mockOrderLineItemshippingEmpty() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillment1);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderEmptyFulfillments());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}

	public static List<Specialty> specialties = new ArrayList<>();

	public static List<MedicalLicense> licenses = new ArrayList<MedicalLicense>();

	public static Provider mockapprovingProvider() {
		Provider approvingProvider = new Provider();
		approvingProvider.setContactAddress(DataBuilder.getMockAddress());
		approvingProvider.setContactPhone(DataBuilder.getMockPhone());
		approvingProvider.setDefaultProvider(true);
		approvingProvider.setEmail("no-email@junodx.com");
		approvingProvider.setFirstName("Ralphie");
		approvingProvider.setId("b1ee000c-f947-46d9-b3df-e5f53cae34b3");
		approvingProvider.setLastName("Jones");
		licenses.add(DataBuilder.getMockMedicalLicense());
		approvingProvider.setLicenses(licenses);
		approvingProvider.setLimsId("78788");
		approvingProvider.setMeta(DataBuilder.getMockMeta());
		approvingProvider.setNpi("234234324");
		approvingProvider.setPractice(DataBuilder.getMockPractice());
		approvingProvider.setPracticing(true);
		// approvingProvider.setSalesforceId(null);
		// specialties.add(mockSpecialty());
		approvingProvider.setSpecialties(specialties);
		approvingProvider.setStatus(UserStatus.NEW);
		approvingProvider.setUpin("345435435345");
		// approvingProvider.setXifinId(null);
		return approvingProvider;
	}

	public static ProviderApproval mockProviderApproval() {
		ProviderApproval providerApproval = new ProviderApproval();
		providerApproval.setApprovalDate(Calendar.getInstance());
		providerApproval.setApproved(true);
		providerApproval.setApprovingProvider(mockapprovingProvider());
		providerApproval.setRequiresApproval(true);
		return providerApproval;
	}

	public static Specialty mockSpecialty() {
		Specialty specialty = new Specialty();
		specialty.setId(5L);
		specialty.setProvider(mockapprovingProvider());
		specialty.setType(SpecialtyType.GYNECOLOGY);
		return specialty;

	}

	public static Consent mockConsent() {
		Consent patientConsent = new Consent();
		patientConsent.setApproval(true);
		patientConsent.setApprovalDate(Calendar.getInstance());
		patientConsent.setFormName("Medical Informed Consent Form");
		patientConsent.setId("6L");
		// patientConsent.setLaboratoryOrder(mockLaboratoryOrder());
		patientConsent.setPatient(mockUser());
		patientConsent.setType(ConsentType.MEDICAL);
		return patientConsent;
	}

	public static Laboratory mockLaboratory() {
		Laboratory lab = new Laboratory();
		lab.setContact(DataBuilder.getMockPhone());
		lab.setDefaultLaboratory(true);
		lab.setId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		lab.setLocation(DataBuilder.getMockAddress());
		lab.setMeta(DataBuilder.getMockMeta());
		lab.setName("San Diego");
		return lab;
	}

	public static ReportConfigurationCounts mockReportConfigurationCounts() {
		ReportConfigurationCounts reportConfigurationCount = new ReportConfigurationCounts();
		reportConfigurationCount.setConfig(ReportConfiguration.FST);
		reportConfigurationCount.setCount(1);
		return reportConfigurationCount;
	}

	public static List<ReportConfigurationCounts> reportConfigurations = new ArrayList<>();

	public static Report mockReport() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		resultData.setData(null);
		resultData.setId("6L");
		resultData.setReportName("reports");
		// resultData.setReport();
		return resultData;
	}

	public static List<LaboratoryStatus> status = new ArrayList<>();

	public static List<TestRun> testRuns = new ArrayList<>();

	public static BatchRun mockBatchRun() {
		BatchRun batch = new BatchRun();
		batch.setEndTime(Calendar.getInstance());
		batch.setId("8L");
		batch.setLaboratory(mockLaboratory());
		batch.setLimsPlateId(null);
		batch.setMeta(DataBuilder.getMockMeta());
		batch.setModelId(null);
		reportConfigurations.add(mockReportConfigurationCounts());
		batch.setReportConfigurations(reportConfigurations);
		batch.setReportConfigurationsInRun(null);
		batch.setReviewed(false);
		batch.setReviewedAt(Calendar.getInstance());
		batch.setRunId(1L);
		batch.setSequencingRunId("FST");
		batch.setStartTime(Calendar.getInstance());
		// testRuns.add(mockTestRun());
		batch.setTestRuns(testRuns);
		batch.setTotalSamples(1);
		return batch;
	}

	public static Signout mockSignoutDetails() {
		Signout signoutDetails = new Signout();
		signoutDetails.setId("6L");
		signoutDetails.setNonReportable(false);
		signoutDetails.setSignatory(mockUser());
		signoutDetails.setSignedOutAt(Calendar.getInstance());
		return signoutDetails;
	}

	public static TestReport mockTestReport() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		report.setEstimatedToBeAvailableAt(Calendar.getInstance());
		report.setFirstAvailableAt(Calendar.getInstance());
		report.setApproved(true);
		report.setSignedOut(false);
		report.setSignoutDetails(mockSignoutDetails());
		report.setResultsUrl("url");
		report.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		report.setOrderNumber("1234");
		report.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		report.setPatient(mockUser());
		report.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		report.setNoOrder(false);
		report.setControl(false);
		report.setResearchSample(false);
		report.setReportType(ReportType.STANDARD);
		report.setResearchProjectName("researchProjectName");
		report.setBatchRunId("8L");
		report.setCompletedAt(Calendar.getInstance());
		report.setSignedOutType(SignedOutType.AUTOMATIC);
		report.setResultData(mockReport());
		report.setSampleNumber("67898");
		report.setDeliveredToProvider(false);
		report.setDeliveredToPatient(false);
		report.setDeliveredToPatientAt(Calendar.getInstance());
		report.setViewedByPatient(false);
		report.setViewedByPatientAt(Calendar.getInstance());
		report.setRetestRequested(true);
		report.setRetestRequestDate(Calendar.getInstance());
		report.setRetestRequester("request");
		report.setReportable(false);
		report.setMeta(DataBuilder.getMockMeta());
		return report;
	}

	public static LaboratoryStatus mockLaboratoryStatuBeyondKitAssignment() {
		LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
		laboratoryStatus.setCreatedAt(Calendar.getInstance());
		laboratoryStatus.setCreatedBy("San Diego");
		laboratoryStatus.setCurrent(false);
		laboratoryStatus.setId(8L);
		laboratoryStatus.setStatus(LaboratoryStatusType.CLOSED);
		// laboratoryStatus.setTestRun(mockTestRun());
		return laboratoryStatus;
	}

	public static LaboratoryStatus mockLaboratoryStatuss() {
		LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
		laboratoryStatus.setCreatedAt(Calendar.getInstance());
		laboratoryStatus.setCreatedBy("San Diego");
		laboratoryStatus.setCurrent(false);
		laboratoryStatus.setId(8L);
		laboratoryStatus.setStatus(LaboratoryStatusType.KIT_ASSIGNED);
		// laboratoryStatus.setTestRun(mockTestRun());
		return laboratoryStatus;
	}

	public static Kit mockKitss() {
		Kit kitss = new Kit();
		kitss.setId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber(null);
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId(null);
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(true);
		kitss.setActivated(true);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
	}

	public static Kit mockKitExistingKit() {
		Kit kitss = new Kit();
		kitss.setId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber(null);
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId(null);
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(false);
		kitss.setActivated(false);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
	}

	public static Kit mockKitExistingKits() {
		Kit kitss = new Kit();
		kitss.setId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber("12345");
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId(null);
		kitss.setTestRun(mockTestRuns());
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(false);
		kitss.setActivated(false);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
	}

	public static Kit mockKitComplted() {
		Kit kitss = new Kit();
		kitss.setId(null);
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber(null);
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId(null);
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(true);
		kitss.setActivated(true);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
	}

	public static TestRun mockTestRuns() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(false);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitss());
		testRun.setLaboratoryOrder(mockLaboratoryOrders());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReport());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(status);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
	}

	public static TestRun mockTestRunCompleted() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(true);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitss());
		testRun.setLaboratoryOrder(mockLaboratoryOrders());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReport());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(status);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
	}

	public static List<LaboratoryStatus> statusBeyondKitAssignment = new ArrayList<>();

	public static TestRun mockTestRunBeyondKitAssignment() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(false);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitss());
		testRun.setLaboratoryOrder(mockLaboratoryOrders());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReport());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(statusBeyondKitAssignment);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
	}

	public static TestRun mockTestRunCompltedEmpty() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(false);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitComplted());
		testRun.setLaboratoryOrder(mockLaboratoryOrders());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReport());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(statusBeyondKitAssignment);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
	}

	public static ShippingTransaction mockShippingTransaction() {
		ShippingTransaction shippingTransactionDetails = new ShippingTransaction();
		shippingTransactionDetails.setAmount(349.99f);
		shippingTransactionDetails.setCurrency(Currency.getInstance("USD"));
		// shippingTransactionDetails.setShippingDetails(mockShippingDetails());
		shippingTransactionDetails.setTransactionDate(Calendar.getInstance());
		shippingTransactionDetails.setId("1L");
		return shippingTransactionDetails;
	}

	public static ShippingTarget mockShippingTarget() {
		ShippingTarget toAddress = new ShippingTarget();
		toAddress.setAddress(DataBuilder.getMockAddress());
		toAddress.setId("5L");
		toAddress.setRecipientName("San Diego");
		toAddress.setPhone(DataBuilder.getMockPhone());
		return toAddress;
	}

	public static ShippingMethod mockShippingMethod() {
		ShippingMethod toMethod = new ShippingMethod();
		toMethod.setCarrier(mockShippingCarrier());
		toMethod.setDeliveryDescription("deleievery");
		toMethod.setEta(Calendar.getInstance());
		toMethod.setId("3L");
		toMethod.setLabelId("89L");
		toMethod.setLabelId("url");
		toMethod.setReturn(false);
		toMethod.setShipped(false);
		// toMethod.setShippingDetails(mockShippingDetails());
		toMethod.setTrackingCode("68876");
		toMethod.setTrackingUrl("url");
		toMethod.setType(ShippingDeliveryType.GROUND);
		return toMethod;

	}

	public static ShippingStatus mockShippingStatus() {
		ShippingStatus shippingStatus = new ShippingStatus();
		shippingStatus.setCurrent(true);
		shippingStatus.setId("3L");
		// shippingStatus.setShippingDetails(mockShippingDetails());
		shippingStatus.setStatusTimestamp(Calendar.getInstance());
		shippingStatus.setToCustomer(true);
		shippingStatus.setStatus(ShippingStatusType.ARRIVED);
		return shippingStatus;
	}

	public static List<ShippingStatus> shippingStatuss = new ArrayList<>();

	public static ShippingDetails mockShippingDetail() {
		ShippingDetails shippingDetails = new ShippingDetails();
		// shippingDetails.setFulfillment(mockFulfillment());
		shippingDetails.setId("7l");
		shippingDetails.setMeta(DataBuilder.getMockMeta());
		shippingDetails.setReturnAddress(mockShippingTarget());
		shippingDetails.setReturnMethod(mockShippingMethod());
		// shippingStatuss.add(mockShippingStatus());
		shippingDetails.setShippingStatus(shippingStatuss);
		shippingDetails.setShippingTransactionDetails(mockShippingTransaction());
		shippingDetails.setToAddress(mockShippingTarget());
		shippingDetails.setToMethod(mockShippingMethod());
		shippingDetails.setTrackingCode("68876");
		return shippingDetails;
	}

	public static Fulfillment mockFulfillments() {
		Fulfillment fulfillment = new Fulfillment();
		// fulfillment.setCompleted(false);
		fulfillment.setEstimatedToShipAt(Calendar.getInstance());
		fulfillment.setFulfilledAt(Calendar.getInstance());
		fulfillment.setFulfillmentOrderId("b1ee000c-f947-46d9-b3df-e5f53cae34b3");
		fulfillment.setFulfillmentProvider(mockFulfillmentProvider());
		fulfillment.setId("8fdf6101-f19a-4154-a36f-521528d02654");
		fulfillment.setKit(mockKitss());
		fulfillment.setMeta(DataBuilder.getMockMeta()); 
		// fulfillment.setOrderLineItem(mockOrderLineItem());
		fulfillment.setRedraw(false);
		fulfillment.setShipmentCreated(false);
		fulfillment.setShippingDetails(mockShippingDetail());
		return fulfillment;
	}

	public static Fulfillment mockFulfillmentss() {
		Fulfillment fulfillment = new Fulfillment();
		fulfillment.setCompleted(false);
		fulfillment.setEstimatedToShipAt(Calendar.getInstance());
		fulfillment.setFulfilledAt(Calendar.getInstance());
		fulfillment.setFulfillmentOrderId("b1ee000c-f947-46d9-b3df-e5f53cae34b3");
		fulfillment.setFulfillmentProvider(mockFulfillmentProvider());
		fulfillment.setId("8fdf6101-f19a-4154-a36f-521528d02654");
		fulfillment.setKit(DataBuilderOrder.mockKits());
		fulfillment.setMeta(DataBuilder.getMockMeta());
		fulfillment.setOrderLineItem(mockOrderLineItemss());
		fulfillment.setRedraw(true);
		fulfillment.setShipmentCreated(true);
		fulfillment.setShippingDetails(mockShippingDetail());
		return fulfillment;
	}

	public static List<Fulfillment> fulfillments = new ArrayList<>();

//	public static OrderLineItem orderLineItem = new OrderLineItem("845dba6b-4c0f-4cc9-ba77-62e7216bd692", mockOrder(),
//			ProductType.TEST, "Juno Early Fetal Sex Test", 349.99f, 5f, 5f, "KIT238349",
//			"786518f5-4fa9-4576-a173-6318556ccb0b", "Juno Early Fetal Sex Test", "Juno Early Fetal Sex Test", false,
//			true, false, false, mockLaboratoryOrder(), fulfillments, "8fdf6101-f19a-4154-a36f-521528d02654", 1,
//			DataBuilder.getMockMeta());

	public static OrderLineItem mockOrderLineItemss() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	
	public static OrderLineItem mockOrderLineItemOrderEmpty() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
	//	orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	
	public static OrderLineItem mockOrderLineItemOrderProduct() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	public static List<Fulfillment> fulfillmentassignExistingKit = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemOrderassignExistingKit() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentassignExistingKit);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderassignExistingKit());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	
	public static List<Fulfillment> fulfillmentassignExistingKits = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemOrderassignExistingKits() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentassignExistingKits);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderassignExistingKits());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	
	
	public static List<Fulfillment> fulfillmentProducts = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemOrderProducts() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentProducts);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrdermProducts());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}
	
	public static List<Fulfillment> fulfillmentempty = new ArrayList<>();
	public static OrderLineItem mockOrderLineItemOrderlim() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentempty ); 
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderlimOrder());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static List<Fulfillment> fulfillmentKit = new ArrayList<>();

	public static OrderLineItem mockOrderLineItemKit() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillmentKit);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderKit());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);

		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static Fulfillment mockFulfillmentKit() {
		Fulfillment fulfillment = new Fulfillment();
		fulfillment.setCompleted(false);
		fulfillment.setEstimatedToShipAt(Calendar.getInstance());
		fulfillment.setFulfilledAt(Calendar.getInstance());
		fulfillment.setFulfillmentOrderId("b1ee000c-f947-46d9-b3df-e5f53cae34b3");
		fulfillment.setFulfillmentProvider(mockFulfillmentProvider());
		fulfillment.setId("8fdf6101-f19a-4154-a36f-521528d02654");
		fulfillment.setKit(mockKitExistingKits());
		fulfillment.setMeta(DataBuilder.getMockMeta());
		// fulfillment.setOrderLineItem(mockOrderLineItem());
		fulfillment.setRedraw(false);
		fulfillment.setShipmentCreated(false);
		fulfillment.setShippingDetails(mockShippingDetail());
		return fulfillment;
	}

	public static OrderLineItem mockOrderLineItemKitCompleted() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrderKit());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}

	public static List<TestRun> testRunKit = new ArrayList<>();

	public static LaboratoryOrder mockLaboratoryOrderKit() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRunKit);
		return laboratoryOrderDetails;
	}

	public static OrderLineItem mockOrderLineItemKitBeyondKitAssignment() {
		OrderLineItem orderLineItem = new OrderLineItem();
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrderBeyondKitAssignment());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderBeyondKitAssignment());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static OrderLineItem mockOrderLineItemTesRunEmpty() {
		OrderLineItem orderLineItem = new OrderLineItem();
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrderTesRunEmpty());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderTestRunEmpty());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static OrderLineItem mockOrderLineItemTesRunsEmpty() {
		OrderLineItem orderLineItem = new OrderLineItem();
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrderTesRunsEmpty());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderTestRunEmpty());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static OrderLineItem mockOrderLineItemTesRunsEmptys() {
		OrderLineItem orderLineItem = new OrderLineItem();
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrderTesRunsEmptys());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrderTestRunEmpty());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;
	}

	public static List<TestRun> testRunemptys = new ArrayList<>();

	public static LaboratoryOrder mockLaboratoryOrderTesRunsEmptys() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRunemptys);
		return laboratoryOrderDetails;
	}

	public static LaboratoryOrder mockLaboratoryOrderTesRunsEmpty() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRunempty);
		return laboratoryOrderDetails;
	}

	public static List<TestRun> testRunempty = new ArrayList<>();

	public static LaboratoryOrder mockLaboratoryOrderTesRunEmpty() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		// laboratoryOrderDetails.setTestRuns(testRunBeyondKitAssignment);
		return laboratoryOrderDetails;
	}

	public static List<TestRun> testRunBeyondKitAssignment = new ArrayList<>();

	public static LaboratoryOrder mockLaboratoryOrderBeyondKitAssignment() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRunBeyondKitAssignment);
		return laboratoryOrderDetails;
	}

	public static OrderLineItem mockOrderLineItemEmptyLabOrder() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		// orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrdersEmptyLab());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		// orderLineItem.addFulfillment(mockFulfillments());
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}

	public static OrderLineItem mockOrderLineItemsss() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrders());
		orderLineItem.setOriginalUnitPrice(5f);
		orderLineItem.setPriceBookEntryId(null);
		orderLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		orderLineItem.setProductName("Juno Early Fetal Sex Test");
		orderLineItem.setProductImageUrl("Juno Early Fetal Sex Test");
		orderLineItem.setQuantity(1);
		orderLineItem.setRequiresShipping(true);
		orderLineItem.setSku("KIT238349");
		orderLineItem.setTaxable(false);
		// orderLineItem.addFulfillment(mockFulfillments());
		orderLineItem.setType(ProductType.TEST);
		return orderLineItem;

	}

	public static LaboratoryOrder mockLaboratoryOrders() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItemss());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrders());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRuns);
		return laboratoryOrderDetails;
	}

	public static List<CheckoutLineItem> items = new ArrayList<>();

	public static List<Laboratory> laboratoryProviders = new ArrayList<>();

}
