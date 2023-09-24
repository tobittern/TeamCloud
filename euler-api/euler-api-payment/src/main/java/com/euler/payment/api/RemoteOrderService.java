package com.euler.payment.api;

import com.euler.common.core.domain.R;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.api.domain.TradeResultDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RemoteOrderService {
    /**
     * 获取逾期未支付的订单
     *
     * @param num
     * @return
     */
    List<PayOrderVo> queryExpiredList(Integer num);

    /**
     * 获取游戏通知失败的支付单
     *
     * @param num
     * @return
     */
    List<PayOrderVo> queryGameNotifyFailList(Integer num);

    /**
     * 自动补单操作
     *
     * @param orderId
     * @return
     */
    R autoSupplyOrder(String orderId, String opUser, String opContent);

    /**
     * 查询支付单状态
     *
     * @param outTradeNo
     * @return
     */
    TradeResultDto queryPayOrderState(String outTradeNo);

    /**
     * 统一修改订单状态
     *
     * @param tradeResult
     */
    void payStatus(TradeResultDto tradeResult);


    /**
     * 获取会员订单统计数据
     *
     * @param memberId
     * @param gameId
     * @return
     */
    MemberOrderStatDto getMemberOrderStatInfo(Long memberId, Integer gameId, BigDecimal rechargeAmount);

    /**
     * 获取订单相关数据
     *
     * @param map 参数
     * @return 订单数据
     */
    public Object getOrderDataByparam(Map<String, Object> map);

    /**
     * 获取订单相关充值数据
     *
     * @param map 参数
     * @return 订单数据
     */
    public List<BusinessOrderVo> getOrderChargeByparam(Map<String, Object> map);

    /**
     * 提现到微信
     *
     * @param amount
     * @param remark
     * @param openId
     * @return
     */
    String cashOutToWX(BigDecimal amount, String remark, String openId);

    /**
     * 查询用户已支付的订单数
     */
    int getOrderCountByUser(Long userId, Long gameId);

}
