package com.euler.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "webconfig")
public class WebConfig {



    /**
     * 是否调试环境
     */
    private Boolean isDebug;


    /**
     * 是否校验签名
     */
    private Boolean  isCheckSign;


    /**
     * 是否打印签名日志
     */
    private  Boolean signLog;


    /**
     * 签名过期时间,秒
     */
    private Integer tickExpireTime;

    /**
     * url是否只可访问一次
     */
    private Boolean callOnce;

    /**
     * 是否上报用户数据
     */
    private  Boolean isSubmitBehavior;






}
