package com.book.order.service.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.book.order.service.demo.entity.Order;


@Repository
public interface OrderRepository extends CrudRepository<Order, String> {

	void deleteById(String orderDetails);

   
//    void deleteByOrderId(String orderId);
}
