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
import com.junodx.api.models.laboratory.reports.NIPSBasicRawData;
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

public class DataBuilderReport {
	public static User mockUser = User.createDummyUser();
	public static List<GrantedAuthority> authorities = mockUser().getAuthorities().stream()
			.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	public static UserDetailsImpl userDetailsImpl = (UserDetailsImpl) new UserDetailsImpl(1L, "General_test_user",
			"no-email@junodx.com", "12345", "Password&123", "123456", authorities);

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

	public static Order mockOrdersempty() {
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
		//order.setLineItems(lineItemss);
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

	public static List<OrderLineItem> lineItemss = new ArrayList<>();

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
		testQC.setPassed(false);
		testQC.setRawCounts(15976784);
		testQC.setSnpIdentity(SnpIdentityType.PASS);
		testQC.setType(QCType.SEQUENCING);
		return testQC;

	}

	public static List<ReportConfigurationCounts> reportConfigurations = new ArrayList<>();

	public static Report mockReportExceptions() {
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

	public static List<Transaction> transactions = new ArrayList<>();

	public static List<CustomerActionRequest> customerActionRequests = new ArrayList<>();

	public static Report mockReportException() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		resultData.setData(mockNIPSBasicRawDatas());
		resultData.setId("6L");
		resultData.setReportName("NIPS_PLUS");
		// resultData.setReport(mockTestReport());
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

	public static FetalSexTest mockFetalSexTest() {
		FetalSexTest fetalSexTest = new FetalSexTest();
		fetalSexTest.setGenderConfidence(1f);
		fetalSexTest.setGenderResult(GenderResultType.FEMALE);
		fetalSexTest.setId("1L");
		fetalSexTest.setScaConfidence(1f);
		fetalSexTest.setScaResult("result");
		fetalSexTest.setScaResultType(SCAResultType.XY);
		fetalSexTest.setxVec(1f);
		fetalSexTest.setyVec(1f);
		fetalSexTest.setyVec2(1f);
		return fetalSexTest;

	}
	public static Report mockReports() {
		Report resultData = new Report();
		resultData.setConfidenceIndex(ConfidenceIndexType.MEDIUM);
		resultData.setData(mockNIPSBasicRawDatae());
		resultData.setId("6L");
		resultData.setReportName("NIPS_PLUS");
		// resultData.setReport(mockTestReport());
		// resultData.setReport();
		resultData.setQc(mockTestQC());
		return resultData;
	}

	public static NIPSBasicRawData mockNIPSBasicRawDatae() {
		NIPSBasicRawData nIPSBasicRawData = new NIPSBasicRawData();
		nIPSBasicRawData.setEuploid(mockEuploidTest());
		nIPSBasicRawData.setFetalFraction(1f);
		// nIPSBasicRawData.setFst(mockFetalSexTest());
		//nIPSBasicRawData.setSca(mockSCATest());
		nIPSBasicRawData.setT13(mockT13Test());
		nIPSBasicRawData.setT18(mockT18Test());
		nIPSBasicRawData.setT21(mockT21Test());
		return nIPSBasicRawData;
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

	public static T13Test mockT13Test() {
		T13Test t13Test = new T13Test();
		t13Test.setCall(true);
		t13Test.setConfidence(1f);
		t13Test.setConfidenceLower(1f);
		t13Test.setConfidenceUpper(1f);
		t13Test.setId("1L");
		t13Test.setzScore(1f);
		return t13Test;
	}

	public static T18Test mockT18Test() {
		T18Test t18Test = new T18Test();
		t18Test.setCall(true);
		t18Test.setConfidence(1f);
		t18Test.setConfidenceLower(1f);
		t18Test.setConfidenceUpper(1f);
		t18Test.setId("1L");
		t18Test.setzScore(1f);
		return t18Test;
	}

	public static T21Test mockT21Test() {
		T21Test t21Test = new T21Test();
		t21Test.setCall(true);
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
	public static BatchRun mockBatchRunSequence() {
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
	//	batch.setSequencingRunId("FST");
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
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
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
		// report.setSignedOutType(SignedOutType.AUTOMATIC);
		report.setResultData(mockReport());
		report.setPipelineRunId("12345");
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

	public static TestReport mockTestReportException() {
		TestReport report = new TestReport();
		report.setId("3L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.FST);
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
		report.setPipelineRunId("12345");
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

	public static TestReport mockTestReports() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
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
		report.setPipelineRunId("12345");
		report.setTestRun(mockTestRuns());
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

	public static TestReport mockTestReportempty() {
		TestReport report = new TestReport();
		report.setId("2L");
		report.setAvailable(true);
		report.setReportConfiguration(ReportConfiguration.NIPS_PLUS);
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
		report.setPipelineRunId("12345");
		report.setTestRun(mockTestRuns());
		report.setSampleNumber("67898");
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
	
	
	public static TestReport mockTestReportReportable() {
		TestReport report = new TestReport();
		report.setId("34L");
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
		report.setReportable(false);
		report.setMeta(DataBuilder.getMockMeta());
		return report;
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

	public static LaboratoryOrder mockLaboratoryOrdersign() {
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
		laboratoryOrderDetails.setReportableTestReportId("1L");
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
