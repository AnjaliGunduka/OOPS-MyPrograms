package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.providers.ProviderDto;

import java.util.Calendar;

public class ProviderOrderApprovalDto {

    private boolean requiresApproval;
    private boolean approved;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar approvalDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProviderDto approvingProvider;

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

    public ProviderDto getApprovingProvider() {
        return approvingProvider;
    }

    public void setApprovingProvider(ProviderDto approvingProvider) {
        this.approvingProvider = approvingProvider;
    }
}
