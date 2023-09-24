package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;


/**
 * 用户的认证的审核记录对象 open_user_auth_audit_record
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@TableName("open_user_certification_audit_record")
public class OpenUserCertificationAuditRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键自增
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 审核用户id
     */
    private Long auditUserId;
    /**
     * 用户认证提交信息的ID
     */
    private Integer userAuthId;
    /**
     * 审核状态(1审核成功  2审核拒绝)
     */
    private Integer auditStatus;
    /**
     * 操作行为 1提交审核申请 2上架 3下架 4通过 5驳回
     */
    private Integer operationAction;
    /**
     * 审核原因
     */
    private String auditRecord;
    /**
     * 删除状态
     */
    @TableLogic
    private String delFlag;

}
