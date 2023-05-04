package com.junodx.api.models.commerce;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.stripe.model.CustomerCollection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.net.HttpHeaders;
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
import com.junodx.api.controllers.lab.actions.TestReportUpdateActions;
import com.junodx.api.controllers.lab.payloads.TestRunRemovalPayload;
import com.junodx.api.controllers.payloads.CheckoutRequestPayload;
import com.junodx.api.controllers.payloads.OrderUpdateRequest;
import com.junodx.api.controllers.payloads.TestReportUpdateRequest;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.commerce.MedicalDetailsOrderCreateDto;
import com.junodx.api.dto.models.commerce.OrderBatchDto;
import com.junodx.api.dto.models.commerce.OrderLineItemBatchDto;
import com.junodx.api.dto.models.commerce.PaymentIntentDto;
import com.junodx.api.dto.models.laboratory.LabOrderBatchDto;
import com.junodx.api.dto.models.laboratory.LaboratoryReviewStatisticsDto;
import com.junodx.api.dto.models.laboratory.TestReportsAwaitingReviewDto;
import com.junodx.api.dto.models.laboratory.TestReportsReviewResultsDto;
import com.junodx.api.models.auth.FetalSexResultsPreferences;
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
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.DMA;
import com.junodx.api.models.core.LengthUnit;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.WeightUnit;
import com.junodx.api.models.core.ZipCode;
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
import com.junodx.api.models.laboratory.reports.NIPSBasicRawData;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.tests.EuploidTest;
import com.junodx.api.models.laboratory.tests.FetalSexTest;
import com.junodx.api.models.laboratory.tests.SCATest;
import com.junodx.api.models.laboratory.tests.T13Test;
import com.junodx.api.models.laboratory.tests.T18Test;
import com.junodx.api.models.laboratory.tests.T21Test;
import com.junodx.api.models.laboratory.tests.TestQC;
import com.junodx.api.models.laboratory.tests.types.GenderResultType;
import com.junodx.api.models.laboratory.tests.types.QCType;
import com.junodx.api.models.laboratory.tests.types.SCAResultType;
import com.junodx.api.models.laboratory.tests.types.SnpIdentityType;
import com.junodx.api.models.laboratory.types.ConfidenceIndexType;
import com.junodx.api.models.laboratory.types.CustomerActionRequestType;
import com.junodx.api.models.laboratory.types.KitType;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportConfigurationCounts;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.laboratory.types.RetestActionType;
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
import com.stripe.exception.StripeException;
import com.stripe.model.Application;
import com.stripe.model.AutomaticPaymentMethodsPaymentIntent;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeError;
import com.stripe.model.PaymentIntent.NextAction;
import com.stripe.net.LiveStripeResponseGetter;
import com.stripe.net.StripeRequest;
import com.stripe.net.StripeResponse;
import com.stripe.net.StripeResponseGetter;
import com.stripe.param.PaymentIntentCreateParams;

public class DataBuilderCheckOut {

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
		order.setCustomerActionRequests(customerActionRequests);
		order.setTransactions(transactions);
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

	public static Order mockOrderss() {
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
		lineItems.add(mockOrderLineItems());
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

	public static OrderStatus mockOrderStatus() {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setCurrent(false);
		orderStatus.setId(2L);
		orderStatus.setOrder(mockOrder());
		orderStatus.setStatusType(OrderStatusType.CREATED);
		orderStatus.setUpdatedAt(Calendar.getInstance());
		return orderStatus;
	}

	public static List<OrderStatus> orderStatusHistory = new ArrayList<>();

	public static List<OrderLineItem> lineItems = new ArrayList<>();

	public static PaymentProcessorProvider mockPaymentProcessorProvider() {

		PaymentProcessorProvider paymentProcessorProvider = new PaymentProcessorProvider();
		paymentProcessorProvider.setContactAddress(DataBuilder.getMockAddress());
		paymentProcessorProvider.setContactEmail("no-email@junodx.com");
		paymentProcessorProvider.setContactPhone(DataBuilder.getMockPhone());
		paymentProcessorProvider.setId("1L");
		paymentProcessorProvider.setName("jhon");
		return paymentProcessorProvider;

	}

	public static Transaction mockTransaction() {
		Transaction transaction = new Transaction();
		transaction.setCreatedAt(Calendar.getInstance());
		transaction.setCreatedBy(Calendar.getInstance());
		transaction.setExternalTransactionId("1233ET");
		transaction.setId("1L");
		transaction.setOrder(mockOrder());
		transaction.setPaymentInstrumentType(PaymentInstrumentType.CREDIT_CARD);
		transaction.setProcessor(mockPaymentProcessorProvider());
		transaction.setTransactionId("678Tr");
		transaction.setTransactionJson("transactionJson");
		transaction.setType(TransactionType.CARD_CHARGED);
		return transaction;
	}

	public static List<Transaction> transactions = new ArrayList<>();

	public static CustomerActionRequest mockCustomerActionRequest() {
		CustomerActionRequest customerActionRequest = new CustomerActionRequest();
		customerActionRequest.setActive(true);
		customerActionRequest.setApprovalDate(Calendar.getInstance());
		customerActionRequest.setApproved(true);
		customerActionRequest.setCustomerActionRequestType(CustomerActionRequestType.REDRAW);
		customerActionRequest.setId(1L);
		customerActionRequest.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		customerActionRequest.setMeta(DataBuilder.getMockMeta());
		customerActionRequest.setOrder(mockOrder());
		customerActionRequest.setResolveByDate(Calendar.getInstance());
		customerActionRequest.setResolvedAt(Calendar.getInstance());
		return customerActionRequest;
	}

	// public static ServiceOptions serviceOptions = new ServiceOptions(true, true);

	public static ServiceOptions mockServiceOptions() {
		ServiceOptions serviceOptions = new ServiceOptions();
		serviceOptions.setAssistedSampleCollection(true);
		serviceOptions.setSelfCollected(true);
		return serviceOptions;
	}

	public static List<CustomerActionRequest> customerActionRequests = new ArrayList<>();

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

	public static Kit mockKit() {
		Kit kitss = new Kit();
		kitss.setId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber("9897987");
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId("1L");
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(false);
		kitss.setActivated(true);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
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

//	public static Consent mockPatientConsent = new Consent("6L", mockUser(), "Medical Informed Consent Form",
//			ConsentType.MEDICAL, true, Calendar.getInstance());

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
		batch.setPipelineRunId("12345");
		batch.setSequencingRunId("887687");
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

	public static Report mockReport() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		resultData.setData(mockNIPSBasicRawData());
		resultData.setId("6L");
		resultData.setReportName("NIPS_PLUS");
		// resultData.setReport(mockTestReport());
		// resultData.setReport();
		resultData.setQc(mockTestQC());
		return resultData;
	}

	public static Report mockReportsss() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(null);
		resultData.setData(mockNIPSBasicRawDatass());
		resultData.setId("6L");
		resultData.setReportName("NIPS_PLUS");
		// resultData.setReport(mockTestReports());
		// resultData.setReport();
		resultData.setQc(mockTestQCss());
		return resultData;
	}

	public static TestQC mockTestQCss() {
		TestQC testQC = new TestQC();
		testQC.setC13Snr(true);
		testQC.setChr13Sens(1.0f);
		testQC.setC18Snr(true);
		testQC.setC21Snr(true);
		testQC.setChr18Sens(1.0f);
		testQC.setChr18Sens(1.0f);
		testQC.setChr21Sens(1.0f);
		testQC.setFragmentDistribution(true);
		testQC.setFragmentDistributionPlotUrl("");
		testQC.setLowReads(true);
		testQC.setPassed(true);
		testQC.setRawCounts(15976784);
		testQC.setSnpIdentity(SnpIdentityType.PASS);
		testQC.setType(QCType.SEQUENCING);
		return testQC;
	}

	public static NIPSBasicRawData mockNIPSBasicRawDatass() {
		NIPSBasicRawData nIPSBasicRawData = new NIPSBasicRawData();
		nIPSBasicRawData.setEuploid(mockEuploidTest());
		nIPSBasicRawData.setFetalFraction(1f);
		nIPSBasicRawData.setFst(mockFetalSexTest());
		nIPSBasicRawData.setSca(mockSCATest());
		nIPSBasicRawData.setT13(mockT13Test());
		nIPSBasicRawData.setT18(mockT18Test());
		nIPSBasicRawData.setT21(mockT21Test());
		return nIPSBasicRawData;
	}

	public static Report mockReportss() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		// resultData.setData(mockNIPSBasicRawData());
		resultData.setId("6L");
		resultData.setReportName("reports");
		// resultData.setReport(mockTestReports());
		// resultData.setReport();
		resultData.setQc(mockTestQC());
		return resultData;
	}

	public static TestQC mockTestQC() {
		TestQC testQC = new TestQC();
		testQC.setC13Snr(true);
		testQC.setChr13Sens(1.0f);
		testQC.setC18Snr(true);
		testQC.setC21Snr(true);
		testQC.setChr18Sens(1.0f);
		testQC.setChr18Sens(1.0f);
		testQC.setChr21Sens(1.0f);
		testQC.setFragmentDistribution(true);
		testQC.setFragmentDistributionPlotUrl("");
		testQC.setLowReads(true);
		testQC.setPassed(true);
		testQC.setRawCounts(15976784);
		testQC.setSnpIdentity(SnpIdentityType.PASS);
		testQC.setType(QCType.SEQUENCING);
		return testQC;

	}

	public static Report mockReports() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		resultData.setData(mockNIPSBasicRawDatas());
		resultData.setId("6L");
		resultData.setReportName("reports");
		// resultData.setReport(mockTestReports());
		// resultData.setReport();
		resultData.setQc(mockTestQC());
		return resultData;
	}

	public static NIPSBasicRawData mockNIPSBasicRawDatas() {
		NIPSBasicRawData nIPSBasicRawData = new NIPSBasicRawData();
		// nIPSBasicRawData.setEuploid(mockEuploidTest());
		nIPSBasicRawData.setFetalFraction(1f);
		nIPSBasicRawData.setFst(mockFetalSexTest());
		// nIPSBasicRawData.setSca(mockSCATest());
		// nIPSBasicRawData.setT13(mockT13Test());
		// nIPSBasicRawData.setT18(mockT18Test());
		// nIPSBasicRawData.setT21(mockT21Test());
		return nIPSBasicRawData;
	}

	public static NIPSBasicRawData mockNIPSBasicRawData() {
		NIPSBasicRawData nIPSBasicRawData = new NIPSBasicRawData();
		nIPSBasicRawData.setEuploid(mockEuploidTest());
		nIPSBasicRawData.setFetalFraction(1f);
		// nIPSBasicRawData.setFst(mockFetalSexTest());
		nIPSBasicRawData.setSca(mockSCATest());
		nIPSBasicRawData.setT13(mockT13Test());
		nIPSBasicRawData.setT18(mockT18Test());
		nIPSBasicRawData.setT21(mockT21Test());
		return nIPSBasicRawData;
	}

	public static FetalSexTest mockFetalSexTest() {
		FetalSexTest fetalSexTest = new FetalSexTest();
		fetalSexTest.setGenderConfidence(1f);
		fetalSexTest.setGenderResult(GenderResultType.FEMALE);
		fetalSexTest.setId("1L");
		fetalSexTest.setScaConfidence(1f);
		fetalSexTest.setScaResult("Result");
		fetalSexTest.setScaResultType(SCAResultType.ICCL);
		fetalSexTest.setxVec(1f);
		fetalSexTest.setXzScores(1f);
		fetalSexTest.setyVec(1f);
		fetalSexTest.setyVec2(1f);
		return fetalSexTest;

	}

	public static T13Test mockT13Test() {
		T13Test t13Test = new T13Test();
		t13Test.setCall(false);
		t13Test.setConfidence(1f);
		t13Test.setConfidenceLower(1f);
		t13Test.setConfidenceUpper(1f);
		t13Test.setId("1L");
		t13Test.setzScore(1f);
		return t13Test;
	}

	public static T18Test mockT18Test() {
		T18Test t18Test = new T18Test();
		t18Test.setCall(false);
		t18Test.setConfidence(1f);
		t18Test.setConfidenceLower(1f);
		t18Test.setConfidenceUpper(1f);
		t18Test.setId("1L");
		t18Test.setzScore(1f);
		return t18Test;
	}

	public static T21Test mockT21Test() {
		T21Test t21Test = new T21Test();
		t21Test.setCall(false);
		t21Test.setConfidence(1f);
		t21Test.setConfidenceLower(1f);
		t21Test.setConfidenceUpper(1f);
		t21Test.setId("1L");
		t21Test.setzScore(1f);
		return t21Test;
	}

	public static SCATest mockSCATest() {
		SCATest sCATest = new SCATest();
		sCATest.setGenderConfidence(1f);
		sCATest.setGenderResult(GenderResultType.MALE);
		sCATest.setId("1L");
		sCATest.setScaConfidence(1f);
		sCATest.setScaResult("Result");
		sCATest.setScaResultType(SCAResultType.XY);
		sCATest.setxVec(1f);
		sCATest.setXzScores(1f);
		sCATest.setyVec(1f);
		sCATest.setyVec2(1f);
		return sCATest;

	}

	public static EuploidTest mockEuploidTest() {
		EuploidTest euploidTest = new EuploidTest();
		euploidTest.setEuploid(true);
		euploidTest.setId("1L");
		return euploidTest;
	}

	public static TestReport mockTestReport() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		report.setEstimatedToBeAvailableAt(Calendar.getInstance());
		report.setFirstAvailableAt(Calendar.getInstance());
		report.setApproved(true);
		report.setSignedOut(true);
		report.setSignoutDetails(mockSignoutDetails());
		report.setResultsUrl("url");
		report.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		report.setOrderNumber("1234");
		report.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		report.setPatient(mockUser());
		report.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		report.setNoOrder(true);
		report.setControl(false);
		report.setResearchSample(false);
		report.setReportType(ReportType.STANDARD);
		report.setResearchProjectName("researchProjectName");
		report.setBatchRunId("8L");
		report.setPipelineRunId("12345");
		report.setCompletedAt(Calendar.getInstance());
		report.setSignedOutType(SignedOutType.MANUAL);
		report.setResultData(mockReport());
		report.setSampleNumber("123455");
		report.setDeliveredToProvider(true);
		report.setDeliveredToPatient(true);
		report.setDeliveredToPatientAt(Calendar.getInstance());
		report.setViewedByPatient(true);
		report.setViewedByPatientAt(Calendar.getInstance());
		report.setRetestRequested(true);
		report.setRetestRequestDate(Calendar.getInstance());
		report.setRetestRequester("request");
		report.setReportable(true);
		// report.setTestRun(mockTestRun());
		report.setMeta(DataBuilder.getMockMeta());
		return report;
	}

	public static TestReport mockTestReports() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		report.setEstimatedToBeAvailableAt(Calendar.getInstance());
		report.setFirstAvailableAt(Calendar.getInstance());
		report.setApproved(true);
		report.setSignedOut(true);
		report.setSignoutDetails(mockSignoutDetails());
		report.setResultsUrl("url");
		report.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		report.setOrderNumber("1234");
		report.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		report.setPatient(mockUser());
		report.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		report.setNoOrder(true);
		report.setControl(false);
		report.setResultData(mockReport());
		report.setResearchSample(false);
		report.setReportType(ReportType.STANDARD);
		report.setResearchProjectName("researchProjectName");
		report.setBatchRunId("8L");
		report.setCompletedAt(Calendar.getInstance());
		report.setSignedOutType(SignedOutType.MANUAL);
		// report.setResultData(mockReport());
		report.setSampleNumber("123455");
		report.setPipelineRunId("12345");
		report.setSequenceRunId("887687");
		report.setTestRun(mockTestRuns());
		report.setDeliveredToProvider(true);
		report.setDeliveredToPatient(true);
		report.setDeliveredToPatientAt(Calendar.getInstance());
		report.setViewedByPatient(true);
		report.setViewedByPatientAt(Calendar.getInstance());
		report.setRetestRequested(true);
		report.setRetestRequestDate(Calendar.getInstance());
		report.setRetestRequester("request");
		report.setReportable(true);
		report.setMeta(DataBuilder.getMockMeta());
		return report;
	}

	public static TestReport mockTestReportss() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		report.setEstimatedToBeAvailableAt(Calendar.getInstance());
		report.setFirstAvailableAt(Calendar.getInstance());
		report.setApproved(false);
		report.setSignedOut(false);
		report.setSignoutDetails(null);
		report.setResultsUrl("url");
		report.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		report.setOrderNumber("1234");
		report.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		report.setPatient(mockUser());
		report.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		report.setNoOrder(false);
		report.setControl(false);
		report.setResultData(mockReport());
		report.setResearchSample(false);
		report.setReportType(ReportType.STANDARD);
		report.setResearchProjectName("researchProjectName");
		report.setBatchRunId("8L");
		report.setCompletedAt(Calendar.getInstance());
		report.setSignedOutType(null);
		report.setSampleNumber("123455");
		report.setPipelineRunId("12345");
		report.setSequenceRunId("887687");
		report.setTestRun(mockTestRunss());
		report.setDeliveredToProvider(false);
		report.setDeliveredToPatient(false);
		report.setDeliveredToPatientAt(Calendar.getInstance());
		report.setViewedByPatient(false);
		report.setViewedByPatientAt(Calendar.getInstance());
		report.setRetestRequested(true);
		report.setRetestRequestDate(Calendar.getInstance());
		report.setRetestRequester("request");
		report.setReportable(true);
		report.setMeta(DataBuilder.getMockMeta());
		return report;
	}

	public static TestReport mockTestReportsss() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(false);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		report.setEstimatedToBeAvailableAt(Calendar.getInstance());
		report.setFirstAvailableAt(Calendar.getInstance());
		report.setApproved(false);
		report.setSignedOut(false);
		report.setSignoutDetails(null);
		report.setResultsUrl("url");
		report.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		report.setOrderNumber("1234");
		report.setLabId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		report.setPatient(mockUser());
		report.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		report.setNoOrder(false);
		report.setControl(false);
		report.setSequenceRunId("887687");
		report.setResultData(mockReport());
		report.setResearchSample(false);
		report.setReportType(ReportType.STANDARD);
		report.setResearchProjectName("researchProjectName");
		report.setBatchRunId("8L");
		report.setPipelineRunId("12345");
		report.setCompletedAt(Calendar.getInstance());
		report.setSignedOutType(SignedOutType.AUTOMATIC);
		report.setSampleNumber("123455");
		report.setTestRun(mockTestRuns());
		report.setDeliveredToProvider(false);
		report.setDeliveredToPatient(false);
		report.setDeliveredToPatientAt(null);
		report.setViewedByPatient(false);
		report.setViewedByPatientAt(null);
		report.setRetestRequested(true);
		report.setRetestRequestDate(Calendar.getInstance());
		report.setRetestRequester("request");
		report.setReportable(false);
		report.setMeta(DataBuilder.getMockMeta());
		return report;
	}

	public static TestRun mockTestRunss() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(true);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKit());
		testRun.setLaboratoryOrder(mockLaboratoryOrder());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReportsss());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(status);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
	}

	public static LaboratoryStatus mockLaboratoryStatus() {
		LaboratoryStatus laboratoryStatus = new LaboratoryStatus();
		laboratoryStatus.setCreatedAt(Calendar.getInstance());
		laboratoryStatus.setCreatedBy("San Diego");
		laboratoryStatus.setCurrent(true);
		laboratoryStatus.setId(8L);
		laboratoryStatus.setStatus(LaboratoryStatusType.KIT_ASSIGNED);
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

	public static Kit mockKits() {
		Kit kitss = new Kit();
		kitss.setId("c06071ba-bf8e-414c-8622-2aa57d5b6a7f");
		kitss.setCode("JO135wwa33jj789");
		kitss.setSampleNumber("123455");
		kitss.setPsdSleeveNumber(null);
		kitss.setType(KitType.CAPILLARY);
		kitss.setTestRunId(null);
		kitss.setAddedToLimsAt(Calendar.getInstance());
		kitss.setAssigned(true);
		kitss.setActivated(false);
		kitss.setUnusable(false);
		kitss.setMeta(DataBuilder.getMockMeta());
		return kitss;
	}

	public static LaboratoryOrder mockLaboratoryOrder() {
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
		// laboratoryOrderDetails.setOrderLineItem(mockOrderLineItem());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrder());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId("2L");
		laboratoryOrderDetails.setReportableTestRunId("1L");
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRuns);
		return laboratoryOrderDetails;
	}

	public static LaboratoryOrder mockLaboratoryOrders() {
		LaboratoryOrder laboratoryOrderDetails = new LaboratoryOrder();
		laboratoryOrderDetails.setDateReceivedInLab(Calendar.getInstance());
		laboratoryOrderDetails.setEstArrivalInLab(Calendar.getInstance());
		laboratoryOrderDetails.setId("76c97d43-347f-4132-ba18-ddf3b313c226");
		// laboratoryOrderDetails.setLab(mockLaboratory());
		laboratoryOrderDetails.setLimsOrderId(null);
		laboratoryOrderDetails.setLimsReportId("78788");
		laboratoryOrderDetails.setMeta(DataBuilder.getMockMeta());
		laboratoryOrderDetails.setNotes("notes");
		// laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		laboratoryOrderDetails.setOrderLineItem(mockOrderLineItem());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrder());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId(null);
		laboratoryOrderDetails.setReportableTestRunId(null);
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		// testRuns.add(mockTestRun());
		laboratoryOrderDetails.setTestRuns(testRuns);
		return laboratoryOrderDetails;
	}

	public static LaboratoryOrder mockLaboratoryOrderss() {
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
		laboratoryOrderDetails.setOrderLineItem(mockOrderLineItem());
		laboratoryOrderDetails.setOrderType(OrderType.SELF_CREATED);
		laboratoryOrderDetails.setParentOrder(mockOrder());
		laboratoryOrderDetails.setPatient(mockUser());
		laboratoryOrderDetails.setPatientConsent(mockConsent());
		laboratoryOrderDetails.setProviderApproval(mockProviderApproval());
		laboratoryOrderDetails.setReportableTestReportId("2L");
		laboratoryOrderDetails.setReportableTestRunId("1L");
		laboratoryOrderDetails.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		laboratoryOrderDetails.setRequisitionFormUrl("https://somewhere.ons3.com/finditpath.df3");
		laboratoryOrderDetails.setTestRuns(testRuns);
		return laboratoryOrderDetails;
	}

	public static TestRun mockTestRun() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(false);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitss());
		testRun.setLaboratoryOrder(mockLaboratoryOrder());
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

	public static TestRun mockTestRuns() {
		TestRun testRun = new TestRun();
		testRun.setBatch(mockBatchRun());
		testRun.setCompleted(false);
		testRun.setEndTime(Calendar.getInstance());
		testRun.setId("1L");
		testRun.setKit(mockKitss());
		testRun.setLaboratoryOrder(mockLaboratoryOrder());
		testRun.setLimsReportId("78788");
		testRun.setName("San Diego");
		testRun.setRedraw(false);
		testRun.setReflex(false);
		testRun.setReport(mockTestReport());
		testRun.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
		testRun.setRetest(false);
		testRun.setStartTime(Calendar.getInstance());
		// status.add(mockLaboratoryStatus());
		testRun.setStatus(status);
		testRun.setType(TestRunType.STANDARD);
		return testRun;
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

	public static Kit mockKitsss() {
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

	public static Fulfillment mockFulfillment() {
		Fulfillment fulfillment = new Fulfillment();
		fulfillment.setCompleted(false);
		fulfillment.setEstimatedToShipAt(Calendar.getInstance());
		fulfillment.setFulfilledAt(Calendar.getInstance());
		fulfillment.setFulfillmentOrderId("b1ee000c-f947-46d9-b3df-e5f53cae34b3");
		fulfillment.setFulfillmentProvider(mockFulfillmentProvider());
		fulfillment.setId("8fdf6101-f19a-4154-a36f-521528d02654");
		fulfillment.setKit(mockKit());
		fulfillment.setMeta(DataBuilder.getMockMeta());
		fulfillment.setOrderLineItem(mockOrderLineItem());
		fulfillment.setRedraw(false);
		fulfillment.setShipmentCreated(false);
		fulfillment.setShippingDetails(mockShippingDetail());
		return fulfillment;
	}

	public static Fulfillment mockFulfillments() {
		Fulfillment fulfillment = new Fulfillment();
		fulfillment.setCompleted(false);
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

	public static List<Fulfillment> fulfillments = new ArrayList<>();

	public static OrderLineItem mockOrderLineItem() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrder());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrder());
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

	public static OrderLineItem mockOrderLineItemss() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		// orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrder());
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

	public static OrderLineItem mockOrderLineItems() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setFulfillments(fulfillments);
		orderLineItem.setId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderLineItem.setAmount(349.99f);
		orderLineItem.setCurrentFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		orderLineItem.setDescription("Juno Early Fetal Sex Test");
		orderLineItem.setDirectlyProvided(false);
		orderLineItem.setInOfficeCollected(false);
		orderLineItem.setInsuranceEstimatedCoveredAmount(5f);
		orderLineItem.setLaboratoryOrderDetails(mockLaboratoryOrders());
		orderLineItem.setMeta(DataBuilder.getMockMeta());
		orderLineItem.setOrder(mockOrder());
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

	public static CheckoutLineItem mockCheckoutLineItem() {
		CheckoutLineItem checkoutLineItem = new CheckoutLineItem();
		checkoutLineItem.setAmount(99f);
		checkoutLineItem.setCheckout(mockCheckout());
		checkoutLineItem.setCurrency("USD");
		checkoutLineItem.setId("2L");
		checkoutLineItem.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		checkoutLineItem.setQuantity(1);
		checkoutLineItem.setShipped(false);
		return checkoutLineItem;
	}

	public static List<CheckoutLineItem> items = new ArrayList<>();

//		public static StripeResponse mockStripeResponse =new StripeResponse(code,headers,body);

	public static Customer mockstripeCustomer() {
		Customer customer = new Customer();
		customer.setId("req");
		customer.setEmail("no-email@junodx.com");
		customer.setCurrency("USD");
		return customer;
	}

	public static UserCheckout mockCustomer() {
		UserCheckout customer = new UserCheckout();
		customer.setCardConnectCustomerId("78643");
		customer.setCustomerId("1c4d2dc0-7358-459d-8f15-eefc00f53aa0");
		customer.setDateOfBirth("1997-12-15");
		customer.setEmail("no-email@junodx.com");
		customer.setFirstName("Juno_test");
		customer.setLastName("User");
		customer.setPhone("3334441212");
		customer.setShippingAddress(DataBuilder.getMockAddress());
		customer.setStripeCustomerId("");
		customer.setUseMobileForNotifications(false);
		customer.setUserType(UserType.STANDARD);
		return customer;
	}

	public static Checkout mockCheckout() {
		Checkout checkout = new Checkout();
		checkout.setAgreedToInformedConsent(true);
		checkout.setAgreedToTerms(true);
		checkout.setAgreeNoTransplantNorTransfusion(true);
		checkout.setApprovingProviderDesignated(false);
		checkout.setClientId("12345");
		checkout.setConception(Calendar.getInstance());
		checkout.setCurrency("USD");
		checkout.setCustomer(mockCustomer());
		checkout.setFetalSexResultsPreferences(DataBuilder.getMockFetalSexResultsPreferences());
		checkout.setId("1L");
		checkout.setApprovingProvider(mockapprovingProvider());
		// items.add(mockCheckoutLineItem());
		checkout.setItems(items);
		checkout.setLmpDate(Calendar.getInstance());
		checkout.setLoggedInSession(true);
		checkout.setMeta(DataBuilder.getMockMeta());
		checkout.setOrderNumber("1234");
		checkout.setPaymentMethod(PaymentMethodType.card);
		checkout.setProcessor(PaymentProcessingType.STRIPE);
		checkout.setServiceOptions(mockServiceOptions());
		checkout.setToken(
				"bWRuaXJhbmphbjgxNEBnbWFpbC5jb206OTM0OC04ODkyLTkzNDItMDE6Njk2MWVjZmEtODlmZi00MGRkLThlYmUtZjBkOTQ2MWE0ZmI5");
		checkout.setTotalAmount(99f);
		checkout.setWithInsurance(false);
		return checkout;

	}

	public static List<Laboratory> laboratoryProviders = new ArrayList<>();

//	public static ProductDimensions mockProductDimensions = new ProductDimensions(1.5f, 4.5f, 8.0f, 0.8f, LengthUnit.in,
//			WeightUnit.lb);
	public static ProductDimensions mockProductDimensions() {
		ProductDimensions dimensions = new ProductDimensions();
		dimensions.setHeight(1.5f);
		dimensions.setLength(8.0f);
		dimensions.setLengthUnits(LengthUnit.in);
		dimensions.setWeight(0.8f);
		dimensions.setWeightUnits(WeightUnit.lb);
		dimensions.setWidth(4.5f);
		return dimensions;
	}

	public static List<FulfillmentProvider> fulfillmentProviders = new ArrayList<>();

	public static Product mockProduct() {
		Product product = new Product();
		product.setId("786518f5-4fa9-4576-a173-6318556ccb0b");
		product.setName("Juno Early Fetal Sex Test");
		product.setLongDescription("Fetal Sex Testing");
		product.setShortDescription("Fetal Sex Testing");
		product.setType(ProductType.TEST);
		product.setActive(true);
		product.setShippable(true);
		product.setTaxable(true);
		product.setReportConfiguration(ReportConfiguration.FST);
		product.setRequiresProviderApproval(true);
		product.setDimensions(mockProductDimensions());
		product.setPrice(99.0f);
		product.setCurrency(Currency.getInstance("USD"));
		// fulfillmentProviders.add(mockFulfillmentProvider());
		product.setFulfillmentProviders(fulfillmentProviders);
		laboratoryProviders.add(mockLaboratory());
		product.setLaboratoryProviders(laboratoryProviders);
		product.setXifinTestId("BSX-FST-0001");
		product.setStripeProductId(null);
		product.setCardConnectProductId(null);
		product.setSalesforceProductId("01t03000003CWt6AAG");
		product.setSalesforcePriceBookId("01s030000002ZCRAA2");
		product.setSalesforcePriceBookEntryId("01u03000007wW69AAE");
		product.setAllowsAssistedCollectionVideoCall(true);
		product.setAllowsSelfCollectedVideoUpload(true);
		product.setResultsConfigurationTemplate(ResultsConfigurationTemplate.FETAL_SEX);
		product.setReportConfiguration(ReportConfiguration.NIPS_BASIC);
		product.setLimsReportId("78788");
		product.setSku("KIT238349");
		product.setSalesforcePriceBookEntryId(null);
		product.setAlliedPackageUnitId(null);
		product.setAvailablity(null);
		return product;
	}

	public static InventoryItem mockInventoryItem() {
		InventoryItem inventoryItem = new InventoryItem();
		inventoryItem.setAvailable(true);
		inventoryItem.setAvailableUnits(1L);
		inventoryItem.setFirstCreated(Calendar.getInstance());
		inventoryItem.setId("3L");
		inventoryItem.setLastUpdated(Calendar.getInstance());
		inventoryItem.setMeta(DataBuilder.getMockMeta());
		inventoryItem.setProduct(mockProduct());
		inventoryItem.setReleased(true);
		return inventoryItem;
	}

	public static ElementsClinic mockElementsClinic() {
		ElementsClinic elementsClinic = new ElementsClinic();
		elementsClinic.setContactFirstName("Juno_test");
		elementsClinic.setContactLastName("User");
		elementsClinic.setEmail("no-email@junodx.com");
		elementsClinic.setId(1);
		elementsClinic.setName("General_test_user");
		elementsClinic.setPhone("3334441212");
		elementsClinic.setReportEmails("no-email@junodx.com");
		elementsClinic.setWebsite("www.google.com");
		elementsClinic.setReportFaxNumber("1234543");
		elementsClinic.setPdfPassword("Password$123");
		elementsClinic.setFax("fax");
		elementsClinic.setPracticeDirector("director");
		elementsClinic.setHostCode("87656");
		elementsClinic.setGeneticCounselor(false);
		return elementsClinic;
	}

	public static ElementsPractitioner mockElementsPractitioner() {
		ElementsPractitioner practitioner = new ElementsPractitioner();
		practitioner.setId("78788");
		practitioner.setAddress1("Dharpally");
		practitioner.setAddress2("Nizamabad");
		practitioner.setCity("Hyderabad");
		practitioner.setCountry("CA");
		practitioner.setState("Telanagana");
		practitioner.setZip("503165");
		practitioner.setLastName("User");
		practitioner.setFirstName("Juno_test");
		practitioner.setEmail("no-email@junodx.com");
		practitioner.setNpi("234234324");
		practitioner.setExternalId("5L");
		practitioner.setTitle("Dev");
		practitioner.setPhone("3334441212");
		return practitioner;

	}

	public static ElementsLocation mockElementsLocation() {
		ElementsLocation patientLocation = new ElementsLocation();
		patientLocation.setAddressOne("Dharpally");
		patientLocation.setAddressTwo("Nizamabad");
		patientLocation.setCity("Hyderabad");
		patientLocation.setState("Telanagana");
		patientLocation.setZip("503165");
		patientLocation.setCountry("CA");
		return patientLocation;

	}

	public static ElementsReport[] reports = {};
	public static ElementsKit[] kits = {};
	public static String[] ethnicities = {};

	public static ElementsPatient mockElementsPatient() {
		ElementsPatient patient = new ElementsPatient();
		patient.setId(2);
		patient.setExternalId("5L");
		patient.setFirstName("Juno_test");
		patient.setLastName("User");
		patient.setDeceased(false);
		patient.setEmail("no-email@junodx.com");
		patient.setEthnicities(ethnicities);
		patient.setBirthDate("1997-15-12");
		patient.setGender("Female");
		patient.setKits(kits);
		patient.setLocation(mockElementsLocation());
		patient.setPhone("3334441212");
		patient.setReports(reports);
		patient.setBirthDateInternal(Date.from(Instant.now()));
		patient.setStatus("status");
		patient.setMedicalRecordNumber("245");
		return patient;
	}

	public static ElementsKit mockElementsKit() {
		ElementsKit elementskit = new ElementsKit();
		elementskit.setId(3);
		elementskit.setCollectionDate(Calendar.getInstance());
		elementskit.setCollectionType(CollectionType.Blood);
		elementskit.setCreateShipment(true);
		elementskit.setExternalId("5L");
		elementskit.setTrackingNumber("345678");
		elementskit.setBarcode("24698");
		elementskit.setExternalBarcode("24566");
		return elementskit;
	}

	public static ElementsReport mockElementsReport() {
		ElementsReport elementsreport = new ElementsReport();
		elementsreport.setId(4);
		elementsreport.setExternalId("5L");
		elementsreport.setKitExtBarcode("24698");
		elementsreport.setReportTypeId("78788");
		elementsreport.setStatus("status");
		elementsreport.setAvailablePanelId("86766");
		return elementsreport;
	}

	public static List<String> consents = new ArrayList<>();

	public static ElementsShipment mockElementsShipment() {
		ElementsShipment shipment = new ElementsShipment();
		shipment.setLocation(mockElementsLocation());
		shipment.setId("8L");
		return shipment;
	}

	public static ElementsOrder mockElementsOrder() {

		ElementsOrder elementsOrder = new ElementsOrder();
		elementsOrder.setClinic(mockElementsClinic());
		elementsOrder.setConsents(consents);
		elementsOrder.setKit(mockElementsKit());
		elementsOrder.setPatient(mockElementsPatient());
		elementsOrder.setPatientLocation(mockElementsLocation());
		elementsOrder.setPractitioner(mockElementsPractitioner());
		elementsOrder.setReport(mockElementsReport());
		elementsOrder.setShipment(mockElementsShipment());
		return elementsOrder;
	}

	public static ActivationPayload mockActivationPayload() {
		ActivationPayload activationPayload = new ActivationPayload();
		activationPayload.setKitCode("JO135wwa33jj789");
		activationPayload.setDob("1997-12-15");
		activationPayload.setLastName("User");
		return activationPayload;
	}

	public static ActivationResponsePayload mockActivationResponsePayload() {
		ActivationResponsePayload activationResponsePayload = new ActivationResponsePayload();
		activationResponsePayload.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		activationResponsePayload.setCustomerActivated(true);
		activationResponsePayload.setCustomerEmail("no-email@junodx.com");
		activationResponsePayload.setCustomerFirstName("Juno_test");
		activationResponsePayload.setCustomerLastName("User");
		activationResponsePayload.setCustomerId("1c4d2dc0-7358-459d-8f15-eefc00f53aa0");
		activationResponsePayload.setFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		activationResponsePayload.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		activationResponsePayload.setProductId("786518f5-4fa9-4576-a173-6318556ccb0b");
		activationResponsePayload.setProductName("Juno Early Fetal Sex Test");
		return activationResponsePayload;
	}

	public static List<GrantedAuthority> authorities = mockUser().getAuthorities().stream()
			.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	public static UserDetailsImpl userDetailsImpl = (UserDetailsImpl) new UserDetailsImpl(1L, "General_test_user",
			"no-email@junodx.com", "12345", "Password&123", "123456", authorities);

	public static List<OrderLineItemBatchDto> OrderLineItemBatchDtolineItems = new ArrayList<>();

	public static OrderBatchDto MockOrderBatchDto() {
		OrderBatchDto orderBatchDto = new OrderBatchDto();
		orderBatchDto.setAmount(349.9f);
		orderBatchDto.setSubTotal(349.9f);
		orderBatchDto.setTotalShipping(5.99f);
		orderBatchDto.setTotalTax(0.0f);
		orderBatchDto.setCurrency(Currency.getInstance("USD"));
		orderBatchDto.setRequiresShipping(false);
		orderBatchDto.setWithInsurance(false);
		orderBatchDto.setResultsAvailable(false);
		orderBatchDto.setCustomer(mockUserOrderDto());
		orderBatchDto.setCurrentStatus(OrderStatusType.CREATED);
		// orderBatchDto.setLineItems(OrderLineItemBatchDtolineItems);
		return orderBatchDto;
	}

	public static OrderUpdateRequest mockOrderUpdateRequest() {
		OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
		orderUpdateRequest.setAction(OrderUpdateActions.UPDATE_CUSTOMER);
		orderUpdateRequest.setNewStatus(OrderStatusType.CREATED);
		orderUpdateRequest.setOrder(orderUpdateRequest);
		return orderUpdateRequest;

	}

	public static OrderKitAssignPayload mockOrderKitAssignPayload() {
		OrderKitAssignPayload orderKitAssignPayload = new OrderKitAssignPayload();
		orderKitAssignPayload.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		orderKitAssignPayload.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		orderKitAssignPayload.setKit(mockKit());
		orderKitAssignPayload.setFulfillmentId("8fdf6101-f19a-4154-a36f-521528d02654");
		return orderKitAssignPayload;

	}

	public static RedrawRequestPayload mockRedrawRequestPayload() {

		RedrawRequestPayload redrawRequestPayload = new RedrawRequestPayload();
		redrawRequestPayload.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		redrawRequestPayload.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		redrawRequestPayload.setTestRunId("1L");
		redrawRequestPayload.setCreate(true);
		redrawRequestPayload.setApproved(false);
		return redrawRequestPayload;

	}

	public static RefundRequestPayload mockRefundRequestPayload() {

		RefundRequestPayload refundRequestPayload = new RefundRequestPayload();
		refundRequestPayload.setOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		refundRequestPayload.setLineItemId("845dba6b-4c0f-4cc9-ba77-62e7216bd692");
		refundRequestPayload.setCreate(true);
		refundRequestPayload.setApproved(false);
		return refundRequestPayload;

	}

	public static LabReceiptPayload mockLabReceiptPayload() {
		LabReceiptPayload labReceiptPayload = new LabReceiptPayload();
		labReceiptPayload.setKit(mockKit());
		return labReceiptPayload;

	}

	public static TestReportsReviewResultsDto mockTestReportsReviewResultsDto() {
		TestReportsReviewResultsDto resultsDto = new TestReportsReviewResultsDto();
		resultsDto.setId(2L);
		resultsDto.setRecentlyAutomaticallySignedOut(1);
		resultsDto.setRecentlyManuallySignedOut(1);
		resultsDto.setRecentlySignedOut(1);
		resultsDto.setResultsAwaitingInvestigation(1);
		resultsDto.setRunsToBeApproved(1);
		resultsDto.setUpcomingResults(1);
		return resultsDto;
	}

	public static TestReportsAwaitingReviewDto mockTestReportsAwaitingReviewDto() {
		TestReportsAwaitingReviewDto reviewDto = new TestReportsAwaitingReviewDto();
		reviewDto.setReportConfiguration(ReportConfiguration.FST);
		reviewDto.setTotalAwaitingReview(1);
		reviewDto.setTotalRequiringAutomaticReview(1);
		reviewDto.setTotalRequiringManualReview(1);
		return reviewDto;
	}

	public static List<TestReportsAwaitingReviewDto> counts = new ArrayList<>();

	public static LaboratoryReviewStatisticsDto mockLaboratoryReviewStatisticsDto() {
		LaboratoryReviewStatisticsDto statsGroup = new LaboratoryReviewStatisticsDto();
		statsGroup.setLaboratoryId("f450237e-20a3-4bf1-b64d-9ecaab16be7a");
		statsGroup.setReview(counts);
		return statsGroup;
	}

	public static List<TestReport> testReports = new ArrayList<>();

	public static TestReportUpdateRequest mockTestReportUpdateRequest() {
		TestReportUpdateRequest statsGroup = new TestReportUpdateRequest();
		statsGroup.setAction(TestReportUpdateActions.APPROVE);
		statsGroup.setName("Juno_test");
		statsGroup.setNoResult(true);
		statsGroup.setNotes("notes");
		statsGroup.setPipelineRunId("12345");
		statsGroup.setReports(testReports);
		statsGroup.setReTestType(RetestActionType.REDRAW);
		statsGroup.setUserId("1L");
		return statsGroup;
	}

	public static LabOrderBatchDto mockLabOrderBatchDto() {
		LabOrderBatchDto labOrderBatchDto = new LabOrderBatchDto();
		labOrderBatchDto.setEstArrivalInLab(Calendar.getInstance());
		labOrderBatchDto.setId("1L");
		labOrderBatchDto.setParentOrderId("aa86f0ec-9ea1-487c-a814-b1c6001be7e8");
		labOrderBatchDto.setPatient(mockUserOrderDto());
		labOrderBatchDto.setReceivedAt(Calendar.getInstance());
		return labOrderBatchDto;
	}
	public static UserOrderDto mockUserOrderDto() {
		UserOrderDto customer = new UserOrderDto();
		customer.setCardConnectCustomerId("78643");
		customer.setCustomerId("1c4d2dc0-7358-459d-8f15-eefc00f53aa0");
		customer.setDateOfBirth("1997-12-15");
		customer.setEmail("no-email@junodx.com");
		customer.setFirstName("Juno_test");
		customer.setLastName("User");
		customer.setPhone("3334441212");
		customer.setShippingAddress(DataBuilder.getMockAddress());
		customer.setStripeCustomerId("");
		customer.setUseMobileForNotifications(false);
		customer.setUserType(UserType.STANDARD);
		return customer;
	}
	public static TestRunRemovalPayload mockTestRunRemovalPayload() {
		TestRunRemovalPayload testRunRemovalPayload = new TestRunRemovalPayload();
		testRunRemovalPayload.setLaboratoryOrderId("76c97d43-347f-4132-ba18-ddf3b313c226");
		testRunRemovalPayload.setTestRunId("1L");
		return testRunRemovalPayload;
	}

	public static CheckoutRequestPayload mockCheckoutRequestPayload() {
		CheckoutRequestPayload checkoutRequestPayload = new CheckoutRequestPayload();
		checkoutRequestPayload.setPaymentMethod(PaymentMethodType.card);
		checkoutRequestPayload.setClientId("12345");
		checkoutRequestPayload.setCurrency("USD");
		checkoutRequestPayload.setCustomer(mockCustomer());
		checkoutRequestPayload.setFetalSexResultsPreferences(DataBuilder.getMockFetalSexResultsPreferences());
		checkoutRequestPayload.setItems(items);
	//	checkoutRequestPayload.setProcessor(PaymentProcessingType.CARD_CONNECT);
		checkoutRequestPayload.setProvider(DataBuilder.getMockProvider());
		checkoutRequestPayload.setServiceOptions(mockServiceOptions());
		checkoutRequestPayload.setTotalAmount(399.9f);
		checkoutRequestPayload.setMedicalDetails(mockMedicalDetailsOrderCreateDto());
		return checkoutRequestPayload;

	}

	public static MedicalDetailsOrderCreateDto mockMedicalDetailsOrderCreateDto() {
		MedicalDetailsOrderCreateDto medicalDetailsOrderCreateDto = new MedicalDetailsOrderCreateDto();
		medicalDetailsOrderCreateDto.setAgreedToTerms(false);
		medicalDetailsOrderCreateDto.setConsented(false);
		medicalDetailsOrderCreateDto.setLmpDate(Calendar.getInstance());
		medicalDetailsOrderCreateDto.setNoTransplantNorTransfusion(false);
		medicalDetailsOrderCreateDto.setOverTenWeeks(true);
		return medicalDetailsOrderCreateDto;

	}

	public static List<DMA> allowedDMAs = new ArrayList<>();
	public static List<State> allowedStates = new ArrayList<>();

	public static DMA mockDMA() {
		DMA dMA = new DMA();
		dMA.setCode("876689");
		return dMA;

	}

	public static State mockState() {
		State state = new State();
		state.setAbbr("abbr");
		return state;

	}

	public static List<ZipCode> allowedZipCodes = new ArrayList<>();

	public static ZipCode mockZipCode() {
		ZipCode zipCode = new ZipCode();
		zipCode.setZip("503165");
		return zipCode;

	}

	public static ProductAvailablity mockProductAvailablity() {
		ProductAvailablity productAvailablity = new ProductAvailablity();
		productAvailablity.setAllowedDMAs(allowedDMAs);
		productAvailablity.setAllowedDMAsAsString("876689");
		productAvailablity.setAllowedStates(allowedStates);
		productAvailablity.setAllowedStatesAsString("abbr");
		productAvailablity.setAllowedZipCodes(allowedZipCodes);
		productAvailablity.setAllowedZipCodesAsString("503165");
		productAvailablity.setAvailableInYourRegionAt(Calendar.getInstance());
		productAvailablity.setId(1L);
		productAvailablity.setMeta(DataBuilder.getMockMeta());
		productAvailablity.setNewStockAvailableAt(Calendar.getInstance());
		productAvailablity.setOutOfRegion(false);
		productAvailablity.setProduct(DataBuilderCheckOut.mockProduct());
		productAvailablity.setSoldOut(false);
		return productAvailablity;

	}

	public static PaymentIntentDto mockPaymentIntentDto() {
		PaymentIntentDto paymentIntentDto = new PaymentIntentDto();
		paymentIntentDto.setAmount(1f);
		paymentIntentDto.setCaptureMethod("capture");
		paymentIntentDto.setClientSecret("secret");
		paymentIntentDto.setCreatedAt(1L);
		paymentIntentDto.setCurrency("USD");
		paymentIntentDto.setCustomerId(mockCustomer().getId());
		paymentIntentDto.setLiveMode(false);
		paymentIntentDto.setObject("obj");
		paymentIntentDto.setPaymentMethodTypes(consents);
		paymentIntentDto.setStatus("ACTIVATED");
		return paymentIntentDto;

	}

	public static Application application = new Application();
	public static AutomaticPaymentMethodsPaymentIntent automaticPaymentMethodsPaymentIntent = new AutomaticPaymentMethodsPaymentIntent();
	public static NextAction nextAction = new NextAction();
	private static StripeResponseGetter stripeResponseGetter = new LiveStripeResponseGetter();

//	public static PaymentIntent mockPaymentIntent() {
//		PaymentIntent paymentIntent = new PaymentIntent();
//		paymentIntent.setCurrency("USD");
//		paymentIntent.setAmount(1L);
//		paymentIntent.setAmountCapturable(1L);
//		paymentIntent.setAmountReceived(1L);
//		paymentIntent.setApplication("1L");
//		paymentIntent.setApplicationFeeAmount(1L);
//		paymentIntent.setApplicationObject(application);
//		paymentIntent.setAutomaticPaymentMethods(automaticPaymentMethodsPaymentIntent);
//		paymentIntent.setCanceledAt(1L);
//		paymentIntent.setCancellationReason("1L");
//		paymentIntent.setCaptureMethod("1L");
//		paymentIntent.setClientSecret("1L");
//		paymentIntent.setConfirmationMethod("1L");
//		paymentIntent.setCreated(1L);
//
//		paymentIntent.setStripeResponseGetter(stripeResponseGetter);
//		paymentIntent.setCustomerObject(mockstripeCustomer());
//		paymentIntent.setDescription("des");
//		paymentIntent.setId("1L");
//		paymentIntent.setInvoice("1L");
//		paymentIntent.setLivemode(false);
//		paymentIntent.setNextAction(nextAction);
//		paymentIntent.setObject("1L");
//		paymentIntent.setOnBehalfOf("1L");
//		paymentIntent.setPaymentMethod("");
//		paymentIntent.setPaymentMethodOptions(null);
//		paymentIntent.setPaymentMethodTypes(consents);
//		paymentIntent.setProcessing(null);
//		return paymentIntent;
//	}

}
