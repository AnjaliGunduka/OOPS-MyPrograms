package com.junodx.api.models.payment.insurance;

import com.junodx.api.models.payment.PaymentInstrument;

public class InsuranceInstrument extends PaymentInstrument {
    private InsurancePolicy policy;

    public InsurancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(InsurancePolicy policy) {
        this.policy = policy;
    }
}
