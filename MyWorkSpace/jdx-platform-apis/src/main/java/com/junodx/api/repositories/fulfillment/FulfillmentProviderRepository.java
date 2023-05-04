package com.junodx.api.repositories.fulfillment;

import com.junodx.api.models.fulfillment.FulfillmentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FulfillmentProviderRepository extends JpaRepository<FulfillmentProvider, String> {
    void deleteFulfillmentProviderById(String id);
    List<FulfillmentProvider> findAllByIdIn(List<String> ids);
    Optional<FulfillmentProvider> findFulfillmentProviderByDefaultProviderIsTrue();
    Optional<FulfillmentProvider> findFulfillmentProviderByName(String name);
}
