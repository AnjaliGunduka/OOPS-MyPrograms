package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Currency;
import java.util.List;

public class PaymentIntentDto {
    private String id;

    @JsonProperty("client_secret")
    private String clientSecret;
    private float amount;
    private String currency;

    @JsonProperty("created")
    private long createdAt;

    @JsonProperty("livemode")
    private boolean liveMode;

    @JsonProperty("payment_method_types")
    private List<String> paymentMethodTypes;
    private String status;

    @JsonProperty("capture_method")
    private String captureMethod;

    @JsonProperty("confirmation_method")
    private String confirmationMethod;

    @JsonProperty("customer_id")
    private String customerId;

    private String object;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLiveMode() {
        return liveMode;
    }

    public void setLiveMode(boolean liveMode) {
        this.liveMode = liveMode;
    }

    public List<String> getPaymentMethodTypes() {
        return paymentMethodTypes;
    }

    public void setPaymentMethodTypes(List<String> paymentMethodTypes) {
        this.paymentMethodTypes = paymentMethodTypes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaptureMethod() {
        return captureMethod;
    }

    public void setCaptureMethod(String captureMethod) {
        this.captureMethod = captureMethod;
    }

    public String getConfirmationMethod() {
        return confirmationMethod;
    }

    public void setConfirmationMethod(String confirmationMethod) {
        this.confirmationMethod = confirmationMethod;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
