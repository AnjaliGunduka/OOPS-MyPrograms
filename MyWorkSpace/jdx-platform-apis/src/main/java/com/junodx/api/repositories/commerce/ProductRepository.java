package com.junodx.api.repositories.commerce;

import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findProductsByIdIn(List<String> ids);
    List<Product> findProductsByFulfillmentProvidersIn(List<FulfillmentProvider> providers);
}
