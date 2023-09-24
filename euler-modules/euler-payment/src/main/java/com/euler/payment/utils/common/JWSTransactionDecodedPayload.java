package com.euler.payment.utils.common;

import lombok.Data;

/**
 * JSON Web签名（JWS）格式的交易信息
 */
@Data
public class JWSTransactionDecodedPayload {
    /**
     * app账号的token, 用于将事务与您自己服务上的用户相关联
     */
    private String appAccountToken;
    /**
     * 捆绑包标识符
     */
    private String bundleId;
    /**
     * 服务器环境（沙箱环境、生产环境）
     */
    private String environment;
    /**
     * 订阅过期或续订的UNIX时间（以毫秒为单位
     */
    private Long expiresDate;
    /**
     * 描述交易是由用户购买的，还是通过家庭共享可供用户使用
     */
    private String inAppOwnershipType;
    /**
     * 是否升级到另一个订阅
     */
    private Boolean isUpgraded;
    /**
     * 促销代码或促销优惠标的标识符
     */
    private String offerIdentifier;
    /**
     * 促销优惠类型的值
     *  1：介绍性报价
     *  2：促销优惠
     *  3：带有订阅优惠代码的优惠
     */
    private Integer offerType;
    /**
     * 原始交易的购买时间（以毫秒timestamp为单位）
     */
    private Long originalPurchaseDate;
    /**
     * 购买的原始交易标识符
     */
    private String originalTransactionId;
    /**
     * 购买的产品标识符
     */
    private String productId;
    /**
     * 购买时间
     */
    private Long purchaseDate;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 撤销事务的UNIX时间（以毫秒timestamp为单位）
     */
    private Long revocationDate;
    /**
     * 签名的UNIX时间（以毫秒timestamp为单位）
     */
    private Long signedDate;
    /**
     * 订阅所属的订阅组的标识符
     */
    private String subscriptionGroupIdentifier;
    /**
     * 购买的交易标识符
     */
    private String transactionId;
    /**
     * 购买的类型
     *  Auto-Renewable Subscription：自动续订
     *  Non-Consumable：非消耗品购买
     *  Consumable：消耗品购买
     *  Non-Renewing Subscription：不续费的订阅
     */
    private String type;
    /**
     * 跨设备的订阅购买事件的唯一标识符，包括订阅续订
     */
    private String webOrderLineItemId;

}
