package com.euler.platform.config;

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
     * 是否获取真实验证码
     */
    private Boolean isShowCode;


    /**
     * geetest  captchaId
     */
    private String geetestCaptchaId;

    /**
     * geetest  captchaKey
     */
    private String geetestCaptchaKey;

    /**
     * geetest  domain
     */
    private String geetestDomain;

    /**
     * 阿里短信签名
     */
    private String signName;
    /**
     * 阿里短信模板
     */
    private String smsTemplateCode;


}
