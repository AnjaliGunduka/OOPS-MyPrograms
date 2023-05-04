package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
public class Preferences {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FetalSexResultsPreferences fstPreferences;

    @Column(name = "opt_out", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean optOut;

    @Column(name = "sms_messages_ok", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean smsMessages;

    public Preferences(){
        this.id = UUID.randomUUID().toString();
        this.fstPreferences = new FetalSexResultsPreferences();
    }

    public FetalSexResultsPreferences getFstPreferences() {
        return fstPreferences;
    }

    public void setFstPreferences(FetalSexResultsPreferences fstPreferences) {
        this.fstPreferences = fstPreferences;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public boolean isSmsMessages() {
        return smsMessages;
    }

    public void setSmsMessages(boolean smsMessages) {
        this.smsMessages = smsMessages;
    }
}
