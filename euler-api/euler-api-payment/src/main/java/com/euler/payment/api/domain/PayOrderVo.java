package com.euler.payment.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单视图对象 pay_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("支付订单视图对象")
@ExcelIgnoreUnannotated
public class PayOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @ExcelProperty(value = "支付订单号")
    @ApiModelProperty("支付订单号")
    private String id;

    /**
     * 业务订单id
     */
    @ExcelProperty(value = "业务订单id")
    @ApiModelProperty("业务订单id")
    private String businessOrderId;

    /**
     * 支付金额,单位元
     */
    @ExcelProperty(value = "支付金额")
    @ApiModelProperty("支付金额,单位元")
    private BigDecimal amount;

    /**
     * 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
     */
    @ExcelProperty(value = "支付状态")
    @ApiModelProperty("支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款")
    private Integer state;

    /**
     * 客户端IP
     */
    @ExcelProperty(value = "客户端IP")
    @ApiModelProperty("客户端IP")
    private String clientIp;

    /**
     * 商品标题
     */
    @ExcelProperty(value = "商品标题")
    @ApiModelProperty("商品标题")
    private String subject;

    /**
     * 商品描述信息
     */
    @ExcelProperty(value = "商品描述信息")
    @ApiModelProperty("商品描述信息")
    private String body;

    /**
     * 支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap
     */
    @ExcelProperty(value = "支付渠道")
    @ApiModelProperty("支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap")
    private String payChannel;

    /**
     * 支付渠道用户标识,如微信openId,支付宝账号，钱包支付的话，为会员账号
     */
    @ExcelProperty(value = "支付渠道用户标识")
    @ApiModelProperty("支付渠道用户标识,如微信openId,支付宝账号，钱包支付的话，为会员账号")
    private String payChannelUser;

    /**
     * 支付渠道订单号
     */
    @ExcelProperty(value = "支付渠道订单号")
    @ApiModelProperty("支付渠道订单号")
    private String payChannelOrderNo;

    /**
     * 特定支付渠道发起时额外参数
     */
    @ExcelProperty(value = "特定支付渠道发起时额外参数")
    @ApiModelProperty("特定支付渠道发起时额外参数")
    private String payChannelExtra;

    /**
     * 退款状态: 0-未发生实际退款, 1-部分退款, 2-全额退款
     */
    @ExcelProperty(value = "退款状态")
    @ApiModelProperty("退款状态: 0-未发生实际退款, 1-部分退款, 2-全额退款")
    private Integer refundState;

    /**
     * 退款次数
     */
    @ExcelProperty(value = "退款次数")
    @ApiModelProperty("退款次数")
    private Integer refundTimes;

    /**
     * 退款总金额,单位元
     */
    @ExcelProperty(value = "退款总金额")
    @ApiModelProperty("退款总金额,单位元")
    private BigDecimal refundAmount;

    /**
     * 渠道支付错误码
     */
    @ExcelProperty(value = "渠道支付错误码")
    @ApiModelProperty("渠道支付错误码")
    private String errCode;

    /**
     * 渠道支付错误描述
     */
    @ExcelProperty(value = "渠道支付错误描述")
    @ApiModelProperty("渠道支付错误描述")
    private String errMsg;

    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     */
    @ExcelProperty(value = "通知状态")
    @ApiModelProperty("通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败")
    private Integer notifyState;

    /**
     * 通知次数
     */
    @ExcelProperty(value = "通知次数")
    @ApiModelProperty("通知次数")
    private Integer notifyCount;

    /**
     * 最大通知次数, 默认6次
     */
    @ExcelProperty(value = "最大通知次数, 默认6次")
    @ApiModelProperty("最大通知次数, 默认6次")
    private Integer notifyCountLimit;

    /**
     * 最后一次通知时间
     */
    @ExcelProperty(value = "最后一次通知时间")
    @ApiModelProperty("最后一次通知时间")
    private Date lastNotifyTime;

    /**
     * 异步通知地址
     */
    @ExcelProperty(value = "异步通知地址")
    @ApiModelProperty("异步通知地址")
    private String notifyUrl;

    /**
     * 页面跳转地址
     */
    @ExcelProperty(value = "页面跳转地址")
    @ApiModelProperty("页面跳转地址")
    private String returnUrl;

    /**
     * 订单失效时间
     */
    @ExcelProperty(value = "订单失效时间")
    @ApiModelProperty("订单失效时间")
    private Date expiredTime;

    /**
     * 订单支付成功时间
     */
    @ExcelProperty(value = "订单支付成功时间")
    @ApiModelProperty("订单支付成功时间")
    private Date successTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
