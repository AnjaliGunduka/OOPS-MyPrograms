package com.junodx.api.services.lab;

import com.junodx.api.models.laboratory.tests.LabConstants;
import com.junodx.api.repositories.lab.LabConstantsRepository;
import com.junodx.api.services.ServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LabConstantsService extends ServiceBase {
    @Autowired
    private LabConstantsRepository labConstantsRepository;

    public List<LabConstants> get(){
        return labConstantsRepository.findAll();
    }

    public LabConstants save(LabConstants constants){
        return labConstantsRepository.save(constants);
    }

    public LabConstants update(LabConstants constants){
        return labConstantsRepository.save(constants);
    }
}
