package com.junodx.api.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.providers.*;
import com.junodx.api.repositories.providers.*;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LocationService extends ServiceBase {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    public LocationService(){
        mapper = new ObjectMapper();
    }

    public ServiceBase.ServiceResponse<Location> getLocation(String practiceId, String id){
        if(id != null){
            Optional<Location> returnLocation = locationRepository.findById(id);
            if(returnLocation.isPresent()){
                Location location = returnLocation.get();
                if(location.getPractice().getId().equals(practiceId))
                    return new ServiceBase.ServiceResponse(LogCode.SUCCESS, location, true);
            }
        }

        return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);
    }

    public ServiceBase.ServiceResponse<?> getAllLocations(String practiceId){
        if(practiceId != null){
            List<Location> returnLocations = locationRepository.findLocationsByPractice_Id(practiceId);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, returnLocations, true);
        }

        return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);
    }

    /*
    public ServiceBase.ServiceResponse<?> getProviderByNPI(String npi, String[] includes){
        Optional<Provider> provider = providerRepository.findProviderByNpi(npi);

        if(provider == null)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, loadAdditionalData(provider.get(), includes), true);
    }

    public ServiceBase.ServiceResponse<?> getProviderByUPIN(String upin, String[] includes){
        Optional<Provider> provider = providerRepository.findProviderByUpin(upin);

        if(provider == null)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, loadAdditionalData(provider.get(), includes), true);
    }

    public ServiceBase.ServiceResponse<?> getProviderByEmail(String email, String[] includes){
        Optional<Provider> provider = providerRepository.findProviderByEmail(email);

        if(provider.isEmpty())
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, loadAdditionalData(provider.get(), includes), true);
    }


    public ServiceBase.ServiceResponse<List<Location>> searchProviders(String firstName, String lastName, String city, String state, String postalCode){
        List<Provider> providers = providerRepository
                .findAllByFirstNameIsLikeAndLastNameIsLikeAndContactAddress_CityAndContactAddress_StateAndContactAddress_PostalCode(firstName, lastName, city, state, postalCode)
                .stream().collect(Collectors.toList());

        if(providers == null)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, providers, true);
    }

     */

    public ServiceBase.ServiceResponse<Location> saveLocation(Location location, UserDetailsImpl user){
        try {
            Optional<Practice> practice = practiceRepository.findById(location.getPractice().getId());
            if(practice.isPresent())
                location.setPractice(practice.get());

            location = locationRepository.save(location);
        } catch (Exception e){
            e.printStackTrace();
            return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, location, false);
        }

        return new ServiceBase.ServiceResponse(LogCode.RESOURCE_CREATE, location, true);
    }

    public ServiceBase.ServiceResponse<?> saveAllLocations(List<Location> locations, UserDetailsImpl user){
        try {
            locations = locationRepository.saveAll(locations);
        } catch (Exception intE ){
            log(LogCode.RESOURCE_CREATE_ERROR, "Cannot save test results because " + intE.getMessage(), user);
            return new ServiceResponse<>(LogCode.RESOURCE_CREATE_ERROR, locations, false);
        }

        if(locations != null){
            return new ServiceBase.ServiceResponse<>(LogCode.SUCCESS, locations, true);
        }

        return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, locations, false);
    }

    public ServiceBase.ServiceResponse<Location> updateLocation(Location location, UserDetailsImpl user){
        try {
            if (location != null) {
                Location update = locationRepository.getById(location.getId());
                if (update != null) {
                    if (location.getName() != null) update.setName(location.getName());
                    if (location.getAddress() != null) update.setAddress(location.getAddress());
                    if (location.getPhone() != null) update.setPhone(location.getPhone());
                    //if (location.getFax() != null) update.setFax(location.getFax());
                    if (location.getPractice() != null) update.setPractice(location.getPractice());

                    location = locationRepository.save(update);

                    log(LogCode.RESOURCE_UPDATE, "Updated a Location " + location.getId(), user);

                    return new ServiceBase.ServiceResponse(LogCode.SUCCESS, location, true);
                }
            }
        } catch (Exception e){
            return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, null, false);
        }

        return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, null, false);
    }

    public ServiceBase.ServiceResponse<?> deleteLocation(String locationId, UserDetailsImpl user){
        Optional<Location> oLocation = locationRepository.findById(locationId);
        Location location = null;

        if(oLocation.isPresent())
            location = oLocation.get();

        if(location != null) {
            locationRepository.delete(location);
            log(LogCode.RESOURCE_DELETE, "Deleted the Location " + location.getName() ,  user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Location with Id " + locationId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }

    public ServiceBase.ServiceResponse<?> deleteAllLocations(String practiceId, UserDetailsImpl user){
        if(practiceId != null) {
            locationRepository.deleteLocationsByPractice_Id(practiceId);
            log(LogCode.RESOURCE_DELETE, "Deleted the Locations in Practice " + practiceId ,  user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Location in Practice " + practiceId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }
}


