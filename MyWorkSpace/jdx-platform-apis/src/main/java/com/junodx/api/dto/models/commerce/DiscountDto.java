package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.models.commerce.types.DiscountType;

import javax.persistence.*;


public class DiscountDto {


    private DiscountType type;
    private DiscountMode mode;
    private boolean isDiscountApplied;
    private float amountDiscounted;

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private OrderDto order;

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public DiscountMode getMode() {
        return mode;
    }

    public void setMode(DiscountMode mode) {
        this.mode = mode;
    }

    public boolean isDiscountApplied() {
        return isDiscountApplied;
    }

    public void setDiscountApplied(boolean discountApplied) {
        isDiscountApplied = discountApplied;
    }

    public float getAmountDiscounted() {
        return amountDiscounted;
    }

    public void setAmountDiscounted(float amountDiscounted) {
        this.amountDiscounted = amountDiscounted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }
}

