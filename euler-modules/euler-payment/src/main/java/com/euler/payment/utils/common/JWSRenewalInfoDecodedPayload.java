package com.euler.payment.utils.common;

import com.euler.payment.utils.enums.Environment;
import lombok.Data;

/**
 * JSON Web签名（JWS）格式的订阅续订信息
 */
@Data
public class JWSRenewalInfoDecodedPayload {

    /**
     * 下一个计费周期续订的产品的产品标识符
     */
    private String autoRenewProductId;
    /**
     * 自动续订订阅的续订状态
     */
    private Integer autoRenewStatus;
    /**
     * 服务器环境（沙箱环境、生产环境）
     */
    private String environment;
    /**
     * 订阅过期的原因:
     *  1: 客户取消了订阅
     *  2: 计费错误；例如，客户的付款信息不再有效
     *  3: 客户不同意需要客户同意的自动可再生订阅价格上涨，导致订阅到期
     *  4: 续订时无法购买该产品
     *  5: 由于其他原因，订阅已过期)
     */
    private Integer expirationIntent;
    /**
     * 续订订阅的计费宽限期到期的时间（以毫秒timestamp为单位）
     */
    private Long gracePeriodExpiresDate;
    /**
     * 是否正在尝试自动续订过期订阅
     */
    private Boolean isInBillingRetryPeriod;
    /**
     * 优惠代码或促销优惠标识
     */
    private String offerIdentifier;
    /**
     * 促销优惠的类型
     *  1：介绍性报价
     *  2：促销优惠
     *  3：带有订阅优惠代码的优惠
     */
    private Integer offerType;
    /**
     * 购买的原始交易标识符
     */
    private String originalTransactionId;
    /**
     * 是否会涨价的状态
     *  0：客户尚未对订阅价格上涨做出回应
     *  1：需要客户同意的订阅价格上涨，或者应用商店已通知客户不需要同意的自动再生订阅价格上涨
     */
    private Integer priceIncreaseStatus;
    /**
     * 购买的产品标识符
     */
    private String productId;
    /**
     * 签名的UNIX时间（以毫秒timestamp为单位）
     */
    private Long signedDate;

}
