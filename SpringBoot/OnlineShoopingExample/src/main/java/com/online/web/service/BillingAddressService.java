package com.online.web.service;

import java.util.Optional;

import com.online.web.model.BillingAddress;

public interface BillingAddressService {

	public void addBillingAddress(BillingAddress billingAddress);
	
	Optional<BillingAddress> getBillingAddressbyId(long billingAddressId);
}
