package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_authority")
public class Authority implements Serializable {

    public static final String ROLE_PATIENT = "ROLE_PATIENT";
    public static final String ROLE_LAB_TECHNICIAN = "ROLE_LAB_TECHNICIAN";
    public static final String ROLE_LAB_DIRECTOR = "ROLE_LAB_DIRECTOR";
    public static final String ROLE_LAB_SUPERVISOR = "ROLE_LAB_SUPERVISOR";
    public static final String ROLE_MEDICAL_PROVIDER = "ROLE_MEDICAL_PROVIDER";
    public static final String ROLE_LAB_REPORTS_VIEWER = "ROLE_LAB_REPORTS_VIEWER";
    public static final String ROLE_CSR = "ROLE_CSR";
    public static final String ROLE_SHIPPER = "ROLE_SHIPPER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PRACTICE_ADMIN = "ROLE_PRACTICE_ADMIN";
    public static final String ROLE_PRACTICE_REP = "ROLE_PRACTICE_REP";
    public static final String ROLE_PROVIDER = "ROLE_PROVIDER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "NAME", length = 50, nullable = false, unique = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Authority(String eauth){
        this.name = eauth;
    }

    public Authority() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return name == authority.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}


