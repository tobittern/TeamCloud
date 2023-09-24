package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态操作错误日志视图对象 dynamic_operation_error_log
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("动态操作错误日志视图对象")
@ExcelIgnoreUnannotated
public class DynamicOperationErrorLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 动态id
     */
    @ExcelProperty(value = "动态id")
    @ApiModelProperty("动态id")
    private Long dynamicId;

    /**
     * 操作类型  A 数据入es报错  B 指定操作报错  C 当个字段修改报错
     */
    @ExcelProperty(value = "操作类型  A 数据入es报错  B 指定操作报错  C 当个字段修改报错")
    @ApiModelProperty("操作类型  A 数据入es报错  B 指定操作报错  C 当个字段修改报错")
    private String operationType;


}
