package com.junodx.api.repositories.commerce;

import com.junodx.api.models.commerce.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutRepository  extends JpaRepository<Checkout, String> {
    Optional<Checkout> findCheckoutByToken(String token);
    Optional<Checkout> findByCustomerCustomerId(String id);
}
