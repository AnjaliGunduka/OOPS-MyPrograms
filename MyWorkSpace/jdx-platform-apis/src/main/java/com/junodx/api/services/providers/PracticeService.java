package com.junodx.api.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.lims.elements.client.ElementsClient;
import com.junodx.api.connectors.lims.elements.entities.ElementsClinic;
import com.junodx.api.connectors.lims.elements.entities.ElementsLocation;
import com.junodx.api.controllers.payloads.SalesforceLocationData;
import com.junodx.api.controllers.payloads.SalesforcePracticeChangedDataMap;
import com.junodx.api.controllers.payloads.SalesforcePracticeChildAccounts;
import com.junodx.api.controllers.payloads.SalesforcePracticePayload;
import com.junodx.api.controllers.payloads.SalesforcePracticeRecordChanged;
import com.junodx.api.controllers.payloads.SalesforceProviderData;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.providers.Location;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.providers.LocationRepository;
import com.junodx.api.repositories.providers.PracticeRepository;
import com.junodx.api.repositories.providers.ProviderRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PracticeService extends ServiceBase {

    @Autowired
    private PracticeRepository practiceRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private LocationRepository locationRepository;

    private static final Logger logger = LoggerFactory.getLogger(PracticeService.class);

    private ObjectMapper mapper;

    public PracticeService(){
        mapper = new ObjectMapper();
    }

    public Practice getPractice(String id, String[] includes) throws JdxServiceException {
        if(id != null){
            Optional<Practice> returnPractice = practiceRepository.findById(id);
            if(returnPractice.isPresent()){
                Practice practice = returnPractice.get();
                practice = loadAdditionalData(practice, includes);
                return practice;
            }
        }
        throw new JdxServiceException("Cannot find practice with id " + id);
    }

    public Optional<Practice> getDefaultPractice() throws JdxServiceException {
        try {
            return practiceRepository.findPracticeByDefaultPracticeIsTrue();
        } catch(Exception e){
            throw new JdxServiceException("Cannot find default practice: " + e.getMessage());
        }
    }

    public ServiceBase.ServiceResponse<List<Practice>> getAllPractices(){
        List<Practice> labs = practiceRepository.findAll();
        if(labs == null)
            labs = new ArrayList<>();

        return new ServiceBase.ServiceResponse(LogCode.SUCCESS, labs, true);
    }

    public Optional<Practice> findPracticeByLimsId(String id) {
        return practiceRepository.findPracticeByLimsId(id);
    }

    public Optional<Practice> findPracticeByXifinId(String id) {
        return practiceRepository.findPracticeByXifinId(id);
    }

    public Optional<Practice> findPracticeBySalesforceId(String id) {
        return practiceRepository.findPracticeBySalesforceId(id);
    }

    public Practice savePractice(Practice practice, UserDetailsImpl user) throws JdxServiceException {

        practice.setMeta(buildMeta(user));
        List<Location> locations = practice.getLocations();
        List<Provider> providers = practice.getProviders();

        for (Location location : locations)
            location.setPractice(practice);

        for (Provider provider : providers)
            provider.setPractice(practice);

        try {
          ElementsClinic clinic = manageClinicInLims(practice);
            if(clinic != null)
                practice.setLimsId(clinic.getId().toString());

            return practiceRepository.save(practice);

        } catch (Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot save practice " + e.getMessage());
        }
    }

    public Practice updatePractice(Practice practice, UserDetailsImpl user){
        try {
            logger.info("Updating practice " + mapper.writeValueAsString(practice.getLocations()));
            if (practice != null) {
                Practice update = practiceRepository.getById(practice.getId());
                if (update != null) {
                    if (practice.getName() != null) update.setName(practice.getName());
                    if (practice.getBillingEmail() != null) update.setBillingEmail(practice.getBillingEmail());
                    if (practice.getPatientEmail() != null) update.setPrimaryEmail(practice.getPatientEmail());
                    if (practice.getPrimaryEmail() != null) update.setPrimaryEmail(practice.getPrimaryEmail());
                    if (practice.getContactAddress() != null) update.setContactAddress(practice.getContactAddress());
                    if (practice.getPrimaryPhone() != null) update.setPrimaryPhone(practice.getPrimaryPhone());
                 //   if (practice.getSecondaryPhone() != null) update.setSecondaryPhone(practice.getSecondaryPhone());
                    update.setActive(practice.isActive());
                    update.setMeta(updateMeta(update.getMeta(), user));

                    practice = practiceRepository.save(update);

                    log(LogCode.RESOURCE_UPDATE, "Updated a Practice " + practice.getId(), user);

                    ElementsClinic clinic = manageClinicInLims(practice);
                    if(clinic == null)
                        logger.info("Could not update clinic in LIMS");

                    return practice;
                }
            }
        } catch(Exception e){
            throw new JdxServiceException("Cannot update practice");
        }

        throw new JdxServiceException("Cannot update practice");
    }

    public ServiceBase.ServiceResponse<?> deletePractice(String practiceId, UserDetailsImpl user, boolean cascade){
        Optional<Practice> oPractice = practiceRepository.findById(practiceId);
        Practice practice = null;

        if(oPractice.isPresent())
            practice = oPractice.get();

        if(practice != null) {
            //Only delete the locations and providers on command
            if(cascade) {
                List<Location> locations = locationRepository.findLocationsByPractice_Id(practice.getId()).stream().collect(Collectors.toList());
                List<Provider> providers = providerRepository.findByPractice_Id(practice.getId()).stream().collect(Collectors.toList());

                locationRepository.deleteLocationsByPractice_Id(practice.getId());
                providerRepository.deleteProvidersByPractice_Id(practice.getId());
            }

            practiceRepository.delete(practice);



            log(LogCode.RESOURCE_DELETE, "Deleted the Practice " + practice.getName(), user);
            return new ServiceBase.ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Practice with Id " + practiceId, user);
            return new ServiceBase.ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }

    public Practice loadAdditionalData(Practice practice, String[] includes){
        if(includes != null && includes.length > 0) {
            if (Arrays.stream(includes).anyMatch(s -> s.equals("locations") || s.equals("all"))) {
                practice.setLocations(locationRepository.findLocationsByPractice_Id(practice.getId()).stream().collect(Collectors.toList()));
            }
            if (Arrays.stream(includes).anyMatch(s -> s.equals("providers") || s.equals("all"))) {
                practice.setProviders(providerRepository.findByPractice_Id(practice.getId()));
            }
        }

        return practice;

    }

    protected ElementsClinic manageClinicInLims(Practice practice) {
        try {
            ElementsClient elements = new ElementsClient();
            ElementsClinic clinic = new ElementsClinic();
            if(practice.getLimsId() != null)
                clinic.setId(Integer.valueOf(practice.getLimsId()));

            clinic.setName(practice.getName());
            clinic.setEmail(practice.getPrimaryEmail());
            clinic.setPhone(practice.getPrimaryPhone().getPhoneNumber());

            ElementsLocation location = new ElementsLocation();
            if (practice.getContactAddress() != null) {
                location.setAddressOne(practice.getContactAddress().getStreet());
                location.setState(practice.getContactAddress().getState());
                location.setCity(practice.getContactAddress().getCity());
                location.setZip(practice.getContactAddress().getPostalCode());
                location.setCountry(practice.getContactAddress().getCountry());
            }
          logger.info("Clinic: " +mapper.writeValueAsString(clinic) + " and location: " + mapper.writeValueAsString(location));
            return elements.createClinic(clinic, location);
        }catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot create clinic in lims " + e.getMessage());
        }
    }

    public void updatePracticeFromSalesforce(SalesforcePracticePayload payload, UserDetailsImpl userContext) throws JdxServiceException {
        int updates = 0;
        try {
            List<Practice> practicesToUpdate = new ArrayList<>();

            for (SalesforcePracticeRecordChanged record : payload.getRecordsChanged()) {
                Practice practice = practiceRepository.getById(record.getId());
                boolean newPractice = false;

                if (practice == null)
                    logger.info("Practice not found with the id {}", record.getId());
                else {
                    practice = new Practice();
                    practice.setMeta(buildMeta(userContext));
                    newPractice = true;
                }

                SalesforcePracticeChangedDataMap data = record.getChangedDataMap();
                if (data != null) {
                    if(data.getId() == null) {
                        logger.info("Cannot find Id of Salesforce record, skipping");
                        continue;
                    } else if(practice.getSalesforceId() == null || (practice.getSalesforceId() != null && !practice.getSalesforceId().equals(data.getId())))
                        practice.setSalesforceId(data.getId());

                    SalesforceLocationData locationData = data.getLocation();
                    if (locationData != null && StringUtils.isNotBlank(locationData.getId())) {
                        if (newPractice) {
                            Location location = new Location();
                            updateLocation(locationData, location);
                            location.setPractice(practice);
                            practice.addLocation(location);
                        } else {
                            Location location = locationRepository.findLocationByIdAndPractice_Id(locationData.getId(), record.getId());
                            if (location != null) {
                                updateLocation(locationData, location);
                            } else {
                                logger.error("could not find location {} for the practice {}", locationData.getId(), record.getId());
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(data.getBillingEmail())) {
                        practice.setBillingEmail(data.getBillingEmail());
                    }
                    if (StringUtils.isNotBlank(data.getPrimaryEmail())) {
                        practice.setPrimaryEmail(data.getPrimaryEmail());
                    }

                /*
                if (data.getChildAccounts() != null) {
                    SalesforcePracticeChildAccounts childAccounts = data.getChildAccounts();
                    List<SalesforceProviderData> providers = childAccounts.getRecords();
                    for (SalesforceProviderData providerData : providers) {
                        if (StringUtils.isBlank(providerData.getId())) {
                            logger.error("provider id is null.");
                            continue;
                        }
                        Provider provider = providerRepository.findProviderByIdAndPractice_Id(providerData.getId(), data.getId());
                        if (provider == null) {
                            logger.error("could not find provider {} for practice {}",providerData.getId(), data.getId());
                            continue;
                        }

                        if (StringUtils.isNotBlank(providerData.getName())) {
                            String[] name = StringUtils.split(providerData.getName());
                            if (name.length == 1) {
                                provider.setFirstName(name[0]);
                            } else if (name.length > 1) {
                                provider.setFirstName(name[0]);
                                String lastName = "";
                                for(int i=0; i < name.length; i++) {
                                    if (i != 0) {
                                        lastName = lastName + " " + name[i];
                                    }
                                }
                                provider.setLastName(lastName.strip());
                            }
                        }
                        if (StringUtils.isNotBlank(providerData.getPhone())) {
                            Phone phone = provider.getContactPhone();
                            if (phone == null) {
                                phone = new Phone();
                                provider.setContactPhone(phone);
                            }
                            provider.getContactPhone().setPhoneNumber(providerData.getPhone());
                        }
                        Meta.setMeta(provider.getMeta(), userContext);
                    }
                }
                 */
                }
                Meta.setMeta(practice.getMeta(), userContext);
                updates++;

                practicesToUpdate.add(practice);
            }
            //TODO Need to actually save to the DB here
            if (practicesToUpdate.size() > 0)
                practiceRepository.saveAll(practicesToUpdate);

            logger.info("processed {} records", updates);
        } catch (Exception e){
            throw new JdxServiceException("Cannot update practices from Salesforce update");
        }
    }

    private void updateLocation(SalesforceLocationData data, Location location) {
        if (data != null && location != null) {
            if (StringUtils.isNotBlank(data.getName())) {
                location.setName(data.getName());
            }
            if (StringUtils.isNotBlank(data.getPhone())) {
                Phone phone = location.getPhone();
                if (phone == null) {
                    phone = new Phone();
                    location.setPhone(phone);
                }
                location.getPhone().setPhoneNumber(data.getPhone());
            }
            Address addr = data.getAddress();
            if (addr != null) {
                Address address = location.getAddress();
                if (address == null) {
                    address = new Address();
                    location.setAddress(address);
                }
                if (StringUtils.isNotBlank(addr.getCity()) && !addr.getCity().equals(address.getCity())) {
                    address.setCity(addr.getCity());
                }
                if (StringUtils.isNotBlank(addr.getCountry()) && !addr.getCountry().equals(address.getCountry())) {
                    address.setCountry(addr.getCountry());
                }
                if (StringUtils.isNotBlank(addr.getName()) && !addr.getName().equals(address.getName())) {
                    address.setName(addr.getName());
                }
                if (StringUtils.isNotBlank(addr.getPostalCode()) && !addr.getPostalCode().equals(address.getPostalCode())) {
                    address.setPostalCode(addr.getPostalCode());
                }
                if (StringUtils.isNotBlank(addr.getState()) && !addr.getState().equals(address.getState())) {
                    address.setState(addr.getState());
                }
                if (StringUtils.isNotBlank(addr.getStreet()) && !addr.getStreet().equals(address.getStreet())) {
                    address.setStreet(addr.getStreet());
                }
            }
        }
    }
}
