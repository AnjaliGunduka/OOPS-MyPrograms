package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, String> {

  //The query may have to get with trackingCode and carrierName
  public Optional<ShippingMethod> findByTrackingCode(String trackingNumber);
}
