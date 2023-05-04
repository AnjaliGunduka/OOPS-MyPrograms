package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.dto.models.fulfillment.FulfillmentDto;
import com.junodx.api.dto.models.laboratory.LaboratoryOrderDto;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.core.Meta;

import javax.persistence.Column;

public class OrderLineItemDto {

    private String id;
    private ProductType type;
    private String description;
    private float amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float originalUnitPrice;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float insuranceEstimatedCoveredAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sku;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean taxable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean requiresShipping;

    private boolean isDirectlyProvided;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isInOfficeCollected;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LaboratoryOrderDto laboratoryOrder;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FulfillmentDto fulfillment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getOriginalUnitPrice() {
        return originalUnitPrice;
    }

    public void setOriginalUnitPrice(float originalUnitPrice) {
        this.originalUnitPrice = originalUnitPrice;
    }

    public float getInsuranceEstimatedCoveredAmount() {
        return insuranceEstimatedCoveredAmount;
    }

    public void setInsuranceEstimatedCoveredAmount(float insuranceEstimatedCoveredAmount) {
        this.insuranceEstimatedCoveredAmount = insuranceEstimatedCoveredAmount;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public boolean isRequiresShipping() {
        return requiresShipping;
    }

    public void setRequiresShipping(boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

    public boolean isDirectlyProvided() {
        return isDirectlyProvided;
    }

    public void setDirectlyProvided(boolean directlyProvided) {
        isDirectlyProvided = directlyProvided;
    }

    public boolean isInOfficeCollected() {
        return isInOfficeCollected;
    }

    public void setInOfficeCollected(boolean inOfficeCollected) {
        isInOfficeCollected = inOfficeCollected;
    }

    public LaboratoryOrderDto getLaboratoryOrder() {
        return laboratoryOrder;
    }

    public void setLaboratoryOrder(LaboratoryOrderDto laboratoryOrder) {
        this.laboratoryOrder = laboratoryOrder;
    }

    public FulfillmentDto getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(FulfillmentDto fulfillment) {
        this.fulfillment = fulfillment;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
