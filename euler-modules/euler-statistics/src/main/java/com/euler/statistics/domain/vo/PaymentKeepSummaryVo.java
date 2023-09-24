package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 付费留存数据汇总
 *
 * @author euler
 * @date 2022-10-09
 */
@Data
@ApiModel("付费留存数据汇总")
@ExcelIgnoreUnannotated
public class PaymentKeepSummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 付费用户汇总
     */
    @ExcelProperty(value = "付费用户汇总")
    @ApiModelProperty("付费用户汇总")
    private Integer paymentUserSum = 0;

    /**
     * 2日留存汇总
     */
    @ExcelProperty(value = "2日留存汇总")
    @ApiModelProperty("2日留存汇总")
    private BigDecimal remain2Sum = new BigDecimal(0);

    /**
     * 3日留存汇总
     */
    @ExcelProperty(value = "3日留存汇总")
    @ApiModelProperty("3日留存汇总")
    private BigDecimal remain3Sum = new BigDecimal(0);

    /**
     * 4日留存汇总
     */
    @ExcelProperty(value = "4日留存汇总")
    @ApiModelProperty("4日留存汇总")
    private BigDecimal remain4Sum = new BigDecimal(0);

    /**
     * 5日留存汇总
     */
    @ExcelProperty(value = "5日留存汇总")
    @ApiModelProperty("5日留存汇总")
    private BigDecimal remain5Sum = new BigDecimal(0);

    /**
     * 6日留存汇总
     */
    @ExcelProperty(value = "6日留存汇总")
    @ApiModelProperty("6日留存汇总")
    private BigDecimal remain6Sum = new BigDecimal(0);

    /**
     * 7日留存汇总
     */
    @ExcelProperty(value = "7日留存汇总")
    @ApiModelProperty("7日留存汇总")
    private BigDecimal remain7Sum = new BigDecimal(0);

    /**
     * 15日留存汇总
     */
    @ExcelProperty(value = "15日留存汇总")
    @ApiModelProperty("15日留存汇总")
    private BigDecimal remain15Sum = new BigDecimal(0);

    /**
     * 30日留存汇总
     */
    @ExcelProperty(value = "30日留存汇总")
    @ApiModelProperty("30日留存汇总")
    private BigDecimal remain30Sum = new BigDecimal(0);

    /**
     * 60日留存汇总
     */
    @ExcelProperty(value = "60日留存汇总")
    @ApiModelProperty("60日留存汇总")
    private BigDecimal remain60Sum = new BigDecimal(0);

    /**
     * 90日留存汇总
     */
    @ExcelProperty(value = "90日留存汇总")
    @ApiModelProperty("90日留存汇总")
    private BigDecimal remain90Sum = new BigDecimal(0);

    /**
     * 2日
     */
    @ExcelProperty(value = "2日")
    @ApiModelProperty("2日")
    private Long day2Sum = 0L;

    /**
     * 3日
     */
    @ExcelProperty(value = "3日")
    @ApiModelProperty("3日")
    private Long day3Sum = 0L;

    /**
     * 4日
     */
    @ExcelProperty(value = "4日")
    @ApiModelProperty("4日")
    private Long day4Sum = 0L;

    /**
     * 5日
     */
    @ExcelProperty(value = "5日")
    @ApiModelProperty("5日")
    private Long day5Sum = 0L;

    /**
     * 6日
     */
    @ExcelProperty(value = "6日")
    @ApiModelProperty("6日")
    private Long day6Sum = 0L;

    /**
     * 7日
     */
    @ExcelProperty(value = "7日")
    @ApiModelProperty("7日")
    private Long day7Sum = 0L;

    /**
     * 15日
     */
    @ExcelProperty(value = "15日")
    @ApiModelProperty("15日")
    private Long day15Sum = 0L;

    /**
     * 30日
     */
    @ExcelProperty(value = "30日")
    @ApiModelProperty("30日")
    private Long day30Sum = 0L;

    /**
     * 60日
     */
    @ExcelProperty(value = "60日")
    @ApiModelProperty("60日")
    private Long day60Sum = 0L;

    /**
     * 90日
     */
    @ExcelProperty(value = "90日")
    @ApiModelProperty("90日")
    private Long day90Sum = 0L;

}
