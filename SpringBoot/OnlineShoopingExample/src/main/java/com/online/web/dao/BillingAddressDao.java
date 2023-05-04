package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.BillingAddress;

@Repository

public interface BillingAddressDao extends CrudRepository<BillingAddress, Long>{

	 
}
