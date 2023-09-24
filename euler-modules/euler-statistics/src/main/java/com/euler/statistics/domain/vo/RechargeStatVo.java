package com.euler.statistics.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 充值视图对象 recharge_stat
 *
 * @author euler
 * @date 2022-04-29
 */
@Data
@ApiModel("充值视图对象")
@ExcelIgnoreUnannotated
public class RechargeStatVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 数据批次号，yyyyMMddHHmmss
     */
    @ApiModelProperty("数据批次号，yyyyMMddHHmmss")
    private Long batchNo;

    /**
     * 日期id
     */
    @ExcelProperty(value = "日期id")
    @ApiModelProperty("日期id")
    private Date dateId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 角色注册时间，yyyy-MM-dd
     */
    @ExcelProperty(value = "角色注册时间")
    @ApiModelProperty("角色注册时间，yyyy-MM-dd")
    private Date userDateId;
    /**
     * 用户注册时间，yyyy-MM-dd
     */
    @ExcelProperty(value = "用户注册时间")
    @ApiModelProperty("用户注册时间，yyyy-MM-dd")
    private Date memberDateId;

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

    /**
     * 角色id
     */
    @ExcelProperty(value = "角色id")
    @ApiModelProperty("角色id")
    private String roleId;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 渠道id
     */
    @ExcelProperty(value = "渠道id")
    @ApiModelProperty("渠道id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ExcelProperty(value = "渠道名称")
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 渠道号
     */
    @ExcelProperty(value = "渠道号")
    @ApiModelProperty("渠道号")
    private String channelPackageCode;

    /**
     * 游戏平台：1、安卓，2、ios，3、h5
     */
    @ExcelProperty(value = "游戏平台")
    @ApiModelProperty("游戏平台：1、安卓，2、ios，3、h5")
    private String operationPlatform;

    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    /**
     * 订单id
     */
    @ExcelProperty(value = "订单id")
    @ApiModelProperty("订单id")
    private String orderId;

    /**
     * 充值类型：1、平台币，2、余额，3：游戏消费
     */
    @ExcelProperty(value = "充值类型")
    @ApiModelProperty("充值类型：1、平台币，2、余额，3：游戏消费")
    private String rechargeType;

    /**
     * 订单类型：P、平台消费，G、游戏消费
     */
    @ExcelProperty(value = "订单类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "G=游戏消费,P=平台消费")
    private String orderType;

    /**
     * 商品类型：1、年费商品，2、充值商品，3、游戏商品
     */
    @ExcelProperty(value = "商品类型")
    @ApiModelProperty("商品类型：1、年费商品，2、充值商品，3、游戏商品")
    private String goodsType;


    /**
     * 服务器id
     */
    @ApiModelProperty("服务器id")
    private String serverId;

    /**
     * 服务器名称
     */
    @ApiModelProperty("服务器名称")
    private String serverName;

    /**
     * 注册ip地址
     */
    @ApiModelProperty("注册ip地址")
    private String registerIp;


}
