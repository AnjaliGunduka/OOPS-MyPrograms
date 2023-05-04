package com.junodx.api.models.providers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="provider")
public class Provider  {

    @Id
    @Column(name="id")
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = true, unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = true)
    private Phone contactPhone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = true)
    private Address contactAddress;

    @Column(name="npi")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String npi;

    @Column(name="upin")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String upin;

    @Column(name = "is_practicing")
    private boolean isPracticing;

    @Column(name="salesforce_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String salesforceId;

    @Column(name="lims_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsId;

    @Column(name="xifin_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String xifinId;

    @Column(name = "default_provider", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean defaultProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

  //  @JsonInclude(JsonInclude.Include.NON_NULL)
  //  private Phone faxNumber;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Specialty> specialties;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MedicalLicense> licenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="practice_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Practice practice;

    private Meta meta;

    public Provider(){
        this.id = UUID.randomUUID().toString();
        this.specialties = new ArrayList<>();
        this.licenses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    public String getNpi() {
        return npi;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getName(){
        return this.firstName + " " + this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(Phone contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Address getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(Address contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getUpin() {
        return upin;
    }

    public void setUpin(String upin) {
        this.upin = upin;
    }

    public boolean isPracticing() {
        return isPracticing;
    }

    public void setPracticing(boolean practicing) {
        isPracticing = practicing;
    }

    /*
    public Phone getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(Phone faxNumber) {
        this.faxNumber = faxNumber;
    }

     */

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }

    public List<MedicalLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<MedicalLicense> licenses) {
        this.licenses = licenses;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    @JsonProperty("practice_id")
    public String getPracticeId() {
        if(this.practice != null && this.practice.getId() != null)
            return this.practice.getId();

        return null;
    }

    public boolean isDefaultProvider() {
        return defaultProvider;
    }

    public void setDefaultProvider(boolean defaultProvider) {
        this.defaultProvider = defaultProvider;
    }

    public UserStatus getStatus() {
        if(this.status == null)
            this.status = UserStatus.NEW;

        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
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
