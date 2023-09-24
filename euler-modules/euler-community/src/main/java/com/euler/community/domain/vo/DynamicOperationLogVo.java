package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态操作错误日志视图对象 dynamic_operation_log
 *
 * @author euler
 * @date 2022-06-20
 */
@Data
@ApiModel("动态操作错误日志视图对象")
@ExcelIgnoreUnannotated
public class DynamicOperationLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 操作用户ID
     */
    @ExcelProperty(value = "操作用户ID")
    @ApiModelProperty("操作用户ID")
    private Long memberId;

    /**
     * 动态id
     */
    @ExcelProperty(value = "动态id")
    @ApiModelProperty("动态id")
    private Long dynamicId;

    /**
     * 操作类型
     */
    @ExcelProperty(value = "操作类型")
    @ApiModelProperty("操作类型")
    private Integer operationType;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @ApiModelProperty("内容")
    private String operationContent;

    /**
     * 操作者
     */
    @ExcelProperty(value = "操作者")
    @ApiModelProperty("操作者")
    private String createBy;

}
