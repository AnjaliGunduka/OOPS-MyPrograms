package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stripe.model.PaymentIntent;

public class PendingTransactionDto {
    private String provider;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentIntentDto paymentIntent;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public PaymentIntentDto getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(PaymentIntentDto paymentIntent) {
        this.paymentIntent = paymentIntent;
    }
}
