package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 行为类型视图对象 behavior_type
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("行为类型视图对象")
@ExcelIgnoreUnannotated
public class BehaviorTypeVo {

    private static final long serialVersionUID = 1L;

    /**
     * 行为类型id
     */
    @ExcelProperty(value = "行为类型id")
    @ApiModelProperty("行为类型id")
    private Integer id;

    /**
     * 平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    @ExcelProperty(value = "平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    @ApiModelProperty("平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    private String platform;

    /**
     * 设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序
     */
    @ExcelProperty(value = "设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    @ApiModelProperty("设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    private String device;

    /**
     * 模块
     */
    @ExcelProperty(value = "模块")
    @ApiModelProperty("模块")
    private String model;

    /**
     * 行为类型标识
     */
    @ExcelProperty(value = "行为类型标识")
    @ApiModelProperty("行为类型标识")
    private String code;

    /**
     * 行为请求url
     */
    @ExcelProperty(value = "行为请求url")
    @ApiModelProperty("行为请求url")
    private String path;

    /**
     * 行为类型名称
     */
    @ExcelProperty(value = "行为类型名称")
    @ApiModelProperty("行为类型名称")
    private String name;

    /**
     * 行为类型描述
     */
    @ExcelProperty(value = "行为类型描述")
    @ApiModelProperty("行为类型描述")
    private String description;

    /**
     *处理数据反射类表达式
     */
    @ApiModelProperty("处理数据反射类表达式")
    private String reflectExpression;


}
