package com.junodx.api.dto.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.commerce.types.OrderStatusType;

import java.util.Calendar;

public class OrderStatusDto {

    private OrderStatusType statusType;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar updatedAt;

    public OrderStatusDto(OrderStatusType type){
        this.statusType = type;
        this.updatedAt = Calendar.getInstance();
    }

    public OrderStatusDto() {

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
}

