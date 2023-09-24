package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 审核记录视图对象 dynamic_audit_record
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("审核记录视图对象")
@ExcelIgnoreUnannotated
public class DynamicAuditRecordVo {

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
     * 审核人id
     */
    @ExcelProperty(value = "审核人id")
    @ApiModelProperty("审核人id")
    private Long auditId;

    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    @ExcelProperty(value = "审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝")
    @ApiModelProperty("审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝")
    private Integer auditStatus;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    @ApiModelProperty("审核原因")
    private String auditContent;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private Date auditTime;


    /**
     * 审核人
     */
    @ExcelProperty(value = "审核人")
    @ApiModelProperty("审核人")
    private String createBy;


}
