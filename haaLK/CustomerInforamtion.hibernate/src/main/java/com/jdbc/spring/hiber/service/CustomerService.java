package com.jdbc.spring.hiber.service;
import java.util.List;

import com.jdbc.spring.hiber.model.Customer;
public interface CustomerService {
	public List < Customer > getCustomers();

    public void saveCustomer(Customer theCustomer);

    public Customer getCustomer(int theId);

    public void deleteCustomer(int theId);
}
