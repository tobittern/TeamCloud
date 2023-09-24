package com.euler.common.payment.core;

import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;
import com.euler.common.payment.enums.AliTradeStatusEnum;
import com.euler.common.payment.enums.PayTypeEnumd;
import org.springframework.stereotype.Component;

@Component
public class WalletPay implements  Pay<Trade> {
    /**
     * 支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    @Override
    public TradeToken<String> pay(Trade trade) {
        return new TradeToken<String>() {
            @Override
            public String value() {
                return "";
            }

            @Override
            public Boolean success() {
                return true;
            }

            @Override
            public  String businessOrderId(){
                return  trade.getBusinessOrderId();
            }
        };
    }

    /**
     * 订单状态查询
     *
     * @param trade 订单
     * @return 订单状态
     */
    @Override
    public TradeResult query(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WalletQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        tradeResult.setTradeStatus(AliTradeStatusEnum.TRADE_SUCCESS.getCode());
        tradeResult.setSuccess(true);
        return tradeResult;
    }

    /**
     * 退款
     *
     * @param trade 订单
     */
    @Override
    public TradeResult refund(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WalletRefund);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        tradeResult.setTradeStatus(AliTradeStatusEnum.TRADE_SUCCESS.getCode());
        tradeResult.setSuccess(true);
        return tradeResult;
    }

    /**
     * 退款
     *
     * @param trade 订单
     */
    @Override
    public TradeResult refundQuery(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WalletRefund);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        tradeResult.setTradeStatus(AliTradeStatusEnum.REFUND_SUCCESS.getCode());
        tradeResult.setSuccess(true);
        return tradeResult;
    }

}
