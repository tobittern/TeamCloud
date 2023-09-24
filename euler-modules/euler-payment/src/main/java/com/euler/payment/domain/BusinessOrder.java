package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("business_order")
public class BusinessOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final Integer ORDER_STATE_INIT = 1; //待付款
    public static final Integer ORDER_STATE_PAY = 2; //已支付
    public static final Integer ORDER_STATE_SUCCESS = 3; //交易成功
    public static final Integer ORDER_STATE_CLOSE = 4; //交易关闭

    /**
     * 业务订单号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 订单类型，G：游戏消费，P：平台消费
     */
    private String orderType;
    /**
     * 支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform, 苹果内购：apple_iap
     */
    private String payChannel;
    /**
     * 支付状态: 支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭, 7-交易完成，无法退款
     */
    private Integer payOrderState;
    /**
     * 订单状态，1：待付款，2：已支付，3：交易成功，4：交易关闭
     */
    private Integer orderState;

    /**
     * 退款状态:0-未退款,1-订单生成,2-退款中,3-退款成功,4-退款失败,5-退款任务关闭
     */
    private Integer refundOrderState;

    /**
     * 订单金额,单位元
     */
    private BigDecimal orderAmount;
    /**
     * 订单完成时间
     */
    private Date successTime;
    /**
     * 订单失效时间
     */
    private Date expiredTime;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 会员昵称
     */
    private String memberNickName;
    /**
     * 会员账号
     */
    private String memberAccount;
    /**
     * 会员手机号
     */
    private String memberMobile;

    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格，元
     */
    private BigDecimal goodsPrice;
    /**
     * 商品划线价格
     */
    private BigDecimal goodsScribePrice;
    /**
     * 商品缩略图
     */
    private String goodsImg;
    /**
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 商品数量
     */
    private  Integer goodsNum;

    /**
     * 外部订单号
     */
    private String outTradeNo;

    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏区服
     */
    private String gameServerId;
    /**
     * 游戏区服
     */
    private String gameServerName;

    /**
     * 游戏角色
     */
    private String gameRoleId;

    /**
     * 游戏角色
     */
    private String gameRoleName;

    /**
     * 主渠道id
     */
    private Integer gameChannelId;
    /**
     * 主渠道名称
     */
    private String gameChannelName;
    /**
     * 渠道号，分包号
     */
    private String gamePackageCode;

    /**
     *扩展数据，提交订单时的扩展数据
     */
    private  String extData;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     */
    private Integer notifyState;

    /**
     * 苹果的外部订单号
     */
    private  String appleTradeNo;

}
