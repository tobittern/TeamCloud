package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * 屏蔽信息视图对象 shield
 *
 * @author euler
 * @date 2022-09-15
 */
@Data
@ApiModel("屏蔽信息视图对象")
@ExcelIgnoreUnannotated
public class ShieldVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 业务id
     */
    @ExcelProperty(value = "业务id")
    @ApiModelProperty("业务id")
    private Long businessId;

    /**
     * 业务类型，1：用户：2动态
     */
    @ExcelProperty(value = "业务类型，1：用户：2动态")
    @ApiModelProperty("业务类型，1：用户：2动态")
    private Integer businessType;

    /**
     * 原因
     */
    @ExcelProperty(value = "原因")
    @ApiModelProperty("原因")
    private String reason;


}
