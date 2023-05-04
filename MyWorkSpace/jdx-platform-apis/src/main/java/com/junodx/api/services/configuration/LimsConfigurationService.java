package com.junodx.api.services.configuration;

import com.junodx.api.models.configuration.lims.LIMSConfigurationEntity;
import com.junodx.api.repositories.configuration.LimsConfigurationRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LimsConfigurationService extends ServiceBase {

    @Autowired
    private LimsConfigurationRepository limsConfigurationRepository;

    //get
    public LIMSConfigurationEntity get() throws JdxServiceException {
        try {
            List<LIMSConfigurationEntity> all = limsConfigurationRepository.findAll();
            if(all.size() == 0)
                return null;
            else if(all.size() > 1)
                return null;

            return all.get(0);
        } catch (Exception e){
            throw new JdxServiceException("Cannot find LIMS configuration: " + e.getMessage());
        }
    }

    //upsert
    public LIMSConfigurationEntity save(LIMSConfigurationEntity entity) throws JdxServiceException {
        try {
            if(entity == null)
                throw new JdxServiceException("Cannot save LIMS entity configuration as the payload is not present");

            LIMSConfigurationEntity existing = this.get();
            if(existing != null) {
                //update the existing configuration
                if(StringUtils.isNotBlank(entity.getDistributorId()) && !entity.getDistributorId().equals(existing.getDistributorId()))
                    existing.setDistributorId(entity.getDistributorId());

                if(StringUtils.isNotBlank(entity.getHostCode()) && !entity.getHostCode().equals(existing.getHostCode()))
                    existing.setHostCode(entity.getHostCode());

                if(entity.getReportTypes() != null && existing.getReportTypes() != null && entity.getReportTypes().size() != existing.getReportTypes().size())
                    existing.setReportTypes(entity.getReportTypes());

                if(existing.getApiClientConfiguration() == null)
                    existing.setApiClientConfiguration(entity.getApiClientConfiguration());

                if(entity.getApiClientConfiguration() != null) {
                    if(existing.getApiClientConfiguration().getApiToken() == null)
                        existing.getApiClientConfiguration().setApiPath(entity.getApiClientConfiguration().getApiToken());
                    else if(StringUtils.isNotBlank(entity.getApiClientConfiguration().getApiPath())
                            && existing.getApiClientConfiguration().getApiToken() != null
                            && !entity.getApiClientConfiguration().getApiPath().equals(existing.getApiClientConfiguration().getApiPath()))
                        existing.getApiClientConfiguration().setApiPath(entity.getApiClientConfiguration().getApiToken());

                    if(existing.getApiClientConfiguration().getApiVersion() == null)
                        existing.getApiClientConfiguration().setApiVersion(entity.getApiClientConfiguration().getApiVersion());
                    else if(StringUtils.isNotBlank(entity.getApiClientConfiguration().getApiVersion())
                            && existing.getApiClientConfiguration().getApiVersion() != null
                            && !entity.getApiClientConfiguration().getApiVersion().equals(existing.getApiClientConfiguration().getApiVersion()))
                        existing.getApiClientConfiguration().setApiPath(entity.getApiClientConfiguration().getApiVersion());

                    if(existing.getApiClientConfiguration().getApiBaseUrl() == null)
                        existing.getApiClientConfiguration().setApiBaseUrl(entity.getApiClientConfiguration().getApiBaseUrl());
                    else if(StringUtils.isNotBlank(entity.getApiClientConfiguration().getApiBaseUrl())
                            && existing.getApiClientConfiguration().getApiBaseUrl() != null
                            && !entity.getApiClientConfiguration().getApiBaseUrl().equals(existing.getApiClientConfiguration().getApiBaseUrl()))
                        existing.getApiClientConfiguration().setApiVersion(entity.getApiClientConfiguration().getApiBaseUrl());

                    if(existing.getApiClientConfiguration().getApiPath() == null)
                        existing.getApiClientConfiguration().setApiPath(entity.getApiClientConfiguration().getApiPath());
                    else if(StringUtils.isNotBlank(entity.getApiClientConfiguration().getApiPath())
                            && existing.getApiClientConfiguration().getApiPath() != null
                            && !entity.getApiClientConfiguration().getApiPath().equals(existing.getApiClientConfiguration().getApiPath()))
                        existing.getApiClientConfiguration().setApiPath(entity.getApiClientConfiguration().getApiPath());
                }

                if(existing.getWebhookServerConfiguration() == null)
                    existing.setWebhookServerConfiguration(entity.getWebhookServerConfiguration());

                if(entity.getWebhookServerConfiguration() != null){
                    if(existing.getWebhookServerConfiguration().getWebhookToken() == null)
                        existing.getWebhookServerConfiguration().setWebhookToken(entity.getWebhookServerConfiguration().getWebhookToken());
                    else if(StringUtils.isNotBlank(entity.getWebhookServerConfiguration().getWebhookToken())
                        && existing.getWebhookServerConfiguration().getWebhookToken() != null
                        && !entity.getWebhookServerConfiguration().getWebhookToken().equals(existing.getWebhookServerConfiguration().getWebhookToken()))
                        existing.getWebhookServerConfiguration().setWebhookToken(entity.getWebhookServerConfiguration().getWebhookToken());

                    if(existing.getWebhookServerConfiguration().getWebhookId() == null)
                        existing.getWebhookServerConfiguration().setWebhookId(entity.getWebhookServerConfiguration().getWebhookId());
                    else if(StringUtils.isNotBlank(entity.getWebhookServerConfiguration().getWebhookId())
                            && existing.getWebhookServerConfiguration().getWebhookId() != null
                            && !entity.getWebhookServerConfiguration().getWebhookId().equals(existing.getWebhookServerConfiguration().getWebhookId()))
                        existing.getWebhookServerConfiguration().setWebhookId(entity.getWebhookServerConfiguration().getWebhookId());

                    if(existing.getWebhookServerConfiguration().getWebhookHashingAlgorithm() == null)
                        existing.getWebhookServerConfiguration().setWebhookHashingAlgorithm(entity.getWebhookServerConfiguration().getWebhookHashingAlgorithm());
                    else if(StringUtils.isNotBlank(entity.getWebhookServerConfiguration().getWebhookHashingAlgorithm())
                            && existing.getWebhookServerConfiguration().getWebhookHashingAlgorithm() != null
                            && !entity.getWebhookServerConfiguration().getWebhookHashingAlgorithm().equals(existing.getWebhookServerConfiguration().getWebhookHashingAlgorithm()))
                        existing.getWebhookServerConfiguration().setWebhookHashingAlgorithm(entity.getWebhookServerConfiguration().getWebhookHashingAlgorithm());
                }
                return limsConfigurationRepository.save(existing);
            } else {
                //we have a new configuration, save this one
                return limsConfigurationRepository.save(entity);
            }

        } catch (Exception e){
            throw new JdxServiceException("Cannot find LIMS configuration: " + e.getMessage());
        }
    }
}
