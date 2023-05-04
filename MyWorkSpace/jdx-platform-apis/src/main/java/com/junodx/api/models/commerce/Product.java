package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.commerce.types.ResultsConfigurationTemplate;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.types.ReportConfiguration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Entity
public class Product {
    @Id
    protected String id;

    protected String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String longDescription;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String shortDescription;
    protected ProductType type;

    protected String productImageUrlPoster;
    protected String productImageUrlThumbnail;
    protected String productImageUrlOption2;
    protected String productImageUrlOption3;

    protected boolean isActive;
    protected boolean isShippable;
    protected boolean isTaxable;

    @Enumerated(EnumType.STRING)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected ReportConfiguration reportConfiguration;

    @Column(name = "requires_provider_approval", columnDefinition = "BOOLEAN DEFAULT TRUE")
    protected boolean requiresProviderApproval;

    protected ProductDimensions dimensions;
    protected float price;
    protected Currency currency;

    @OneToMany
    @JoinTable(
            name="product_fulfillment_providers",
            joinColumns = @JoinColumn( name="product_id"),
            inverseJoinColumns = @JoinColumn( name="fulfillment_provider_id"))
    protected List<FulfillmentProvider> fulfillmentProviders;

    @OneToMany
    @JoinTable(
            name="product_laboratory_providers",
            joinColumns = @JoinColumn( name="product_id"),
            inverseJoinColumns = @JoinColumn( name="laboratory_provider_id"))
    protected List<Laboratory> laboratoryProviders;

    protected String xifinTestId;
    protected String stripeProductId;
    protected String cardConnectProductId;
    protected String salesforceProductId;
    protected String salesforcePriceBookId;

    @Column(name = "salesforce_pricebook_entry_id")
    protected String salesforcePriceBookEntryId;

    protected String sku;
    protected String alliedPackageUnitId;

    @Column(name = "lab_tat")
    private Integer laboratoryTurnAroundTime;

    protected String limsReportId;

    @Enumerated(EnumType.STRING)
    protected ResultsConfigurationTemplate resultsConfigurationTemplate;

    @Column(name = "allows_assisted_collection_video_call", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean allowsAssistedCollectionVideoCall;

    @Column(name = "allows_self_collected_video_upload", columnDefinition = "BOOLEAN DEFAULT FALSE")
    protected boolean allowsSelfCollectedVideoUpload;

    @OneToOne(mappedBy = "product")
    protected InventoryItem inventoryItem;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected ProductAvailablity availablity;

    public Product(){
        this.id = UUID.randomUUID().toString();
        this.laboratoryProviders = new ArrayList<>();
        this.fulfillmentProviders = new ArrayList<>();
    }

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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public void addFulfillmentProvider(FulfillmentProvider provider){
        if(fulfillmentProviders == null)
            fulfillmentProviders = new ArrayList<>();
        fulfillmentProviders.add(provider);
    }
    public List<FulfillmentProvider> getFulfillmentProviders() {
        return fulfillmentProviders;
    }

    public void setFulfillmentProviders(List<FulfillmentProvider> fulfillmentProviders) {
        this.fulfillmentProviders = fulfillmentProviders;
    }

    public List<Laboratory> getLaboratoryProviders() {
        return laboratoryProviders;
    }

    public void addLaboratory(Laboratory lab){
        if(this.laboratoryProviders == null)
            this.laboratoryProviders = new ArrayList<>();
        laboratoryProviders.add(lab);
    }
    public void setLaboratoryProviders(List<Laboratory> laboratoryProviders) {
        this.laboratoryProviders = laboratoryProviders;
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

    public String getXifinTestId() {
        return xifinTestId;
    }

    public void setXifinTestId(String xifinTestId) {
        this.xifinTestId = xifinTestId;
    }

    public String getStripeProductId() {
        return stripeProductId;
    }

    public void setStripeProductId(String stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public String getCardConnectProductId() {
        return cardConnectProductId;
    }

    public void setCardConnectProductId(String cardConnectProductId) {
        this.cardConnectProductId = cardConnectProductId;
    }

    public String getSalesforceProductId() {
        return salesforceProductId;
    }

    public void setSalesforceProductId(String salesforceProductId) {
        this.salesforceProductId = salesforceProductId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAlliedPackageUnitId() {
        return alliedPackageUnitId;
    }

    public void setAlliedPackageUnitId(String alliedPackageUnitId) {
        this.alliedPackageUnitId = alliedPackageUnitId;
    }

    public boolean isRequiresProviderApproval() {
        return requiresProviderApproval;
    }

    public void setRequiresProviderApproval(boolean requiresProviderApproval) {
        this.requiresProviderApproval = requiresProviderApproval;
    }

    public String getLimsReportId() {
        return limsReportId;
    }

    public void setLimsReportId(String limsReportId) {
        this.limsReportId = limsReportId;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    public String getSalesforcePriceBookId() {
        return salesforcePriceBookId;
    }

    public void setSalesforcePriceBookId(String salesforcePriceBookId) {
        this.salesforcePriceBookId = salesforcePriceBookId;
    }

    public String getSalesforcePriceBookEntryId() {
        return salesforcePriceBookEntryId;
    }

    public void setSalesforcePriceBookEntryId(String salesforcePriceBookEntryId) {
        this.salesforcePriceBookEntryId = salesforcePriceBookEntryId;
    }

    public ResultsConfigurationTemplate getResultsConfigurationTemplate() {
        return resultsConfigurationTemplate;
    }

    public void setResultsConfigurationTemplate(ResultsConfigurationTemplate resultsConfigurationTemplate) {
        this.resultsConfigurationTemplate = resultsConfigurationTemplate;
    }

    public boolean isAllowsAssistedCollectionVideoCall() {
        return allowsAssistedCollectionVideoCall;
    }

    public void setAllowsAssistedCollectionVideoCall(boolean allowsAssistedCollectionVideoCall) {
        this.allowsAssistedCollectionVideoCall = allowsAssistedCollectionVideoCall;
    }

    public boolean isAllowsSelfCollectedVideoUpload() {
        return allowsSelfCollectedVideoUpload;
    }

    public void setAllowsSelfCollectedVideoUpload(boolean allowsSelfCollectedVideoUpload) {
        this.allowsSelfCollectedVideoUpload = allowsSelfCollectedVideoUpload;
    }

    public Integer getLaboratoryTurnAroundTime() {
        return laboratoryTurnAroundTime;
    }

    public void setLaboratoryTurnAroundTime(Integer laboratoryTurnAroundTime) {
        this.laboratoryTurnAroundTime = laboratoryTurnAroundTime;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public ProductAvailablity getAvailablity() {
        return availablity;
    }

    public void setAvailablity(ProductAvailablity availablity) {
        this.availablity = availablity;
    }

    public String getProductImageUrlPoster() {
        return productImageUrlPoster;
    }

    public void setProductImageUrlPoster(String productImageUrlPoster) {
        this.productImageUrlPoster = productImageUrlPoster;
    }

    public String getProductImageUrlThumbnail() {
        return productImageUrlThumbnail;
    }

    public void setProductImageUrlThumbnail(String productImageUrlThumbnail) {
        this.productImageUrlThumbnail = productImageUrlThumbnail;
    }

    public String getProductImageUrlOption2() {
        return productImageUrlOption2;
    }

    public void setProductImageUrlOption2(String productImageUrlOption2) {
        this.productImageUrlOption2 = productImageUrlOption2;
    }

    public String getProductImageUrlOption3() {
        return productImageUrlOption3;
    }

    public void setProductImageUrlOption3(String productImageUrlOption3) {
        this.productImageUrlOption3 = productImageUrlOption3;
    }
}
