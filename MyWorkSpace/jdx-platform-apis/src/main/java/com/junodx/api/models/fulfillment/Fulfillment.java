package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.Kit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="order_fulfillment")
public class Fulfillment {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name="line_item", nullable = false)
    @JsonIgnore
    private OrderLineItem orderLineItem;

    private String fulfillmentOrderId; //typically from a 3rd party

    @OneToOne
    @JoinColumn(name = "fulfillment_provider_id", nullable = true)
    private FulfillmentProvider fulfillmentProvider;

    @OneToOne
    @JoinColumn(name = "kit_id", nullable = true)
    private Kit kit;

    //If the same line item needs to be sent out a second time given a redraw, then it is a different fulfillment
    @OneToOne(mappedBy = "fulfillment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingDetails shippingDetails;

    @Column(name = "completed", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean completed;

    @Column(name = "shipment_created", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean shipmentCreated;

    @Column(name = "redraw", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean redraw;

    @Column(name = "estimated_to_ship_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Calendar estimatedToShipAt;

    @Column(name = "fulfilled_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar fulfilledAt;

    private Meta meta;

    public Fulfillment(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderLineItem getOrderLineItem() {
        return orderLineItem;
    }

    public void setOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItem = orderLineItem;
    }

    public String getFulfillmentOrderId() {
        return fulfillmentOrderId;
    }

    public void setFulfillmentOrderId(String fulfillmentOrderId) {
        this.fulfillmentOrderId = fulfillmentOrderId;
    }

    public FulfillmentProvider getFulfillmentProvider() {
        return fulfillmentProvider;
    }

    public void setFulfillmentProvider(FulfillmentProvider fulfillmentProvider) {
        this.fulfillmentProvider = fulfillmentProvider;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
        this.shippingDetails.setFulfillment(this);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public boolean isShipmentCreated() {
        return shipmentCreated;
    }

    public void setShipmentCreated(boolean shipmentCreated) {
        this.shipmentCreated = shipmentCreated;
    }

    public Calendar getEstimatedToShipAt() {
        return estimatedToShipAt;
    }

    public void setEstimatedToShipAt(Calendar estimatedToShipAt) {
        this.estimatedToShipAt = estimatedToShipAt;
    }

    public Calendar getFulfilledAt() {
        return fulfilledAt;
    }

    public void setFulfilledAt(Calendar fulfilledAt) {
        this.fulfilledAt = fulfilledAt;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
