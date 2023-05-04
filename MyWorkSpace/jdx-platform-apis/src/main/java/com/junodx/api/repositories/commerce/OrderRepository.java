package com.junodx.api.repositories.commerce;

import com.junodx.api.controllers.SortType;
import com.junodx.api.controllers.commerce.types.OrderSortType;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.types.OrderStatusType;
import com.junodx.api.models.core.types.OpenState;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findOrdersByCustomer_IdOrderByOrderedAt(String id);
    Optional<Order> findOrderByLineItems(OrderLineItem item);

    @Query("select o from Order o where o.customer.lastName = :lastName and o.customer.dateOfBirth = :dob")
    Optional<Order> findOrderByCustomer_LastNameAndCustomer_DateOfBirth(@Param("lastName") String lastName, @Param("dob") Calendar dob);

    List<Order> findOrderByCustomer_LastNameOrderByOrderedAtDesc(String lastName);

    @Query("select o from Order o where (:patientId is null or o.customer.id = :patientId)" +
            " and (:firstName is null or o.customer.firstName = :firstName)" +
            " and (:lastName is null or o.customer.lastName = :lastName)" +
            " and (:email is null or o.customer.email = :email)" +
            " and (:xifinId is null or o.customer.xifinPatientId = :xifinId)" +
            " and (:stripeId is null or o.customer.stripeCustomerId = :stripeId)" +
            " and (:requiresShipment is null or o.requiresShipment = :requiresShipment)" +
            " and (:requiresRedraw is null or o.requiresRedraw = :requiresRedraw)" +
            " and (:resultsAvailable is null or o.resultsAvailable = :resultsAvailable)" +
            " and (:isOpen is null or o.open = :isOpen)" +
          //  " and (:status is null or o.currentStatus = :status)" +
            " and (:withInsurance is null or o.withInsurance = :withInsurance)" +
            " and (:customerActivated is null or o.customer.activated = :customerActivated)" +
            " and (:orderNumber is null or o.orderNumber = :orderNumber)" +
            " and (:orderId is null or o.id = :orderId)" +
          //  " and (:containsProductId is null or r.line = :labOrderId)" +
          //  " and (:containsTests is null or r.isAvailable = :isAvailable)" +
            " and (cast(:after as timestamp) is null or o.meta.createdAt >= :after)" +
            " order by o.meta.createdAt DESC")
    Page<Order> searchOrderByCreatedAtDESC(@Param("patientId") String patientId,
                                           @Param("firstName") String firstName,
                                           @Param("lastName") String lastName,
                                           @Param("email") String email,
                                           @Param("xifinId") String xifinId,
                                           @Param("stripeId") String stripeId,
                                                 @Param("after") Calendar after,
                                                 @Param("requiresShipment") Boolean requiresShipment,
                                                 @Param("requiresRedraw") Boolean requiresRedraw,
                                                 @Param("resultsAvailable") Boolean resultsAvailable,
                                                 @Param("isOpen") Boolean isOpen,
                                                // @Param("status") OrderStatusType status,
                                                 @Param("withInsurance") Boolean withInsurance,
                                                 @Param("customerActivated") Boolean customerActivated,
                                                 @Param("orderNumber") String orderNumber,
                                                 @Param("orderId") String orderId,
                                                 //@Param("containsProductId") String containsProductId,
                                                 //@Param("containsTests") Boolean containsTests,
                                                 //@Param("sortBy") OrderSortType sortBy,
                                                 Pageable pageable);

    @Query("select o from Order o join fetch OrderLineItem l on l.order.id = o.id join fetch Fulfillment f on l.id = f.orderLineItem.id join fetch Kit k on f.kit.id = k.id where (:code is null or k.code = :code) and (:sampleNumber is null or k.sampleNumber = :sampleNumber)")
    Page<Order> searchOnKitCodeOrSampleNumber(@Param("code") String code, @Param("sampleNumber") String sampleNumber, Pageable pageable);

    @Query("select count(t) from Order t where t.requiresShipment = true and (cast(:after as timestamp) is null or t.meta.createdAt >= :after)")
    long countOfRequiresShipment(@Param("after") Calendar after);

    @Query("select count(t) from Order t where t.requiresRedraw = true and (cast(:after as timestamp) is null or t.meta.createdAt >= :after)")
    long countOfRequiresRedraw(@Param("after") Calendar after);

    //@Query("select count(t) from Order t where t. and (cast(:after as timestamp) is null or t.meta.createdAt >= :after)")
    //long countOfNewOrders(@Param("after") Calendar after);

}