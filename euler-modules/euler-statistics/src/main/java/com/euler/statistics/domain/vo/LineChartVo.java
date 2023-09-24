package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 折线数据
 */
@ApiModel("折线数据")
@Data
public class LineChartVo {
    /**
     * 日期
     */
    @ApiModelProperty("日期")
    private String dateId;
    /**
     * 指标标识
     */
    @ApiModelProperty("指标标识")
    private String measureId;
    /**
     * 指标值
     */
    @ApiModelProperty("指标值")
    private String measureValue;

    /**
     * 指标单位
     */
    @ApiModelProperty("指标单位")
    private String measureUnit;

    /**
     * 变动值
     */
    @ApiModelProperty("变动值")
    private String changeValue="";

    /**
     * 变动类型，1：增加，0：不变，-1：减少
     */
    @ApiModelProperty("变动类型，1：增加，0：不变，-1：减少")
    private Integer changeFlag=0;
}
