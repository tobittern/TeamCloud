package com.euler.payment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.core.CallablePlus;
import com.euler.common.payment.core.PayNotifySevice;
import com.euler.common.payment.core.WxPay;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.factory.PayFactory;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.api.domain.RefundOrderVo;
import com.euler.payment.bean.AppleTradeResult;
import com.euler.payment.config.WebConfig;
import com.euler.payment.domain.*;
import com.euler.payment.domain.dto.AppleRefundOrderDto;
import com.euler.payment.domain.dto.LaunchRefundDto;
import com.euler.payment.domain.dto.RefundOrderPageDto;
import com.euler.payment.enums.ApplePayTypeEnumd;
import com.euler.payment.enums.PayChannelEnum;
import com.euler.payment.factory.ApplePayFactory;
import com.euler.payment.mapper.BusinessOrderMapper;
import com.euler.payment.mapper.RefundOrderMapper;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.payment.service.IOrderLogService;
import com.euler.payment.service.IPayOrderService;
import com.euler.payment.service.IRefundOrderService;
import com.euler.payment.utils.JwsUtil;
import com.euler.payment.utils.PayChannelUtils;
import com.euler.payment.utils.common.JWSRenewalInfoDecodedPayload;
import com.euler.payment.utils.common.JWSTransactionDecodedPayload;
import com.euler.payment.utils.common.ResponseBodyV2DecodedPayload;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 退款订单Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements IRefundOrderService {

    private final RefundOrderMapper baseMapper;
    @Autowired
    private IBusinessOrderService businessOrderService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private PayChannelUtils payChannelUtils;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IOrderLogService orderLogService;
    @Autowired
    private PayNotifySevice payNotifySevice;
    @Autowired
    private WxPay wxPay;
    @Autowired
    private BusinessOrderMapper businessOrderMapper;
    @DubboReference
    private RemoteMemberService remoteMemberService;

    /**
     * 查询退款订单
     *
     * @param id 退款订单主键
     * @return 退款订单
     */
    @Override
    public RefundOrderVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询退款订单列表
     *
     * @param orderPageDto 退款订单
     * @return 退款订单
     */
    @Override
    public TableDataInfo<RefundOrderVo> queryPageList(RefundOrderPageDto orderPageDto) {
        LambdaQueryWrapper<RefundOrder> lqw = buildQueryWrapper(orderPageDto);
        Page<RefundOrderVo> result = baseMapper.selectVoPage(orderPageDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询退款订单列表
     *
     * @param orderPageDto 退款订单
     * @return 退款订单
     */
    @Override
    public List<RefundOrderVo> queryList(RefundOrderPageDto orderPageDto) {
        LambdaQueryWrapper<RefundOrder> lqw = buildQueryWrapper(orderPageDto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据业务订单号获取支付订单
     *
     * @param payOrderId
     * @return
     */
    @Override
    public RefundOrder getByPayOrderId(String payOrderId) {
        RefundOrderPageDto orderPageDto = new RefundOrderPageDto();
        orderPageDto.setPayOrderId(payOrderId);
        var wrapper = buildQueryWrapper(orderPageDto);
        wrapper.last("limit 1");
        return baseMapper.selectOne(wrapper);
    }

    private LambdaQueryWrapper<RefundOrder> buildQueryWrapper(RefundOrderPageDto bo) {
        LambdaQueryWrapper<RefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessOrderId()), RefundOrder::getBusinessOrderId, bo.getBusinessOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getPayOrderId()), RefundOrder::getPayOrderId, bo.getPayOrderId());
        lqw.eq(bo.getState() != null, RefundOrder::getState, bo.getState());
        return lqw;
    }

    /**
     * 发起退款请求
     *
     * @param refundDto
     * @return
     */
    @Transactional
    @Override
    public R<TradeResult> launch(LaunchRefundDto refundDto) {

        PayOrder payOrder = payOrderService.getById(refundDto.getPayOrderId());

        if (payOrder == null) {
            log.info("发起退款请求--支付订单不存在，订单号：{}", refundDto.getPayOrderId());
            throw new ServiceException("支付订单不存在");
        }

        var businessOrder = businessOrderService.getById(payOrder.getBusinessOrderId());
        if (businessOrder == null) {
            log.info("发起退款请求--订单不存在，订单号：{}", refundDto.getPayOrderId());
            throw new ServiceException("订单不存在");
        }

        if (BusinessOrder.ORDER_STATE_INIT.equals(businessOrder.getOrderState()))
            throw new ServiceException("待支付订单，不允许退款");

        if (BusinessOrder.ORDER_STATE_SUCCESS.equals(businessOrder.getOrderState()))
            throw new ServiceException("已完成订单，不允许退款");

        if (BusinessOrder.ORDER_STATE_CLOSE.equals(businessOrder.getOrderState()))
            throw new ServiceException("已关闭订单，不允许补单");

        TradeResult result = null;
        if(!StringUtils.equals("apple_iap", payOrder.getPayChannel())) {
            String outRefundNo = PayFactory.getTradeNo("R");
            RefundOrder refundOrder = genRefundOrder(payOrder, refundDto, outRefundNo);
            var tradePay = PayFactory.getPayType(payOrder.getPayChannel());
            Trade trade = new Trade();
            trade.setTradeNo(payOrder.getPayChannelOrderNo());
            trade.setOutTradeNo(payOrder.getId());
            trade.setRefundAmount(payOrder.getAmount().toString());
            trade.setRefundReason(refundDto.getRefundReason());
            trade.setOutRequestNo(outRefundNo);
            result = tradePay.refund(trade);
            if (result.isSuccess()) {
                refundOrder.setState(RefundOrder.STATE_ING);
            } else {
                refundOrder.setState(RefundOrder.STATE_FAIL);
                refundOrder.setErrCode(result.getSubCode());
                refundOrder.setErrMsg(result.getSubMsg());
            }
            updateRefundOrderInfo(refundOrder);

            PayTypeEnumd payTypeEnumd = PayTypeEnumd.find(payOrder.getPayChannel());
            if (PayChannelEnum.WALLET.getPayChannel().equals(payTypeEnumd.getPayChannelType())) {
                RechargeTypeEnum rechargeTypeEnum = null;
                //平台币校验
                switch (payTypeEnumd) {
                    case WalletBalance:
                        rechargeTypeEnum = RechargeTypeEnum.balance;
                        break;
                    case WalletPlatform:
                        rechargeTypeEnum = RechargeTypeEnum.platform_currency;
                        break;
                }
                remoteMemberService.modifyWallet(businessOrder.getMemberId(), businessOrder.getGameId(), 1, businessOrder.getOrderAmount(), rechargeTypeEnum, 1, "运营人员申请退款");

                TradeResult tradeResult = new TradeResult(PayTypeEnumd.WalletRefund);
                tradeResult.setSuccess(true);
                tradeResult.setOutTradeNo(trade.getOutTradeNo());
                tradeResult.setTradeStatus("REFUND_SUCCESS");
                try {
                    payNotifySevice.payStatus(tradeResult);
                } catch (Exception e) {
                    log.error("发起退款请求--钱包支付--成功后回调异常:{}", JsonHelper.toJson(tradeResult), e);
                }
            }
        }

        if (result.isSuccess())
            return R.ok(result);
        else
            return R.fail(result.getSubMsg());
    }

    /**
     * 创建退款单
     *
     * @param payOrder
     * @param refundDto
     * @param refundOrderId
     */
    @Transactional
    public RefundOrder genRefundOrder(PayOrder payOrder, LaunchRefundDto refundDto, String refundOrderId) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setId(refundOrderId);
        refundOrder.setBusinessOrderId(payOrder.getBusinessOrderId());
        refundOrder.setRefundReason(refundDto.getRefundReason());
        refundOrder.setClientIp(ServletUtils.getClientIP());
        refundOrder.setExpiredTime(DateUtil.offsetMinute(new Date(), webConfig.getOrderExpiredTime()));
        refundOrder.setPayAmount(payOrder.getAmount());
        refundOrder.setPayChannel(payOrder.getPayChannel());
        refundOrder.setPayChannelPayOrderNo(payOrder.getPayChannelOrderNo());
        refundOrder.setPayOrderId(payOrder.getId());
        refundOrder.setRefundAmount(payOrder.getAmount());
        refundOrder.setState(RefundOrder.STATE_INIT);
        this.save(refundOrder);

        //设置订单退款状态
        Integer refundTimes = Convert.toInt(payOrder.getRefundTimes(), 0) + 1;
        payOrder.setRefundTimes(refundTimes);
        payOrder.setRefundState(PayOrder.REFUND_STATE_NONE);
        payOrderService.updatePayOrderInfo(payOrder);

        //记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setBusinessOrderId(payOrder.getBusinessOrderId());
        orderLog.setOrderType(OrderLog.ORDERTYPE_REFUND);
        orderLog.setCreateBy(LoginHelper.getUsername());

        orderLog.setOpContent(StringUtils.format("由运营人员{}发起订单退款", LoginHelper.getUsername()));
        orderLogService.save(orderLog);

        return refundOrder;
    }

    /**
     * 退款单状态查询
     *
     * @param outTradeNo
     * @return
     */
    @Override
    public TradeResult refundQuery(String outTradeNo) {
        // 获取订单详情，以便获取支付渠道
        RefundOrder refundOrder = this.getByPayOrderId(outTradeNo);
        if (refundOrder == null)
            return new TradeResult();
        String payType = refundOrder.getPayChannel().split("_")[0] + "_RefundQuery";

        Trade trade = new Trade();
        trade.setTradeNo(refundOrder.getPayChannelPayOrderNo());
        trade.setOutTradeNo(refundOrder.getPayOrderId());
        TradeResult result;
        // 判断是否是苹果内购
        if(StringUtils.equals("apple_iap", refundOrder.getPayChannel())) {
            var tradePay = ApplePayFactory.getPayType(payType);
            result = tradePay.refundQuery(trade);
        } else {
            var tradePay = PayFactory.getPayType(payType);
            result = tradePay.refundQuery(trade);
        }

        return result;
    }

    /**
     * 修改订单状态
     *
     * @param refundOrder
     * @return
     */
    public boolean updateRefundOrderInfo(RefundOrder refundOrder) {
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        updateChainWrapper.eq(RefundOrder::getId, refundOrder.getId())
            .set(refundOrder.getState() != null, RefundOrder::getState, refundOrder.getState())
            .set(refundOrder.getSuccessTime() != null, RefundOrder::getSuccessTime, refundOrder.getSuccessTime());
        return updateChainWrapper.update();
    }

    /**
     * 微信异步回调
     *
     * @param request
     * @param response
     */
    @Override
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {

        //解析请求参数
        CallablePlus c = tradeResult -> {
            //处理业务
            payNotifySevice.payStatus(tradeResult);
            return "";
        };
        wxPay.AsyncRefundNotify(request, response, c);
    }

    /**
     * 苹果内购退款回调
     */
    @SneakyThrows
    @Override
    public R applePayRefund(AppleRefundOrderDto dto) {
        // 退款凭证，解码JWS数据
        ResponseBodyV2DecodedPayload decodedPayload = JwsUtil.decodePayload(dto.getSignedPayload());

        log.info("环境:{}", decodedPayload.getData().getEnvironment().toString());
        log.info("苹果通知类型:{}", decodedPayload.getNotificationType().toString());
        String notificationType = decodedPayload.getNotificationType().toString();
        if(StringUtils.equals("REFUND", notificationType)) {
            // JSON Web签名（JWS）格式的订阅续订信息
            String signedRenewalInfo = decodedPayload.getData().getSignedRenewalInfo();
            JWSRenewalInfoDecodedPayload jwsRenewalInfo = JwsUtil.decodeRenewalInfo(signedRenewalInfo);
            log.info("originalTransactionId:{}, productId:{}", jwsRenewalInfo.getOriginalTransactionId(), jwsRenewalInfo.getProductId());

            // JSON Web签名（JWS）格式的交易信息
            String signedTransactionInfo = decodedPayload.getData().getSignedTransactionInfo();
            JWSTransactionDecodedPayload jwsTransaction = JwsUtil.decodeTransaction(signedTransactionInfo);
            log.info("TransactionId:{}, originalTransactionId:{}, productId:{}", jwsTransaction.getTransactionId(), jwsTransaction.getOriginalTransactionId(), jwsTransaction.getProductId());

            // 交易信息存在的情况下
            if (jwsTransaction != null && jwsTransaction.getOriginalTransactionId() != null) {
                LambdaQueryWrapper<BusinessOrder> lqw = Wrappers.lambdaQuery();
                lqw.eq(BusinessOrder::getPayChannel, "apple_iap");
                lqw.eq(BusinessOrder::getAppleTradeNo, jwsRenewalInfo.getOriginalTransactionId());
                lqw.eq(BusinessOrder::getDelFlag, "0");
                BusinessOrderVo vo = businessOrderMapper.selectVoOne(lqw);
                if (vo != null) {
                    // 苹果内购，修改退款状态
                    AppleTradeResult tradeResult = new AppleTradeResult(String.valueOf(ApplePayTypeEnumd.AppleRefund));
                    tradeResult.setSuccess(true);
                    tradeResult.setOutTradeNo(vo.getOrderType() + vo.getId());
                    tradeResult.setTradeStatus("REFUND_SUCCESS");
                    TradeResult newTradeResult = BeanCopyUtils.copy(tradeResult, TradeResult.class);
                    try {
                        payNotifySevice.payStatus(newTradeResult);
                        return R.ok("退款成功");
                    } catch (Exception e) {
                        log.error("苹果内购退款--成功后回调异常:{}", JsonHelper.toJson(tradeResult), e);
                    }
                }
            }
            return R.fail("没有该退款订单");
        }
        return R.fail("不是退款通知");
    }

}
