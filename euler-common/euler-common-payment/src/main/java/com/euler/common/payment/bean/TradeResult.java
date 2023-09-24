package com.euler.common.payment.bean;

import com.euler.common.payment.enums.PayTypeEnumd;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TradeResult {
    private String code;
    private String msg = "请求失败";
    private String subCode;
    private String subMsg;

    private boolean success = false;

    public TradeResult(String payType) {
        this.payType = payType;
    }

    public TradeResult(PayTypeEnumd payTypeEnumd) {
        switch (payTypeEnumd) {
            case AliMobilePay:
            case AliQuery:
                this.payType = "app";
                this.payChannelType = "ali";
                this.businessType = 1;
                break;
            case AliWebMobilePay:
                this.payType = "h5";
                this.payChannelType = "ali";
                this.businessType = 1;
                break;
            case AliRefund:
            case AliRefundQuery:
                this.payType = "app";
                this.payChannelType = "ali";
                this.businessType = 2;
                break;
            case WxMobilePay:
            case WxQuery:
                this.payType = "app";
                this.payChannelType = "wx";
                this.businessType = 1;
                break;
            case WxWebMobilePay:
                this.payType = "h5";
                this.payChannelType = "wx";
                this.businessType = 1;
                break;
            case WxRefund:
            case WxRefundQuery:
                this.payType = "app";
                this.payChannelType = "wx";
                this.businessType = 2;
                break;

            case WalletBalance:
            case WalletQuery:
            case WalletPlatform:
                this.payType = "app";
                this.payChannelType = "wallet";
                this.businessType = 1;
                break;
            case WalletRefund:
            case WalletRefundQuery:
                this.payType = "app";
                this.payChannelType = "wallet";
                this.businessType = 2;
                break;

        }
    }

    /**
     * 支付渠道订单号
     */
    private String tradeNo;

    /**
     * 支付渠道退款订单号
     */
    private String refundTradeNo;

    /**
     * 支付账号
     */
    private String buyerLogonId;
    /**
     * 支渠道订单状态
     */
    private String tradeStatus;
    /**
     * 业务订单号
     */
    private String outTradeNo;
    /**
     * 业务退款单号
     */
    private String outBizNo;

    /**
     * 支付方式，app,h5,iap
     */
    private String payType;

    /**
     * 支付渠道类型,wx,ali,wallet,apple
     */
    private String payChannelType;

    /**
     * 业务类型，1：支付，2：退款，3：转账
     */
    private Integer businessType;

}
