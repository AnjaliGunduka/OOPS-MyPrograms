package com.junodx.api.services.commerce;

import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.repositories.commerce.OrderLineItemRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderLineItemService extends  ServiceBase {
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderService orderService;


    public Optional<OrderLineItem> getOrderLineItemById(String id){
        return orderLineItemRepository.findById(id);
    }

    public List<OrderLineItem> getAllLineItemsForOrderId(String id){
        return orderLineItemRepository.findOrderLineItemsByOrderId(id);
    }

    public Optional<OrderLineItem> getOrderLineItemByLaboratoryOrderId(String id) throws JdxServiceException {
        try {
            return orderLineItemRepository.findOrderLineItemByLaboratoryOrderDetails_Id(id);
        } catch( Exception e ){
            throw new JdxServiceException("Cannot get line item based on teh test order");
        }
    }


    public OrderLineItem save(OrderLineItem orderLineItem, UserDetailsImpl user){
        orderLineItem.setMeta(buildMeta(user));

        if(orderLineItem != null) {
            if(orderLineItem.getOrder() != null && orderLineItem.getOrder().getId() != null) {
                Optional<Order> parentOrder = orderService.findOneByOrderId(orderLineItem.getOrder().getId(), new String[]{}, user);
                if(parentOrder.isPresent())
                    orderLineItem.setOrder(parentOrder.get());
                try {
                    return orderLineItemRepository.save(orderLineItem);
                } catch (Exception intE) {
                    log(LogCode.RESOURCE_CREATE_ERROR, "Cannot create kit because " + intE.getMessage(), user);
                    throw new JdxServiceException("Cannot save Laboratory Order ");
                }
            }
        }

        return null;
    }

/*
    public OrderLineItem update(OrderLineItem lineItem, UserDetailsImpl user){
        if(lineItem != null) {
            Kit update = kitRepository.getById(kit.getId());
            if(update != null) {
                if(kit.getType() != null) update.setType(kit.getType());
                update.setMeta(updateMeta(update.getMeta(), user));
                try {
                    kit = kitRepository.save(update);
                } catch (Exception e){
                    return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, kit, false);
                }

                log(LogCode.RESOURCE_UPDATE, "Updated a Kit " + kit.getId(), user);

                return new ServiceBase.ServiceResponse(LogCode.SUCCESS, kit , true);
            }
        }

        return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, null, false);
    }


    public void deleteKit(String kitId, UserDetailsImpl user){
        Optional<Kit> oKit = kitRepository.findById(kitId);
        Kit kit = null;

        if(oKit.isPresent())
            kit = oKit.get();

        if(kit != null) {
            kitRepository.delete(kit);
            log(LogCode.RESOURCE_DELETE, "Deleted the Kit " + kit.getCode(), user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Kit with Id " + kitId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }

 */
}
