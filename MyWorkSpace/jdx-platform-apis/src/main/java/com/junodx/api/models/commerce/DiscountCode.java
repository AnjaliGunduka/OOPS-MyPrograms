package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.IOwnershipModel;
import com.junodx.api.models.core.Meta;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "discount_code")
public class DiscountCode implements IOwnershipModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ownerId;

    private String owningClientId;

    @Column(name = "code", unique = true, updatable = false)
    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar validFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar expires;

    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean active;

    private Discount discount;

    private Meta meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwningClientId(String owningClientId) {
        this.owningClientId = owningClientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Calendar getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Calendar validFrom) {
        this.validFrom = validFrom;
    }

    public Calendar getExpires() {
        return expires;
    }

    public void setExpires(Calendar expires) {
        this.expires = expires;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String getOwnerId() {
        return this.ownerId;
    }

    @Override
    public String getOwningClientId() {
        return this.owningClientId;
    }
}
