package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingCarrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingCarrierRepository extends JpaRepository<ShippingCarrier, String> {
    Optional<ShippingCarrier> findShippingCarrierByName(String name);
}
