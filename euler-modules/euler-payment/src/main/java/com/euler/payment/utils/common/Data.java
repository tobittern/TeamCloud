package com.euler.payment.utils.common;

import com.euler.payment.utils.enums.Environment;

@lombok.Data
public class Data {

    /**
     * 苹果应用的唯一标识符，不存在于沙箱环境中
     */
    private String appAppleId;

    /**
     * 捆绑标识符
     */
    private String bundleId;

    /**
     * 捆绑迭代版本
     */
    private String bundleVersion;

    /**
     * 服务器环境（沙箱环境、生产环境）
     */
    private Environment environment;

    /**
     * JSON Web签名（JWS）格式的订阅续订信息
     */
    private String signedRenewalInfo;

    /**
     * JSON Web签名（JWS）格式的交易信息
     */
    private String signedTransactionInfo;

}
