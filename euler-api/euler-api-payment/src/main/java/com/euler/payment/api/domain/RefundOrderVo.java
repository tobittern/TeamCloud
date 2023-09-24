package com.euler.payment.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 退款订单视图对象 refund_order
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("退款订单视图对象")
@ExcelIgnoreUnannotated
public class RefundOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款订单号
     */
    @ExcelProperty(value = "退款订单号")
    @ApiModelProperty("退款订单号")
    private String id;

    /**
     * 支付订单号（与pay_order的id对应）
     */
    @ExcelProperty(value = "支付订单号")
    @ApiModelProperty("支付订单号（与pay_order的id对应）")
    private String payOrderId;

    /**
     * 渠道支付单号（与pay_order channel_order_no对应）
     */
    @ExcelProperty(value = "渠道支付单号")
    @ApiModelProperty("渠道支付单号（与pay_order channel_order_no对应）")
    private String payChannelPayOrderNo;

    /**
     * 业务单号,order表id
     */
    @ExcelProperty(value = "业务单号")
    @ApiModelProperty("业务单号,order表id")
    private String businessOrderId;

    /**
     * 支付金额,单位元
     */
    @ExcelProperty(value = "支付金额")
    @ApiModelProperty("支付金额,单位元")
    private BigDecimal payAmount;

    /**
     * 退款金额,单位分
     */
    @ExcelProperty(value = "退款金额")
    @ApiModelProperty("退款金额,单位元")
    private BigDecimal refundAmount;

    /**
     * 退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭
     */
    @ExcelProperty(value = "退款状态")
    @ApiModelProperty("退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭")
    private Integer state;

    /**
     * 客户端IP
     */
    @ExcelProperty(value = "客户端IP")
    @ApiModelProperty("客户端IP")
    private String clientIp;

    /**
     * 退款原因
     */
    @ExcelProperty(value = "退款原因")
    @ApiModelProperty("退款原因")
    private String refundReason;

    /**
     * 支付渠道，支付宝app：ali_app，微信：wx_app，钱包支付：wallet，苹果内购：apple
     */
    @ExcelProperty(value = "支付渠道")
    @ApiModelProperty("支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap")
    private String payChannel;

    /**
     * 支付渠道退款单号
     */
    @ExcelProperty(value = "支付渠道退款单号")
    @ApiModelProperty("支付渠道退款单号")
    private String payChannelRefundOrderNo;

    /**
     * 特定渠道发起时额外参数
     */
    @ExcelProperty(value = "特定渠道发起时额外参数")
    @ApiModelProperty("特定渠道发起时额外参数")
    private String payChannelExtra;

    /**
     * 渠道错误码
     */
    @ExcelProperty(value = "渠道错误码")
    @ApiModelProperty("渠道错误码")
    private String errCode;

    /**
     * 渠道错误描述
     */
    @ExcelProperty(value = "渠道错误描述")
    @ApiModelProperty("渠道错误描述")
    private String errMsg;

    /**
     * 通知地址
     */
    @ExcelProperty(value = "通知地址")
    @ApiModelProperty("通知地址")
    private String notifyUrl;

    /**
     * 订单退款成功时间
     */
    @ExcelProperty(value = "订单退款成功时间")
    @ApiModelProperty("订单退款成功时间")
    private Date successTime;

    /**
     * 退款失效时间（失效后系统更改为退款任务关闭状态）
     */
    @ExcelProperty(value = "退款失效时间", converter = ExcelDictConvert.class)
    @ApiModelProperty("退款失效时间（失效后系统更改为退款任务关闭状态）")
    private Date expiredTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
