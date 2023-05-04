package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.auth.FetalSexResultsPreferences;
import com.junodx.api.models.commerce.types.PaymentMethodType;
import com.junodx.api.models.commerce.types.PaymentProcessingType;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.providers.Provider;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "checkout", indexes = {
        @Index(name = "fn_index_token", columnList = "token")
})
public class Checkout {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "processor")
    private PaymentProcessingType processor;

    @Column(name = "token")
    private String token;

    private UserOrderDto customer;

    @Column(name = "approving_provider_designated", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean approvingProviderDesignated;

    @OneToOne
    @JoinColumn(name = "approving_provider_id", nullable = true)
    protected Provider approvingProvider;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CheckoutLineItem> items;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gestational_age")
    private Calendar conception;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "lmp_date")
    private Calendar lmpDate;

    private boolean withInsurance;

    private float totalAmount;

    private String currency;

    private PaymentMethodType paymentMethod;

    private boolean agreedToInformedConsent;

    @Column(name = "agree_no_transplant_nor_transfusion", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean agreeNoTransplantNorTransfusion;

    private boolean agreedToTerms;

    private String clientId;

    @Column(name = "logged_in_session", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean loggedInSession;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceOptions serviceOptions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FetalSexResultsPreferences fetalSexResultsPreferences;

    private Meta meta;

    public Checkout(){
        this.id = UUID.randomUUID().toString();
        this.serviceOptions = new ServiceOptions();
        this.fetalSexResultsPreferences = new FetalSexResultsPreferences();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentProcessingType getProcessor() {
        return processor;
    }

    public void setProcessor(PaymentProcessingType processor) {
        this.processor = processor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserOrderDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserOrderDto customer) {
        this.customer = customer;
    }

    public List<CheckoutLineItem> getItems() {
        return items;
    }

    public void setItems(List<CheckoutLineItem> items) {
        this.items = items;
    }

    public Calendar getConception() {
        return conception;
    }

    public void setConception(Calendar conception) {
        this.conception = conception;
    }

    public boolean isWithInsurance() {
        return withInsurance;
    }

    public void setWithInsurance(boolean withInsurance) {
        this.withInsurance = withInsurance;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentMethodType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isAgreedToInformedConsent() {
        return agreedToInformedConsent;
    }

    public void setAgreedToInformedConsent(boolean agreedToInformedConsent) {
        this.agreedToInformedConsent = agreedToInformedConsent;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public boolean isApprovingProviderDesignated() {
        return approvingProviderDesignated;
    }

    public void setApprovingProviderDesignated(boolean approvingProviderDesignated) {
        this.approvingProviderDesignated = approvingProviderDesignated;
    }

    public Provider getApprovingProvider() {
        return approvingProvider;
    }

    public void setApprovingProvider(Provider approvingProvider) {
        this.approvingProvider = approvingProvider;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Boolean getLoggedInSession() {
        return loggedInSession;
    }

    public void setLoggedInSession(Boolean loggedInSession) {
        this.loggedInSession = loggedInSession;
    }

    public Calendar getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Calendar lmpDate) {
        this.lmpDate = lmpDate;
    }

    public boolean isAgreeNoTransplantNorTransfusion() {
        return agreeNoTransplantNorTransfusion;
    }

    public void setAgreeNoTransplantNorTransfusion(boolean agreeNoTransplantNorTransfusion) {
        this.agreeNoTransplantNorTransfusion = agreeNoTransplantNorTransfusion;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ServiceOptions getServiceOptions() {
        return serviceOptions;
    }

    public void setServiceOptions(ServiceOptions serviceOptions) {
        this.serviceOptions = serviceOptions;
    }

    public FetalSexResultsPreferences getFetalSexResultsPreferences() {
        return fetalSexResultsPreferences;
    }

    public void setFetalSexResultsPreferences(FetalSexResultsPreferences fetalSexResultsPreferences) {
        this.fetalSexResultsPreferences = fetalSexResultsPreferences;
    }

    public String generateOrderNumber(){
      //  int leftLimit = 48; // numeral '0'
      //  int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
      //  Random random = new Random();

        /*
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

         */

        String generatedString = RandomStringUtils.random(targetStringLength, 0, targetStringLength, true, true, 'A','B','C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        String dashedString = "";
        generatedString.toUpperCase(Locale.ROOT);
        dashedString += generatedString.substring(0,5) + "-" + generatedString.substring(6,10) + "-" + generatedString.substring(11,generatedString.length());


        return dashedString;
    }
}
