package com.junodx.api.controllers.users;

import com.junodx.api.models.patient.PatientChartEntry;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.services.auth.UserServiceImpl;
import com.junodx.api.services.patients.PatientDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.ControllerUtils;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.logging.LogCode;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/users/patient")
public class PatientDetailsController {

    @Autowired
    private PatientDetailsService patientDetailsService;

    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(PatientDetailsController.class);

    private ObjectMapper mapper;

    public PatientDetailsController(){
        mapper = new ObjectMapper();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')"  + "|| hasRole('ROLE_PATIENT')")
    public ResponseEntity<?> getPatientDetails(@PathVariable("userId") String userId, @RequestParam("include") Optional<String> includeFields) {
        //Grab the userId from the security context (access token) and use that Role is patient
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        String iFields = "";
        if(includeFields.isPresent())
            iFields = includeFields.get();

        String[] includes = ControllerUtils.extractIncludeFields(iFields);

        if(userId != null) {
            Optional<User> user = userService.findOne(userId);
            if(user.isPresent()) {
                Optional<PatientDetails> patientDetails = patientDetailsService.getByUserId(userId, includes);
                if(patientDetails.isPresent()) {
                    user.get().setPatientDetails(patientDetails.get());
                    patientDetails.get().setUser(user.get());

                    return ResponseEntity.ok().body(user);
                }
            }
        }

        return ResponseEntity.notFound().build();
    }


    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')"  + "|| hasRole('ROLE_PATIENT')")
    public ResponseEntity<?> createPatientDetails(@RequestBody PatientDetails patientDetails) {
        //Grab the userId from the security context (access token) and use that Role is patient
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        try {
            Optional<PatientDetails> resp = patientDetailsService.save(patientDetails, userContext);
            if (resp.isPresent())
                return ResponseEntity.ok().body(resp.get());
            else
                return ResponseEntity.badRequest().body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code, Calendar.getInstance().getTime(), "Unable to create a patient details for name " + patientDetails.getUser().getEmail()));
        } catch( Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code, Calendar.getInstance().getTime(), "Unable to create a provider for name " + patientDetails.getUser().getEmail() + " with exception " + e.getMessage()));
        }
    }

    @PostMapping("/chart/{userId}")
    @PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> upsertChart(@PathVariable("userId") String userId, @RequestBody PatientChartEntry entry) {
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        try {
            PatientChartEntry chartEntry = userService.handleChart(userId, entry, userContext);

            if (chartEntry != null)
                return ResponseEntity.ok().body(chartEntry);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Cannot process chart update, " + e.getMessage());
        }

        return ResponseEntity.badRequest().body("Cannot add patient chart");
    }

    @GetMapping("/chart/{userId}")
    @PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getChart(@PathVariable("userId") String userId) {
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        try {
            return ResponseEntity.ok().body(userService.getChart(userId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Cannot process chart update, " + e.getMessage());
        }
    }

    @GetMapping("/chart/{userid}/entry/{entryid}")
    @PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getChartEntry(@PathVariable("userid") String userId, @PathVariable("entryid") String chartEntryId) {
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        try {
            PatientChartEntry chart = userService.getChartEntry(userId, chartEntryId);

            if (chart != null)
                return ResponseEntity.ok().body(chart);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Cannot process chart update, " + e.getMessage());
        }

        return ResponseEntity.badRequest().body("Cannot add patient chart");
    }

    /*
    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProvider(@RequestBody Provider provider) {
        //Grab the userId from the security context (access token) and use that Role is patient
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

        try {
            logger.info("Received object provider " + mapper.writeValueAsString(provider));
        } catch ( Exception e ){ }

        ServiceBase.ServiceResponse<Provider> resp = providerService.updateProvider(provider, userContext);
        if(resp.isSucceeded())
            if(resp.getResponseValue() != null)
                return ResponseEntity.ok().body(resp.getResponseValue());

        return ResponseEntity.badRequest().body(new MessageResponse(resp.getCode().label, resp.getCode().code, Calendar.getInstance().getTime(), "Unable to update the provider for name " + provider.getName()));
    }

    @DeleteMapping("/{providerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProvider(@PathVariable("providerId") String providerId) {
        //Grab the userId from the security context (access token) and use that Role is patient
        Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();
        ServiceBase.ServiceResponse<?> resp = providerService.deleteProvider(providerId, userContext);
        if(resp.getCode().code == LogCode.SUCCESS.code)
            return ResponseEntity.ok().body(new MessageResponse("Deleted provider " + providerId,
                    LogCode.RESOURCE_DELETE.code,
                    Calendar.getInstance().getTime(),
                    "Deletion of provider was successful"));
        else
            return ResponseEntity.badRequest().body(new MessageResponse(LogCode.RESOURCE_DELETE_ERROR.label, LogCode.RESOURCE_DELETE_ERROR.code, Calendar.getInstance().getTime(), "Encountered an error trying to delete the provider at ID " + providerId));
    }

     */
}





