package com.junodx.api.models.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.payment.types.PaymentInstrumentType;

public abstract class PaymentInstrument {
    @JsonIgnore
    private Long id;

    private String paymentInstrumentId;
    private PaymentInstrumentType type;
    private String description;
    private Address billingAddress;
    private PaymentProcessorProvider provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentInstrumentId() {
        return paymentInstrumentId;
    }

    public void setPaymentInstrumentId(String paymentInstrumentId) {
        this.paymentInstrumentId = paymentInstrumentId;
    }

    public PaymentInstrumentType getType() {
        return type;
    }

    public void setType(PaymentInstrumentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public PaymentProcessorProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProcessorProvider provider) {
        this.provider = provider;
    }
}
