package com.junodx.api.services.lab;

import com.junodx.api.logging.LogCode;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.repositories.lab.LaboratoryRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LaboratoryService extends ServiceBase {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public Optional<Laboratory> getLaboratory(String id){
        return laboratoryRepository.findById(id);
    }

    public ServiceResponse<List<Laboratory>> getAllLaboratories(){
        List<Laboratory> labs = laboratoryRepository.findAll();
        if(labs == null)
            labs = new ArrayList<>();

        return new ServiceResponse(LogCode.SUCCESS, labs, true);
    }

    public Optional<Laboratory> getDefaultLaboratory(){
        return laboratoryRepository.findLaboratoryByDefaultLaboratoryIsTrue();
    }

    public List<Laboratory> getAllLaboratoriesByIds(List<String> ids){
        return laboratoryRepository.findAllByIdIn(ids);
    }

    public Laboratory saveLaboratory(Laboratory lab, UserDetailsImpl user){
        lab.setMeta(buildMeta(user));

        log(LogCode.RESOURCE_CREATE, "Created a Laboratory " + lab.getId(), user);

        lab = laboratoryRepository.save(lab);
        if(lab != null)
            return lab;

        return null;
    }

    public Laboratory updateLaboratory(Laboratory lab, UserDetailsImpl user){
        if(lab != null) {
            Laboratory update = laboratoryRepository.getById(lab.getId());
            if(update != null) {
                if(lab.getName() != null) update.setName(lab.getName());
                if(lab.getLocation() != null) update.setLocation(lab.getLocation());
                if(lab.getContact() != null) update.setContact(lab.getContact());
                update.setDefaultLaboratory(lab.isDefaultLaboratory());
                update.setMeta(updateMeta(update.getMeta(), user));

                return  laboratoryRepository.save(update);
            }
        }

        return null;
    }

    public ServiceResponse<?> deleteLaboratory(String labId, UserDetailsImpl user){
        Optional<Laboratory> oLab = laboratoryRepository.findById(labId);
        Laboratory lab = null;

        if(oLab.isPresent())
            lab = oLab.get();

        if(lab != null) {
            laboratoryRepository.delete(lab);
            log(LogCode.RESOURCE_DELETE, "Deleted the Laboratory " + lab.getName(), user);
            return new ServiceResponse(LogCode.SUCCESS, null, true);
        } else {
            log(LogCode.RESOURCE_DELETE, "Could not delete the Laboratory with Id " + labId, user);
            return new ServiceResponse(LogCode.RESOURCE_DELETE_ERROR, null, false);
        }
    }
}
