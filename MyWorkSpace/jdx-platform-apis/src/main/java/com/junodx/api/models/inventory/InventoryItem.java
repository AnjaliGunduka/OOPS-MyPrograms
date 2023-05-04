package com.junodx.api.models.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.core.Meta;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name="product_inventory")
public class InventoryItem {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Transient
    private String productId;

    private Long availableUnits;

    private Boolean released;

    private Boolean available;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar firstCreated;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar lastUpdated;

    private Meta meta;

    //private WarehouseLocation warehouseLocation;


    public InventoryItem(){
        this.id = UUID.randomUUID().toString();
    }

   

	public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(long availableUnits) {
        this.availableUnits = availableUnits;
    }

    public void setAvailableUnits(Long availableUnits) {
        this.availableUnits = availableUnits;
    }

    public Boolean getReleased() {
        return released;
    }

    public void setReleased(Boolean released) {
        this.released = released;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Calendar getFirstCreated() {
        return firstCreated;
    }

    public void setFirstCreated(Calendar firstCreated) {
        this.firstCreated = firstCreated;
    }

    public Calendar getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Calendar lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("productId")
    public String getProductId(){
        if(this.product != null && this.product.getId() != null)
            return this.product.getId();

        return null;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
