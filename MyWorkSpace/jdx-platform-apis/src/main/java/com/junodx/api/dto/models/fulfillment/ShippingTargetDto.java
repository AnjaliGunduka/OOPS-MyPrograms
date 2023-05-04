package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class ShippingTargetDto {
    @JsonIgnore
    private String id;

    private String recipientName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
