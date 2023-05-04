package com.online.web.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.dao.ShippingAddressDao;
import com.online.web.model.ShippingAddress;
import com.online.web.service.ShippingAddressService;

@Service

public class ShippingAddressServiceImpl implements ShippingAddressService{

	@Autowired
	private ShippingAddressDao shippingAddressDao;
	@Transactional()
	@Override
	public void addShippingAddress(ShippingAddress shippingAddress) {
		
		shippingAddressDao.save(shippingAddress);
		
	}
	@Transactional(readOnly = true)
	@Override
	public ShippingAddress getShippingAddressById(long shippingAddressId) {
		
		return shippingAddressDao.findOne(shippingAddressId);
	}

}
