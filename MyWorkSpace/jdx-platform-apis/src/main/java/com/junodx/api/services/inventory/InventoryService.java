package com.junodx.api.services.inventory;

import com.junodx.api.controllers.commerce.payloads.InventoryUpdatePayload;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.repositories.inventory.InventoryRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.ProductService;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService  extends ServiceBase {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public InventoryItem upsert(InventoryUpdatePayload item, UserDetailsImpl updater) throws JdxServiceException {
        if(item == null && item.getProductId() == null)
            throw new JdxServiceException("Cannot look up inventory item because the item or productId is not provided");

        Optional<InventoryItem> inventoryItem = inventoryRepository.findInventoryItemByProduct_Id(item.getProductId());

        //New inventory item
        if(inventoryItem.isEmpty()){
            InventoryItem newItem = new InventoryItem();
            Optional<Product> product = productService.get(item.getProductId());
            if(product.isEmpty())
                throw new JdxServiceException("Cannot find product associated to inventory item to create a new inventory item");

            newItem.setProduct(product.get());
            if(item.getAvailableUnits() != null)
                newItem.setAvailableUnits(item.getAvailableUnits());
            else
                newItem.setAvailableUnits(0);

            if(item.getAvailable() != null)
                newItem.setAvailable(item.getAvailable());
            else
                newItem.setAvailable(false);

            if(item.getReleased() != null)
                newItem.setReleased(item.getReleased());
            else
                newItem.setReleased(false);

            newItem.setFirstCreated(Calendar.getInstance());
            newItem.setLastUpdated(Calendar.getInstance());

            newItem.setMeta(buildMeta(updater));

            return inventoryRepository.save(newItem);
        } else { //got an update
            if(item.getAvailableUnits() != null)
                inventoryItem.get().setAvailableUnits(item.getAvailableUnits());

            if(item.getAvailable() != null)
                inventoryItem.get().setAvailable(item.getAvailable());

            if(item.getReleased() != null)
                inventoryItem.get().setReleased(item.getReleased());

            inventoryItem.get().setLastUpdated(Calendar.getInstance());

            inventoryItem.get().setMeta(updateMeta(inventoryItem.get().getMeta(), updater));

            return inventoryRepository.save(inventoryItem.get());
        }
    }

    public InventoryItem save(InventoryItem item, UserDetailsImpl updater){
        item.setMeta(updateMeta(item.getMeta(), updater));

        return inventoryRepository.save(item);
    }

    public Optional<InventoryItem> get(String productId){
        return inventoryRepository.findInventoryItemByProduct_Id(productId);
    }

    public List<InventoryItem> getAll(){ return inventoryRepository.findAll(); }

    public void delete(String inventoryId) throws JdxServiceException  {
        try {
            Optional<InventoryItem> delete = inventoryRepository.findById(inventoryId);
            if (delete.isPresent())
                inventoryRepository.delete(delete.get());
        } catch(Exception e){
            throw e;
        }
    }
}

