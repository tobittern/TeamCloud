package com.euler.common.payment.core;

import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

public interface Pay<T> {

    String defaultCharset = "UTF-8";

    /**
     * 支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    default TradeToken<String> pay(T trade) {
        return new TradeToken<String>() {
            @Override
            public String value() {
                return "";
            }

            @Override
            public Boolean success() {
                return true;
            }
        };
    }


    /**
     * 订单状态查询
     *
     * @param trade 订单
     * @return 订单状态
     */
    TradeResult query(T trade);

    /**
     * 退款
     *
     * @param trade 订单
     */
    TradeResult refund(T trade);

    /**
     * 退款
     *
     * @param trade 订单
     */
    TradeResult refundQuery(T trade);

    /**
     * 异步回调
     *
     * @param request  请求
     * @param response 输出
     * @param callable 业务回调
     */
    default void AsyncNotify(HttpServletRequest request, HttpServletResponse response, CallablePlus callable) {
    }


    /**
     * 退款异步回调
     *
     * @param request  请求
     * @param response 输出
     * @param callable 业务回调
     */
    default void AsyncRefundNotify(HttpServletRequest request, HttpServletResponse response, CallablePlus callable) {
    }


}
