package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.commerce.OrderStatus;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name="shipping_details")
public class ShippingDetails {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "fulfillment_id", nullable = false)
    @JsonIgnore
    private Fulfillment fulfillment;

    @OneToMany(mappedBy = "shippingDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ShippingStatus> shippingStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_to_method", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ShippingMethod toMethod;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_return_method", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ShippingMethod returnMethod;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="shipping_to_target", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ShippingTarget toAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="shipping_return_target", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ShippingTarget returnAddress;

    @OneToOne(mappedBy = "shippingDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingTransaction shippingTransactionDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trackingCode;

    private Meta meta;

    public ShippingDetails(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShippingStatus> getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(List<ShippingStatus> shippingStatus) {
        this.shippingStatus = shippingStatus;
        for(ShippingStatus s : this.shippingStatus)
            s.setShippingDetails(this);
    }

    public void addShippingStatus(ShippingStatus status){
        if(this.shippingStatus == null)
            this.shippingStatus = new ArrayList<>();

        if(!this.shippingStatus.stream().anyMatch(x -> x.getStatus().equals(status.getStatus()) && x.isCurrent())) {
            setAllStatusesToNotCurrent();
            this.shippingStatus.add(status);
        }
    }

    @JsonProperty("currentStatus")
    public ShippingStatusType getCurrentStatus(){
        if(this.shippingStatus != null) {
            List<ShippingStatus> statusType = this.shippingStatus.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            if (this.shippingStatus.size() == 0)
                return null;

            if (statusType != null && statusType.size() == 0) {
                setLatestStatusToCurrent();
                statusType = this.shippingStatus.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
            } else if (statusType != null && statusType.size() > 1)
                resetStatusAndFindCurrent();

            return statusType.get(0).getStatus();
        }
        return null;
    }

    private void resetStatusAndFindCurrent(){
        List<ShippingStatus> statusType = this.shippingStatus.stream().filter(x -> x.isCurrent()).collect(Collectors.toList());
        if(statusType != null && statusType.size() > 1) {
            //Collections.sort(statusType);
            int index = 0;
            for(ShippingStatus s : statusType){
                if(index > 0)
                    s.setCurrent(false);
            }
        }
    }

    private void setLatestStatusToCurrent(){
        //Collections.sort(this.shippingStatus);
        if(this.shippingStatus.size() > 0)
            this.shippingStatus.get(0).setCurrent(true);
    }

    private void setAllStatusesToNotCurrent(){
        if(this.shippingStatus != null){
            for(ShippingStatus l : this.shippingStatus)
                l.setCurrent(false);
        }
    }


    public Fulfillment getFulfillment() {
        return fulfillment;
    }

    public void setFulfillment(Fulfillment fulfillment) {
        this.fulfillment = fulfillment;
    }

    public ShippingMethod getToMethod() {
        return toMethod;
    }

    public void setToMethod(ShippingMethod toMethod) {
        this.toMethod = toMethod;
        this.toMethod.setShippingDetails(this);
    }

    public ShippingMethod getReturnMethod() {
        return returnMethod;
    }

    public void setReturnMethod(ShippingMethod returnMethod) {
        this.returnMethod = returnMethod;
        this.returnMethod.setShippingDetails(this);
    }

    public ShippingTarget getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(ShippingTarget returnAddress) {
        this.returnAddress = returnAddress;
    }

    public ShippingTarget getToAddress() {
        return toAddress;
    }

    public void setToAddress(ShippingTarget toAddress) {
        this.toAddress = toAddress;
    }

    public ShippingTransaction getShippingTransactionDetails() {
        return shippingTransactionDetails;
    }

    public void setShippingTransactionDetails(ShippingTransaction shippingTransactionDetails) {
        this.shippingTransactionDetails = shippingTransactionDetails;
        this.shippingTransactionDetails.setShippingDetails(this);
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public boolean hasShippingStatusOfType(ShippingStatusType type){
        return shippingStatus.stream().filter(x->x.getStatus().equals(type)).findAny().isPresent();
    }

}
