package com.junodx.api.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.aws.sns.SnsMessageResponse;
import com.junodx.api.connectors.lims.elements.client.ElementsClient;
import com.junodx.api.connectors.lims.elements.entities.ElementsClinic;
import com.junodx.api.connectors.lims.elements.entities.ElementsLocation;
import com.junodx.api.connectors.lims.elements.entities.ElementsPractitioner;
import com.junodx.api.connectors.messaging.SnsMessageHandler;
import com.junodx.api.connectors.messaging.payloads.EntityPayload;
import com.junodx.api.connectors.messaging.payloads.EventType;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.providers.*;
import com.junodx.api.repositories.providers.MedicalLicenseRepository;
import com.junodx.api.repositories.providers.PracticeRepository;
import com.junodx.api.repositories.providers.ProviderRepository;
import com.junodx.api.repositories.providers.SpecialityRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProviderService extends ServiceBase {

    @Value("${jdx.connectors.aws.providerTopic}")
    private String providerTopic;

    @Value("${jdx.connectors.aws.sns.disabled}")
    private boolean snsDisabled;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private MedicalLicenseRepository medicalLicenseRepository;

    @Autowired
    private PracticeRepository practiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    public ProviderService(){
        mapper = new ObjectMapper();
    }

    public Optional<Provider> getProvider(String id, String[] includes){
        if(id != null){
            Optional<Provider> returnProvider = providerRepository.findById(id);
            if(returnProvider.isPresent()){
                Provider provider = returnProvider.get();
                provider = loadAdditionalData(provider, includes);
                return Optional.ofNullable(provider);
            }
        }

        return Optional.empty();
    }

    public List<Provider> getProvidersByPracticeId(String practiceId){
        return providerRepository.findByPractice_Id(practiceId);
    }

    public Optional<Provider> getProviderByNPI(String npi, String[] includes){
        return providerRepository.findProviderByNpi(npi);
    }

    public Optional<Provider> getProviderByUPIN(String upin, String[] includes){
        return providerRepository.findProviderByUpin(upin);
    }

    public Optional<Provider> getProviderByEmail(String email, String[] includes){
        return providerRepository.findProviderByEmail(email);
    }

    public Optional<Provider> getDefaultProvider() throws JdxServiceException {
        return providerRepository.findProviderByDefaultProviderIsTrue();
    }

    public Optional<Provider> getDefaultProviderForPractice(String practiceId) throws JdxServiceException {
        try {
            return providerRepository.findProviderByDefaultProviderIsTrueAndPractice_Id(practiceId);
        } catch (Exception e){
            throw new JdxServiceException("Cannot find default provider for practice " + practiceId + ": " + e.getMessage());
        }
    }

    public Page<Provider> getAllProviders(Pageable pageable){
        return providerRepository.findAll(pageable);
    }

    public Page<Provider> searchProviders(String firstName, String lastName, String city, String state, String postalCode, Pageable pageable){
        return providerRepository
                .findAllByFirstNameIsLikeAndLastNameIsLikeAndContactAddress_CityAndContactAddress_StateAndContactAddress_PostalCode(firstName, lastName, city, state, postalCode, pageable);
    }

    public Provider saveProvider(Provider provider, UserDetailsImpl user) throws JdxServiceException {
        provider.setMeta(buildMeta(user));
        List<Specialty> specialties = provider.getSpecialties();
        List<MedicalLicense> licenses = provider.getLicenses();

        Optional<Practice> practice = practiceRepository.findById(provider.getPractice().getId());
        if(practice.isPresent())
            provider.setPractice(practice.get());

        for (Specialty specialty : specialties)
            specialty.setProvider(provider);

        for (MedicalLicense license : licenses)
            license.setProvider(provider);

        try {
            //Send to LIMS
            ElementsPractitioner practitioner = managePracticionerInLims(provider);
            if(practitioner != null)
                provider.setLimsId(practitioner.getId());

            provider = providerRepository.save(provider);

            if (provider.getSpecialties() != null && provider.getSpecialties().size() > 0)
                provider.setSpecialties(specialityRepository.saveAll(provider.getSpecialties()));

            if (provider.getLicenses() != null && provider.getLicenses().size() > 0)
                provider.setLicenses(medicalLicenseRepository.saveAll(provider.getLicenses()));
        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot save provider " + e.getMessage());
        }

        //Send SNS Message
        sendProviderStatus(provider, EventType.CREATE);

        return provider;
    }

    /*
    public ServiceBase.ServiceResponse<?> saveAllProviders(String practiceId, List<Provider> providers, UserDetailsImpl user){
        for(Provider provider : providers)
            provider.setMeta(buildMeta(user));

        Optional<Practice> practice = practiceRepository.findById(practiceId);
        for(Provider provider : providers) {
            if (practice.isPresent())
                provider.setPractice(practice.get());
        }

        log(LogCode.RESOURCE_CREATE, "Created multiple providers ", user);

        try {
            providers = providerRepository.saveAll(providers);
        } catch (Exception intE ){
            log(LogCode.RESOURCE_CREATE_ERROR, "Cannot save test results because " + intE.getMessage(), user);
            return new ServiceResponse<>(LogCode.RESOURCE_CREATE_ERROR, providers, false);
        }

        if(providers != null){
            return new ServiceBase.ServiceResponse<>(LogCode.SUCCESS, providers, true);
        }

        return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, providers, false);
    }

     */

    public Provider updateProvider(Provider provider, UserDetailsImpl user) throws JdxServiceException {
        try {
            if (provider != null) {
                Provider update = providerRepository.getById(provider.getId());
                if (update != null) {
                    if (provider.getFirstName() != null) update.setFirstName(provider.getFirstName());
                    if (provider.getLastName() != null) update.setLastName(provider.getLastName());
                    if (provider.getNpi() != null) update.setNpi(provider.getNpi());
                    if (provider.getContactAddress() != null) update.setContactAddress(provider.getContactAddress());
                    if (provider.getContactPhone() != null) update.setContactPhone(provider.getContactPhone());
                    if (provider.getEmail() != null) update.setEmail(provider.getEmail());
                    if (provider.getUpin() != null) update.setUpin(provider.getUpin());
                    update.setPracticing(provider.isPracticing());
                    update.setMeta(updateMeta(update.getMeta(), user));

                    //Did provider move to a different practice?
                    if(provider.getPractice() != null) {
                        update.setPractice(provider.getPractice());

                    }

                    //Send to LIMS
                    ElementsPractitioner practitioner = managePracticionerInLims(provider);
                    if(practitioner != null)
                        provider.setLimsId(practitioner.getId());

                    provider = providerRepository.save(update);

                    if (provider.getLicenses() != null) {
                        update.setLicenses(provider.getLicenses());
                        for (MedicalLicense license : provider.getLicenses())
                            license.setProvider(provider);

                        medicalLicenseRepository.saveAll(provider.getLicenses());
                    }
                    if (provider.getSpecialties() != null) {
                        logger.info("Have specialities " + mapper.writeValueAsString(provider.getSpecialties()));
                        update.setSpecialties(provider.getSpecialties());
                        for (Specialty specialty : provider.getSpecialties())
                            specialty.setProvider(provider);

                        specialityRepository.saveAll(provider.getSpecialties());
                    }

                    log(LogCode.RESOURCE_UPDATE, "Updated a Practice " + provider.getId(), user);

                    //Send to SNS
                    sendProviderStatus(provider, EventType.UPDATE);

                    return provider;
                }
            }
        } catch (Exception e){
            throw new JdxServiceException("Cannot update provider " + e.getMessage());
        }

        throw new JdxServiceException("Failed to update provider");
    }

    public ServiceBase.ServiceResponse<?> deleteProvider(String providerId, UserDetailsImpl user){
        Optional<Provider> oProvider = providerRepository.findById(providerId);
        Provider provider = null;

        if(oProvider.isPresent())
            provider = oProvider.get();

        if(provider != null) {
            providerRepository.delete(provider);
            log(LogCode.RESOURCE_DELETE, "Deleted the Provider " + provider.getName() ,  user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Provider with Id " + providerId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }

    public Provider loadAdditionalData(Provider provider, String[] includes){
        if(includes != null && includes.length > 0) {
            if (Arrays.stream(includes).anyMatch(s -> s.equals("specialities") || s.equals("all"))) {
                provider.setSpecialties(specialityRepository.findAllByProvider(provider));
            }
            if (Arrays.stream(includes).anyMatch(s -> s.equals("licenses") || s.equals("all"))) {
                provider.setLicenses(medicalLicenseRepository.findAllByProvider(provider));
            }
        }

        return provider;

    }

    public void sendProviderStatus(Provider o, EventType eventType) throws JdxServiceException {
        try {
            //Send SNS a message that this order was created
            //TODO is there a non-blocking way to do this?
            if (o != null && !snsDisabled) {
                EntityPayload msg = new EntityPayload();
                msg.setEntity(o);
                msg.setEvent(eventType);
                msg.setStatus(o.getStatus().name());
                msg.setEventTs(Calendar.getInstance());
                SnsMessageResponse response = SnsMessageHandler.sendSnsMessage(providerTopic, o);
                logger.info("Sending message to SNS : " +mapper.writeValueAsString(msg));
                if(response.getResponse().sequenceNumber() == null)
                    throw new JdxServiceException("Failed to send SNS message");

            } else
                logger.info("Updated to SNS disabled for event of type: " + eventType );

        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot send SNS message for provider change: " + e.getMessage());
        }
    }

    protected ElementsPractitioner managePracticionerInLims(Provider provider) {
        try {
            ElementsClient elements = new ElementsClient();
            ElementsPractitioner practitioner = new ElementsPractitioner();
            if(provider.getLimsId() != null)
                practitioner.setId(provider.getLimsId());

            practitioner.setExternalId(provider.getId());
            practitioner.setFirstName(provider.getFirstName());
            practitioner.setLastName(provider.getLastName());
            practitioner.setNpi(provider.getNpi());
            practitioner.setEmail(provider.getEmail());
            practitioner.setPhone(provider.getContactPhone().getPhoneNumber());
            practitioner.setAddress1(provider.getContactAddress().getStreet());
            practitioner.setCity(provider.getContactAddress().getCity());
            practitioner.setState(provider.getContactAddress().getState());
            practitioner.setZip(provider.getContactAddress().getPostalCode());
            practitioner.setCountry(provider.getContactAddress().getCountry());

            return elements.createPractitioners(practitioner);
        }catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create clinic in lims " + e.getMessage());
        }
    }
}

