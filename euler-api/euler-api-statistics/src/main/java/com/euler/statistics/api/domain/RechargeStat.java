package com.euler.statistics.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值数据统计对象 recharge_stat
 *
 * @author euler
 * @date 2022-04-29
 */
@Data
@TableName("recharge_stat")
public class RechargeStat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 数据批次号，yyyyMMddHHmmss
     */
    private Long batchNo;
    /**
     * 日期id
     */
    private Date dateId;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 角色注册时间，yyyy-MM-dd
     */
    private Date userDateId;
    /**
     * 用户注册时间，yyyy-MM-dd
     */
    private Date memberDateId;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道号
     */
    private String channelPackageCode;
    /**
     * 游戏平台：1、安卓，2、ios，3、h5
     */
    private String operationPlatform;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 充值类型：1、平台币，2、余额，3：游戏消费
     */
    private String rechargeType;
    /**
     * 订单类型：P、平台消费，G、游戏消费
     */
    private String orderType;
    /**
     * 支付渠道，支付宝app支付：ali_MobilePay，支付宝H5支付：ali_WebMobilePay，微信app支付：wx_MobilePay，微信H5支付：wx_WebMobilePay，钱包余额：wallet_balance，钱包平台币：wallet_platform
     */
    private String payChannel;
    /**
     * 商品类型：1、年费商品，2、充值商品，3、游戏商品
     */
    private String goodsType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 注册ip
     */
    private String registerIp;

    /**
     * 服务器id
     */
    private String serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 数据类型，1：充值数据，2：角色充值数据
     */
    private String dataType;

    /**
     * 游戏天数
     */
    private Integer gameDays;
    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    private Float gameDuration;


}
