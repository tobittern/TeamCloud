package com.euler.risk.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig")
public class WebConfig {
    private String behaviorExchange;
    private String behaviorRoutingkey;
    private String behaviorQueue;
}
