package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 用户的认证的审核记录视图对象 open_user_auth_audit_record
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@ApiModel("用户的认证的审核记录视图对象")
@ExcelIgnoreUnannotated
public class OpenUserCertificationAuditRecordVo {

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
     * 用户认证提交信息的ID
     */
    @ExcelProperty(value = "用户认证提交信息的ID")
    @ApiModelProperty("用户认证提交信息的ID")
    private Integer userAuthId;

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
     * 操作行为描述
     */
    @ExcelProperty(value = "操作行为描述")
    @ApiModelProperty("操作行为描述")
    private String operationActionDescribe;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    @ApiModelProperty("审核原因")
    private String auditRecord;


    /**
     * 添加者
     */
    @ExcelProperty(value = "添加者")
    @ApiModelProperty("添加者")
    private String createBy;

    /**
     * 添加时间
     */
    @ExcelProperty(value = "添加时间")
    @ApiModelProperty("添加时间")
    private Date createTime;

}
