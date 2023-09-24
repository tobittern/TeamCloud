package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class MemberShieldVo implements Serializable  {

    private static final long serialVersionUID = 1L;
    /**
     * 业务id
     */
    @ExcelProperty(value = "业务id")
    @ApiModelProperty("业务id")
    private Long businessId;


}
