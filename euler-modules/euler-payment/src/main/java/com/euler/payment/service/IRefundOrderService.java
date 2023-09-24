package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.bean.TradeResult;
import com.euler.payment.domain.RefundOrder;
import com.euler.payment.domain.dto.AppleRefundOrderDto;
import com.euler.payment.domain.dto.LaunchRefundDto;
import com.euler.payment.domain.dto.RefundOrderPageDto;
import com.euler.payment.api.domain.RefundOrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 退款订单Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IRefundOrderService extends IService<RefundOrder> {

    /**
     * 查询退款订单
     *
     * @param id 退款订单主键
     * @return 退款订单
     */
    RefundOrderVo queryById(String id);

    /**
     * 查询退款订单列表
     *
     * @param orderPageDto 退款订单
     * @return 退款订单集合
     */
    TableDataInfo<RefundOrderVo> queryPageList(RefundOrderPageDto orderPageDto);

    /**
     * 查询退款订单列表
     *
     * @param orderPageDto 退款订单
     * @return 退款订单集合
     */
    List<RefundOrderVo> queryList(RefundOrderPageDto orderPageDto);


    /**
     * 根据业务订单号获取支付订单
     *
     * @param orderId
     * @return
     */
    RefundOrder getByPayOrderId(String orderId);

    /**
     * 发起退款请求
     *
     * @param refundDto
     * @return
     */
    R<TradeResult> launch(LaunchRefundDto refundDto);


    /**
     * 微信异步回调
     *
     * @param request
     * @param response
     */
    void wxNotify(HttpServletRequest request, HttpServletResponse response);


    /**
     * 退款状态查询
     *
     * @param outTradeNo
     * @return
     */
    TradeResult refundQuery(String outTradeNo);

    /**
     * 修改订单状态
     *
     * @param refundOrder
     * @return
     */
     boolean updateRefundOrderInfo(RefundOrder refundOrder);

    /**
     * 苹果内购退款回调
     */
    R applePayRefund(AppleRefundOrderDto dto);

}
