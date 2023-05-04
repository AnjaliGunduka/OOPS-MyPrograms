package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.laboratory.LaboratoryStatus;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class OrderStatus  implements Comparable<OrderStatus>{

    @Enumerated(EnumType.STRING)
    @Column(name="orderstatus_type")
    private OrderStatusType statusType;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="orderstatus_updatedat")
    private Calendar updatedAt;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Column(name = "current")
    private boolean current;

    public OrderStatus(OrderStatusType type, boolean current, Order order){
        this.statusType = type;
        this.updatedAt = Calendar.getInstance();
        this.order = order;
        this.current = true;
    }

    public OrderStatus() {

    }

    public OrderStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(OrderStatusType statusType) {
        this.statusType = statusType;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public int compareTo(OrderStatus o) {
        return this.updatedAt.compareTo(o.getUpdatedAt());
    }
}
