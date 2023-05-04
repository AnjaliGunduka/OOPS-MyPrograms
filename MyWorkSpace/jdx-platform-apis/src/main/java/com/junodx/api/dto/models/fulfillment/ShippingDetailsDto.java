package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.fulfillment.*;

import javax.persistence.*;
import java.util.List;

public class ShippingDetailsDto {
    @JsonIgnore
        private String id;
    @JsonIgnore
        private FulfillmentDto fulfillment;
        private List<ShippingStatusDto> shippingStatus;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShippingMethodDto toMethod;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShippingMethodDto returnMethod;

        @JsonProperty("toAddress")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShippingTargetDto toAddress;

        @JsonProperty("returnAddress")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShippingTargetDto returnAddress;

        @JsonProperty("transactionProperties")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShippingTransactionDto shippingTransactionDetails;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String trackingCode;

        private Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FulfillmentDto getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(FulfillmentDto fulfillment) {
        this.fulfillment = fulfillment;
    }

    public List<ShippingStatusDto> getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(List<ShippingStatusDto> shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public ShippingMethodDto getToMethod() {
        return toMethod;
    }

    public void setToMethod(ShippingMethodDto toMethod) {
        this.toMethod = toMethod;
    }

    public ShippingMethodDto getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(ShippingMethodDto returnMethod) {
        this.returnMethod = returnMethod;
    }

    public ShippingTargetDto getToAddress() {
        return toAddress;
    }

    public void setToAddress(ShippingTargetDto toAddress) {
        this.toAddress = toAddress;
    }

    public ShippingTargetDto getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(ShippingTargetDto returnAddress) {
        this.returnAddress = returnAddress;
    }

    public ShippingTransactionDto getShippingTransactionDetails() {
        return shippingTransactionDetails;
    }

    public void setShippingTransactionDetails(ShippingTransactionDto shippingTransactionDetails) {
        this.shippingTransactionDetails = shippingTransactionDetails;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
