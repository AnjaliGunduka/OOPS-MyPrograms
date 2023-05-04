package com.payment.streams.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.streams.model.Payment;




@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{

}
