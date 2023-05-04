package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.FetalSexResultsPreferences;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.payment.insurance.Claim;
import com.junodx.api.services.exceptions.JdxServiceException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="order_line_item")
public class OrderLineItem {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductType type;

    @Column(name="description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Column(name="amount")
    private float amount;

    @Column(name="original_unit_price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float originalUnitPrice;

    @Column(name = "salesforce_pricebook_entry_id")
    private String priceBookEntryId;

    @Column(name="insurance_estimated_covered_amount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private float insuranceEstimatedCoveredAmount;

    @Column(name="sku")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sku;

    @Column(name="product_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productId;

    @Column(name="product_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productName;

    @Column(name="product_img_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productImageUrl;

    @Column(name="taxable")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean taxable;

    @Column(name="requires_shipping")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean requiresShipping;

    @Column(name="is_directly_provided")
    private boolean isDirectlyProvided;

    @Column(name="is_in_office_collected")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isInOfficeCollected;

    @OneToOne(mappedBy = "orderLineItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private LaboratoryOrder laboratoryOrderDetails;

    @OneToMany(mappedBy = "orderLineItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Fulfillment> fulfillments;

    private String currentFulfillmentId;

    @Column(name = "quantity", nullable = true)
    private int quantity;


    @Embedded
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public OrderLineItem(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public LaboratoryOrder getLaboratoryOrderDetails() {
        return laboratoryOrderDetails;
    }

    public void setLaboratoryOrderDetails(LaboratoryOrder laboratoryOrderDetails) {
        this.laboratoryOrderDetails = laboratoryOrderDetails;
        this.laboratoryOrderDetails.setOrderLineItem(this);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getPriceBookEntryId() {
        return priceBookEntryId;
    }

    public void setPriceBookEntryId(String priceBookEntryId) {
        this.priceBookEntryId = priceBookEntryId;
    }

    public List<Fulfillment> getFulfillments() {
        return fulfillments;
    }

    public void setFulfillments(List<Fulfillment> fulfillments) throws JdxServiceException {
        int incompleteFulfillments = 0;
        for(Fulfillment f : fulfillments){
            if(!f.isCompleted())
                incompleteFulfillments++;
        }

        if(incompleteFulfillments > 1)
            throw new JdxServiceException("Cannot add more than one incomplete fulfillment to this line item");

        this.fulfillments = fulfillments;
    }

    public void addFulfillment(Fulfillment fulfillment) throws JdxServiceException {
        if(this.fulfillments == null)
            this.fulfillments = new ArrayList<>();

        if(this.fulfillments.stream().filter(x->x.isCompleted() == false).findAny().isPresent())
            throw new JdxServiceException("Cannot add a fulfillment as one or more previous fulfillments are not completed, set those to completed first");

        this.fulfillments.add(fulfillment);
        fulfillment.setOrderLineItem(this);

        currentFulfillmentId = fulfillment.getId();
    }

    public String getCurrentFulfillmentId() {
        if(currentFulfillmentId == null)
            if(this.fulfillments != null && this.fulfillments.size() > 0)
                currentFulfillmentId = this.fulfillments.get(this.fulfillments.size()-1).getId();

        return currentFulfillmentId;
    }

    public void setCurrentFulfillmentId(String currentFulfillmentId) {
        this.currentFulfillmentId = currentFulfillmentId;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
