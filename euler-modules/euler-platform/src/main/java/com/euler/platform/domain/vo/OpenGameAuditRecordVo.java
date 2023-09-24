package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 视图对象 open_game_audit_record
 *
 * @author open
 * @date 2022-02-21
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class OpenGameAuditRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ExcelProperty(value = "主键自增")
    @ApiModelProperty("主键自增")
    private Integer id;

    /**
     * 审核用户id
     */
    @ExcelProperty(value = "审核用户id")
    @ApiModelProperty("审核用户id")
    private Long auditUserId;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 审核状态(1审核成功  2审核拒绝)
     */
    @ExcelProperty(value = "审核状态(1审核成功  2审核拒绝)")
    @ApiModelProperty("审核状态(1审核成功  2审核拒绝)")
    private Integer auditStatus;

    /**
     * 操作行为 1提交审核申请 2上架 3下架 4通过 5驳回
     */
    @ExcelProperty(value = "操作行为 1提交审核申请 2上架 3下架 4通过 5驳回")
    @ApiModelProperty("操作行为 1提交审核申请 2上架 3下架 4通过 5驳回")
    private Integer operationAction;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    @ApiModelProperty("审核原因")
    private String auditRecord;

    /**
     * 审核人或者提交人
     */
    @ExcelProperty(value = "审核人或者提交人")
    @ApiModelProperty("审核人或者提交人")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
