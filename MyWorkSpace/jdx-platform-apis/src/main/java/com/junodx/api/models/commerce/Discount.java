package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.models.commerce.types.DiscountType;

import javax.persistence.*;

@Embeddable
public class Discount {


    @Enumerated(EnumType.STRING)
    @Column(name="discount_type")
    private DiscountType type;

    @Enumerated(EnumType.STRING)
    @Column(name="discount_mode")
    private DiscountMode mode;

    @Column(name="is_discount_applied")
    private boolean isDiscountApplied;

    @Column(name="amount_discounted")
    private float amountDiscounted;

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
}
