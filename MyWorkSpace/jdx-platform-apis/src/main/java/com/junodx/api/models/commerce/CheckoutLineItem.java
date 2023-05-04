package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class CheckoutLineItem {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "checkout_id", nullable = false)
    @JsonIgnore
    private Checkout checkout;

    private String productId;
    private float amount;
    private String currency;
    private int quantity;

    @Column(name = "is_shipped", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean shipped;

    public CheckoutLineItem(){
        this.id = UUID.randomUUID().toString();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }
}
