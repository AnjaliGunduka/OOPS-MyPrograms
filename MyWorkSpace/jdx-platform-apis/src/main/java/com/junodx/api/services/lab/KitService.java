package com.junodx.api.services.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.lab.LaboratoryErrorCodes;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.repositories.lab.KitRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.commerce.OrderService;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KitService extends ServiceBase{
    @Autowired
    private KitRepository kitRepository;

    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(KitService.class);

    public KitService(){
        this.mapper = new ObjectMapper();
    }

    public Optional<Kit> getKit(String id){
        return kitRepository.findById(id);
    }

    public List<Kit> findKitsBySampleNumber(String sampleNumber){
        try {
            return kitRepository.findKitsBySampleNumber(sampleNumber);
        } catch(Exception e){
            throw new JdxServiceException("Cannot obtain any kit for sample number");
        }
    }

    public List<Kit> findKitsBySampleNumbersInList(List<String> sampleNumbers){
        try {
            return kitRepository.findKitsBySampleNumberIn(sampleNumbers);
        } catch(Exception e){
            throw new JdxServiceException("Cannot obtain any kit for sample number");
        }
    }

    public List<Kit> findKitsByKitCodeOrSampleNumberOrSleeveNumber(String kitCode, String sampleNumber, String sleeveNumber) throws JdxServiceException {
        try {
            return kitRepository.findKitsByCodeOrSampleNumberOrPsdSleeveNumber(kitCode, sampleNumber, sleeveNumber);
        } catch(Exception e){
            throw new JdxServiceException("Cannot obtain any kit for sample number");
        }
    }

    public List<Kit> findKitsByKitCodeOrSampleNumber(String kitCode, String sampleNumber) throws JdxServiceException {
        try {
            return kitRepository.findKitsByCodeOrSampleNumber(kitCode, sampleNumber);
        } catch(Exception e){
            throw new JdxServiceException("Cannot obtain any kit for sample number");
        }
    }

    public Optional<Kit> findKitBySampleNumber(String sampleNumber){
        try {
            return kitRepository.findKitBySampleNumber(sampleNumber);
        } catch(Exception e){
            throw new JdxServiceException("Cannot obtain any kit for sample number");
        }

    }

    public Page<Kit> getAllKits(Pageable pageable) throws JdxServiceException {
        try {
            return kitRepository.findAll(pageable);
        } catch(Exception e){
            throw new JdxServiceException("Cannot get all kits");
        }

    }

    public List<Kit> findKitsByIds(List<String> ids){
        return kitRepository.findAllById(ids);
    }

    public Kit saveKit(Kit kit, UserDetailsImpl user) throws JdxServiceException {
        kit.setMeta(buildMeta(user));

        log(LogCode.RESOURCE_CREATE, "Created a Laboratory " + kit.getId(), user);

        try {
            if(kit.isUnusable() || kit.isAssigned())
                throw new JdxServiceException("Cannot assign kit " + kit.getId() + " as it is already assigned or unusable");
            return kitRepository.save(kit);
        } catch (Exception intE ){
            log(LogCode.RESOURCE_CREATE_ERROR, "Cannot create kit because " + intE.getMessage(), user);
            throw new JdxServiceException("Cannot save Kit");
        }
    }


    public Kit updateKit(Kit kit, UserDetailsImpl user) throws JdxServiceException {
        if(kit != null) {
            Optional<Kit> update = kitRepository.findById(kit.getId());
            if(update.isPresent()) {
                List<Kit> anyOtherKits = null;
                if(kit.getPsdSleeveNumber() == null)
                    anyOtherKits = kitRepository.findKitsByCodeOrSampleNumber(kit.getCode(), kit.getSampleNumber());
                else
                    anyOtherKits = findKitsByKitCodeOrSampleNumberOrSleeveNumber(kit.getCode(), kit.getSampleNumber(), kit.getPsdSleeveNumber());

                try {
                    logger.info("other kits " + anyOtherKits.size() + " and update kit " + mapper.writeValueAsString(kit));
                } catch (Exception e) {e.printStackTrace();}

                if(anyOtherKits.size() > 1 || (anyOtherKits.size() == 1 && !anyOtherKits.get(0).getId().equals(kit.getId())))
                    throw new JdxServiceException(LaboratoryErrorCodes.KIT_CODES_ASSIGNED_ELSEWHERE_ON_UPDATE.code,
                            LaboratoryErrorCodes.KIT_CODES_ASSIGNED_ELSEWHERE_ON_UPDATE.statusCode,
                            LaboratoryErrorCodes.KIT_CODES_ASSIGNED_ELSEWHERE_ON_UPDATE.message,
                            "One or more of the package codes used in this kit updated are used in other kits");
//TODO can do this once we can support updating the kit barcode in LIMS
            //    if (kit.getCode() != null && !update.get().getCode().equals(kit.getCode()))
            //        update.get().setCode(kit.getCode());

                if (kit.getSampleNumber() != null && update.get().getSampleNumber() != null
                        && !update.get().getSampleNumber().equals(kit.getSampleNumber()))
                    update.get().setSampleNumber(kit.getSampleNumber());

                if(update.get().getPsdSleeveNumber() == null && kit.getPsdSleeveNumber() != null)
                    update.get().setPsdSleeveNumber(kit.getPsdSleeveNumber());

                else if (kit.getPsdSleeveNumber() != null
                        && !update.get().getPsdSleeveNumber().equals(kit.getPsdSleeveNumber()))
                    update.get().setPsdSleeveNumber(kit.getPsdSleeveNumber());

                if(kit.getType() != null) update.get().setType(kit.getType());
                if(!update.get().isAssigned())
                    update.get().setAssigned(true);

                update.get().setMeta(updateMeta(update.get().getMeta(), user));
                try {
                    return kitRepository.save(update.get());
                } catch (Exception e){
                    throw new JdxServiceException("Cannot update kit " + e.getMessage());
                }
            }
        }
        throw new JdxServiceException("Cannot update kit");
    }

    public void deleteKit(String kitId, UserDetailsImpl user) throws JdxServiceException {
        Optional<Kit> oKit = kitRepository.findById(kitId);

        try {
            if (oKit.isPresent()) {
                if (oKit.get().isAssigned())
                    throw new JdxServiceException("Cannot delete kit " + kitId + " as it is assigned");

                kitRepository.delete(oKit.get());

            } else {
                throw new JdxServiceException("Cannot delete kit");
            }
        } catch(Exception e){
            e.printStackTrace();
            throw new JdxServiceException("Cannot delete kit");
        }
    }
}
