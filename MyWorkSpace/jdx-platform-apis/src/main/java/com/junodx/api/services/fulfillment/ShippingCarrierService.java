package com.junodx.api.services.fulfillment;

import com.junodx.api.logging.LogCode;
import com.junodx.api.models.fulfillment.ShippingCarrier;
import com.junodx.api.repositories.fulfillment.ShippingCarrierRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShippingCarrierService extends ServiceBase  {
    @Autowired
    private ShippingCarrierRepository shippingCarrierRepository;

    public Optional<ShippingCarrier> getCarrier(String id, String[] includes) {
        return shippingCarrierRepository.findById(id);
    }

    public List<ShippingCarrier> getAllCarriers(String[] includes) {
        return shippingCarrierRepository.findAll();
    }

    public Optional<ShippingCarrier> findCarrierByName(String name){ return shippingCarrierRepository.findShippingCarrierByName(name);}

    public ShippingCarrier saveCarrier(ShippingCarrier carrier, UserDetailsImpl user) {
        carrier.setMeta(buildMeta(user));

        log(LogCode.RESOURCE_CREATE, "Created a carrier " + carrier.getId(), user);

        return shippingCarrierRepository.save(carrier);

    }

    public void deleteProvider(ShippingCarrier carrier, UserDetailsImpl user){
        if(carrier != null && carrier.getId() != null)
            shippingCarrierRepository.delete(carrier);
    }

    public void deleteCarrierById(String id, UserDetailsImpl user){
        if(id != null)
            shippingCarrierRepository.deleteById(id);
    }
}
