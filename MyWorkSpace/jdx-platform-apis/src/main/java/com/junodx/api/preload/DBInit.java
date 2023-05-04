package com.junodx.api.preload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.connectors.lims.elements.client.ElementsClient;
import com.junodx.api.connectors.lims.elements.entities.ElementsPatient;
import com.junodx.api.connectors.lims.elements.entities.ElementsPractitioner;
import com.junodx.api.jobs.StatisticsUpdateJob;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.OAuthClient;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.Product;
import com.junodx.api.models.commerce.types.ProductType;
import com.junodx.api.models.commerce.types.ResultsConfigurationTemplate;
import com.junodx.api.models.core.Address;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.Phone;
import com.junodx.api.models.fulfillment.FulfillmentProvider;
import com.junodx.api.models.fulfillment.ShippingCarrier;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.payment.PaymentProcessorProvider;
import com.junodx.api.models.providers.Location;
import com.junodx.api.models.providers.Practice;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.repositories.AuthorityRepository;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.repositories.lab.TestReportRepository;
import com.junodx.api.services.auth.OAuthService;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.PaymentProcessorProviderService;
import com.junodx.api.services.fulfillment.FulfillmentProviderService;
import com.junodx.api.services.fulfillment.ShippingCarrierService;
import com.junodx.api.services.lab.LaboratoryService;
import com.junodx.api.services.lab.LaboratoryStatisticsService;
import com.junodx.api.services.providers.PracticeService;
import com.junodx.api.services.providers.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.plaf.OptionPaneUI;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class DBInit {

    @Value("${jdx.defaults.laboratory.name}")
    private String labName;

    @Value("${jdx.defaults.laboratory.locations.street}")
    private String labStreet;

    @Value("${jdx.defaults.laboratory.locations.city}")
    private String labCity;

    @Value("${jdx.defaults.laboratory.locations.state}")
    private String labState;

    @Value("${jdx.defaults.laboratory.locations.postalCode}")
    private String labZip;

    @Value("${jdx.defaults.laboratory.locations.country}")
    private String labCountry;

    @Value("${jdx.defaults.laboratory.locations.phone}")
    private String phone;

    @Value("${jdx.defaults.practice.name}")
    private String practiceName;

    @Value("${jdx.defaults.practice.locations.phone}")
    private String practicePhone;

    @Value("${jdx.defaults.practice.secondary.phone}")
    private String practiceSecondaryPhone;

    @Value("${jdx.defaults.practice.locations.street}")
    private String practiceLocationStreet;

    @Value("${jdx.defaults.practice.locations.state}")
    private String practiceLocationState;

    @Value("${jdx.defaults.practice.locations.city}")
    private String practiceLocationCity;

    @Value("${jdx.defaults.practice.locations.postalCode}")
    private String practiceLocationPostalCode;

    @Value("${jdx.defaults.practice.locations.country}")
    private String practiceLocationCountry;

    @Value("${jdx.defaults.practice.primary.email}")
    private String practicePrimaryEmail;

    @Value("${jdx.defaults.providers.firstName}")
    private String providerFirstName;

    @Value("${jdx.defaults.providers.lastName}")
    private String providerLastName;

    @Value("${jdx.defaults.providers.email}")
    private String providerEmail;

    @Value("${jdx.defaults.providers.npi}")
    private String providerNPI;

    @Value("${jdx.defaults.providers.upin}")
    private String providerUPIN;

    private static final Logger log = LoggerFactory.getLogger(DBInit.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository,
                                   AuthorityRepository authorityRepository,
                                   UserRepository userRepository,
                                   TestReportRepository testReportRepository,
                                   LaboratoryStatisticsService laboratoryStatisticsService,
                                   FulfillmentProviderService fulfillmentProviderService,
                                   LaboratoryService laboratoryService,
                                   PaymentProcessorProviderService paymentProcessorProviderService,
                                   PracticeService practiceService,
                                   ProviderService providerService,
                                   ShippingCarrierService shippingCarrierService,
                                   OAuthService oAuthService) {

        return args -> {
          //  log.info("Preloading " + repository.saveAndFlush(new User("234324324324", "testuser@test.com")));
            try {
                ObjectMapper mapper = new ObjectMapper();
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_ADMIN)));
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_PATIENT)));
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_LAB_TECHNICIAN)));
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_MEDICAL_PROVIDER)));
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_LAB_REPORTS_VIEWER)));
//                log.info("Loading authorities: " + authorityRepository.save(new Authority(Authority.ROLE_LAB_DIRECTOR)));
                //Create some defaults onLoad
   //             Optional<User> systemUser = userRepository.findByEmailAndClientId("system_user@junodx.com", "1");
   //             UserDetailsImpl user = null;
   //             if(systemUser.isPresent())
   //                 user = UserDetailsImpl.build(systemUser.get());

   //             if(user == null)
   //                 return;
   //     Create system user

// Required from here down

                User systemuser = new User();
                systemuser.setUserType(UserType.SYSTEM);
                systemuser.setClientId("1");
                systemuser.setEmail("system_user@junodx.com");
                systemuser.setUsername("system_user");
                systemuser.setStatus(UserStatus.ACTIVATED);
                systemuser.setActivated(true);
                systemuser.setActivationTs(Calendar.getInstance());
                systemuser.addAuthority(new Authority(Authority.ROLE_ADMIN));
                User systemUser = null;
                Optional<User> foundSystemUser = userRepository.findUserByEmailAndClientId(systemuser.getEmail(), systemuser.getClientId());
                if(foundSystemUser.isEmpty()) {
                    systemUser = userRepository.save(systemuser);
                    log.info("Creating a new system user");
                }
                else {
                    systemUser = foundSystemUser.get();
                    log.info("Have an existing system user");
                }

                UserDetailsImpl user = UserDetailsImpl.build(systemUser);
                log.info("Loading user: " + user);

         //Create default laboratory
                Laboratory defaultLab = createDefaultLaboratory();
                Optional<Laboratory> foundLab = laboratoryService.getDefaultLaboratory();
                if(foundLab.isEmpty()) {
                    defaultLab = laboratoryService.saveLaboratory(defaultLab, user);
                    log.info("Creating a new default lab");
                }
                else {
                    defaultLab = foundLab.get();
                    log.info("Have an existing default lab");
                }

         //Creatwe default Juno Practice and provider

                Practice practice = createDefaultJunoPractice();
                Optional<Practice> foundPractice = practiceService.getDefaultPractice();
                if(foundPractice.isEmpty()) {
                    practice = practiceService.savePractice(practice, user);
                    log.info("Creating a new default practice");
                } else {
                    practice = foundPractice.get();
                    log.info("Have an existing practice");
                }

                Provider provider = createDefaultJunoProvider(practice);
                Optional<Provider> foundProvider = providerService.getDefaultProviderForPractice(practice.getId());
                if(foundProvider.isEmpty()) {
                    provider = providerService.saveProvider(provider, user);
                    log.info("Creating a default provider in practice " + practice.getId());
                } else  {
                    provider = foundProvider.get();
                    log.info("Have an existing default provider");
                }

        //Create default OAuth clients

                Optional<OAuthClient> client = oAuthService.findByClientId("32423423432345435");
                if(client.isEmpty()) {
                    Optional<User> oauthUser = userRepository.findByUsername("juno_test_client_32423423432345435");
                    if(oauthUser.isEmpty()) {
                        OAuthClient junoTestClient = createOauthClient("juno_test_client_32423423432345435", "juno_test_client_32423423432345435", "1", "juno_test_client", "32423423432345435", "9c362d17-b416-43e1-9163-c056fe0d1785", user);
                        oAuthService.create(junoTestClient);
                    }
                } else {
                    log.info("Have an existing client for Report Sync");
                }

                Optional<OAuthClient> client2 = oAuthService.findByClientId("9348-8892-9342-01");
                if (client2.isEmpty()) {
                    Optional<User> oauthUser = userRepository.findByUsername("juno_app_client_patient_9348-8892-9342-01");
                    if (oauthUser.isEmpty()) {
                        OAuthClient junoAppClient = createOauthClient("juno_app_client_patient_9348-8892-9342-01", "jon+juno_app_client_patient@junodx.com", "1", "juno_app_client_patient", "9348-8892-9342-01", "6bb3d421-6e0b-4934-a700-d8c6d42a7dc8", user);
                        oAuthService.create(junoAppClient);
                    }
                }else {
                    log.info("Have an existing client for Patient portal");
                }

                Optional<OAuthClient> client3 = oAuthService.findByClientId("9358-4292-7682-02");
                if (client3.isEmpty()) {
                    Optional<User> oauthUser = userRepository.findByUsername("juno_app_client_lab_9358-4292-7682-02");
                    if (oauthUser.isEmpty()) {
                        OAuthClient junoTestClientLab = createOauthClient("juno_app_client_lab_9358-4292-7682-02", "jon+juno_app_client_lab@junodx.com", "1", "juno_test_client_lab", "9358-4292-7682-02", "03035ccd-17a8-4dad-843c-c65f234b8bb0", user);
                        oAuthService.create(junoTestClientLab);
                    }
                } else {
                    log.info("Have an existing client for Lab portal");
                }

                Optional<OAuthClient> client4 = oAuthService.findByClientId("0352-8232-9622-03");
                if (client4.isEmpty()) {
                    Optional<User> oauthUser = userRepository.findByUsername("juno_app_client_provider_0352-8232-9622-03");
                    if (oauthUser.isEmpty()) {
                        OAuthClient junoTestClientProvider = createOauthClient("juno_app_client_provider_0352-8232-9622-03", "jon+juno_app_client_provider@junodx.com", "1", "juno_test_client_provider", "0352-8232-9622-03", "28af8408-a6d8-46ad-9df4-00f40397723c", user);
                        oAuthService.create(junoTestClientProvider);
                    }
                } else {
                    log.info("Have an existing client for Provider Portal");
                }



                /*
                OAuthClient oAuthClient = new OAuthClient();
                User accountUser = new User();
                accountUser.setUsername("juno_test_client_32423423432345435");
                accountUser.setEmail("jon+juno_test_client@junodx.com");
                accountUser.setClientId("1");
                accountUser.setUserType(UserType.SYSTEM);
                accountUser.addAuthority(new Authority(Authority.ROLE_ADMIN));

                oAuthClient.setName("juno_test_client");
                oAuthClient.setClientId("32423423432345435");
                oAuthClient.setClientSecret("9c362d17-b416-43e1-9163-c056fe0d1785");
                oAuthClient.setAccountUser(accountUser);
                oAuthService.create(oAuthClient);


                OAuthClient oAuthClientPatient = new OAuthClient();
                User accountUser2 = new User();
                accountUser2.setUsername("juno_app_client_patient_9348-8892-9342-01");
                accountUser2.setEmail("jon+juno_app_client_patient@junodx.com");
                accountUser2.setClientId("1");
                accountUser2.setUserType(UserType.SYSTEM);
                accountUser2.addAuthority(new Authority(Authority.ROLE_ADMIN));

                oAuthClientPatient.setName("juno_app_client_patient");
                oAuthClientPatient.setClientId("9348-8892-9342-01");
                oAuthClientPatient.setClientSecret("6bb3d421-6e0b-4934-a700-d8c6d42a7dc8");
                oAuthClientPatient.setAccountUser(accountUser2);
                oAuthService.create(oAuthClientPatient);


                OAuthClient oAuthClientLab = new OAuthClient();
                User accountUser3 = new User();
                accountUser3.setUsername("juno_app_client_lab_9358-4292-7682-02");
                accountUser3.setEmail("jon+juno_app_client_lab@junodx.com");
                accountUser3.setClientId("1");
                accountUser3.setUserType(UserType.SYSTEM);
                accountUser3.addAuthority(new Authority(Authority.ROLE_ADMIN));

                oAuthClientLab.setName("juno_test_client_lab");
                oAuthClientLab.setClientId("9358-4292-7682-02");
                oAuthClientLab.setClientSecret("03035ccd-17a8-4dad-843c-c65f234b8bb0");
                oAuthClientLab.setAccountUser(accountUser3);
                oAuthService.create(oAuthClientLab);


                OAuthClient oAuthClientProvider = new OAuthClient();
                User accountUser4 = new User();
                accountUser4.setUsername("juno_app_client_provider_0352-8232-9622-03");
                accountUser4.setEmail("jon+juno_app_client_provider@junodx.com");
                accountUser4.addAuthority(new Authority(Authority.ROLE_ADMIN));

                oAuthClientProvider.setName("juno_test_client_provider");
                oAuthClientProvider.setClientId("0352-8232-9622-03");
                oAuthClientProvider.setClientSecret("28af8408-a6d8-46ad-9df4-00f40397723c");
                oAuthClientProvider.setAccountUser(accountUser4);
                oAuthService.create(oAuthClientProvider);
                */


    //Preload Fulfillment providers
                ShippingCarrier ups = new ShippingCarrier();
                ups.setName("UPS");

                ShippingCarrier usps = new ShippingCarrier();
                usps.setName("USPS");

                ShippingCarrier fedex = new ShippingCarrier();
                fedex.setName("FedEx");

                Optional<ShippingCarrier> foundUps = shippingCarrierService.findCarrierByName(ups.getName());
                Optional<ShippingCarrier> foundUsps = shippingCarrierService.findCarrierByName(usps.getName());
                Optional<ShippingCarrier> foundFedex = shippingCarrierService.findCarrierByName(fedex.getName());

                if(foundUps.isEmpty()) {
                    ups = shippingCarrierService.saveCarrier(ups, user);
                    log.info("Created new shipping carrier for UPS");
                } else
                {
                    ups = foundUps.get();
                    log.info("Have an existing carrier for UPS");
                }

                if(foundUsps.isEmpty()) {
                    usps = shippingCarrierService.saveCarrier(usps, user);
                    log.info("Created new shipping carrier for USPS");
                } else
                {
                    usps = foundUsps.get();
                    log.info("Have an existing carrier for USPS");
                }

                if(foundFedex.isEmpty()) {
                    fedex = shippingCarrierService.saveCarrier(fedex, user);
                    log.info("Created new shipping carrier for FedEx");
                } else
                {
                    fedex = foundFedex.get();
                    log.info("Have an existing carrier for Fedex");
                }


                List<ShippingCarrier> carriers = new ArrayList<>();
                carriers.add(ups);
                carriers.add(fedex);
                carriers.add(usps);

                FulfillmentProvider sdProvider = new FulfillmentProvider();
                sdProvider.setName("Juno Distribution");
                sdProvider.setCoveredCountries("US");
                sdProvider.setCoveredStates("WA,OR,HI,AK,CA,NV,AZ,WY,MT,NM,CO,ND,SD,ID,UT,TX,OK,KS,NE");
                sdProvider.setCarriers(carriers);
                sdProvider.setDefaultCarrierId(ups.getId());
                sdProvider.setDefaultProvider(true);
                sdProvider.setLabPortalAssigned(true);
                Address shipFromAddress = new Address();
                shipFromAddress.setStreet("11760 Sorrento Valley Rd");
                shipFromAddress.setCity("San Diego");
                shipFromAddress.setState("CA");
                shipFromAddress.setCountry("US");
                shipFromAddress.setPostalCode("92121");

                sdProvider.setShipFromAddress(shipFromAddress);

                /*
                FulfillmentProvider alliedProvider = new FulfillmentProvider();
                alliedProvider.setName("Allied Group");
                alliedProvider.setCoveredCountries("US");
                alliedProvider.setCoveredStates("ME,NH,VT,MA,RI,NY,NJ,PA,DE,MD,DC,VA,NC,SC,GA,FL,AL,MS,AK,AR,MS,TN,KY,WV,OH,IN,IL,MI,WI,LA,IA,MN");
                alliedProvider.setCarriers(carriers);
                alliedProvider.setDefaultCarrierId(ups.getId());
                Address shipFromAddressAllied = new Address();
                shipFromAddressAllied.setStreet("30 Martin Street");
                shipFromAddressAllied.setCity("Cumberland");
                shipFromAddressAllied.setState("RI");
                shipFromAddressAllied.setCountry("US");
                shipFromAddressAllied.setPostalCode("02864");
                alliedProvider.setShipFromAddress(shipFromAddressAllied);

                //fulfillmentProviderService.saveProvider(alliedProvider, user);

                */

                Optional<FulfillmentProvider> defaultProvider = fulfillmentProviderService.getDefaultProvider();
                if(defaultProvider.isEmpty()) {
                    Optional<FulfillmentProvider> foundShippingProvider = fulfillmentProviderService.findProviderByName("Juno Distribution");
                    if(foundShippingProvider.isEmpty()) {
                        sdProvider = fulfillmentProviderService.saveProvider(sdProvider, user);
                        log.info("Creating new default shipping provider");
                    } else {
                        sdProvider = foundShippingProvider.get();
                        sdProvider.setDefaultProvider(true);
                        sdProvider = fulfillmentProviderService.saveProvider(sdProvider, user);
                        log.info("Found existing provider, but setting to default");
                    }
                }else {
                    sdProvider = defaultProvider.get();
                    log.info("Found existing default fulfillment provider");
                }

//Payment processor setup

                PaymentProcessorProvider stripe = new PaymentProcessorProvider();
                stripe.setName("Stripe");

                Optional<PaymentProcessorProvider> processor = paymentProcessorProviderService.getPaymentProcessorProviderByName("Stripe");
                if(processor.isEmpty()) {
                    paymentProcessorProviderService.save(stripe);
                    log.info("Creating a new payment processor for Stripe");
                } else {
                    stripe = processor.get();
                    log.info("Have an existing Stripe payment processor");
                }
            } catch (Exception e ){
                log.info("Cannot preload the DB " + e.getMessage() );
                e.printStackTrace();
            }
        };
    }

    public Laboratory createDefaultLaboratory(){
        Laboratory lab = new Laboratory();
        lab.setDefaultLaboratory(true);
        lab.setName(labName);

        Address location = new Address();
        location.setStreet(labStreet);
        location.setCity(labCity);
        location.setState(labState);
        location.setPostalCode(labZip);
        location.setCountry(labCountry);

        lab.setLocation(location);
        lab.setContact(new Phone(phone));

        return lab;
    }

    public Practice createDefaultJunoPractice(){
        Practice practice = new Practice();
        practice.setName(practiceName);
        practice.setPrimaryPhone(new Phone(practicePhone));
        Location primaryLocation = new Location();
        primaryLocation.setName(practiceName);

        Address location = new Address();
        location.setStreet(practiceLocationStreet);
        location.setCity(practiceLocationCity);
        location.setState(practiceLocationState);
        location.setPostalCode(practiceLocationPostalCode);
        location.setCountry(practiceLocationCountry);

        primaryLocation.setAddress(location);
        primaryLocation.setPhone(new Phone(practicePhone));
        List<Location> locations = new ArrayList<>();
        locations.add(primaryLocation);

        practice.setLocations(locations);
        practice.setContactAddress(location);
        practice.setPrimaryEmail(practicePrimaryEmail);
        practice.setDefaultPractice(true);

        return practice;
    }

    public Provider createDefaultJunoProvider(Practice practice){
        Provider provider = new Provider();
        provider.setFirstName(providerFirstName);
        provider.setLastName(providerLastName);
        provider.setEmail(providerEmail);
        provider.setNpi(providerNPI);
        provider.setUpin(providerUPIN);
        provider.setPracticing(true);
        provider.setPractice(practice);
        provider.setDefaultProvider(true);
        provider.setContactPhone(new Phone(practicePhone));
        provider.setContactAddress(practice.getContactAddress());

        return provider;
    }

    public OAuthClient createOauthClient(String username, String email, String hostClientId, String clientName, String clientId, String clientSecret, UserDetailsImpl systemuser) {
        OAuthClient oauthClient = new OAuthClient();
        User accountUser = new User();
        accountUser.setUsername(username);
        accountUser.setEmail(email);
        accountUser.setClientId(hostClientId);
        accountUser.setUserType(UserType.SYSTEM);
        accountUser.setActivated(true);
        accountUser.setStatus(UserStatus.ACTIVATED);
        accountUser.setActivationTs(Calendar.getInstance());
        Meta.setMeta(accountUser.getMeta(), systemuser);
        accountUser.addAuthority(new Authority(Authority.ROLE_ADMIN));

        oauthClient.setName(clientName);
        oauthClient.setClientId(clientId);
        oauthClient.setClientSecret(clientSecret);
        oauthClient.setAccountUser(accountUser);
        oauthClient.setGrantTypes("Client Credentials");
        oauthClient.setScope("ADMIN");

        return oauthClient;
    }
}
