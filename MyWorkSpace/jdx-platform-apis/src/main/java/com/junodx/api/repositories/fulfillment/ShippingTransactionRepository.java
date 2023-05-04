package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingTransactionRepository extends JpaRepository<ShippingTransaction, String> {
}
