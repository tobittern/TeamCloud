package com.euler.payment.dubbo;

import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.core.PayNotifySevice;
import com.euler.common.payment.core.WxPay;
import com.euler.payment.api.RemoteOrderService;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.api.domain.TradeResultDto;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.payment.service.IPayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteOrderServiceImpl implements RemoteOrderService {
    @Autowired
    private IBusinessOrderService businessOrderService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private PayNotifySevice payNotifySevice;
    @Autowired
    private WxPay wxPay;

    /**
     * 获取逾期未支付的订单
     *
     * @param num
     * @return
     */
    @Override
    public List<PayOrderVo> queryExpiredList(Integer num) {
        return payOrderService.queryExpiredList(num);
    }

    /**
     * 获取游戏通知失败的支付单
     *
     * @param num
     * @return
     */
    @Override
    public List<PayOrderVo> queryGameNotifyFailList(Integer num) {
        return payOrderService.queryGameNotifyFailList(num);
    }

    /**
     * 自动补单操作
     *
     * @param orderId
     * @return
     */
    @Override
    public R autoSupplyOrder(String orderId, String opUser, String opContent) {
       return businessOrderService.supplyOrder(orderId,  opUser,  opContent,true);
    }

    /**
     * 查询支付单状态
     *
     * @param outTradeNo
     * @return
     */
    @Override
    public TradeResultDto queryPayOrderState(String outTradeNo) {
        var res = JsonHelper.copyObj(businessOrderService.queryPayOrderState(outTradeNo), TradeResultDto.class);
        return res;
    }

    @Override
    public void payStatus(TradeResultDto tradeResult) {
        TradeResult result = JsonHelper.copyObj(tradeResult, TradeResult.class);
        payNotifySevice.payStatus(result);
    }

    /**
     * 获取会员订单统计数据
     *
     * @param memberId
     * @param gameId
     * @return
     */
    @Override
    public MemberOrderStatDto getMemberOrderStatInfo(Long memberId, Integer gameId, BigDecimal rechargeAmount) {
        return businessOrderService.getMemberOrderStatInfo(memberId, gameId, rechargeAmount);
    }

    /**
     * 获取订单相关数据
     *
     * @param map 参数
     * @return 订单数据
     */
    @Override
    public Object getOrderDataByparam(Map<String, Object> map) {
        return businessOrderService.getOrderDataByparam(map);
    }

    @Override
    public List<BusinessOrderVo> getOrderChargeByparam(Map<String, Object> map) {
        return businessOrderService.getOrderChargeByparam(map);
    }

    /**
     * 提现到微信
     *
     * @param amount
     * @param remark
     * @param openId
     * @return
     */
    public String cashOutToWX(BigDecimal amount, String remark, String openId) {
        return wxPay.cashOutToWX(amount, remark, openId);
    }

    /**
     * 查询用户已支付的订单数
     */
    public int getOrderCountByUser(Long userId, Long gameId) {
        return businessOrderService.getOrderCountByUser(userId, gameId);
    }

}
