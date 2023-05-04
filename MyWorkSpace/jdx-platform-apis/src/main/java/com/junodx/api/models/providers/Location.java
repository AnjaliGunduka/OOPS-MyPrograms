package com.junodx.api.models.providers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Phone;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "practice_location")
public class Location {
    @Id
    private String id;

    @Column(name="name")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address address;

    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Phone phone;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    //private Phone fax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="practice_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Practice practice;

    public Location(){
        this.id = UUID.randomUUID().toString();
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

    /*
    public Phone getFax() {
        return fax;
    }

    public void setFax(Phone fax) {
        this.fax = fax;
    }

     */

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }
}
