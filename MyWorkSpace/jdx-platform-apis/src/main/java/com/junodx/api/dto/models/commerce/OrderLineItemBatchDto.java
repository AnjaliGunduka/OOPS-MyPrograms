package com.junodx.api.dto.models.commerce;

import com.junodx.api.dto.models.fulfillment.FulfillmentBatchDto;
import com.junodx.api.dto.models.laboratory.LaboratoryOrderDetailsBatchDto;
import com.junodx.api.models.commerce.types.ProductType;

import java.util.List;

public class OrderLineItemBatchDto {
    private String id;
    private List<FulfillmentBatchDto> fulfillments;
    private LaboratoryOrderDetailsBatchDto laboratoryOrderDetails;
    private ProductType type;
    private String productId;
    private String productName;
    private String currentFulfillmentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FulfillmentBatchDto> getFulfillments() {
        return fulfillments;
    }

    public void setFulfillments(List<FulfillmentBatchDto> fulfillments) {
        this.fulfillments = fulfillments;
    }

    public LaboratoryOrderDetailsBatchDto getLaboratoryOrderDetails() {
        return laboratoryOrderDetails;
    }

    public void setLaboratoryOrderDetails(LaboratoryOrderDetailsBatchDto laboratoryOrderDetails) {
        this.laboratoryOrderDetails = laboratoryOrderDetails;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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

    public String getCurrentFulfillmentId() {
        return currentFulfillmentId;
    }

    public void setCurrentFulfillmentId(String currentFulfillmentId) {
        this.currentFulfillmentId = currentFulfillmentId;
    }
}
