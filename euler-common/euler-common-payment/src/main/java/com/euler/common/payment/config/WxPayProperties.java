package com.euler.common.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * wxpay pay properties.
 *
 * @author Binary Wang
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * apiV3 秘钥值
     */
    private String apiV3Key;

    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateCertPath;

    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径
     */
    private String privateKeyPath;

    /**
     * 该链接是通过基础下单接口中的请求参数“notify_url”来设置的，要求必须为https地址。
     * 请确保回调URL是外部可正常访问的，且不能携带后缀参数，否则可能导致商户无法接收到微信的回调通知信息。
     * 回调URL示例：“https://pay.weixin.qq.com/wxpay/pay.action”
     */
    private String notifyUrl;


    private String refundNotifyUrl;

    /**
     * H5支付的appid
     */
    private String appIdH5;

    /**
     * 支付场景
     */
    private  String sceneInfo;

}
