package com.springboot.repository;

import java.util.Map;

import com.springboot.model.Pincode;

public interface PincodeRepository {

	void save(Pincode pincode);

	Pincode find(Long id);

	Map<Long, Pincode> findAllPincodes();

	void delete(Long id);
}