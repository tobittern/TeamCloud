package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("自定义角色充值视图对象")
@ExcelIgnoreUnannotated
public class DiyRoleRechargeStatVo {


    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String memberId;

    /**
     * 用户注册时间，yyyy-MM-dd
     */
    @ExcelProperty(value = "角色创建时间")
    @ApiModelProperty("角色创建时间，yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String userDateId;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private String gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 渠道id
     */
    @ApiModelProperty("渠道id")
    private String channelId;

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
    @ExcelProperty(value = "游戏平台", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "game_operation_platform")
    @ApiModelProperty("游戏平台：1、安卓，2、ios，3、h5")
    private String operationPlatform;


    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private String roleId;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名")
    @ApiModelProperty("角色名称")
    private String roleName;


    /**
     * 服务器id
     */
    @ApiModelProperty("区服id")
    private String serverId;

    /**
     * 区服名称
     */
    @ExcelProperty("区服名称")
    @ApiModelProperty("区服名称")
    private String serverName;

    /**
     * 注册ip地址
     */
    @ExcelProperty("注册网络ip地址")
    @ApiModelProperty("注册网络ip地址")
    private String registerIp;


    /**
     * 今日充值
     */
    @ApiModelProperty("今日充值")
    @ExcelProperty("今日充值")
    private String todayCurrency;

    /**
     * 总充值
     */
    @ApiModelProperty("总充值")
    @ExcelProperty("总充值")
    private String totalCurrency;


    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("最后登录时间")
    private String lastLoginDate;
    /**
     * 游戏天数
     */
    @ApiModelProperty("游戏天数")
    @ExcelProperty("登录天数")
    private String gameDays="0";
    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    @ApiModelProperty("游戏时长，单位以小时显示，精确到小数点后1位")
    @ExcelProperty("在线时长")
    private String gameDuration ="0.0";


}
