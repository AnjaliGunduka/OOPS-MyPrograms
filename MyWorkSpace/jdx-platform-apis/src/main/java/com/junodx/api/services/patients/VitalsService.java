package com.junodx.api.services.patients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.patient.MedicalDetails;
import com.junodx.api.models.patient.Vital;
import com.junodx.api.repositories.patients.VitalsRepository;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.providers.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VitalsService {
    @Autowired
    private VitalsRepository vitalsRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    ObjectMapper mapper;

    public VitalsService(){
        this.mapper = new ObjectMapper();
    }

    public List<Vital> getAllForMedicalDetailsId(MedicalDetails medicalDetails){
        return vitalsRepository.findVitalsByMedicalDetails_Id(medicalDetails.getId());
    }

    public List<Vital> saveAll(MedicalDetails medicalDetails, List<Vital> vitals){
        for(Vital v : vitals)
            v.setMedicalDetails(medicalDetails);

        return vitalsRepository.saveAll(vitals);
    }

    public List<Vital> updateAll(MedicalDetails medicalDetails, List<Vital> vitals, UserDetailsImpl user){
        if(vitals != null && vitals.size() >0){
            List<Vital> updateVitals = vitalsRepository.findVitalsByMedicalDetails_Id(medicalDetails.getId());

            if(updateVitals != null && updateVitals.size() > 0){
                for(Vital v : vitals){
                    Vital vital = updateVitals.stream()
                            .filter(vit -> vit.getType().equals(v.getType())).findFirst().orElse(null);
                    if(vital != null) {
                        vital.setMedicalDetails(medicalDetails);
                        vital.setRecordedAt(v.getRecordedAt());
                        vital.setRecordedBy(v.getRecordedBy());
                        vital.setValue(v.getValue());
                        vital.setValueType(v.getValueType());
                    }
                }

                return vitalsRepository.saveAll(updateVitals);
            }
            else
                return saveAll(medicalDetails, vitals);

        }
        return null;
    }
}

