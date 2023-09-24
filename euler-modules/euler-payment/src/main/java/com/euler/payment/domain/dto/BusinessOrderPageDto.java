package com.euler.payment.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("订单搜索视图对象")
public class BusinessOrderPageDto extends PageQuery {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "订单号")
    private String businessOrderId;

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty("会员手机号")
    private String memberMobile;

    @ApiModelProperty(value = "支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭")
    private Integer payOrderState;

    @ApiModelProperty("订单状态，1：待付款，2：已支付，3：交易成功，4：交易关闭")
    private Integer orderState;

    @ApiModelProperty(value = "订单类型，P：平台消费，G：游戏消费")
    private String orderType;

    @ApiModelProperty("支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap")
    private String payChannel;

    @ApiModelProperty("会员昵称")
    private String memberNickName;

    @ApiModelProperty("下单开始时间")
    private String beginTime;

    @ApiModelProperty("下单结束时间")
    private String endTime;

    @ApiModelProperty("支付开始时间")
    private String payBeginTime;

    @ApiModelProperty("支付结束时间")
    private String payEndTime;

    @ApiModelProperty("主渠道名称，渠道组")
    private  String gameChannel;

    @ApiModelProperty("渠道号，分包号")
    private  String gamePackageCode;

    @ApiModelProperty("支付游戏")
    private  String gameName;

    @ApiModelProperty("游戏区服")
    private String gameServerName;

    @ApiModelProperty("游戏角色")
    private String gameRoleName;

    @ApiModelProperty("退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭")
    private  Integer refundOrderState;

    @ApiModelProperty(value = "主渠道id")
    private Integer channelId;

    /**
     * 游戏角色id
     */
    @ApiModelProperty("游戏角色id")
    private String gameRoleId;

    /**
     * 外部订单号
     */
    @ApiModelProperty("外部订单号")
    private String outTradeNo;

    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     */
    @ExcelProperty(value = "通知状态")
    private Integer notifyState;

}
