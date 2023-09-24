package com.euler.common.payment.bean;

/**
 * 订单凭证
 *
 * @Auther: dqw
 */
public interface TradeToken<T> {

    /**
     * 凭证内容
     *
     * @return
     */
    T value();

    default String businessOrderId() {
        return "";
    }

    Boolean success();

}
