package com.online.web.service;

import com.online.web.model.ShippingAddress;

public interface ShippingAddressService {

	public void addShippingAddress(ShippingAddress shippingAddress);
	
	ShippingAddress getShippingAddressById(long shippingAddressId);
}
