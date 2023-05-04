package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingTargetRepository extends JpaRepository<ShippingTarget, String> {
}
