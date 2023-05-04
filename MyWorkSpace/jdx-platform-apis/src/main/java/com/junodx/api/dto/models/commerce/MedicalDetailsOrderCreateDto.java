package com.junodx.api.dto.models.commerce;

import java.util.Calendar;

public class MedicalDetailsOrderCreateDto {
    private Calendar lmpDate;
    private boolean overSevenWeeks;
    private boolean overTenWeeks;
    private boolean consented;
    private boolean agreedToTerms;
    private boolean noTransplantNorTransfusion;

    public Calendar getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Calendar lmpDate) {
        this.lmpDate = lmpDate;
    }

    public boolean isOverSevenWeeks() {
        return overSevenWeeks;
    }

    public void setOverSevenWeeks(boolean overSevenWeeks) {
        this.overSevenWeeks = overSevenWeeks;
    }

    public boolean isOverTenWeeks() {
        return overTenWeeks;
    }

    public void setOverTenWeeks(boolean overTenWeeks) {
        this.overTenWeeks = overTenWeeks;
    }

    public boolean isConsented() {
        return consented;
    }

    public void setConsented(boolean consented) {
        this.consented = consented;
    }

    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    public boolean isNoTransplantNorTransfusion() {
        return noTransplantNorTransfusion;
    }

    public void setNoTransplantNorTransfusion(boolean noTransplantNorTransfusion) {
        this.noTransplantNorTransfusion = noTransplantNorTransfusion;
    }
}
