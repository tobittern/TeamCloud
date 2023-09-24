package com.euler.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ali.sms")
public class SmsConfig {

    private  String aliAccessKey;
    private  String aliSecret;
    private  String regionId;
}
