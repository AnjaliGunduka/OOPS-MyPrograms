package com.junodx.api.dto.models.fulfillment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.commerce.OrderLineItemDto;
import com.junodx.api.dto.models.laboratory.KitDto;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.laboratory.Kit;

import javax.persistence.*;
import java.util.List;

public class FulfillmentDto {
    private String id;

    @JsonIgnore
    private OrderLineItemDto orderLineItem;

    private String fulfillmentOrderId; //typically from a 3rd party

    private FulfillmentProviderDto fulfillmentProvider;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KitDto> kits;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ShippingDetailsDto> shippingDetails;


    private Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderLineItemDto getOrderLineItem() {
        return orderLineItem;
    }

    public void setOrderLineItem(OrderLineItemDto orderLineItem) {
        this.orderLineItem = orderLineItem;
    }

    public String getFulfillmentOrderId() {
        return fulfillmentOrderId;
    }

    public void setFulfillmentOrderId(String fulfillmentOrderId) {
        this.fulfillmentOrderId = fulfillmentOrderId;
    }

    public FulfillmentProviderDto getFulfillmentProvider() {
        return fulfillmentProvider;
    }

    public void setFulfillmentProvider(FulfillmentProviderDto fulfillmentProvider) {
        this.fulfillmentProvider = fulfillmentProvider;
    }

    public List<KitDto> getKits() {
        return kits;
    }

    public void setKits(List<KitDto> kits) {
        this.kits = kits;
    }

    public List<ShippingDetailsDto> getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(List<ShippingDetailsDto> shippingDetailsTo) {
        this.shippingDetails = shippingDetailsTo;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}

