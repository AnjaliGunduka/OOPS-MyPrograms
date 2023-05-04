package com.junodx.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "com.junodx.api.security.lab.jwt.aws"
)
public class JwtConfigurationLabPool extends JwtConfiguration {

}
