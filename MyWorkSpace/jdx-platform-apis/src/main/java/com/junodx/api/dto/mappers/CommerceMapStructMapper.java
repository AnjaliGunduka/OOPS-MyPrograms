package com.junodx.api.dto.mappers;

import com.junodx.api.dto.models.commerce.*;
import com.junodx.api.dto.models.fulfillment.FulfillmentBatchDto;
import com.junodx.api.dto.models.fulfillment.ShippingDetailsBatchDto;
import com.junodx.api.dto.models.fulfillment.ShippingMethodBatchDto;
import com.junodx.api.models.commerce.*;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.fulfillment.ShippingDetails;
import com.junodx.api.models.fulfillment.ShippingMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CommerceMapStructMapper {
    LaboratoryMapStructMapper INSTANCE = Mappers.getMapper(LaboratoryMapStructMapper.class);
    UserMapStructMapper USER_INSTANCE = Mappers.getMapper(UserMapStructMapper.class);

    //Commerce
    OrderDto orderToOrderDto(Order order);
    OrderLineItemDto orderLineItemToOrderLineItemDto(OrderLineItem orderLineItem);
    OrderStatusDto orderStatusToOrderStatusDto(OrderStatus orderStatus);
    ProviderOrderApprovalDto providerApprovalToProviderOrderApprovalDto(ProviderApproval providerApproval);
    List<OrderLineItemDto> orderLineItemsToOrderLineItemDtos(List<OrderLineItem> orders);
    OrderBatchDto orderToOrderBatchDto(Order order);
    List<OrderBatchDto> orderToOrderBatchDtos(List<Order> orders);
    ProductTruncatedDto productToProductTruncatedDto(Product product);
    List<ProductTruncatedDto> productToProductTruncatedDtos(List<Product> product);
    ProductAvailabilityDto productAvailabilityToProductAvailabilityDto(ProductAvailablity availablity);
    List<ProductAvailabilityDto> productAvailabilityToProductAvailabilityDtos(List<ProductAvailablity> availablity);
    ProductAvailablity productAvailabilityDtoToProductAvailability(ProductAvailabilityDto availabilityDto);
    List<ProductAvailablity> productAvailabilityDtoToProductAvailabilitys(List<ProductAvailabilityDto> availabilityDto);

    OrderLineItemBatchDto orderLineItemToOrderLineItemBatchDto(OrderLineItem item);
    List<OrderLineItemBatchDto> orderLineItemToOrderLineItemBatchDtos(List<OrderLineItem> items);

    FulfillmentBatchDto fulfillmentToFulfillmentBatchDto(Fulfillment fulfillment);
    ShippingDetailsBatchDto shippingDetailsToShippingDetailsBatchDto(ShippingDetails shippingDetails);
    ShippingMethodBatchDto shippingMethodToShippingMethodBatchDto(ShippingMethod shippingMethod);

}
