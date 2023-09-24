package com.euler.payment.core;

import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;
import com.euler.common.payment.core.Pay;
import com.euler.common.payment.enums.ApplePayStatusEnum;
import com.euler.payment.bean.AppleTradeResult;
import com.euler.payment.domain.PayOrder;
import com.euler.payment.enums.ApplePayTypeEnumd;
import com.euler.payment.service.IPayOrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ApplePay implements Pay<Trade> {

    @Autowired
    private IPayOrderService payOrderService;

    /**
     * 支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    @Override
    public TradeToken<String> pay(Trade trade) {
        if (trade.getPayType().equals("iap"))
            return applePay(trade);

        return new TradeToken<String>() {
            @Override
            public String value() {
                return "不支持的支付方式";
            }

            @Override
            public Boolean success() {
                return false;
            }
        };
    }

    /**
     * 苹果内购支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    @SneakyThrows
    private TradeToken<String> applePay(Trade trade) {
        return new TradeToken<String>() {
            @Override
            public String value() {
                return trade.getBusinessOrderId();
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
    @Override
    public TradeResult query(Trade trade) {
        AppleTradeResult tradeResult = new AppleTradeResult(String.valueOf(ApplePayTypeEnumd.AppleQuery));
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        try {
            // 订单状态查询
            PayOrder vo = payOrderService.getById(trade.getOutTradeNo());
            log.info("苹果内购支付--订单状态查询--苹果内购返回：result：{}，trade:{}", vo.getState(), JsonUtils.toJsonString(trade));
            if (vo != null) {
                // 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
                switch (vo.getState()) {
                    case 0:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.WAIT_BUYER_PAY.getCode());
                        break;
                    case 1:
                        // 判断是否超时未付款
                        if (new Date().after(vo.getExpiredTime())) {
                            tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_CLOSED.getCode());
                        } else {
                            tradeResult.setTradeStatus(ApplePayStatusEnum.WAIT_BUYER_PAY.getCode());
                        }
                        break;
                    case 2:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_SUCCESS.getCode());
                        break;
                    case 7:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_FINISHED.getCode());
                        break;
                    default:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_CLOSED.getCode());
                        break;
                }
            } else {
                tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            }
            tradeResult.setSuccess(true);
        } catch(Exception e) {
            log.info("苹果内购支付--订单状态查询--异常：trade:{}", JsonUtils.toJsonString(trade), e);
            tradeResult.setMsg(e.getMessage());
            tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            tradeResult.setSuccess(false);
        }

        TradeResult newTradeResult = BeanCopyUtils.copy(tradeResult, TradeResult.class);
        return newTradeResult;
    }

    /**
     * 退款
     *
     * @param trade 订单
     */
    @Override
    public TradeResult refund(Trade trade) {
        AppleTradeResult tradeResult = new AppleTradeResult(String.valueOf(ApplePayTypeEnumd.AppleRefund));
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        try {
            // 订单状态查询
            PayOrder vo = payOrderService.getById(trade.getOutTradeNo());
            log.info("苹果内购退款--订单状态查询--苹果内购返回：result：{}，trade:{}", vo.getState(), JsonUtils.toJsonString(trade));
            if(vo != null) {
                // 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
                switch (vo.getState()) {
                    case 5:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.REFUND_SUCCESS.getCode());
                        break;
                    default:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_CLOSED.getCode());
                        break;
                }
            } else {
                tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            }
            tradeResult.setSuccess(true);
        } catch(Exception e) {
            log.info("苹果内购退款--订单状态查询--异常：trade:{}", JsonUtils.toJsonString(trade), e);
            tradeResult.setMsg(e.getMessage());
            tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            tradeResult.setSuccess(false);
        }
        TradeResult newTradeResult = BeanCopyUtils.copy(tradeResult, TradeResult.class);
        return newTradeResult;
    }

    /**
     * 退款查询
     *
     * @param trade 订单
     * @return 订单状态
     */
    @Override
    public TradeResult refundQuery(Trade trade) {
        AppleTradeResult tradeResult = new AppleTradeResult(ApplePayTypeEnumd.AppleRefundQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        try {
            // 订单状态查询
            PayOrder vo = payOrderService.getById(trade.getOutTradeNo());
            log.info("苹果内购退款--订单状态查询--苹果内购返回：result：{}，trade:{}", vo.getState(), JsonUtils.toJsonString(trade));
            if(vo != null) {
                // 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
                switch (vo.getState()) {
                    case 5:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.REFUND_SUCCESS.getCode());
                        break;
                    default:
                        tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_CLOSED.getCode());
                        break;
                }
            } else {
                tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            }
            tradeResult.setSuccess(true);
        } catch(Exception e) {
            log.info("苹果内购退款--订单状态查询--异常：trade:{}", JsonUtils.toJsonString(trade), e);
            tradeResult.setMsg(e.getMessage());
            tradeResult.setTradeStatus(ApplePayStatusEnum.TRADE_NOT_EXIST.getCode());
            tradeResult.setSuccess(false);
        }
        TradeResult newTradeResult = BeanCopyUtils.copy(tradeResult, TradeResult.class);
        return newTradeResult;
    }

}
