package com.junodx.api.models.configuration;

import com.junodx.api.models.configuration.lims.LIMSConfigurationEntity;
import com.junodx.api.repositories.configuration.LimsConfigurationRepository;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ClientConfiguration {
    @Autowired
    private LimsConfigurationRepository limsConfigurationRepository;

    @Bean
    public LIMSConfigurationEntity limsConfiguration() {
        LIMSConfigurationEntity entity = null;

        List<LIMSConfigurationEntity> limsConfigurations = limsConfigurationRepository.findAll();
        if(limsConfigurations.size() == 0)
            return null;
        else if(limsConfigurations.size() > 1)
            return null;
        else
            entity = limsConfigurations.get(0);

        return entity;
    }
}
