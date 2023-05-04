package com.junodx.api.services.patients;

import com.junodx.api.models.patient.Medication;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.repositories.patients.MedicationsRepository;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MedicationsService {
    @Autowired
    private MedicationsRepository medicationsRepository;

    public List<Medication> getAllForPatient(PatientDetails patient){
        return medicationsRepository.findMedicationsByPatientDetails_Id(patient.getId());
    }

    public List<Medication> saveAll(PatientDetails patient, List<Medication> meds){
        for(Medication m : meds)
            m.setPatientDetails(patient);
        return medicationsRepository.saveAll(meds);
    }

    public List<Medication> updateAll(PatientDetails patient, List<Medication> meds, UserDetailsImpl user){
        if(meds != null && meds.size() > 0){
            List<Medication> updateMeds = medicationsRepository.findMedicationsByPatientDetails_Id(patient.getId());
            if(updateMeds != null && updateMeds.size() > 0){
                for(Medication m : meds){
                    Medication foundMed = updateMeds.stream()
                            .filter(med -> med.getName().equals(m.getName())).findFirst().orElse(null);
                    if(foundMed != null)
                        if(!foundMed.getType().equals(m.getType())){
                            m.setPatientDetails(patient);
                            updateMeds.add(m);
                        }
                }

                return medicationsRepository.saveAll(updateMeds);
            }
            else
                return saveAll(patient, meds);
        }
        return null;
    }
}
