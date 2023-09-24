package com.euler.common.payment.enums;

import com.euler.common.payment.core.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储服务商枚举
 *
 * @author platform
 */
@Getter
@AllArgsConstructor
public enum PayTypeEnumd {

    AliMobilePay("ali_MobilePay", "支付宝app","ali","app", AliPay.class),
    AliWebMobilePay("ali_WebMobilePay", "支付宝h5","ali","h5", AliPay.class),
    AliQuery("ali_Query","", "ali","app", AliPay.class),
    AliRefund("ali_Refund", "","ali","app", AliPay.class),
    AliRefundQuery("ali_RefundQuery", "","ali","app", AliPay.class),
    AliComplain("aliComplain","支付宝投诉处理", "ali","app", AliComplainService.class),


    WxMobilePay("wx_MobilePay", "微信app","wx","app", WxPay.class),
    WxWebMobilePay("wx_WebMobilePay","微信h5", "wx","h5", WxPay.class),
    WxQuery("wx_Query", "","wx","app", WxPay.class),
    WxRefund("wx_Refund", "","wx","app", WxPay.class),
    WxRefundQuery("wx_RefundQuery","", "wx","app", WxPay.class),
    WxComplain("wxComplain","微信投诉处理", "wx","app", WxComplainService.class),


    WalletBalance("wallet_balance", "钱包余额","wallet","app", WalletPay.class),
    WalletPlatform("wallet_platform", "钱包平台币","wallet","app", WalletPay.class),
    WalletQuery("wallet_Query", "","wallet","app", WalletPay.class),
    WalletRefund("wallet_Refund", "","wallet","app", WalletPay.class),
    WalletRefundQuery("wallet_RefundQuery","", "wx","app", WalletPay.class),

    ;

    private final String value;
    private final String name;
    private final String payChannelType;

    private final String payType;

    private final Class<?> beanClass;


    public static PayTypeEnumd find(String value) {
        for (PayTypeEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
