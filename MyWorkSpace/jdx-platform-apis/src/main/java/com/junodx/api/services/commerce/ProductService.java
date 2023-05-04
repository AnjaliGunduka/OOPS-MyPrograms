package com.junodx.api.services.commerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.commerce.payloads.ProductAvailabilityResponsePayload;
import com.junodx.api.dto.mappers.CommerceMapStructMapper;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.dto.models.commerce.ProductAvailabilityDto;
import com.junodx.api.models.auth.OAuthClient;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.ProductAvailablity;
import com.junodx.api.models.core.State;
import com.junodx.api.models.core.ZipCode;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.inventory.InventoryItem;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.repositories.OAuthClientRepository;
import com.junodx.api.repositories.commerce.ProductAvailabilityRepository;
import com.junodx.api.repositories.commerce.ProductRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.OAuthService;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.inventory.InventoryService;
import com.junodx.api.services.lab.LaboratoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService  extends ServiceBase {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAvailabilityRepository productAvailabilityRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private FulfillmentProviderService fulfillmentProviderService;

    @Autowired
    private LaboratoryService laboratoryService;

    private ObjectMapper mapper;

    private CommerceMapStructMapper commerceMapStructMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    //public ProductService() {
    //    this.mapper = new ObjectMapper();
   // }

    public ProductService(CommerceMapStructMapper commerceMapStructMapper){
        this.commerceMapStructMapper = commerceMapStructMapper;
        mapper = new ObjectMapper();
    }

    public Product create(Product product) throws JdxServiceException {
        try {
            if(product.getId() != null) {
                if(productRepository.findById(product.getId()).isPresent())
                    return this.update(product);
            }
            List<FulfillmentProvider> providers = fulfillmentProviderService.getAllProvidersByIds(product.getFulfillmentProviders().stream().map(x -> x.getId()).collect(Collectors.toList()));
            if (providers != null && providers.size() > 0)
                product.setFulfillmentProviders(providers);

            List<Laboratory> laboratories = laboratoryService.getAllLaboratoriesByIds(product.getLaboratoryProviders().stream().map(x -> x.getId()).collect(Collectors.toList()));
            if (laboratories != null && laboratories.size() > 0)
                product.setLaboratoryProviders(laboratories);

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot save or update product");
        }
    }

    public Optional<Product> get(String id){
        return productRepository.findById(id);
    }

    public List<Product> getAll(){ return productRepository.findAll(); }

    public List<Product> getAllByIds(List<String> ids){
        return productRepository.findProductsByIdIn(ids);
    }

    public List<Product> getAllByFulfillmentProvider(List<FulfillmentProvider> providers){
        return productRepository.findProductsByFulfillmentProvidersIn(providers);
    }

    public ProductAvailabilityResponsePayload getAvailability(String productId, Optional<String> zipCode) throws JdxServiceException {
        if(productId == null)
            throw new JdxServiceException("Product ID not provided when trying to obtain availability");

        Optional<Product> product = productRepository.findById(productId);

        ProductAvailabilityResponsePayload response = new ProductAvailabilityResponsePayload();
        if(product.isPresent()) {
            response.setProductId(product.get().getId());
            response.setInventoryLeft(getRemainingStock(productId, product));
            if (zipCode.isPresent()) {
                ZipCode zip = new ZipCode();
                if (ZipCode.isValidZipCode(zipCode.get())) {
                    zip.setZip(zipCode.get());
                    response.setAvailable(isAvailableInRegion(productId, zip));
                }
            }
            response.setAsOf(Calendar.getInstance());
        }

        return response;
    }

    public long getRemainingStock(String productId, Optional<Product> product) throws JdxServiceException {
        if(productId == null)
            throw new JdxServiceException("Cannot find remaining stock on product as the productId is not provided");
        if(product.isEmpty())
            product = productRepository.findById(productId);

        if(product.isEmpty())
            throw new JdxServiceException("Cannot find product information for product Id: "+ productId);

        if(product.get().getInventoryItem() != null && product.get().getInventoryItem().getId() != null) {
            Optional<InventoryItem> productInventory = inventoryService.get(product.get().getInventoryItem().getId());
            if (productInventory.isEmpty())
                throw new JdxServiceException("Cannot find inventory information for product Id: " + productId);

            return productInventory.get().getAvailableUnits();
        }

        return 0;
    }

    public boolean isAvailableInRegion(String productId, ZipCode zip) throws JdxServiceException {
        if(productId == null)
            throw new JdxServiceException("Cannot find availability on product as the productId is not provided");

        Optional<ProductAvailablity> availablity = productAvailabilityRepository.findProductAvailablityByProduct_Id(productId);
        if(availablity.isEmpty())
            throw new JdxServiceException("Cannot find product availability information for product Id: "+ productId);

        if(availablity.get().getAllowedZipCodes().stream().filter(x->x.getZip().equals(zip)).findAny().isPresent())
            return true;

        return false;
    }

    public ProductAvailablity updateProductAvailability(ProductAvailabilityDto availablity, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (availablity == null)
                throw new JdxServiceException("Cannot create or update product availability since the upsert payload is missing");
            ProductAvailablity updatedAvailability = null;
            boolean newEntity = false;

            if (availablity.getId() != null) {
                Optional<ProductAvailablity> foundAvailability = productAvailabilityRepository.findById(availablity.getId());
                if (foundAvailability.isPresent())
                    updatedAvailability = foundAvailability.get();
                else {
                    updatedAvailability = commerceMapStructMapper.productAvailabilityDtoToProductAvailability(availablity);
                    newEntity = true;
                }
            } else if (availablity.getProduct() != null && availablity.getProduct().getId() != null) {
                Optional<ProductAvailablity> foundAvailability = productAvailabilityRepository.findProductAvailablityByProduct_Id(availablity.getProduct().getId());
                if (foundAvailability.isPresent())
                    updatedAvailability = foundAvailability.get();
                else {
                    if(commerceMapStructMapper == null)
                        logger.info("Commerce mapper is null");
                    updatedAvailability = commerceMapStructMapper.productAvailabilityDtoToProductAvailability(availablity);
                    newEntity = true;
                }
            }

            if (!newEntity && updatedAvailability != null) {
                if (availablity.getAllowedDMAs() != null && availablity.getAllowedDMAs().size() > 0)
                    updatedAvailability.setAllowedDMAs(availablity.getAllowedDMAs());
                if (availablity.getAllowedStates() != null && availablity.getAllowedStates().size() > 0)
                    updatedAvailability.setAllowedStates(availablity.getAllowedStates());
                if (availablity.getAllowedZipCodes() != null && availablity.getAllowedZipCodes().size() > 0)
                    updatedAvailability.setAllowedZipCodes(availablity.getAllowedZipCodes());

                //Product can't change, must be a new ProductAvailability entity for a different product
            }

            if(updatedAvailability != null) {
                updatedAvailability.setMeta(updateMeta(updatedAvailability.getMeta(), updater));

                return productAvailabilityRepository.save(updatedAvailability);
            } else
                throw new JdxServiceException("Cannot create new product availability");

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create or update product availability: " + e.getMessage());
        }
    }

    public ProductAvailablity getProductAvailability(String productId) throws JdxServiceException {
        try {
            if (productId == null)
                throw new JdxServiceException("Cannot find product availability as the productId is missing");

            Optional<ProductAvailablity> availablity = productAvailabilityRepository.findProductAvailablityByProduct_Id(productId);
            if (availablity.isPresent())
                return availablity.get();
            else
                throw new JdxServiceException("Cannot find product availability for product id: " + productId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create or update product availability: " + e.getMessage());
        }
    }

    public ProductAvailablity getProductAvailabilityWithRegion(String productId, State state, ZipCode zipCode) throws JdxServiceException {
        try {
            if (productId == null)
                throw new JdxServiceException("Cannot find product availability as the productId is missing");

            Optional<ProductAvailablity> availablity = productAvailabilityRepository.findProductAvailablityByProduct_Id(productId);
            boolean inRegion = false;

            if (availablity.isPresent()) {

                if(zipCode != null) {
                    availablity.get().getAllowedZipCodes();
                    if(availablity.get().isAvailableInZip(zipCode))
                        inRegion = true;
                }

                if(state != null)
                    if(availablity.get().isAvailableInState(state))
                        inRegion = true;

                //If out of region, then immediately respond and let customer know
                if(inRegion)
                    availablity.get().setOutOfRegion(false);
                else {
                    availablity.get().setOutOfRegion(true);
                    return availablity.get();
                }

                Optional<InventoryItem> inventoryItem = inventoryService.get(productId);
                if(inventoryItem.isEmpty())
                    throw new JdxServiceException("Cannot look up inventory since none is found for this product");

                if(inventoryItem.get().getAvailableUnits() == 0)
                    availablity.get().setSoldOut(true);
                else
                    availablity.get().setSoldOut(false);

                return availablity.get();
            }
            else
                throw new JdxServiceException("Cannot find product availability for product id: " + productId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create or update product availability: " + e.getMessage());
        }
    }

    public Product update(Product product){
        Optional<Product> update = productRepository.findById(product.getId());
        if(update.isPresent()){
            if(product.getStripeProductId() != null) update.get().setStripeProductId(product.getStripeProductId());
            if(product.getSalesforceProductId() != null) update.get().setSalesforceProductId(product.getSalesforceProductId());
            if(product.getSalesforcePriceBookId() != null) update.get().setSalesforcePriceBookId(product.getSalesforcePriceBookId());
            if(product.getSalesforcePriceBookEntryId() != null) update.get().setSalesforcePriceBookEntryId(product.getSalesforcePriceBookEntryId());
            if(product.getCardConnectProductId() != null) update.get().setCardConnectProductId(product.getCardConnectProductId());
            if(product.getPrice() > 0.0f) update.get().setPrice(product.getPrice());
            if(product.getCurrency() != null) update.get().setCurrency(product.getCurrency());
            if(product.getSku() != null) update.get().setSku(product.getSku());
            if(product.getXifinTestId() != null) update.get().setXifinTestId(product.getXifinTestId());
            if(product.getAlliedPackageUnitId() != null) update.get().setAlliedPackageUnitId(product.getAlliedPackageUnitId());

            if(product.getFulfillmentProviders() != null) {
                List<FulfillmentProvider> providers = fulfillmentProviderService.getAllProvidersByIds(product.getFulfillmentProviders().stream().map(x -> x.getId()).collect(Collectors.toList()));
                if (providers != null && providers.size() > 0)
                    update.get().setFulfillmentProviders(providers);
            }
            if(product.getLaboratoryProviders() != null) {
                List<Laboratory> laboratories = laboratoryService.getAllLaboratoriesByIds(product.getLaboratoryProviders().stream().map(x -> x.getId()).collect(Collectors.toList()));
                if (laboratories != null && laboratories.size() > 0)
                    update.get().setLaboratoryProviders(laboratories);
            }

            if(product.getDimensions() != null) update.get().setDimensions(product.getDimensions());
            if(product.getName() != null) update.get().setName(product.getName());
            if(product.getShortDescription() != null) update.get().setShortDescription(product.getShortDescription());
            if(product.getLongDescription() != null) update.get().setLongDescription(product.getLongDescription());
            if(product.getType() != null) update.get().setType(product.getType());

            return productRepository.save(update.get());
        }

        return null;
    }

    public void delete(String productId) throws JdxServiceException  {
        try {
            Optional<Product> delete = productRepository.findById(productId);
            if (delete.isPresent())
                productRepository.delete(delete.get());
        } catch(Exception e){
            throw e;
        }
    }
}
