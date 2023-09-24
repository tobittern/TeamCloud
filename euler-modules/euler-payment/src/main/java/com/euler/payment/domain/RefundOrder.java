package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款订单对象 refund_order
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order")
public class RefundOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final Integer STATE_NONE = 0; //未退款
    public static final Integer STATE_INIT = 1; //订单生成
    public static final Integer STATE_ING = 2; //退款中
    public static final Integer STATE_SUCCESS = 3; //退款成功
    public static final Integer STATE_FAIL = 4; //退款失败
    public static final Integer STATE_CLOSED = 5; //退款任务关闭

    /**
     * 退款订单号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 支付订单号（与pay_order的id对应）
     */
    private String payOrderId;
    /**
     * 渠道支付单号（与pay_order channel_order_no对应）
     */
    private String payChannelPayOrderNo;

    /**
     * 业务订单id
     */
    private String businessOrderId;
    /**
     * 支付金额,单位元
     */
    private BigDecimal payAmount;
    /**
     * 退款金额,单位元
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭
     */
    private Integer state;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap
     */
    private String payChannel;
    /**
     * 支付渠道退款单号
     */
    private String payChannelRefundOrderNo;
    /**
     * 特定渠道发起时额外参数
     */
    private String payChannelExtra;
    /**
     * 渠道错误码
     */
    private String errCode;
    /**
     * 渠道错误描述
     */
    private String errMsg;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 订单退款成功时间
     */
    private Date successTime;
    /**
     * 退款失效时间（失效后系统更改为退款任务关闭状态）
     */
    private Date expiredTime;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
