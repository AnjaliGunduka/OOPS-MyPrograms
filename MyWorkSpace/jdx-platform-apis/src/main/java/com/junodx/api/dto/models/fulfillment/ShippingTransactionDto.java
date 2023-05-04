package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.fulfillment.ShippingDetails;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Calendar;
import java.util.Currency;

public class ShippingTransactionDto {
    @JsonIgnore
    private String id;

    @JsonIgnore
    private ShippingDetailsDto shippingDetails;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar transactionDate;

    private float amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Currency currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShippingDetailsDto getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetailsDto shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public Calendar getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
