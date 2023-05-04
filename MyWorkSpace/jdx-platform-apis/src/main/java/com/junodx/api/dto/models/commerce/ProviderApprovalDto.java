package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.providers.ProviderOrderDto;

import java.util.Calendar;

public class ProviderApprovalDto {

    @JsonIgnore
    private String id;

    private boolean requiresApproval;
    private boolean approved;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar approvalDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProviderOrderDto approvingProvider;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Calendar getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Calendar approvalDate) {
        this.approvalDate = approvalDate;
    }

    public ProviderOrderDto getApprovingProvider() {
        return approvingProvider;
    }

    public void setApprovingProvider(ProviderOrderDto approvingProvider) {
        this.approvingProvider = approvingProvider;
    }
}
