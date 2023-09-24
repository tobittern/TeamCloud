package com.euler.sdk.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "authentication")
public class Authentication {
    private String appId;
    private String SecretKey;
    private String bizId;
    private String sendUrl;
}
