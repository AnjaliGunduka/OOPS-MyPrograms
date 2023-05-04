package com.junodx.api.models.providers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="practice")
public class Practice {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="name", unique = true)
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "default_practice", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean defaultPractice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address contactAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone primaryPhone;

  //  @JsonInclude(JsonInclude.Include.NON_NULL)
  //  private Phone secondaryPhone;

    @Column(name = "patient_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String patientEmail;

    @Column(name = "salesforce_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salesforceId;

    @Column(name = "lims_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsId;

    @Column(name = "xifin_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String xifinId;

    @Column(name = "billing_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String billingEmail;

    @Column(name = "primary_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String primaryEmail;

    //Do not attach these to hibernate, allow the app to pull them as needed
    @OneToMany(mappedBy = "practice")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Provider> providers;

    //Do not attach these to hibernate, allow the app to pull them as needed
    @OneToMany(mappedBy = "practice", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Location> locations;

    private Meta meta;

    public Practice(){
        this.id = UUID.randomUUID().toString();
        providers = new ArrayList<>();
        locations = new ArrayList<>();
        isActive = true;
        contactAddress = new Address();
        primaryPhone = new Phone();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Address getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(Address contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Phone getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(Phone primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    /*
    public Phone getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(Phone secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

     */

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public void addProvider(Provider provider){
        this.providers.add(provider);
    }

    public void removeProvider(Provider provider){
        this.providers.remove(provider);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location){
        this.locations.add(location);
    }

    public void removeLocation(Location location){
        this.locations.remove(location);
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    public boolean isDefaultPractice() {
        return defaultPractice;
    }

    public void setDefaultPractice(boolean defaultPractice) {
        this.defaultPractice = defaultPractice;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getLimsId() {
        return limsId;
    }

    public void setLimsId(String limsId) {
        this.limsId = limsId;
    }

    public String getXifinId() {
        return xifinId;
    }

    public void setXifinId(String xifinId) {
        this.xifinId = xifinId;
    }

	
}
