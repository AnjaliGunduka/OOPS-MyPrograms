package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.commerce.ProductDimensions;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.commerce.types.ResultsConfigurationTemplate;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.types.ReportConfiguration;

import javax.persistence.*;
import java.util.Currency;
import java.util.List;

public class ProductTruncatedDto {
    protected String id;
    protected String name;
    protected ProductType type;
    protected boolean isActive;
    protected boolean isShippable;
    protected boolean isTaxable;
    protected ProductDimensions dimensions;
    protected float price;
    protected Currency currency;
    protected String sku;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isShippable() {
        return isShippable;
    }

    public void setShippable(boolean shippable) {
        isShippable = shippable;
    }

    public boolean isTaxable() {
        return isTaxable;
    }

    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
    }

    public ProductDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ProductDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
