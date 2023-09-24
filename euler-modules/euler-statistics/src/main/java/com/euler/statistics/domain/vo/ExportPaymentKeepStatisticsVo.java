package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 付费留存数据统计视图对象
 *
 * @author euler
 * @date 2022-10-09
 */
@Data
@ApiModel("付费留存数据统计视图对象")
@ExcelIgnoreUnannotated
public class ExportPaymentKeepStatisticsVo {
    private static final long serialVersionUID = 1L;

    /**
     * 日期-画面展示用
     */
    @ExcelProperty(value = "日期")
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateStr;

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
    private String packageCode;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "游戏平台", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "game_operation_platform")
    private String operationPlatform;

    /**
     * 付费用户
     */
    @ExcelProperty(value = "付费用户")
    @ApiModelProperty("付费用户")
    private Integer paymentUserNum;

    /**
     * 次日留存
     */
    @ExcelProperty(value = "次日")
    @ApiModelProperty("次日")
    private String remain2;

    /**
     * 3日留存
     */
    @ExcelProperty(value = "3日")
    @ApiModelProperty("3日")
    private String remain3;

    /**
     * 4日留存
     */
    @ExcelProperty(value = "4日")
    @ApiModelProperty("4日")
    private String remain4;

    /**
     * 5日留存
     */
    @ExcelProperty(value = "5日")
    @ApiModelProperty("5日")
    private String remain5;

    /**
     * 6日留存
     */
    @ExcelProperty(value = "6日")
    @ApiModelProperty("6日")
    private String remain6;

    /**
     * 7日留存
     */
    @ExcelProperty(value = "7日")
    @ApiModelProperty("7日")
    private String remain7;

    /**
     * 15日留存
     */
    @ExcelProperty(value = "15日")
    @ApiModelProperty("15日")
    private String remain15;

    /**
     * 30日留存
     */
    @ExcelProperty(value = "30日")
    @ApiModelProperty("30日")
    private String remain30;

    /**
     * 60日留存
     */
    @ExcelProperty(value = "60日")
    @ApiModelProperty("60日")
    private String remain60;

    /**
     * 90日留存
     */
    @ExcelProperty(value = "90日")
    @ApiModelProperty("90日")
    private String remain90;

}
