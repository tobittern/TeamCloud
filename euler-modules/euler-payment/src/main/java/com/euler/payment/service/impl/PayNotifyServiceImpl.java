package com.euler.payment.service.impl;

import com.baomidou.lock.LockInfo;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.config.PayMqConfig;
import com.euler.common.payment.core.PayNotifySevice;
import com.euler.common.payment.enums.*;
import com.euler.common.payment.utils.PayUtils;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.redis.utils.LockHelper;
import com.euler.payment.domain.*;
import com.euler.payment.domain.dto.PayChannelDto;
import com.euler.payment.enums.ApplePayTypeEnumd;
import com.euler.payment.enums.PayChannelEnum;
import com.euler.payment.service.*;
import com.euler.payment.utils.PayChannelUtils;
import com.euler.sdk.api.RemoteMemberService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PayNotifyServiceImpl implements PayNotifySevice {

    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IBusinessOrderService businessOrderService;
    @Autowired
    private IRefundOrderService refundOrderService;
    @Autowired
    private IOrderLogService orderLogService;
    @Autowired
    private INotifyRecordService notifyRecordService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private PayChannelUtils payChannelUtils;
    @Autowired
    private LockHelper lockHelper;
    @Autowired
    private PayMqConfig payMqConfig;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private PayUtils payUtils;

    @Override
    public void payStatus(TradeResult tradeResult) {
        log.info("统一修改订单状态：{}", JsonUtils.toJsonString(tradeResult));

        if (StringUtils.isEmpty(tradeResult.getOutTradeNo())) {
            log.info("统一修改订单状态--订单号为空，不支持修改订单操作");
            return;
        }
        String businessOrderId = tradeResult.getOutTradeNo().substring(1);
        String lockKey = StringUtils.format("{}lock:orderstate:{}", Constants.BASE_KEY, businessOrderId);
        LockInfo lockInfo = lockHelper.lock(lockKey, 3, 2000L);
        try {
            if (null == lockInfo) {
                log.info("统一修改订单状态--订单状态操作锁定中，订单号：{},支付渠道单号：{}", tradeResult.getOutTradeNo(), tradeResult.getTradeNo());
                return;
            }
            var payOrder = payOrderService.getById(tradeResult.getOutTradeNo());
            var businessOrder = businessOrderService.getById(businessOrderId);
            if (payOrder == null || businessOrder == null) {
                log.info("统一修改订单状态--系统订单不存在，订单号：{},支付渠道单号：{}", tradeResult.getOutTradeNo(), tradeResult.getTradeNo());
                return;
            }

            // 判断是否是苹果内购
            if(StringUtils.equals("apple_iap", payOrder.getPayChannel())) {
                ApplePayTypeEnumd applePayTypeEnumd = ApplePayTypeEnumd.find(payOrder.getPayChannel());
                switch (applePayTypeEnumd) {
                    // 苹果内购
                    case ApplePay:
                        doAppleStatus(tradeResult, businessOrder, payOrder);
                        break;
                }
            } else {
                //获取订单支付方式
                PayTypeEnumd payTypeEnumd = PayTypeEnumd.find(payOrder.getPayChannel());
                switch (payTypeEnumd) {
                    // 微信回调
                    case WxMobilePay:
                    case WxWebMobilePay:
                        doWxStatus(tradeResult, businessOrder, payOrder);
                        break;
                    // 支付宝回调
                    case AliMobilePay:
                    case AliWebMobilePay:
                    case WalletPlatform:
                    case WalletBalance:
                        doAliStatus(tradeResult, businessOrder, payOrder);
                        break;
                }
            }
        } finally {
            //释放锁
            lockHelper.unLock(lockInfo);
        }
    }

    /**
     * 支付宝回调
     *
     * @param tradeResult
     * @param businessOrder
     * @param payOrder
     */
    public void doAliStatus(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
        String msgId = "ali_pay_" + tradeResult.getOutTradeNo();
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", businessOrder.getMemberId());
        map.put("good_id", tradeResult.getOutTradeNo());
        map.put("good_name", businessOrder.getGoodsName());
        map.put("price", businessOrder.getOrderAmount());
        map.put("device_info", deviceInfo);

        AliTradeStatusEnum aliTradeStatusEnum = AliTradeStatusEnum.find(tradeResult.getTradeStatus());
        if (aliTradeStatusEnum != null) {
            switch (aliTradeStatusEnum) {
                case TRADE_CLOSED:
                    //退款单存在，则是退款通知
                    if (StringUtils.isNotEmpty(tradeResult.getOutBizNo()))
                        doRefundSuccess(tradeResult, businessOrder, payOrder);
                    else
                        doPayClose(tradeResult, businessOrder, payOrder);

                        // 校验游戏配置里是否配置了事件广播
                        if(payUtils.checkIsOpenBroadcast()) {
                            map.put("status", "faill");
                            // 支付失败的订单，在消息队列里发送一条失败的消息
                            rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                        }
                    break;
                case TRADE_SUCCESS:
                    doPaySuccess(tradeResult, businessOrder, payOrder);

                    // 校验游戏配置里是否配置了事件广播
                    if(payUtils.checkIsOpenBroadcast()) {
                        map.put("status", "success");
                        // 支付成功的订单，在消息队列里发送一条成功的消息
                        rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                    }
                    break;
                case REFUND_SUCCESS:
                    doRefundSuccess(tradeResult, businessOrder, payOrder);
                    break;
                case TRADE_FINISHED:
                    doFinshOrder(tradeResult, businessOrder, payOrder);

                    break;
                case WAIT_BUYER_PAY:
                case TRADE_NOT_EXIST:
                    doPayClose(tradeResult, businessOrder, payOrder);
                    break;
            }

        }
    }

    /**
     * 微信回调
     *
     * @param tradeResult
     * @param businessOrder
     * @param payOrder
     */
    public void doWxStatus(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
        Map<String, Object> map = new HashMap<>();
        String msgId = "wx_pay_" + tradeResult.getOutTradeNo();
        map.put("member_id", businessOrder.getMemberId());
        map.put("good_id", tradeResult.getOutTradeNo());
        map.put("good_name", businessOrder.getGoodsName());
        map.put("price", businessOrder.getOrderAmount());
        map.put("device_info", deviceInfo);
        //微信支付回调
        if (tradeResult.getBusinessType() == 1) {
            WxPayStatusEnum wxPayStatusEnum = WxPayStatusEnum.find(tradeResult.getTradeStatus());
            switch (wxPayStatusEnum) {
                case SUCCESS:
                    doPaySuccess(tradeResult, businessOrder, payOrder);

                    // 校验游戏配置里是否配置了事件广播
                    if(payUtils.checkIsOpenBroadcast()) {
                        map.put("status", "success");
                        // 支付成功的订单，在消息队列里发送一条成功的消息
                        rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                    }
                    break;
                case REFUND:
                    doRefundSuccess(tradeResult, businessOrder, payOrder);
                    break;
                case PAYERROR:
                    doPayFail(tradeResult, businessOrder, payOrder);
                    break;
                case REVOKED:
                case CLOSED:
                    doPayClose(tradeResult, businessOrder, payOrder);

                    // 校验游戏配置里是否配置了事件广播
                    if(payUtils.checkIsOpenBroadcast()) {
                        map.put("status", "fail");
                        // 支付失败的订单，在消息队列里发送一条失败的消息
                        rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                    }
                    break;
                case NOTPAY:
                case USERPAYING:
                    log.info("微信支付--异步回调--未支付，trade:{},", JsonUtils.toJsonString(tradeResult));
                    doPayClose(tradeResult, businessOrder, payOrder);
                    break;
            }

        } else {
            //微信退款回调
            WxRefundStatusEnum wxRefundStatusEnum = WxRefundStatusEnum.find(tradeResult.getTradeStatus());
            switch (wxRefundStatusEnum) {
                case SUCCESS:
                    doRefundSuccess(tradeResult, businessOrder, payOrder);
                    break;
                case CLOSED:
                    doPayClose(tradeResult, businessOrder, payOrder);
                    break;
                case ABNORMAL:
                    doRefundFail(tradeResult, businessOrder, payOrder);
                    break;
            }
        }

    }

    /**
     * 苹果内购
     *
     * @param tradeResult
     * @param businessOrder
     * @param payOrder
     */
    public void doAppleStatus(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
        String msgId = "apple_pay_success_" + tradeResult.getOutTradeNo();
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", businessOrder.getMemberId());
        map.put("good_id", tradeResult.getOutTradeNo());
        map.put("good_name", businessOrder.getGoodsName());
        map.put("price", businessOrder.getOrderAmount());
        map.put("device_info", deviceInfo);

        ApplePayStatusEnum applePayStatusEnum = ApplePayStatusEnum.find(tradeResult.getTradeStatus());
        if (applePayStatusEnum != null) {
            switch (applePayStatusEnum) {
                case TRADE_CLOSED:
                    if (StringUtils.isNotEmpty(tradeResult.getOutBizNo()))
                        doRefundSuccess(tradeResult, businessOrder, payOrder);
                    else
                        doPayClose(tradeResult, businessOrder, payOrder);

                        // 校验游戏配置里是否配置了事件广播
                        if(payUtils.checkIsOpenBroadcast()) {
                            map.put("status", "fail");
                            // 支付失败的订单，在消息队列里发送一条失败的消息
                            rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                        }
                    break;
                case TRADE_SUCCESS:
                    doPaySuccess(tradeResult, businessOrder, payOrder);

                    // 校验游戏配置里是否配置了事件广播
                    if(payUtils.checkIsOpenBroadcast()) {
                        map.put("status", "success");
                        // 支付成功的订单，在消息队列里发送一条成功的消息
                        rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                    }
                    break;
                case REFUND_SUCCESS:
                    doRefundSuccess(tradeResult, businessOrder, payOrder);
                    break;
                case TRADE_FINISHED:
                    doFinshOrder(tradeResult, businessOrder, payOrder);
                    break;
                case WAIT_BUYER_PAY:
                case TRADE_NOT_EXIST:
                    log.info("苹果内购--异步回调--未支付，trade:{},", JsonUtils.toJsonString(tradeResult));
                    doPayClose(tradeResult, businessOrder, payOrder);
                    break;
            }
        }
    }

    /**
     * 交易完成，无法退款
     *
     * @param tradeResult
     */
    @Transactional
    public void doFinshOrder(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--交易完成--开始，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());

        //交易状态为交易完成时，更新支付订单和业务订单状态
        //更新支付订单状态为交易完成
        if (!PayOrder.STATE_FINISH.equals(payOrder.getState())) {
            if (StringUtils.isEmpty(payOrder.getPayChannelOrderNo()))
                payOrder.setPayChannelOrderNo(tradeResult.getTradeNo());

            if (StringUtils.isEmpty(payOrder.getPayChannelUser()))
                payOrder.setPayChannelUser(tradeResult.getBuyerLogonId());

            payOrder.setState(PayOrder.STATE_FINISH);
            payOrderService.updatePayOrderInfo(payOrder);
            log.info("统一修改订单状态--交易完成--修改支付订单状态为交易完成，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }

        //更新业务订单状态为交易完成
        if (!BusinessOrder.ORDER_STATE_SUCCESS.equals(payOrder.getState())) {
            businessOrder.setOrderState(BusinessOrder.ORDER_STATE_SUCCESS);
            businessOrder.setPayOrderState(PayOrder.STATE_FINISH);
            businessOrderService.updateOrderInfo(businessOrder);
            log.info("统一修改订单状态--交易完成--修改业务订单状态为交易成功，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());

        }
        log.info("统一修改订单状态--交易完成--结束，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
    }

    /**
     * 支付成功
     *
     * @param tradeResult
     */
    @Transactional
    public void doPaySuccess(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {

        log.info("统一修改订单状态--支付成功--开始，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        boolean needUpdateOrder = false;
        boolean needUpdatePayOrder = false;

        if (!BusinessOrder.ORDER_STATE_PAY.equals(businessOrder.getOrderState())) {
            businessOrder.setOrderState(BusinessOrder.ORDER_STATE_PAY);
            businessOrder.setSuccessTime(new Date());
            needUpdateOrder = true;
            log.info("统一修改订单状态--支付成功，修改业务订单状态为支付成功，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }

        if (!PayOrder.STATE_SUCCESS.equals(payOrder.getState())) {
            payOrder.setPayChannelUser(tradeResult.getBuyerLogonId());
            payOrder.setPayChannelOrderNo(tradeResult.getTradeNo());
            payOrder.setState(PayOrder.STATE_SUCCESS);
            payOrder.setSuccessTime(new Date());
            businessOrder.setPayOrderState(PayOrder.STATE_SUCCESS);
            needUpdateOrder = true;
            needUpdatePayOrder = true;
            log.info("统一修改订单状态--支付成功，修改支付订单状态为支付成功，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
            //增加成长值
            PayChannelDto payChannelDto = payChannelUtils.convertPayChannel(businessOrder.getPayChannel());
            if (PayChannelEnum.ALI.getPayChannel().equals(payChannelDto.getPayChannel())
                || PayChannelEnum.WX.getPayChannel().equals(payChannelDto.getPayChannel())
                || PayChannelEnum.APPLE.getPayChannel().equals(payChannelDto.getPayChannel())) {
                // 成长值增加, 苹果内购：成长值减半
                remoteMemberService.addGrowth(businessOrder.getMemberId(), businessOrder.getGameId(), businessOrder.getOrderAmount(), payChannelDto.getPayChannel());
            }

            //通知业务方
            try {
                notifyRecordService.notifyBusiness(payOrder, businessOrder, 1);
            } catch (Exception e) {
                log.error("统一修改订单状态--通知业务方--异常，{}，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId(), e);
            }
        }
        if (needUpdatePayOrder) {
            payOrderService.updatePayOrderInfo(payOrder);
            OrderLog orderLog = new OrderLog();
            orderLog.setBusinessOrderId(businessOrder.getId());
            orderLog.setOrderType(tradeResult.getBusinessType());
            // 判断是否是苹果内购
            if(StringUtils.equals("apple_iap", payOrder.getPayChannel())) {
                //获取订单支付方式
                ApplePayTypeEnumd payTypeEnumd = ApplePayTypeEnumd.find(payOrder.getPayChannel());
                orderLog.setOpContent(StringUtils.format("用户使用{}完成支付", payTypeEnumd.getName()));
            } else {
                //获取订单支付方式
                PayTypeEnumd payTypeEnumd = PayTypeEnumd.find(payOrder.getPayChannel());
                orderLog.setOpContent(StringUtils.format("用户使用{}完成支付", payTypeEnumd.getName()));
            }
            orderLog.setCreateBy("system");
            orderLogService.save(orderLog);

            OrderLog orderLog2 = new OrderLog();
            orderLog2.setCreateBy("system");
            orderLog2.setBusinessOrderId(businessOrder.getId());
            orderLog2.setOrderType(tradeResult.getBusinessType());
            orderLog2.setOpContent(StringUtils.format("通知游戏方{}", getNotifyStateStr(payOrder.getNotifyState())));
            orderLogService.save(orderLog2);
        }

        if (needUpdateOrder) {
            businessOrderService.updateOrderInfo(businessOrder);
            OrderLog orderLog = new OrderLog();
            orderLog.setCreateBy("system");
            orderLog.setBusinessOrderId(businessOrder.getId());
            orderLog.setOrderType(tradeResult.getBusinessType());
            orderLog.setOpContent("系统完成订单");
            orderLogService.save(orderLog);
        }
        log.info("统一修改订单状态--支付成功--结束，{}", tradeResult.getOutTradeNo());
    }

    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     *
     * @param notifyState
     * @return
     */
    private String getNotifyStateStr(Integer notifyState) {
        String msg = "";
        switch (notifyState) {
            case 0:
                msg = "未触发";
                break;
            case 1:
                msg = "进行中";
                break;
            case 2:
                msg = "成功";
                break;
            case 3:
                msg = "失败";
                break;
        }
        return msg;
    }

    /**
     * 支付失败
     *
     * @param tradeResult
     */
    @Transactional
    public void doPayFail(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--订单关闭--开始，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        boolean needLog = false;

        if (!PayOrder.STATE_FAIL.equals(payOrder.getState())) {
            needLog = true;
            businessOrder.setPayOrderState(PayOrder.STATE_FAIL);
            businessOrder.setOrderState(BusinessOrder.ORDER_STATE_CLOSE);
            businessOrderService.updateOrderInfo(businessOrder);

            payOrder.setPayChannelUser(tradeResult.getBuyerLogonId());
            payOrder.setPayChannelOrderNo(tradeResult.getTradeNo());
            payOrder.setState(PayOrder.STATE_FAIL);
            payOrderService.updatePayOrderInfo(payOrder);
            log.info("统一修改订单状态--支付失败，修改支付订单状态为支付失败，修改业务订单的支付状态为支付失败，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }
        if (needLog) {
            OrderLog orderLog = new OrderLog();
            orderLog.setBusinessOrderId(businessOrder.getId());
            orderLog.setOrderType(tradeResult.getBusinessType());
            orderLog.setOpContent("用户支付失败，订单关闭");
            orderLog.setCreateBy("system");
            orderLogService.save(orderLog);
        }
    }

    /**
     * 等待支付
     */
    @Transactional
    public void doPayWait(BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--等待支付，修改支付订单状态为支付中，修改业务订单状态为待付款，{}", payOrder.getId());
        businessOrder.setPayOrderState(PayOrder.STATE_ING);
        businessOrder.setOrderState(BusinessOrder.ORDER_STATE_INIT);

        businessOrderService.updateOrderInfo(businessOrder);

        payOrder.setState(PayOrder.STATE_ING);
        payOrderService.updatePayOrderInfo(payOrder);
    }

    @Transactional
    public void doPayClose(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--订单关闭--开始，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        if (new Date().before(businessOrder.getExpiredTime())) {
            log.info("统一修改订单状态--订单关闭--订单未过期，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
            return;
        }

        boolean needLog = false;
        if (!BusinessOrder.ORDER_STATE_CLOSE.equals(businessOrder.getOrderState())) {
            needLog = true;

            businessOrder.setOrderState(BusinessOrder.ORDER_STATE_CLOSE);
            businessOrder.setPayOrderState(PayOrder.STATE_CLOSED);
            businessOrderService.updateOrderInfo(businessOrder);
            log.info("统一修改订单状态--订单关闭，修改业务订单状态为订单关闭，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }

        if (!PayOrder.STATE_CLOSED.equals(payOrder.getState())) {
            needLog = true;
            payOrder.setPayChannelUser(tradeResult.getBuyerLogonId());
            payOrder.setPayChannelOrderNo(tradeResult.getTradeNo());
            payOrder.setState(PayOrder.STATE_CLOSED);
            payOrderService.updatePayOrderInfo(payOrder);
            log.info("统一修改订单状态--订单关闭，修改支付订单状态为订单关闭，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }
        //记录订单日志
        if (needLog) {
            OrderLog orderLog = new OrderLog();
            orderLog.setBusinessOrderId(businessOrder.getId());
            orderLog.setOrderType(tradeResult.getBusinessType());
            orderLog.setOpContent("用户超时未支付，订单自动关闭");
            orderLog.setCreateBy("system");
            orderLogService.save(orderLog);
        }
        log.info("统一修改订单状态--订单关闭--结束，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
    }

    /**
     * 退款成功
     *
     * @param tradeResult
     */
    @Transactional
    public void doRefundSuccess(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--退款成功--开始，{}", tradeResult.getOutTradeNo());
        boolean needUpdateOrder = false;
        boolean needUpdatePayOrder = false;
        boolean needUpdateRefundOrder = false;

        if (!BusinessOrder.ORDER_STATE_CLOSE.equals(businessOrder.getOrderState())) {
            businessOrder.setOrderState(BusinessOrder.ORDER_STATE_CLOSE);
            businessOrder.setPayOrderState(PayOrder.STATE_REFUND);
            businessOrder.setRefundOrderState(RefundOrder.STATE_SUCCESS);
            needUpdateOrder = true;
            log.info("统一修改订单状态--退款成功，修改业务订单状态为订单关闭，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }

        if (!PayOrder.STATE_REFUND.equals(payOrder.getState())) {

            payOrder.setState(PayOrder.STATE_REFUND);
            payOrder.setRefundState(PayOrder.REFUND_STATE_ALL);
            payOrder.setRefundAmount(payOrder.getAmount());
            needUpdatePayOrder = true;
            needUpdateOrder = true;
            log.info("统一修改订单状态--退款成功，修改支付订单状态为已退款，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
        }
        //修改退款单状态
        RefundOrder refundOrder = refundOrderService.getByPayOrderId(tradeResult.getOutTradeNo());
        if (refundOrder == null) {
            return;
        }
        if (!RefundOrder.STATE_SUCCESS.equals(refundOrder.getState())) {
            refundOrder.setState(RefundOrder.STATE_SUCCESS);
            refundOrder.setSuccessTime(new Date());
            needUpdateRefundOrder = true;
            log.info("统一修改订单状态--退款成功，修改退款状态为已退款，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
            //记录订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setBusinessOrderId(businessOrder.getId());
            orderLog.setOrderType(tradeResult.getBusinessType());
            orderLog.setOpContent("系统完成退款，订单完成");
            orderLog.setCreateBy("system");
            orderLogService.save(orderLog);
        }

        if (needUpdateOrder)
            businessOrderService.updateOrderInfo(businessOrder);
        if (needUpdatePayOrder)
            payOrderService.updatePayOrderInfo(payOrder);
        if (needUpdateRefundOrder)
            refundOrderService.updateRefundOrderInfo(refundOrder);
        log.info("统一修改订单状态--退款成功--结束，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
    }

    /**
     * 退款失败
     *
     * @param tradeResult
     */
    @Transactional
    public void doRefundFail(TradeResult tradeResult, BusinessOrder businessOrder, PayOrder payOrder) {
        log.info("统一修改订单状态--退款失败--开始，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());

        //修改退款单状态
        RefundOrder refundOrder = refundOrderService.getByPayOrderId(tradeResult.getOutTradeNo());

        businessOrder.setRefundOrderState(RefundOrder.STATE_FAIL);
        businessOrderService.updateOrderInfo(businessOrder);

        if (refundOrder == null) {
            log.info("统一修改订单状态--退款失败--退款单不存在，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
            return;
        }
        refundOrder.setState(RefundOrder.STATE_FAIL);

        refundOrderService.updateRefundOrderInfo(refundOrder);
        log.info("统一修改订单状态--退款失败--修改退款单状态为退款失败，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());

        log.info("统一修改订单状态--退款失败--结束，payOrderId：{},orderId：{}", tradeResult.getOutTradeNo(), payOrder.getBusinessOrderId());
    }

}
