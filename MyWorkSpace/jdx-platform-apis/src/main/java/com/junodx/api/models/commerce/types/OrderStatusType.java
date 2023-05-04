package com.junodx.api.models.commerce.types;

public enum OrderStatusType {
    CREATED,
    PAYMENT_PENDING,
    PAID,
    KIT_ASSEMBLED,
    KIT_ASSIGNED,
    SHIPMENT_ORDER_CREATED,
    KIT_AWAITING_SHIPPING,
    KIT_SHIPPED,
    ARRIVING_SOON,
    ARRIVED,
    KIT_ACTIVATED,
    RETURN_SHIPPED,
    RECEIVED,
    IN_LABORATORY,
    LABORATORY_PROCESSING,
    RESULTS_IN_REVIEW,
    RESULTS_AVAILABLE,
    RESULTS_VIEWED,
    REDRAW_REQUESTED,
    REDRAW_APPROVED,
    REDRAW_FULFILLED,
    PROCESSING_DELAYED,
    CLOSED,
    REFUNDED,
    REFUND_PROCESSING,
    RETURNED,
    CANCELED
}
