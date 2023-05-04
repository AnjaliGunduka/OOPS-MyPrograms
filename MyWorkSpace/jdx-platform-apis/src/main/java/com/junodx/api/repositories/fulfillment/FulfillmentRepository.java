package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.Fulfillment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FulfillmentRepository extends JpaRepository<Fulfillment, String> {

  public Optional<Fulfillment> findByFulfillmentOrderId(String labelId);
}
