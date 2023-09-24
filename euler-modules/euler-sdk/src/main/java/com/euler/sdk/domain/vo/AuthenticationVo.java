package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("活动视图对象")
@ExcelIgnoreUnannotated
public class AuthenticationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @ExcelProperty(value = "活动id")
    @ApiModelProperty("活动id")
    private Integer errcode;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private String errmsg;

    /**
     * data
     */
    private AuthenticationResultVo data;

}
