package com.junodx.api.services.commerce;

import com.junodx.api.controllers.commerce.DiscountCodeErrorCodes;
import com.junodx.api.controllers.commerce.payloads.DiscountCodeValidationPayload;
import com.junodx.api.models.commerce.Discount;
import com.junodx.api.models.commerce.DiscountCode;
import com.junodx.api.models.commerce.types.DiscountMode;
import com.junodx.api.repositories.commerce.DiscountCodeRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiscountCodeService extends ServiceBase {

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    private static final Logger logger = LoggerFactory.getLogger(DiscountCodeService.class);

    public DiscountCode upsert(DiscountCode discountCode, UserDetailsImpl updater) throws JdxServiceException {
        if (discountCode == null)
            throw new JdxServiceException(DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.code, DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.statusCode, DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.message, "Cannot find discount code data to create discount entity");

        Optional<DiscountCode> foundCode = null;

         if (discountCode.getId() != null)
            foundCode = discountCodeRepository.findById(discountCode.getId());

        if ((foundCode == null || foundCode.isEmpty() && discountCode.getCode() != null))
            foundCode = discountCodeRepository.findDiscountCodeByCode(discountCode.getCode());

        if (foundCode.isPresent()) {

            if (discountCode.getActive() != null && !discountCode.getActive().equals(foundCode.get().getActive()))
                foundCode.get().setActive(discountCode.getActive());

            if (discountCode.getExpires() != null && !discountCode.getExpires().equals(foundCode.get().getExpires()))
                foundCode.get().setExpires(discountCode.getExpires());

            if (discountCode.getValidFrom() != null && !discountCode.getValidFrom().equals(foundCode.get().getValidFrom()))
                foundCode.get().setValidFrom(discountCode.getValidFrom());

            if(discountCode.getDiscount() != null) {
                foundCode.get().getDiscount().setAmountDiscounted(discountCode.getDiscount().getAmountDiscounted());
                if(discountCode.getDiscount().getMode() != null && !discountCode.getDiscount().getMode().equals(foundCode.get().getDiscount().getMode()))
                    foundCode.get().getDiscount().setMode(discountCode.getDiscount().getMode());
                if(discountCode.getDiscount().getType() != null && !discountCode.getDiscount().getType().equals(foundCode.get().getDiscount().getType()))
                    foundCode.get().getDiscount().setType(discountCode.getDiscount().getType());
            }

            foundCode.get().setMeta(updateMeta(foundCode.get().getMeta(), updater));

            //We won't change Id, code, ownerId nor OwnerClientId in this upsert. Only upon creation. We'll deal with entity assignment in another method
            return discountCodeRepository.save(foundCode.get());
        } else {
            if (discountCode.getOwnerId() == null)
                discountCode.setOwnerId(updater.getUserId());
            if (discountCode.getOwningClientId() == null)
                discountCode.setOwningClientId(updater.getClientId());

            discountCode.setMeta(buildMeta(updater));

            return discountCodeRepository.save(discountCode);
        }
    }

    public Optional<DiscountCode> get(Long id) throws JdxServiceException {
        return discountCodeRepository.findById(id);
    }
 
    public List<DiscountCode> getAll() throws JdxServiceException {
        return discountCodeRepository.findAll();
    }

    public void delete(Long id) throws JdxServiceException {
        if(id == null)
            throw new JdxServiceException(DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.code,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.statusCode,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.message,
                    "DiscountCode id is not provided for deletion");

        Optional<DiscountCode> discountCode = discountCodeRepository.findById(id);
        if(discountCode.isEmpty())
            throw new JdxServiceException(DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.code,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.statusCode,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.message,
                    "DiscountCode cannot be found with id " + id);

        discountCodeRepository.delete(discountCode.get());
    }

    public DiscountCodeValidationPayload validateCode(String code, float amount) throws JdxServiceException {
        DiscountCodeValidationPayload response = new DiscountCodeValidationPayload();
        if(code == null)
            throw new JdxServiceException(DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.code,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.statusCode,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.message,
                    "DiscountCode cannot be found with code " + code);

        Optional<DiscountCode> discountCode = discountCodeRepository.findDiscountCodeByCode(code);
        if(discountCode.isEmpty())
            throw new JdxServiceException(DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.code,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_CREATION_ERROR.statusCode,
                    DiscountCodeErrorCodes.DISCOUNT_CODE_DELETION_ERROR.message,
                    "DiscountCode cannot be found with code " + code);

        response.setOriginalAmount(amount);
        response.setCode(code);
        response.setDiscountConfiguration(discountCode.get());

        //If this code isn't active
        if(!discountCode.get().getActive()) {
            response.setValid(false);
            response.setRevisedAmount(amount);
            response.setDiscountApplied(0.0f);
        }

        //If this code is valid after now, i.e. starts later
        if(discountCode.get().getValidFrom().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            response.setValid(false);
            response.setRevisedAmount(amount);
            response.setDiscountApplied(0.0f); 
        }

        //If the code has already expired
        if(discountCode.get().getExpires().getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            response.setValid(false);
            response.setRevisedAmount(amount);
            response.setDiscountApplied(0.0f);
        }

        if(discountCode.get().getDiscount().getMode().equals(DiscountMode.PERCENT)) {
            float deductibleAmount = (float) (1.0 - (discountCode.get().getDiscount().getAmountDiscounted() / 100));
            response.setRevisedAmount(amount * deductibleAmount);
            response.setDiscountApplied(discountCode.get().getDiscount().getAmountDiscounted());
            response.setDiscountMode(discountCode.get().getDiscount().getMode());
        } else {
            float deductibleAmount = amount - discountCode.get().getDiscount().getAmountDiscounted();
            if(deductibleAmount < 0)
                deductibleAmount = 0;

            response.setRevisedAmount(deductibleAmount);
            response.setDiscountMode(discountCode.get().getDiscount().getMode());
            response.setDiscountApplied(discountCode.get().getDiscount().getAmountDiscounted());
        }

        response.setValid(true);

        return response;
    }
}
