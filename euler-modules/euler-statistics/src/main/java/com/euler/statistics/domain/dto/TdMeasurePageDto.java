package com.euler.statistics.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

import com.euler.common.mybatis.core.page.PageQuery;

import java.util.Date;


/**
 * 指标维分页业务对象 td_measure
 *
 * @author euler
 * @date 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("指标维分页业务对象")
public class TdMeasurePageDto extends PageQuery {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 模块code
     */
    @ApiModelProperty(value = "模块code")
    private String modelId;

    /**
     * 模块
     */
    @ApiModelProperty(value = "模块")
    private String modelName;

    /**
     * 指标标识
     */
    @ApiModelProperty(value = "指标标识")
    private String measureId;

    /**
     * 指标名称
     */
    @ApiModelProperty(value = "指标名称")
    private String measureName;

    /**
     * 指标描述
     */
    @ApiModelProperty(value = "指标描述")
    private String measureDesc;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;
}
