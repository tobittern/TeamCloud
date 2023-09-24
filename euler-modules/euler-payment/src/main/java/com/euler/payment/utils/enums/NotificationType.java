package com.euler.payment.utils.enums;

public enum NotificationType {
    /** 消费请求 */
    CONSUMPTION_REQUEST,
    /** 订阅更改后更新 */
    DID_CHANGE_RENEWAL_PREF,
    /** 订阅更改后更新状态 */
    DID_CHANGE_RENEWAL_STATUS,
    /** 订阅失败，进入计费重试期 */
    DID_FAIL_TO_RENEW,
    /** 订阅已成功续订 */
    DID_RENEW,
    /** 订阅已过期 */
    EXPIRED,
    /** 计费宽限期已过期 */
    GRACE_PERIOD_EXPIRED,
    /** 兑换了促销优惠或优惠代码 */
    OFFER_REDEEMED,
    /** 价格上涨 */
    PRICE_INCREASE,
    /** 退款 */
    REFUND,
    /** 退款拒绝 */
    REFUND_DECLINED,
    /** 续订扩展 */
    RENEWAL_EXTENDED,
    /** 续订延期 */
    RENEWAL_EXTENSION,
    /** 撤销 */
    REVOKE,
    /** 订阅 */
    SUBSCRIBED,
    /** 测试 */
    TEST
}
