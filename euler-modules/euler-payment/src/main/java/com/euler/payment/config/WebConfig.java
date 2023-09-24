package com.euler.payment.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig")
public class WebConfig {
    /**
     * 订单过期时间，分钟
     */
    private  Integer orderExpiredTime;

    /**
     * 通知游戏方过期时间，毫秒
     */
    private  Integer notifyGameTimeout;
}
