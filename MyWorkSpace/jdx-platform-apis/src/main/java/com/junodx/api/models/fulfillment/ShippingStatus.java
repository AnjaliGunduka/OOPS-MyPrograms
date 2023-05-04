package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.fulfillment.types.ShippingStatusType;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "shipping_status")
public class ShippingStatus {
    @Id
    private String id;

    @Column(name = "current", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean current;

    private boolean toCustomer;

    @Enumerated(EnumType.STRING)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingStatusType status;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar statusTimestamp;

    @ManyToOne
    @JoinColumn(name = "shipping_details_id", nullable = false)
    @JsonIgnore
    private ShippingDetails shippingDetails;

    public ShippingStatus(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShippingStatusType getStatus() {
        return status;
    }

    public void setStatus(ShippingStatusType status) {
        this.status = status;
    }

    public Calendar getStatusTimestamp() {
        return statusTimestamp;
    }

    public void setStatusTimestamp(Calendar statusTimestamp) {
        this.statusTimestamp = statusTimestamp;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isToCustomer() {
        return toCustomer;
    }

    public void setToCustomer(boolean toCustomer) {
        this.toCustomer = toCustomer;
    }
}
