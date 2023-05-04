package com.junodx.api.dto.models.fulfillment;

public class MultipleShippingLabelsDto {
    protected ShipmentLabelDto toLabel;
    protected ShipmentLabelDto returnLabel;

    public ShipmentLabelDto getToLabel() {
        return toLabel;
    }

    public void setToLabel(ShipmentLabelDto toLabel) {
        this.toLabel = toLabel;
    }

    public ShipmentLabelDto getReturnLabel() {
        return returnLabel;
    }

    public void setReturnLabel(ShipmentLabelDto returnLabel) {
        this.returnLabel = returnLabel;
    }
}
