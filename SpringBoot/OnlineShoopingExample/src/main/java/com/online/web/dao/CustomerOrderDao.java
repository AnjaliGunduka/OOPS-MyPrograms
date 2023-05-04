package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.Cart;
import com.online.web.model.CustomerOrder;

@Repository

public interface CustomerOrderDao extends CrudRepository<CustomerOrder, Long>{

	CustomerOrder getCustomerOrderBycart(Cart cart);

	CustomerOrder findOne(long customerOrderId);
}
