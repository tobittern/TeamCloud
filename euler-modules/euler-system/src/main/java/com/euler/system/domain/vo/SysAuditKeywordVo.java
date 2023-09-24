package com.euler.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 审核关键词 - 敏感词视图对象 audit_keyword
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("审核关键词 - 敏感词视图对象")
@ExcelIgnoreUnannotated
public class SysAuditKeywordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 添加用户id
     */
    @ExcelProperty(value = "添加用户id")
    @ApiModelProperty("添加用户id")
    private Long userId;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    @ApiModelProperty("类型")
    private Integer type;

    /**
     * 敏感词
     */
    @ExcelProperty(value = "敏感词")
    @ApiModelProperty("敏感词")
    private String keywords;

    /**
     * 是否有效
     */
    @ExcelProperty(value = "是否有效")
    @ApiModelProperty("是否有效")
    private Integer isItValid;


}
