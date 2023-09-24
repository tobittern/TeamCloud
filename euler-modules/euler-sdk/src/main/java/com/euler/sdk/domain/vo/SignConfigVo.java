package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 sign_config
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class SignConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 星期几
     */
    @ExcelProperty(value = "星期几")
    @ApiModelProperty("星期几")
    private Integer week;

    /**
     * 分数
     */
    @ExcelProperty(value = "分数")
    @ApiModelProperty("分数")
    private Integer score;


}
