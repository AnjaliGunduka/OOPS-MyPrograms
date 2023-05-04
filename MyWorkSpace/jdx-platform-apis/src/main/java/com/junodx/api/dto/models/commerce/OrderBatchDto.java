package com.junodx.api.dto.models.commerce;

import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.commerce.types.OrderStatusType;

import java.util.Currency;
import java.util.List;

public class OrderBatchDto {
    private String id;
    private float amount;
    private float subTotal;
    private float totalShipping;
    private float totalTax;
    private Currency currency;
    private boolean requiresShipping;
    private boolean requiresRedraw;
    private boolean withInsurance;
    private boolean resultsAvailable;
    private UserOrderDto customer;
    private OrderStatusType currentStatus;
    private List<OrderLineItemBatchDto> lineItems;

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

    public boolean isRequiresShipping() {
        return requiresShipping;
    }

    public void setRequiresShipping(boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

    public boolean isRequiresRedraw() {
        return requiresRedraw;
    }

    public void setRequiresRedraw(boolean requiresRedraw) {
        this.requiresRedraw = requiresRedraw;
    }

    public boolean isWithInsurance() {
        return withInsurance;
    }

    public void setWithInsurance(boolean withInsurance) {
        this.withInsurance = withInsurance;
    }

    public boolean isResultsAvailable() {
        return resultsAvailable;
    }

    public void setResultsAvailable(boolean resultsAvailable) {
        this.resultsAvailable = resultsAvailable;
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

    public List<OrderLineItemBatchDto> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItemBatchDto> lineItems) {
        this.lineItems = lineItems;
    }
}
