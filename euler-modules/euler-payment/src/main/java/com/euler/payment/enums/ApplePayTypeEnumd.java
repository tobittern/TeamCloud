package com.euler.payment.enums;

import com.euler.payment.core.ApplePay;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储服务商枚举
 *
 * @author platform
 */
@Getter
@AllArgsConstructor
public enum ApplePayTypeEnumd {

    ApplePay("apple_iap", "苹果内购","apple","iap", ApplePay.class),
    AppleQuery("apple_Query","", "apple","iap", ApplePay.class),
    AppleRefund("apple_Refund", "","apple","iap", ApplePay.class),
    AppleRefundQuery("apple_RefundQuery", "","apple","iap", ApplePay.class),

    ;

    private final String value;
    private final String name;
    private final String payChannelType;

    private final String payType;

    private final Class<?> beanClass;


    public static ApplePayTypeEnumd find(String value) {
        for (ApplePayTypeEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
