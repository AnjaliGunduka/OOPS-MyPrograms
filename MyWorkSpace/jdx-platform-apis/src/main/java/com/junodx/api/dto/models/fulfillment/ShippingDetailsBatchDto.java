package com.junodx.api.dto.models.fulfillment;

import com.junodx.api.models.fulfillment.ShippingStatus;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;

public class ShippingDetailsBatchDto {
    private String id;
    private ShippingStatusType currentStatus;
    private ShippingMethodBatchDto toMethod;
    private ShippingMethodBatchDto returnMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShippingStatusType getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ShippingStatusType currentStatus) {
        this.currentStatus = currentStatus;
    }

    public ShippingMethodBatchDto getToMethod() {
        return toMethod;
    }

    public void setToMethod(ShippingMethodBatchDto toMethod) {
        this.toMethod = toMethod;
    }

    public ShippingMethodBatchDto getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(ShippingMethodBatchDto returnMethod) {
        this.returnMethod = returnMethod;
    }
}
