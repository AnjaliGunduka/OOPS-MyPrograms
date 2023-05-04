package com.junodx.api.models.payment.insurance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;

public class InsuranceCarrier {
    @JsonIgnore
    private Long id;

    private String carrierId;
    private String name;
    private Address claimsAddress;
    private Phone claimsPhone;
    private Address customerSupportAddress;
    private Phone customerSupportPhone;
    private String claimsEmail;
    private String customerSupportEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getClaimsAddress() {
        return claimsAddress;
    }

    public void setClaimsAddress(Address claimsAddress) {
        this.claimsAddress = claimsAddress;
    }

    public Phone getClaimsPhone() {
        return claimsPhone;
    }

    public void setClaimsPhone(Phone claimsPhone) {
        this.claimsPhone = claimsPhone;
    }

    public Address getCustomerSupportAddress() {
        return customerSupportAddress;
    }

    public void setCustomerSupportAddress(Address customerSupportAddress) {
        this.customerSupportAddress = customerSupportAddress;
    }

    public Phone getCustomerSupportPhone() {
        return customerSupportPhone;
    }

    public void setCustomerSupportPhone(Phone customerSupportPhone) {
        this.customerSupportPhone = customerSupportPhone;
    }

    public String getClaimsEmail() {
        return claimsEmail;
    }

    public void setClaimsEmail(String claimsEmail) {
        this.claimsEmail = claimsEmail;
    }

    public String getCustomerSupportEmail() {
        return customerSupportEmail;
    }

    public void setCustomerSupportEmail(String customerSupportEmail) {
        this.customerSupportEmail = customerSupportEmail;
    }
}
