package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("自定义充值视图对象")
@ExcelIgnoreUnannotated
public class DiyConsumptionRechargeStatVo {
    /**
     * 日期id
     */
    @ExcelProperty(value = "充值时间")
    @ApiModelProperty("充值时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String memberId;

    /**
     * 用户注册时间，yyyy-MM-dd
     */
    @ExcelProperty(value = "用户注册时间")
    @ApiModelProperty("用户注册时间，yyyy-MM-dd")
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
    @ApiModelProperty("游戏平台：1、安卓，2、ios，3、h5")
    @ExcelProperty(value = "游戏平台", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "game_operation_platform")
    private String operationPlatform;

    /**
     * 现金充值金额
     */
    @ExcelProperty(value = "现金充值金额")
    @ApiModelProperty("现金充值金额")
    private String cashCurrency = "0";

    /**
     * 余额充值金额
     */
    @ExcelProperty(value = "余额充值金额")
    @ApiModelProperty("余额充值金额")
    private String balanceCurrency = "0";

    /**
     * 平台币金额
     */
    @ExcelProperty(value = "平台币金额")
    @ApiModelProperty("平台币金额")
    private String platformCurrency = "0";


}
