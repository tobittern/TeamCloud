package com.euler.platform.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 用户的认证的审核记录业务对象 open_user_auth_audit_record
 *
 * @author open
 * @date 2022-02-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户的认证的审核记录业务对象")
public class OpenUserCertificationAuditRecordBo extends BaseEntity {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增", required = true)
    private Integer id;

    /**
     * 审核用户id
     */
    @ApiModelProperty(value = "审核用户id", required = true)
    private Long auditUserId;

    /**
     * 用户认证提交信息的ID
     */
    @ApiModelProperty(value = "用户认证提交信息的ID", required = true)
    @NotNull(message = "用户认证提交信息的ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer userAuthId;

    /**
     * 审核状态(1审核成功  2审核拒绝)
     */
    @ApiModelProperty(value = "审核状态(1审核成功  2审核拒绝)", required = true)
    @NotNull(message = "审核状态(1审核成功  2审核拒绝)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer auditStatus;

    /**
     * 操作行为 1提交审核申请 2上架 3下架 4通过 5驳回
     */
    @Xss(message = "操作行为 1提交审核申请 2上架 3下架 4通过 5驳回")
    @ApiModelProperty(value = "操作行为 1提交审核申请 2上架 3下架 4通过 5驳回")
    private Integer operationAction;

    /**
     * 审核原因
     */
    @Xss(message = "审核原因不能包含脚本字符")
    @ApiModelProperty(value = "审核原因", required = true)
    private String auditRecord;


}
