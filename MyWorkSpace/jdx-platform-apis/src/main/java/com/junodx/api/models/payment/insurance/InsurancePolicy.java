package com.junodx.api.models.payment.insurance;

import java.util.Calendar;

public class InsurancePolicy {
    private InsuranceCarrier carrier;
    private String policyId;
    private String groupId;
    private String policyHolderName;
    private Calendar expiresAt;

    public InsuranceCarrier getCarrier() {
        return carrier;
    }

    public void setCarrier(InsuranceCarrier carrier) {
        this.carrier = carrier;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public Calendar getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Calendar expiresAt) {
        this.expiresAt = expiresAt;
    }
}
