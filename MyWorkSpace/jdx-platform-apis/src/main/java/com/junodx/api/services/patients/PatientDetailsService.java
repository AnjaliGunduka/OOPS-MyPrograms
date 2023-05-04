package com.junodx.api.services.patients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.patient.*;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.repositories.auth.PatientChartEntryRepository;
import com.junodx.api.repositories.patients.PatientDetailsRepository;
import com.junodx.api.repositories.providers.ProviderRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserService;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.providers.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientDetailsService extends ServiceBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientChartEntryRepository patientChartEntryRepository;

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    @Autowired
    private MedicalDetailsService medicalDetailsService;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private MedicationsService medicationsService;



    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    public PatientDetailsService(){
        mapper = new ObjectMapper();
    }

    public Optional<PatientDetails> get(String id, String[] includes){
       return patientDetailsRepository.findById(id);
    }

    public Optional<PatientDetails> getByUserId(String userId, String[] includes){
        return patientDetailsRepository.findPatientDetailsByUser_Id(userId);
    }
    /*
    public ServiceBase.ServiceResponse<?> getPatientsByPracticeId(String practiceId){
        List<PatientDetails> providers = patientDetailsRepository.findByPractice_Id(practiceId);

        if(providers == null || providers.size() == 0)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, providers, true);
    }


    public ServiceBase.ServiceResponse<?> getProviderByNPI(String npi, String[] includes){
        Optional<Provider> provider = patientDetailsRepository.findProviderByNpi(npi);

        if(provider == null)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, loadAdditionalData(provider.get(), includes), true);
    }

    public ServiceBase.ServiceResponse<?> getProviderByUPIN(String upin, String[] includes){
        Optional<Provider> provider = patientDetailsRepository.findProviderByUpin(upin);

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


    public ServiceBase.ServiceResponse<List<Provider>> searchProviders(String firstName, String lastName, String city, String state, String postalCode){
        List<Provider> providers = patientDetailsRepository
                .findAllByFirstNameIsLikeAndLastNameIsLikeAndContactAddress_CityAndContactAddress_StateAndContactAddress_PostalCode(firstName, lastName, city, state, postalCode)
                .stream().collect(Collectors.toList());

        if(providers == null)
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_GET_ERROR, "", false);

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, providers, true);
    }

     */

    public Optional<PatientDetails> save(PatientDetails patientDetails, UserDetailsImpl updater) throws JdxServiceException {
        //patientDetails.setMeta(buildMeta(updater));
        MedicalDetails medicalDetails = patientDetails.getMedicalDetails();
        List<Medication> medications = patientDetails.getMedications();

        Optional<User> userO = userRepository.findById(patientDetails.getUser().getId());
        User user = null;
        if(userO.isPresent())
            user = userO.get();
        else
            return Optional.empty();

        if(patientDetails.getProviders() != null && patientDetails.getProviders().size() > 0) {
            List<String> provIds = patientDetails.getProviders().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Provider> providers = providerRepository.findProvidersByIdIn(provIds);

            patientDetails.setProviders(providers);
        }

        if(user != null)
            patientDetails.setUser(user);
        else
            return Optional.empty();

        try {
            patientDetails = patientDetailsRepository.saveAndFlush(patientDetails);

            patientDetails.setMedicalDetails(medicalDetailsService.save(patientDetails, medicalDetails));

           patientDetails.setMedications(medicationsService.saveAll(patientDetails, medications));

            user.setPatientDetails(patientDetails);

            return Optional.of(patientDetails);

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException(e.getMessage());
        }
    }

    public Optional<PatientDetails> save(User user, PatientDetails patientDetails, UserDetailsImpl updater) throws JdxServiceException {
        //patientDetails.setMeta(buildMeta(updater));
        MedicalDetails medicalDetails = patientDetails.getMedicalDetails();
        List<Medication> medications = patientDetails.getMedications();

        if(patientDetails.getProviders() != null && patientDetails.getProviders().size() > 0) {
            List<String> provIds = patientDetails.getProviders().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Provider> providers = providerRepository.findProvidersByIdIn(provIds);

            patientDetails.setProviders(providers);
        }

        if(user != null)
            patientDetails.setUser(user);
        else
            return Optional.empty();

        try {
            patientDetails = patientDetailsRepository.save(patientDetails);

            patientDetails.setMedicalDetails(medicalDetailsService.save(patientDetails, medicalDetails));

            patientDetails.setMedications(medicationsService.saveAll(patientDetails, medications));

            user.setPatientDetails(patientDetails);

            return Optional.of(patientDetails);

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException(e.getMessage());
        }
    }

    /*
    public ServiceBase.ServiceResponse<?> saveAllProviders(String practiceId, List<Provider> providers, UserDetailsImpl user){
        for(Provider provider : providers)
            provider.setMeta(buildMeta(user));

        Optional<Practice> practice = patientDetailsRepository.findById(practiceId);
        for(Provider provider : providers) {
            if (practice.isPresent())
                provider.setPractice(practice.get());
        }

        log(LogCode.RESOURCE_CREATE, "Created multiple providers ", user);

        try {
            providers = patientDetailsRepository.saveAll(providers);
        } catch (Exception intE ){
            log(LogCode.RESOURCE_CREATE_ERROR, "Cannot save test results because " + intE.getMessage(), user);
            return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_CREATE_ERROR, providers, false);
        }

        if(providers != null){
            return new ServiceBase.ServiceResponse<>(LogCode.SUCCESS, providers, true);
        }

        return new ServiceBase.ServiceResponse<>(LogCode.RESOURCE_UPDATE_ERROR, providers, false);
    }
    */

    public Optional<PatientDetails> update(PatientDetails patientDetails, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (patientDetails != null) {
                Optional<User> userO = userRepository.findById(patientDetails.getUser().getId());
                User user = null;
                if(userO.isPresent())
                    user = userO.get();
                else
                    return Optional.empty();

                Optional<PatientDetails> updateO = patientDetailsRepository.findById(patientDetails.getId());

                MedicalDetails medicalDetails = patientDetails.getMedicalDetails();
                List<Medication> medications = patientDetails.getMedications();

                if (updateO.isPresent()) {

                    try {
                        updateO.get().setUser(user);

                        patientDetails = patientDetailsRepository.save(updateO.get());

                        patientDetails.setMedicalDetails(medicalDetailsService.update(updateO.get(), medicalDetails, updater));

                        patientDetails.setMedications(medicationsService.updateAll(patientDetails, medications, updater));


                    } catch (Exception e){
                        e.printStackTrace();
                        throw new JdxServiceException(e.getMessage());
                    }
                    //update.setMeta(updateMeta(update.getMeta(), user));

                    return Optional.of(patientDetails);
                }
                else
                    return save(user, patientDetails, updater);
            }
        } catch (Exception e){
            throw new JdxServiceException(e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<PatientDetails> update(User user, PatientDetails patientDetails, UserDetailsImpl updater) throws JdxServiceException {
        try {
            if (patientDetails != null) {
                Optional<PatientDetails> updateO = patientDetailsRepository.findPatientDetailsByUser_Id(user.getId());

                MedicalDetails medicalDetails = patientDetails.getMedicalDetails();
                List<Medication> medications = patientDetails.getMedications();

                if (updateO.isPresent()) {
                    PatientDetails update = updateO.get();
                    try {
                        //update.setUser(user);
                        //if(patientDetails.get)

                        update.setProviders(patientDetails.getProviders());

                        patientDetails = patientDetailsRepository.save(update);

                        if(medicalDetails != null)
                            patientDetails.setMedicalDetails(medicalDetailsService.update(patientDetails, medicalDetails, updater));

                        if(medications != null && medications.size() > 0)
                            patientDetails.setMedications(medicationsService.updateAll(patientDetails, medications, updater));

                    } catch (Exception e){
                        e.printStackTrace();
                        throw new JdxServiceException(e.getMessage());
                    }
                }
                else //actually a new set of patient data
                    return save(user, patientDetails, updater);
            }
        } catch (Exception e){
            throw new JdxServiceException(e.getMessage());
        }

        return Optional.empty();
    }

    /*
    public ServiceBase.ServiceResponse<?> deleteProvider(String providerId, UserDetailsImpl user){
        Optional<Provider> oProvider = patientDetailsRepository.findById(providerId);
        Provider provider = null;

        if(oProvider.isPresent())
            provider = oProvider.get();

        if(provider != null) {
            patientDetailsRepository.delete(provider);
            log(LogCode.RESOURCE_DELETE, "Deleted the Provider " + provider.getName() ,  user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Provider with Id " + providerId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }


     */
    public PatientDetails loadAdditionalData(PatientDetails patientDetails, String[] includes){
        /*
        if(includes != null && includes.length > 0) {
            if (Arrays.stream(includes).anyMatch(s -> s.equals("medicalDetails") || s.equals("all"))) {
                Optional<MedicalDetails> medicalDetails = medicalDetailsService.getForPatientId(patientDetails);
                if(medicalDetails.isPresent())
                    patientDetails.setMedicalDetails(medicalDetails.get());
            }
            if (Arrays.stream(includes).anyMatch(s -> s.equals("medictions") || s.equals("all"))) {
                patientDetails.setMedications(medicationsService.getAllForPatient(patientDetails));
            }
        }

         */

        return patientDetails;

    }

}
