package com.euler.common.payment.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "ali.pay")
public class AliPayConfig {

    /**
     * 应用ID
     */
    private String appid;
    /**
     * 商户的私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private  String publicKey;

    /**
     * 支付回调地址
     */
    private String notifyUrl;

    /**
     * 同步跳转地址
     */
    private  String returnUrl;
    /**
     * 支付宝网关
     */
    private String gateway;


    /**
     * 内容加密秘钥
     */
    private  String aesKey;


}
