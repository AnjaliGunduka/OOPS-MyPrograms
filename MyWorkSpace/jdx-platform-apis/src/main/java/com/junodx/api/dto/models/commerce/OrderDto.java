package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Discount;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.commerce.Tax;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.commerce.types.OrderStatusType;

import javax.persistence.*;
import java.util.*;

public class OrderDto {

    private String id;
    private float amount;
    private float subTotal;
    private float totalShipping;
    private float totalTax;
    private Currency currency;
    private UserOrderDto customer;
    private OrderStatusType currentStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checkoutId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cartId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String crmOrderId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String crmContactId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shippingOrderId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsOrderId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsContactId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String insuranceBillingOrderId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DiscountDto discount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Tax tax;
    private List<OrderStatusDto> orderStatusHistory;
    private List<OrderLineItemDto> lineItems;

    //   @JsonInclude(JsonInclude.Include.NON_NULL)
    //   private List<Transaction> transactions;

    //   @JsonInclude(JsonInclude.Include.NON_NULL)
    //   private List<PaymentInstrument> paymentInstruments;

    //   @JsonInclude(JsonInclude.Include.NON_NULL)
    //   private PatientDetails patient;

    //private MedicalDetailsRepository medicalDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getTotalShipping() {
        return totalShipping;
    }

    public void setTotalShipping(float totalShipping) {
        this.totalShipping = totalShipping;
    }

    public float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(float totalTax) {
        this.totalTax = totalTax;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public UserOrderDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserOrderDto customer) {
        this.customer = customer;
    }

    public OrderStatusType getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(OrderStatusType currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCrmOrderId() {
        return crmOrderId;
    }

    public void setCrmOrderId(String crmOrderId) {
        this.crmOrderId = crmOrderId;
    }

    public String getCrmContactId() {
        return crmContactId;
    }

    public void setCrmContactId(String crmContactId) {
        this.crmContactId = crmContactId;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public String getLimsOrderId() {
        return limsOrderId;
    }

    public void setLimsOrderId(String limsOrderId) {
        this.limsOrderId = limsOrderId;
    }

    public String getLimsContactId() {
        return limsContactId;
    }

    public void setLimsContactId(String limsContactId) {
        this.limsContactId = limsContactId;
    }

    public String getInsuranceBillingOrderId() {
        return insuranceBillingOrderId;
    }

    public void setInsuranceBillingOrderId(String insuranceBillingOrderId) {
        this.insuranceBillingOrderId = insuranceBillingOrderId;
    }

    public DiscountDto getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDto discount) {
        this.discount = discount;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public List<OrderStatusDto> getOrderStatusHistory() {
        return orderStatusHistory;
    }

    public void setOrderStatusHistory(List<OrderStatusDto> orderStatusHistory) {
        this.orderStatusHistory = orderStatusHistory;
    }

    public List<OrderLineItemDto> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItemDto> lineItems) {
        this.lineItems = lineItems;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void addLineItem(OrderLineItemDto lineItem){
        if(this.lineItems == null)
            this.lineItems = new ArrayList<>();
        this.lineItems.add(lineItem);
    }
}

