package com.book.order.service.demo.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.book.order.service.demo.entity.Order;


@Repository
public interface OrderRepository extends CrudRepository<Order, String> {

	void deleteById(String orderDetails);

   
//    void deleteByOrderId(String orderId);
}
