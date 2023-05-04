package com.junodx.api.repositories.lab;

import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.laboratory.LaboratoryOrder;
import com.junodx.api.models.laboratory.TestRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LaboratoryOrderRepository extends JpaRepository<LaboratoryOrder, String> {
    Optional<LaboratoryOrder> findLaboratoryOrderByTestRuns(TestRun testRun);
    List<LaboratoryOrder> findLaboratoryOrdersByIdIn(List<String> ids);
    //LaboratoryOrder findLaboratoryOrderByTestRunsIn()
    Page<LaboratoryOrder> findAll(Pageable pageable);
    Optional<LaboratoryOrder> findLaboratoryOrderByOrderLineItem(OrderLineItem item);
    Optional<LaboratoryOrder> findLaboratoryOrderByOrderLineItem_Id(String id);
}
