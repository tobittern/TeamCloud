package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.payment.domain.BusinessOrder;
import com.euler.payment.domain.dto.ApplePayDto;
import com.euler.payment.domain.dto.BusinessOrderPageDto;
import com.euler.payment.domain.dto.FrontQueryResultDto;
import com.euler.payment.domain.dto.UnifiedOrderDto;
import com.euler.payment.domain.vo.StatisticsChargeVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付订单Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IBusinessOrderService extends IService<BusinessOrder> {

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    BusinessOrderVo queryById(String id);

    /**
     * 查询订单列表
     *
     * @param businessOrderPageDto 订单
     * @return 订单集合
     */
    TableDataInfo<BusinessOrderVo> queryPageList(BusinessOrderPageDto businessOrderPageDto);

    /**
     * 查询订单列表
     *
     * @param businessOrderPageDto 订单
     * @return 订单集合
     */
    List<BusinessOrderVo> queryList(BusinessOrderPageDto businessOrderPageDto);

    /**
     * 补单
     *
     * @param orderId   订单id
     * @return
     */
    R supplyOrder(String orderId, String opUser, String opContent,boolean isAuto);

    /**
     * 统一下单
     *
     * @param unifiedOrderDto
     * @return
     */
    TradeToken<?> unifiedOrder(UnifiedOrderDto unifiedOrderDto);

    /**
     * 查询支付订单状态
     *
     * @param outTradeNo
     * @return
     */
    TradeResult queryPayOrderState(String outTradeNo);

    FrontQueryResultDto frontQueryOrderState(String businessOrderId);

    /**
     * 支付宝异步回调
     *
     * @return
     */
    void orderAliNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 微信异步回调
     *
     * @param request
     * @param response
     */
    void orderWxNotify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 苹果内购回调
     *
     * @param dto
     */
    R orderApplePay(ApplePayDto dto);

    /**
     * 修改订单状态
     *
     * @param order
     * @return
     */
    boolean updateOrderInfo(BusinessOrder order);

    /**
     * 获取会员订单统计数据
     *
     * @param memberId
     * @param gameId
     * @return
     */
     MemberOrderStatDto getMemberOrderStatInfo(Long memberId, Integer gameId, BigDecimal rechargeAmount);

    /**
     * 根据条件查询订单汇总数据
     * @param map 参数
     * @return 数据
     */
    List<StatisticsChargeVo> getOrderDataByparam(Map<String,Object> map);

    /**
     * 查询订单付款相关信息
     * @param map 参数
     * @return 数据
     */
    List<BusinessOrderVo> getOrderChargeByparam(Map<String,Object> map);

    /**
     * 查询用户已支付的订单数
     */
    int getOrderCountByUser(Long userId, Long gameId);

}
