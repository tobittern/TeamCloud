package com.euler.payment.bean;
import com.euler.payment.enums.ApplePayTypeEnumd;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AppleTradeResult {
    private String code;
    private String msg = "请求失败";
    private String subCode;
    private String subMsg;

    private boolean success = false;

    public AppleTradeResult(String payType) {
        this.payType = payType;
    }

    public AppleTradeResult(ApplePayTypeEnumd payTypeEnumd) {
        switch (payTypeEnumd) {
            case ApplePay:
            case AppleQuery:
                this.payType = "iap";
                this.payChannelType = "apple";
                this.businessType = 1;
                break;
            case AppleRefund:
            case AppleRefundQuery:
                this.payType = "iap";
                this.payChannelType = "apple";
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
