package com.junodx.api.repositories.commerce;

import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, String> {
    Optional<OrderLineItem> findOrderLineItemByLaboratoryOrderDetails(LaboratoryOrder laboratoryOrder);
    Optional<OrderLineItem> findOrderLineItemByLaboratoryOrderDetails_Id(String id);
    List<OrderLineItem> findOrderLineItemsByOrderId(String id);
    OrderLineItem findOrderLineItemByIdAndOrder_id(String id, String orderId);
}
