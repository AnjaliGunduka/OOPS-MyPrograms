package com.junodx.api.dto.models.commerce;

import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;

import java.util.Calendar;
import java.util.List;


public class PendingOrderDto {
    private String id;
    private float amount;
    private UserOrderDto customer;
  // private List<OrderLineItem> lineItems;
  //  private String cartId;
  //  private String checkoutId;
    private Calendar orderedAt;
    private PendingTransactionDto transaction;

    public static PendingOrderDto fromOrder(Order o, UserOrderDto customer){
        PendingOrderDto newOrder = new PendingOrderDto();
        if(o != null) {
            newOrder.setId(o.getId());
            newOrder.setAmount(o.getAmount());
            newOrder.setOrderedAt(Calendar.getInstance());
            newOrder.setCustomer(customer);
        }

        return newOrder;
    }

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

    public UserOrderDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserOrderDto customer) {
        this.customer = customer;
    }


    /*
    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
    }

     */

    public Calendar getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Calendar orderedAt) {
        this.orderedAt = orderedAt;
    }

    public PendingTransactionDto getTransaction() {
        return transaction;
    }

    public void setTransaction(PendingTransactionDto transaction) {
        this.transaction = transaction;
    }
}
