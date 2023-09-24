package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 留存数据统计视图对象
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("留存数据统计视图对象")
@ExcelIgnoreUnannotated
public class KeepDataStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    @ApiModelProperty("日期")
    private Date date;

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
     * 新增用户
     */
    @ExcelProperty(value = "新增用户")
    @ApiModelProperty("新增用户")
    private Integer newUserNum;

    /**
     * 2日留存
     */
    @ExcelProperty(value = "次日留存")
    @ApiModelProperty("次日留存")
    private String remain2;

    /**
     * 3日留存
     */
    @ExcelProperty(value = "3日留存")
    @ApiModelProperty("3日留存")
    private String remain3;

    /**
     * 4日留存
     */
    @ExcelProperty(value = "4日留存")
    @ApiModelProperty("4日留存")
    private String remain4;

    /**
     * 5日留存
     */
    @ExcelProperty(value = "5日留存")
    @ApiModelProperty("5日留存")
    private String remain5;

    /**
     * 6日留存
     */
    @ExcelProperty(value = "6日留存")
    @ApiModelProperty("6日留存")
    private String remain6;

    /**
     * 7日留存
     */
    @ExcelProperty(value = "7日留存")
    @ApiModelProperty("7日留存")
    private String remain7;

    /**
     * 15日留存
     */
    @ExcelProperty(value = "15日留存")
    @ApiModelProperty("15日留存")
    private String remain15;

    /**
     * 30日留存
     */
    @ExcelProperty(value = "30日留存")
    @ApiModelProperty("30日留存")
    private String remain30;

    /**
     * 60日留存
     */
    @ExcelProperty(value = "60日留存")
    @ApiModelProperty("60日留存")
    private String remain60;

    /**
     * 90日留存
     */
    @ExcelProperty(value = "90日留存")
    @ApiModelProperty("90日留存")
    private String remain90;

    /**
     * 2日
     */
    @ExcelProperty(value = "2日")
    @ApiModelProperty("2日")
    private Long day2;

    /**
     * 3日
     */
    @ExcelProperty(value = "3日")
    @ApiModelProperty("3日")
    private Long day3;

    /**
     * 4日
     */
    @ExcelProperty(value = "4日")
    @ApiModelProperty("4日")
    private Long day4;

    /**
     * 5日
     */
    @ExcelProperty(value = "5日")
    @ApiModelProperty("5日")
    private Long day5;

    /**
     * 6日
     */
    @ExcelProperty(value = "6日")
    @ApiModelProperty("6日")
    private Long day6;

    /**
     * 7日
     */
    @ExcelProperty(value = "7日")
    @ApiModelProperty("7日")
    private Long day7;

    /**
     * 15日
     */
    @ExcelProperty(value = "15日")
    @ApiModelProperty("15日")
    private Long day15;

    /**
     * 30日
     */
    @ExcelProperty(value = "30日")
    @ApiModelProperty("30日")
    private Long day30;

    /**
     * 60日
     */
    @ExcelProperty(value = "60日")
    @ApiModelProperty("60日")
    private Long day60;

    /**
     * 90日
     */
    @ExcelProperty(value = "90日")
    @ApiModelProperty("90日")
    private Long day90;
}
