package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.fulfillment.ShippingCarrier;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.types.ShippingDeliveryType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class ShippingMethodDto {
    @JsonIgnore
    private String id;

    @JsonIgnore
    private ShippingDetailsDto shippingDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingCarrierDto carrier;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingDeliveryType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trackingCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trackingUrl;

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

    public ShippingCarrierDto getCarrier() {
        return carrier;
    }

    public void setCarrier(ShippingCarrierDto carrier) {
        this.carrier = carrier;
    }

    public ShippingDeliveryType getType() {
        return type;
    }

    public void setType(ShippingDeliveryType type) {
        this.type = type;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }
}
