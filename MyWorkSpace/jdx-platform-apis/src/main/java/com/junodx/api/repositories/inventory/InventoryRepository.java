package com.junodx.api.repositories.inventory;

import com.junodx.api.models.core.types.OpenState;
import com.junodx.api.models.inventory.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, String> {
    Optional<InventoryItem> findInventoryItemByProduct_Id(String id);
}
