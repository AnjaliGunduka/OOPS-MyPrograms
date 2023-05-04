package com.junodx.api.services.patients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.patient.MedicalDetails;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.models.patient.Vital;
import com.junodx.api.repositories.patients.MedicalDetailsRepository;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.providers.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicalDetailsService {
    @Autowired
    private MedicalDetailsRepository medicalDetailsRepository;

    @Autowired
    private VitalsService vitalsService;

    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    ObjectMapper mapper;

    public MedicalDetailsService(){
        this.mapper = new ObjectMapper();
    }

    public Optional<MedicalDetails> getForPatientId(PatientDetails patient){
        return medicalDetailsRepository.findMedicalDetailsByPatientDetails_Id(patient.getId());
    }

    public MedicalDetails save(PatientDetails patient, MedicalDetails medicalDetails){
        medicalDetails.setPatientDetails(patient);
        List<Vital> vitals = medicalDetails.getVitals();

        medicalDetails =  medicalDetailsRepository.save(medicalDetails);
        if(vitals != null && vitals.size() > 0)
            medicalDetails.setVitals(vitalsService.saveAll(medicalDetails, vitals));

        return medicalDetails;
    }

    public MedicalDetails update(PatientDetails patient, MedicalDetails medicalDetails, UserDetailsImpl updater){
        if(medicalDetails != null){
            Optional<MedicalDetails> updateDetails = medicalDetailsRepository.findMedicalDetailsByPatientDetails_Id(patient.getId());
            List<Vital> vitals = medicalDetails.getVitals();

            if(updateDetails.isPresent()){
                MedicalDetails update = updateDetails.get();
                update.setPregnant(medicalDetails.isPregnant());
                update.setGestationalAge(medicalDetails.getGestationalAge());
                update.setNumberOfFetuses(medicalDetails.getNumberOfFetuses());
                update.setThreeOrMoreFetuses(medicalDetails.isThreeOrMoreFetuses());

                update.setPatientDetails(patient);

                medicalDetails = medicalDetailsRepository.save(update);

                if(vitals != null && vitals.size() > 0)
                    medicalDetails.setVitals(vitalsService.updateAll(medicalDetails, vitals, updater));

                return medicalDetails;
            }
            else
                return save(patient, medicalDetails);

        }
        return null;
    }
}
