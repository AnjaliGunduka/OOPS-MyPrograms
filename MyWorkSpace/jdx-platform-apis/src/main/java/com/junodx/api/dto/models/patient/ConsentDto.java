package com.junodx.api.dto.models.patient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.patient.types.ConsentType;

import java.util.Calendar;

public class ConsentDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String formName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConsentType type;

    private boolean approval;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar approvalDate;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public ConsentType getType() {
        return type;
    }

    public void setType(ConsentType type) {
        this.type = type;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public Calendar getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Calendar approvalDate) {
        this.approvalDate = approvalDate;
    }


}
