package com.euler.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "apple.pay")
public class ApplePayConfig {

    /**
     * 密码
     */
    private String password;

}
