package com.euler.payment.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单视图对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("业务订单视图对象")
@ExcelIgnoreUnannotated
public class BusinessOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @ExcelProperty(value = "支付订单号")
    @ApiModelProperty("支付订单号")
    private String id;

    /**
     * 订单类型，G：游戏消费，P：平台消费
     */
    @ExcelProperty(value = "订单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "G=游戏消费,P=平台消费")
    @ApiModelProperty("订单类型，G=游戏消费,P=平台消费")
    private String orderType;

    /**
     * 支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap
     */
    @ExcelProperty(value = "支付渠道", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "ali_MobilePay=支付宝app,ali_WebMobilePay=支付宝H5,wx_MobilePay=微信app,wx_WebMobilePay=微信H5,wallet_balance=余额,wallet_platform=平台币,apple_iap=苹果内购")
    @ApiModelProperty("支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap")
    private String payChannel;

    /**
     * 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
     */
    @ExcelProperty(value = "支付状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=订单生成,1=支付中,2=支付成功,3=支付失败,4=已撤销,5=已退款,6=订单关闭,7=交易完成，无法退款")
    @ApiModelProperty("支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款")
    private Integer payOrderState;

    /**
     * 订单状态，1：待付款，2：已支付，3：交易成功，4：交易关闭
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=待付款,2=已支付,3=交易成功,4=交易关闭")
    @ApiModelProperty("订单状态，1：待付款，2：已支付，3：交易成功，4：交易关闭")
    private Integer orderState;

    /**
     * 退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭
     */
    @ExcelProperty(value = "退款状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未退款,1=订单生成,2=退款中,3=退款成功,4=退款失败,5=退款任务关闭")
    @ApiModelProperty("退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭")
    private  Integer refundOrderState;

    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     */
    @ExcelProperty(value = "通知游戏方状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=未通知,1=通知中,2=通知成功,3=通知失败")
    private Integer notifyState;

    /**
     * 订单金额,单位元
     */
    @ExcelProperty(value = "订单金额")
    @ApiModelProperty("订单金额,单位元")
    private BigDecimal orderAmount;

    @ExcelProperty(value = "下单时间")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 订单完成时间
     */
    @ExcelProperty(value = "订单完成时间")
    @ApiModelProperty("订单完成时间")
    private Date successTime;

    /**
     * 订单失效时间
     */
    @ApiModelProperty("订单失效时间")
    private Date expiredTime;

    /**
     * 会员id
     */
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 会员昵称
     */
    @ApiModelProperty("会员昵称")
    private String memberNickName;

    /**
     * 会员账号
     */
    @ApiModelProperty("会员账号")
    private String memberAccount;

    /**
     * 会员手机号
     */
    @ApiModelProperty("会员手机号")
    private String memberMobile;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    @ApiModelProperty("商品id")
    private String goodsId;

    /**
     * 外部订单号
     */
    @ExcelProperty(value = "外部订单号")
    @ApiModelProperty("外部订单号")
    private String outTradeNo;

    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量")
    @ApiModelProperty("商品数量")
    private  Integer goodsNum;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品价格，元
     */
    @ExcelProperty(value = "商品价格")
    @ApiModelProperty("商品价格，元")
    private BigDecimal goodsPrice;

    /**
     * 商品划线价格
     */
    @ExcelProperty(value = "商品划线价格")
    @ApiModelProperty("商品划线价格")
    private BigDecimal goodsScribePrice;

    /**
     * 商品缩略图
     */
    @ApiModelProperty("商品缩略图")
    private String goodsImg;

    /**
     * 商品描述
     */
    @ExcelProperty(value = "商品描述")
    @ApiModelProperty("商品描述")
    private String goodsDesc;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    @ExcelProperty(value = "游戏区服Id")
    @ApiModelProperty("游戏区服Id")
    private String gameServerId;

    @ExcelProperty(value = "游戏区服")
    @ApiModelProperty("游戏区服")
    private String gameServerName;

    /**
     * 游戏角色
     */
    @ExcelProperty(value = "游戏角色Id")
    @ApiModelProperty("游戏角色Id")
    private String gameRoleId;

    /**
     * 游戏角色
     */
    @ExcelProperty(value = "游戏角色")
    @ApiModelProperty("游戏角色")
    private String gameRoleName;

    /**
     * 主渠道id
     */
    @ApiModelProperty("主渠道id")
    private Integer gameChannelId;

    /**
     * 主渠道名称
     */
    @ExcelProperty(value = "渠道名称")
    @ApiModelProperty("渠道名称")
    private String gameChannelName;

    /**
     * 渠道号，分包号
     */
    @ExcelProperty(value = "渠道号")
    @ApiModelProperty("渠道号，分包号")
    private String gamePackageCode;

    /**
     * 扩展数据
     */
    @ApiModelProperty(value = "扩展数据")
    private  String extData;

    /**
     * 苹果的外部订单号
     */
    @ApiModelProperty(value = "苹果的外部订单号")
    private  String appleTradeNo;

}
