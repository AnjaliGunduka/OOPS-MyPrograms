package com.junodx.api.dto.models.fulfillment;

public class FulfillmentBatchDto {
    private String id;
    private ShippingDetailsBatchDto shippingDetails;
    private boolean completed;
    private boolean shipmentCreated;
    private boolean redraw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShippingDetailsBatchDto getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetailsBatchDto shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isShipmentCreated() {
        return shipmentCreated;
    }

    public void setShipmentCreated(boolean shipmentCreated) {
        this.shipmentCreated = shipmentCreated;
    }

    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }
}
