package com.sise.graduation.config.common.auth;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "web.auth.login")
public class WebAuthConfig {

    private String indexUrl;

    private String loginUrl;

    @Value("${web.auth.rsa-public}")
    private String rsaPublicKey;

    @Value("${web.auth.rsa-private}")
    private String rsaPrivateKey;

    @Value("${web.auth.use-encode}")
    private Boolean isEncodePassword;
}