package com.junodx.api.models.payment.card;

import com.junodx.api.models.payment.PaymentInstrument;

public class CreditCardInstrument extends PaymentInstrument {
    private String externalCardId;

    public String getExternalCardId() {
        return externalCardId;
    }

    public void setExternalCardId(String externalCardId) {
        this.externalCardId = externalCardId;
    }
}
