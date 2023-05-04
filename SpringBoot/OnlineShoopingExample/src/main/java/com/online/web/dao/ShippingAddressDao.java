package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.ShippingAddress;

@Repository

public interface ShippingAddressDao extends CrudRepository<ShippingAddress, Long>{

	ShippingAddress findOne(long shippingAddressId);

}
