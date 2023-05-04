package com.junodx.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "com.junodx.api.security.patient.jwt.aws"
)
public class JwtConfigurationPatientPool  extends JwtConfiguration {
}
