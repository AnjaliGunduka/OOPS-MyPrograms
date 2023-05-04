package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.laboratory.BatchRun;
import com.junodx.api.models.patient.PatientDetails;
import com.sun.istack.NotNull;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(name = "UniqueEmailAndClientId", columnNames = { "email", "owning_client_id" }))
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "username")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 255)
    private String password;

    @Column(name="owning_client_id")
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @NotNull
    @Column(name = "activated", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean activated;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "activation_ts")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar activationTs;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<com.junodx.api.models.auth.Authority> authorities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Phone primaryPhone;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    //@Temporal(TemporalType.DATE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar lastLoggedIn;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar lastOrderedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address primaryAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AttributeOverrides({
            @AttributeOverride(name="name",
                    column=@Column(name="billing_name")),
            @AttributeOverride(name="street",
                    column=@Column(name="billing_street")),
            @AttributeOverride(name="city",
                    column=@Column(name="billing_city")),
            @AttributeOverride(name="state",
                    column=@Column(name="billing_state")),
            @AttributeOverride(name="postalCode",
                    column=@Column(name="billing_postal_code")),
            @AttributeOverride(name="country",
                    column=@Column(name="billing_country")),
            @AttributeOverride(name="primaryMailingAddress",
                    column=@Column(name="billing_primary_address")),
            @AttributeOverride(name="primaryAddress",
                    column=@Column(name="billing_address")),
            @AttributeOverride(name="isResidential",
                    column=@Column(name="billing_is_residential")),
    })
    private Address billingAddress;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PatientDetails patientDetails;

    @Column(name="lims_contact_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsContactId;

    @Column(name="stripe_customer_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stripeCustomerId;

    @Column(name="xifin_patient_id", unique = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String xifinPatientId;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    //private List<UserDevice> devices;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    @JsonIgnore
    private UserType userType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Preferences preferences;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public User() {
        this.id = String.valueOf(UUID.randomUUID());
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.authorities = new ArrayList<>();
        this.userType = UserType.STANDARD;
        this.primaryAddress = new Address();
        this.primaryPhone = new Phone();
        this.activated = false;
        this.status = UserStatus.PROVISIONAL;
       // this.devices = new ArrayList<>();
    }

    public User(String email, UserType type, Authority defaultAuthority){
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.authorities = new ArrayList<>();
            this.authorities.add(defaultAuthority);
        this.userType = type;
        this.firstName = "SYSTEM";
        this.lastName = "USER";
        this.username = "system_user";
        this.primaryAddress = new Address();
        this.primaryPhone = new Phone();
        this.activated = false;
        this.status = UserStatus.PROVISIONAL;
        defaultAuthority.setUser(this);
      //  this.devices = new ArrayList<>();
    }

    public User(String uuid, String email, String firstName, String lastName){
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = new ArrayList<>();
        this.userType = UserType.STANDARD;
        this.primaryAddress = new Address();
        this.primaryPhone = new Phone();
        this.activated = false;
        this.status = UserStatus.PROVISIONAL;
      //  this.devices = new ArrayList<>();
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Calendar getActivationTs() {
        return activationTs;
    }

    public void setActivationTs(Calendar activationTs) {
        this.activationTs = activationTs;
    }

    /*
    public List<UserDevice> getDevices() {
        return devices;
    }

    public void addDevice(UserDevice device){
        if(this.devices == null)
            this.devices = new ArrayList<>();
        this.devices.add(device);
    }

    public void clearDevices(){
        if(this.devices != null)
            this.devices.clear();
    }
    
    public void setDevices(List<UserDevice> devices) {
        this.devices = devices;
    }

     */

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
        for(Authority a : this.authorities)
            a.setUser(this);
    }

    //@JsonIgnore
    public List<String> getAuthoritiesAsStringList(){
        List<String> auths = new ArrayList<>();
        if(this.authorities == null)
            this.authorities = new ArrayList<>();

        for(Authority a : this.authorities)
            auths.add(a.getName());

        return auths;
    }

    public void addAuthority(Authority authority){
        this.authorities.add(authority);
        authority.setUser(this);
    }

    public void removeAuthority(Authority authority){
        this.authorities.remove(authority);
    }

    public void clearAuthorities(){
        this.authorities.clear();
    }

    public Phone getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(Phone primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonIgnore
    public void updateAge(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateOfBirth);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Long val = (Calendar.getInstance().getTimeInMillis() - cal.getTimeInMillis()) / 31556952000l;
            this.age = val.intValue();
        } catch (Exception e) {}
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
        patientDetails.setUser(this);
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public UserType getUserType() {
        if(userType == null)
            this.userType = UserType.STANDARD;

        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getLimsContactId() {
        return limsContactId;
    }

    public void setLimsContactId(String limsContactId) {
        this.limsContactId = limsContactId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Integer getAge() {
        if(age == null && dateOfBirth != null)
            updateAge();

        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
        if(this.age == null && dateOfBirth != null)
            updateAge();
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getXifinPatientId() {
        return xifinPatientId;
    }

    public void setXifinPatientId(String xifinPatientId) {
        this.xifinPatientId = xifinPatientId;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Calendar getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(Calendar lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public Calendar getLastOrderedAt() {
        return lastOrderedAt;
    }

    public void setLastOrderedAt(Calendar lastOrderedAt) {
        this.lastOrderedAt = lastOrderedAt;
    }

    public static User createDummyUser(){
        User u = new User();
        u.setUsername("General_test_user");
        u.setFirstName("Juno_test");
        u.setLastName("User");
        u.setEmail("no-email@junodx.com");
        u.setUserType(UserType.TEST);

        return u;
    }

    /*
    @Override
    public String toString() {
        String returnVal = "{ \n\t\"id\": \"" + this.id + "\",\n" + "\t\"uuid\": \"" + this.uuid + "\",\n" +  "\t\"username\": \"" + this.username + "\",\n" + "\t\"firstName\": \"" + this.firstName + "\",\n\t\"lastName\": \"" + this.lastName + "\",\n\t\"email\": \"" + this.email + "\",\n";

        returnVal += "\t\"authorities\": [";

        Iterator<Authority> i = this.authorities.iterator();
        while(i.hasNext()){
            Authority authority = i.next();
            if(authority != null){
                returnVal += "\"" + authority + "\"";
            }
        }
        returnVal += "]\n}";

        return returnVal;
    }

     */

}
