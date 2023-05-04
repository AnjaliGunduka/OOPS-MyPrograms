package com.junodx.api.services.fulfillment;

import com.junodx.api.logging.LogCode;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.repositories.fulfillment.FulfillmentProviderRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.ProductService;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FulfillmentProviderService extends ServiceBase {

    @Autowired
    private FulfillmentProviderRepository fulfillmentProviderRepository;

    @Autowired
    private ProductService productService;

    public Optional<FulfillmentProvider> getProvider(String id, String[] includes) {
        return fulfillmentProviderRepository.findById(id);
    }

    public List<FulfillmentProvider> getAllProviders(String[] includes) {
        List<FulfillmentProvider> providers = fulfillmentProviderRepository.findAll();
        if (providers == null)
            providers = new ArrayList<>();

        return providers;
    }

    public Optional<FulfillmentProvider> getDefaultProvider() throws JdxServiceException {
        return fulfillmentProviderRepository.findFulfillmentProviderByDefaultProviderIsTrue();
    }

    public List<FulfillmentProvider> getAllProvidersByIds(List<String> ids){
        return fulfillmentProviderRepository.findAllByIdIn(ids);
    }

    public Optional<FulfillmentProvider> findProviderByName(String name) throws JdxServiceException {
        try {
            return fulfillmentProviderRepository.findFulfillmentProviderByName(name);
        } catch (Exception e){
            throw new JdxServiceException("Cannot find provider due to an exception: " + e.getMessage());
        }
    }

    public FulfillmentProvider saveProvider(FulfillmentProvider provider, UserDetailsImpl user) {
        provider.setMeta(buildMeta(user));

        log(LogCode.RESOURCE_CREATE, "Created a Batch " + provider.getId(), user);

       return fulfillmentProviderRepository.save(provider);

    }

    public void deleteProvider(FulfillmentProvider provider, UserDetailsImpl user){
        if(provider != null && provider.getId() != null) {
            //Find all of the products that reference this fulfillment provider and remove the reference to it.
            List<FulfillmentProvider> providers = new ArrayList<>();
            providers.add(provider);
            List<Product> attachedProducts = productService.getAllByFulfillmentProvider(providers);
            for(Product p : attachedProducts) {
                p.getFulfillmentProviders().remove(provider);
                productService.update(p);
            }

            fulfillmentProviderRepository.delete(provider);
        }
    }

    public void deleteProviderById(String id, UserDetailsImpl user){
        if(id != null) {
            Optional<FulfillmentProvider> provider = fulfillmentProviderRepository.findById(id);
            if(provider.isPresent())
                deleteProvider(provider.get(), user);
        }
    }
}
