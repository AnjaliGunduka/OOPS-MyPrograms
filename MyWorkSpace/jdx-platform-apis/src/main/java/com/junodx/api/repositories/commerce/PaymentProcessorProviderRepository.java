package com.junodx.api.repositories.commerce;

import com.junodx.api.models.payment.PaymentProcessorProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentProcessorProviderRepository extends JpaRepository<PaymentProcessorProvider, String> {
    @Query("Select r from PaymentProcessorProvider r where r.name = :name")
    Optional<PaymentProcessorProvider> findPaymentProcessorProvider(String name);

}
