package com.junodx.api.dto.models.laboratory.reports;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.auth.UserSignoutDto;
import com.junodx.api.models.auth.User;

import javax.persistence.*;
import java.util.Calendar;

public class SignoutDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserSignoutDto signatory;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar signedOutAt;

    public UserSignoutDto getSignatory() {
        return signatory;
    }

    public void setSignatory(UserSignoutDto signatory) {
        this.signatory = signatory;
    }

    public Calendar getSignedOutAt() {
        return signedOutAt;
    }

    public void setSignedOutAt(Calendar signedOutAt) {
        this.signedOutAt = signedOutAt;
    }
}
