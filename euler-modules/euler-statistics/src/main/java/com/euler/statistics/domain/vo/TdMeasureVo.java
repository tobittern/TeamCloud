package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 指标维视图对象 td_measure
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@ApiModel("指标维视图对象")
@ExcelIgnoreUnannotated
public class TdMeasureVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 模块code
     */
    @ExcelProperty(value = "模块code")
    @ApiModelProperty("模块code")
    private String modelId;

    /**
     * 模块
     */
    @ExcelProperty(value = "模块")
    @ApiModelProperty("模块")
    private String modelName;

    /**
     * 指标标识
     */
    @ExcelProperty(value = "指标标识")
    @ApiModelProperty("指标标识")
    private String measureId;

    /**
     * 指标名称
     */
    @ExcelProperty(value = "指标名称")
    @ApiModelProperty("指标名称")
    private String measureName;

    /**
     * 指标描述
     */
    @ExcelProperty(value = "指标描述")
    @ApiModelProperty("指标描述")
    private String measureDesc;


}
