package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("统一下单对象")
public class UnifiedOrderDto {

    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private String goodsId;

    @ApiModelProperty(value = "订单类型，P：平台消费，G：游戏消费", required = true)
    @NotNull(message = "订单类型不能为空")
    private String orderType;

    @ApiModelProperty(value = "支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap", required = true)
    @NotBlank(message = "支付渠道不能为空")
    private String payChannelCode;

    @ApiModelProperty(value = "商品标题，订单类型平台消费时为空", required = true)
    @NotBlank(message = "商品标题不能为空")
    private String subject;

    @ApiModelProperty(value = "商品描述信息，订单类型平台消费时为空", required = true)
    @NotBlank(message = "商品描述信息不能为空")
    private String body;

    @ApiModelProperty(value = "订单金额，订单类型平台消费时为空,单位元", required = true)
    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "商品图片，订单类型平台消费时为空", required = true)
    private String goodsImg;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer goodsNum = 1;

    @ApiModelProperty("外部订单号")
    private String outTradeNo;

    @ApiModelProperty("扩展数据")
    private  String ExtData;

}
