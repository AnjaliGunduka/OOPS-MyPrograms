package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, String> {
}
