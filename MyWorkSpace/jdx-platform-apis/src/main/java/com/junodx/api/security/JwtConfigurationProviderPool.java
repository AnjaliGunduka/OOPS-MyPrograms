package com.junodx.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "com.junodx.api.security.provider.jwt.aws"
)
public class JwtConfigurationProviderPool  extends JwtConfiguration {
}
