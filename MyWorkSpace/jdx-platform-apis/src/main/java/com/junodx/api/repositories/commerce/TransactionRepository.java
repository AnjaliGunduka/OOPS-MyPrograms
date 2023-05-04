package com.junodx.api.repositories.commerce;

import com.junodx.api.models.payment.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, String> {

    public List<Transaction> findByExternalTransactionId(String id);
}
