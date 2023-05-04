package com.junodx.api.services.lab;

import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.LaboratoryStatus;
import com.junodx.api.models.laboratory.TestRun;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;
import com.junodx.api.models.laboratory.types.TestRunType;
import com.junodx.api.repositories.lab.LaboratoryOrderRepository;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.OrderLineItemService;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LaboratoryOrderService extends ServiceBase {

    @Autowired
    private LaboratoryOrderRepository laboratoryOrderRepository;

    @Autowired
    private OrderLineItemService orderLineItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestRunService testRunService;

    public Optional<LaboratoryOrder> getLaboratoryOrder(String id) throws JdxServiceException {
        try {
            Optional<LaboratoryOrder> labOrder = laboratoryOrderRepository.findById(id);
            if (syncOrderWithLab(labOrder)) return labOrder;
        } catch(Exception e){
            throw new JdxServiceException("Cannot get Laboratory Order");
        }

        return Optional.empty();
    }

    public Optional<LaboratoryOrder> findLaboratoryOrderByTestRun(TestRun testRun) throws JdxServiceException {
        try {
            Optional<LaboratoryOrder> labOrder = laboratoryOrderRepository.findLaboratoryOrderByTestRuns(testRun);
            if (syncOrderWithLab(labOrder)) 
            	return labOrder;
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot get Laboratory Order " );

        }

        return Optional.empty();
    }

    public Optional<LaboratoryOrder> findLaboratoryOrderByTestRunId(String id) throws JdxServiceException {
        try {
            Optional<TestRun> run = testRunService.getTestRun(id);
            if (run.isPresent()) {
                Optional<LaboratoryOrder> labOrder = laboratoryOrderRepository.findLaboratoryOrderByTestRuns(run.get());
                if (syncOrderWithLab(labOrder)) return labOrder;
            }
        } catch(Exception e){
            throw new JdxServiceException("Cannot get Laboratory Order");
        }

        return Optional.empty();
    }

    public Optional<LaboratoryOrder> findLaboratoryOrderByOrderLineItemId(String id) throws JdxServiceException {
        try {
            Optional<TestRun> run = testRunService.getTestRun(id);
            if (run.isPresent()) {
                Optional<LaboratoryOrder> labOrder = laboratoryOrderRepository.findLaboratoryOrderByOrderLineItem_Id(id);
                if (syncOrderWithLab(labOrder)) return labOrder;
            }
        } catch(Exception e){
            throw new JdxServiceException("Cannot get Laboratory Order");
        }

        return Optional.empty();
    }

    private boolean syncOrderWithLab(Optional<LaboratoryOrder> labOrder) {
        if (labOrder.isPresent()) {
            Optional<OrderLineItem> lineItem = orderLineItemService.getOrderLineItemByLaboratoryOrderId(labOrder.get().getId());
            if (lineItem.isPresent()) {
                Optional<Order> order = orderService.findByLineItem(lineItem.get());
                if (order.isPresent()) {
                    labOrder.get().setParentOrder(order.get());
                    return true;
                }
            }
        }
        return false;
    }

    public Page<LaboratoryOrder> getAllLabOrders(Pageable pageable) throws JdxServiceException {
        try {
            return laboratoryOrderRepository.findAll(pageable);
        }catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot get all lab orders");
        }

    }

    public List<LaboratoryOrder> findLabOrdersByIds(List<String> ids){
        return laboratoryOrderRepository.findLaboratoryOrdersByIdIn(ids);
    }

    public LaboratoryOrder saveLaboratoryOrder(LaboratoryOrder laboratoryOrder, UserDetailsImpl user){
        laboratoryOrder.setMeta(buildMeta(user));

        if(laboratoryOrder != null) {
            if(laboratoryOrder.getOrderLineItem() != null && laboratoryOrder.getOrderLineItem().getId() != null) {
                Optional<OrderLineItem> orderLineItem = orderLineItemService.getOrderLineItemById(laboratoryOrder.getOrderLineItem().getId());
                if(orderLineItem.isPresent())
                    laboratoryOrder.setOrderLineItem(orderLineItem.get());
                try {
                    return laboratoryOrderRepository.save(laboratoryOrder);
                } catch (Exception intE) {
                    log(LogCode.RESOURCE_CREATE_ERROR, "Cannot create lab order because " + intE.getMessage(), user);
                    throw new JdxServiceException("Cannot save Laboratory Order ");
                }
            }
        }

        return null;
    }



    public LaboratoryOrder update(LaboratoryOrder order, UserDetailsImpl user) throws JdxServiceException {
        if(order != null) {
            LaboratoryOrder update = laboratoryOrderRepository.getById(order.getId());
            if(update != null) {
                if(order.getProviderApproval() != null) update.setProviderApproval(order.getProviderApproval());
                if(order.getRequisitionFormUrl() != null) update.setRequisitionFormUrl(order.getRequisitionFormUrl());
                if(order.getPatientConsent() != null) update.setPatientConsent(order.getPatientConsent());
                if(order.getLab() != null) update.setLab(order.getLab());
                if(order.getTestRuns() != null) {
                    //if the number of test runs are the same, assume that they are identical and not update
                    if(order.getTestRuns().size() != update.getTestRuns().size()){

                    }

                }

                update.setMeta(updateMeta(update.getMeta(), user));
                try {
                    return laboratoryOrderRepository.save(update);
                } catch (Exception e){
                    throw new JdxServiceException("Failed to update Laboratory Order");
                }
            }
        }

        throw new JdxServiceException("Failed to update Laboratory Order");
    }

    public LaboratoryOrder updateTestRunWithKit(LaboratoryOrder order, Kit kit, TestRunType type, boolean retest, boolean redraw) throws JdxServiceException {
        if(order == null)
            throw new JdxServiceException("Cannot update test run for kit since order is missing");

        if(kit == null)
            throw new JdxServiceException("Cannot update test run with kit since the kit is invalid or not present");

        //If this laborder already has a testrun with the kit assigned, exit
        if(kit != null && (order.getTestRuns().stream().filter(x->x.getKit().getId().equals(kit.getId())).findAny().isPresent()))
            throw new JdxServiceException("Cannot add a kit to this Laboratory order since it is already assigned to a test run");

        TestRun newRun = new TestRun();
        newRun.setKit(kit);
        newRun.addStatus(new LaboratoryStatus(LaboratoryStatusType.KIT_ASSIGNED, newRun, true));
        newRun.setLaboratoryOrder(order);
        newRun.setType(type);
        if(retest && redraw)
            throw new JdxServiceException("Can't have both a retest and redraw type on the test run at the same time");

        newRun.setRedraw(redraw);
        newRun.setRetest(retest);

        order.addTestRun(newRun);

        return order;
    }

    /*
    public ServiceBase.ServiceResponse<?> deleteKit(String kitId, UserDetailsImpl user){
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

    public LaboratoryOrder removeTestRun(String laboratoryOrderId, String testRunId) throws JdxServiceException {
        try {
            Optional<LaboratoryOrder> laboratoryOrder = laboratoryOrderRepository.findById(laboratoryOrderId);
            if(laboratoryOrder.isEmpty())
                throw new JdxServiceException("Cannot find laboratory order to remove test run from");

            Optional<TestRun> testRun = laboratoryOrder.get().getTestRuns().stream().filter(x->x.getId().equals(testRunId)).findAny();
            if(testRun.isPresent()){
                laboratoryOrder.get().getTestRuns().remove(testRun.get());

                LaboratoryOrder order = laboratoryOrderRepository.save(laboratoryOrder.get());

                testRunService.delete(testRun.get());

                return order;
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Encountered error trying to remove test run from Laboratory Order: " + e.getMessage());
        }

        throw new JdxServiceException("Encountered error trying to remove test run from Laboratory Order");
    }
}
