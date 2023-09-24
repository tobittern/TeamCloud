package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 数据统计 - 每日的ltv基础数据统计视图对象 statistics_everyday_ltv_basedata
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("数据统计 - 每日的ltv基础数据统计视图对象")
@ExcelIgnoreUnannotated
public class BasicStatisticsReturnVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期标签
     */
    @ExcelProperty(value = "日期标签")
    @ApiModelProperty("日期标签")
    private String dateLabel;

    /**
     * 主渠道id
     */
    @ExcelProperty(value = "主渠道id")
    @ApiModelProperty("主渠道id")
    private Integer channelId;

    /**
     * 主渠道名
     */
    @ExcelProperty(value = "主渠道名")
    @ApiModelProperty("主渠道名")
    private String channelName;

    /**
     * 渠道code
     */
    @ExcelProperty(value = "渠道code")
    @ApiModelProperty("渠道code")
    private String packageCode;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 运行平台
     */
    @ExcelProperty(value = "运行平台", converter = ExcelDictConvert.class)
    @ApiModelProperty("运行平台")
    @ExcelDictFormat(dictType = "game_operation_platform")
    private String operationPlatform;

    /**
     * 新增用户数
     */
    @ExcelProperty(value = "新增用户数")
    @ApiModelProperty("新增用户数")
    private Integer newIncrUserNum;

    /**
     * 新增付费用户数
     */
    @ExcelProperty(value = "新增付费用户数")
    @ApiModelProperty("新增付费用户数")
    private Integer newIncrPayNum;

    /**
     * 新增付费订单数
     */
    @ApiModelProperty("新增付费订单数")
    private Integer newIncrPayOrderNum;

    /**
     * 新增付费率
     */
    @ExcelProperty(value = "新增付费率(%)")
    @ApiModelProperty("新增付费率")
    private double newIncrPayLv;

    /**
     * 新增付费金额
     */
    @ExcelProperty(value = "新增付费金额")
    @ApiModelProperty("新增付费金额")
    private BigDecimal newIncrPayAmount;

    @ExcelProperty(value = "1日LTV")
    @ApiModelProperty("1日LTV")
    private double oneDay = new Double(0);

    @ExcelProperty(value = "2日LTV")
    @ApiModelProperty("2日LTV")
    private double twoDay = new Double(0);

    @ExcelProperty(value = "3日LTV")
    @ApiModelProperty("3日LTV")
    private double threeDay = new Double(0);

    @ExcelProperty(value = "4日LTV")
    @ApiModelProperty("5日LTV")
    private double fourDay = new Double(0);

    @ExcelProperty(value = "5日LTV")
    @ApiModelProperty("5日LTV")
    private double fiveDay = new Double(0);

    @ExcelProperty(value = "6日LTV")
    @ApiModelProperty("6日LTV")
    private double sixDay = new Double(0);

    @ExcelProperty(value = "7日LTV")
    @ApiModelProperty("7日LTV")
    private double sevenDay = new Double(0);

    @ExcelProperty(value = "15日LTV")
    @ApiModelProperty("15日LTV")
    private double fifteenDay = new Double(0);

    @ExcelProperty(value = "30日LTV")
    @ApiModelProperty("30日LTV")
    private double thirtyDay = new Double(0);

    @ExcelProperty(value = "60日LTV")
    @ApiModelProperty("60日LTV")
    private double sixtyDay = new Double(0);

    @ExcelProperty(value = "90日LTV")
    @ApiModelProperty("90日LTV")
    private double ninetyDay = new Double(0);
}
