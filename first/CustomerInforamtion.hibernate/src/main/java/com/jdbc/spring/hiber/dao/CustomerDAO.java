package com.jdbc.spring.hiber.dao;

import java.util.List;

import com.jdbc.spring.hiber.model.Customer;

public interface CustomerDAO {
	public List < Customer > getCustomers();

    public void saveCustomer(Customer theCustomer);

    public Customer getCustomer(int theId);

    public void deleteCustomer(int theId);
}
