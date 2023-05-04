package com.junodx.api.models.payment.insurance;

import com.junodx.api.models.auth.User;
import com.junodx.api.models.payment.insurance.types.ClaimStatusType;

import java.util.Calendar;

public class ClaimStatus {
    private Long id;
    private String claimStatusId;
    private Calendar createdAt;
    private User createdBy;
    private ClaimStatusType type;
    private String note;

    private String claimProcessorEventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimStatusId() {
        return claimStatusId;
    }

    public void setClaimStatusId(String claimStatusId) {
        this.claimStatusId = claimStatusId;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public ClaimStatusType getType() {
        return type;
    }

    public void setType(ClaimStatusType type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClaimProcessorEventId() {
        return claimProcessorEventId;
    }

    public void setClaimProcessorEventId(String claimProcessorEventId) {
        this.claimProcessorEventId = claimProcessorEventId;
    }
}
