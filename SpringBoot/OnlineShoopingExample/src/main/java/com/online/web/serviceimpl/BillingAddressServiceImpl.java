package com.online.web.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.dao.BillingAddressDao;
import com.online.web.model.BillingAddress;
import com.online.web.service.BillingAddressService;

@Service

public class BillingAddressServiceImpl implements BillingAddressService{

	@Autowired
	private BillingAddressDao billingAddressDao;
	
	//persisting and deleting objects requires a transaction in JPA. Thus we need to make sure a transaction is running, which we do by having the method annotated with @Transactional.
	@Transactional()
	@Override
	public void addBillingAddress(BillingAddress billingAddress) {
		
		billingAddressDao.save(billingAddress);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<BillingAddress> getBillingAddressbyId(long billingAddressId) {
		
		return billingAddressDao.findById(billingAddressId);
	}

}
