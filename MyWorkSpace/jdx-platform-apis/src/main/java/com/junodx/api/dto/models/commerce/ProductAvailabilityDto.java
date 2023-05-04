package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.core.DMA;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.ZipCode;

import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Calendar;
import java.util.List;

public class ProductAvailabilityDto {
    protected Long id;
    protected ProductTruncatedDto product;

    protected Boolean outOfRegion;
    protected Boolean soldOut;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Calendar newStockAvailableAt;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected Calendar availableInYourRegionAt;

    protected List<ZipCode> allowedZipCodes;
    protected List<State> allowedStates;
    protected List<DMA> allowedDMAs;

    protected Meta meta;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ProductTruncatedDto getProduct() {
        return product;
    }

    public void setProduct(ProductTruncatedDto product) {
        this.product = product;
    }

    public List<ZipCode> getAllowedZipCodes() {
        return allowedZipCodes;
    }

    public void setAllowedZipCodes(List<ZipCode> allowedZipCodes) {
        this.allowedZipCodes = allowedZipCodes;
    }

    public List<State> getAllowedStates() {
        return allowedStates;
    }

    public void setAllowedStates(List<State> allowedStates) {
        this.allowedStates = allowedStates;
    }

    public List<DMA> getAllowedDMAs() {
        return allowedDMAs;
    }

    public void setAllowedDMAs(List<DMA> allowedDMAs) {
        this.allowedDMAs = allowedDMAs;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Boolean getOutOfRegion() {
        return outOfRegion;
    }

    public void setOutOfRegion(Boolean outOfRegion) {
        this.outOfRegion = outOfRegion;
    }

    public Boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        this.soldOut = soldOut;
    }

    public Calendar getNewStockAvailableAt() {
        return newStockAvailableAt;
    }

    public void setNewStockAvailableAt(Calendar newStockAvailableAt) {
        this.newStockAvailableAt = newStockAvailableAt;
    }

    public Calendar getAvailableInYourRegionAt() {
        return availableInYourRegionAt;
    }

    public void setAvailableInYourRegionAt(Calendar availableInYourRegionAt) {
        this.availableInYourRegionAt = availableInYourRegionAt;
    }
}
