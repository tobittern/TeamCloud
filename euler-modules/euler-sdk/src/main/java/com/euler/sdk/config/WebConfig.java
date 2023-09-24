package com.euler.sdk.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig")
public class WebConfig {
    private Integer canPlayAge;
    private String commonAesKey;
    /**
     * 云存储域名
     */
    private String yunDomain;
    /**
     * 是否开启调试模式
     */
    private Boolean isDebug;
    /**
     * 是否开启会员注销功能，true：开启，false：关闭
     */
    private Boolean isOpenCancellation;

    private String tempToken;


    /**
     * 游戏信息上报交换器
     */
    private String gameUserExchange;
    /**
     * 游戏信息上报Rk
     */
    private String gameUserRoutingkey;
    /**
     * 游戏信息上报队列
     */
    private String gameUserQueue;
}
