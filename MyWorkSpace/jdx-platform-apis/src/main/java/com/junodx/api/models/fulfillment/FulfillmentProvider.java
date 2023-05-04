package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="fulfillment_provider")
public class FulfillmentProvider {
    @Id
    private String id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = true)
    private Address shipFromAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = true)
    private Phone shipFromContactNumber;

    private String email;

    private String coveredStates;
    private String coveredCountries;

    protected String subProviderName;
    protected String subProviderLogoUrl;

    @OneToMany(targetEntity = ShippingCarrier.class, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<ShippingCarrier> carriers;


    @Column(name = "lab_portal_assigned", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean labPortalAssigned;

    @Column(name = "is_default_provider", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean defaultProvider;

    protected String defaultCarrierId;

   // @JsonInclude(JsonInclude.Include.NON_NULL)
   // private User shipFromResponsibleParty;

    private Meta meta;

    public FulfillmentProvider(){
        this.id = UUID.randomUUID().toString();
        this.shipFromAddress = new Address();
        this.shipFromContactNumber = new Phone();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getShipFromAddress() {
        return shipFromAddress;
    }

    public void setShipFromAddress(Address shipFromAddress) {
        this.shipFromAddress = shipFromAddress;
    }

    public Phone getShipFromContactNumber() {
        return shipFromContactNumber;
    }

    public void setShipFromContactNumber(Phone shipFromContactNumber) {
        this.shipFromContactNumber = shipFromContactNumber;
    }

    public String getCoveredStates() {
        return coveredStates;
    }

    public void setCoveredStates(String coveredStates) {
        this.coveredStates = coveredStates;
    }

    public String getCoveredCountries() {
        return coveredCountries;
    }

    public void setCoveredCountries(String coveredCountries) {
        this.coveredCountries = coveredCountries;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getSubProviderName() {
        return subProviderName;
    }

    public void setSubProviderName(String subProviderName) {
        this.subProviderName = subProviderName;
    }

    public String getSubProviderLogoUrl() {
        return subProviderLogoUrl;
    }

    public void setSubProviderLogoUrl(String subProviderLogoUrl) {
        this.subProviderLogoUrl = subProviderLogoUrl;
    }

    public List<ShippingCarrier> getCarriers() {
        return carriers;
    }

    public void setCarriers(List<ShippingCarrier> carriers) {
        this.carriers = carriers;
    }

    public String getDefaultCarrierId() {
        return defaultCarrierId;
    }

    public void setDefaultCarrierId(String defaultCarrierId) {
        this.defaultCarrierId = defaultCarrierId;
    }

    public boolean isLabPortalAssigned() {
        return labPortalAssigned;
    }

    public void setLabPortalAssigned(boolean labPortalAssigned) {
        this.labPortalAssigned = labPortalAssigned;
    }

    public boolean isDefaultProvider() {
        return defaultProvider;
    }

    public void setDefaultProvider(boolean defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
