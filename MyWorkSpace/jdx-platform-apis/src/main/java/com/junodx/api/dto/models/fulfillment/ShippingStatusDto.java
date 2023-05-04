package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;

import javax.persistence.JoinColumn;
import java.util.Calendar;

public class ShippingStatusDto {
    @JsonIgnore
    private String id;

    private boolean isCurrent;

    private boolean toCustomer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingStatusType status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar statusTimestamp;

    @JsonIgnore
    private ShippingDetailsDto shippingDetails;

    private boolean toPatient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean isToCustomer() {
        return toCustomer;
    }

    public void setToCustomer(boolean toCustomer) {
        this.toCustomer = toCustomer;
    }

    public ShippingStatusType getStatus() {
        return status;
    }

    public void setStatus(ShippingStatusType status) {
        this.status = status;
    }

    public Calendar getStatusTimestamp() {
        return statusTimestamp;
    }

    public void setStatusTimestamp(Calendar statusTimestamp) {
        this.statusTimestamp = statusTimestamp;
    }

    public ShippingDetailsDto getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetailsDto shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public boolean isToPatient() {
        return toPatient;
    }

    public void setToPatient(boolean toPatient) {
        this.toPatient = toPatient;
    }
}
