package com.tem.cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tem.cart.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
