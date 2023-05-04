package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, String> {
}
