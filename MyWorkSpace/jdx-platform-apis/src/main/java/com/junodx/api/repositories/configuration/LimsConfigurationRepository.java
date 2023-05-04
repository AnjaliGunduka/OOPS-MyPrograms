package com.junodx.api.repositories.configuration;

import com.junodx.api.models.configuration.lims.LIMSConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimsConfigurationRepository extends JpaRepository<LIMSConfigurationEntity, Long> {
}
