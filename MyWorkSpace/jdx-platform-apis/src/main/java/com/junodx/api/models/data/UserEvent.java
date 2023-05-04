package com.junodx.api.models.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.data.types.UserEventType;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "user_status_event")
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="status_type")
    private UserEventType type;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="created_at")
    private Calendar createdAt;

    private String userId;

    @Column(name = "macro_reportable", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean macroReportable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "user_agent")
    private String userAgent;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "ip_address")
    private String ip4Address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "referrer")
    private String referrer;

    @Column(name = "attributes")
    private String attributes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEventType getType() {
        return type;
    }

    public void setType(UserEventType type) {
        this.type = type;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isMacroReportable() {
        return macroReportable;
    }

    public void setMacroReportable(boolean macroReportable) {
        this.macroReportable = macroReportable;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp4Address() {
        return ip4Address;
    }

    public void setIp4Address(String ip4Address) {
        this.ip4Address = ip4Address;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
