package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户相关的标准指标统计视图对象 tf_user_quota
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@ApiModel("用户相关的标准指标统计视图对象")
@ExcelIgnoreUnannotated
public class TfUserQuotaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private Integer id;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    @ApiModelProperty("日期")
    private String dateId;

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
     * 指标标识
     */
    @ExcelProperty(value = "指标标识")
    @ApiModelProperty("指标标识")
    private String measureId;

    /**
     * 指标值
     */
    @ExcelProperty(value = "指标值")
    @ApiModelProperty("指标值")
    private BigDecimal measureValue;


}
