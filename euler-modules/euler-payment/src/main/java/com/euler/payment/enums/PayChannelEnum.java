package com.euler.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付渠道
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {
    ALI("ali","支付宝"),
    WX("wx","微信"),
    WALLET("wallet","钱包"),
    APPLE("apple","苹果内购"),
    ;

    private  String payChannel;
    private  String payChannelName;

    public static PayChannelEnum find(String value) {
        for (PayChannelEnum enumd : values()) {
            if (enumd.getPayChannel().equals(value)) {
                return enumd;
            }
        }
        return null;
    }
}
