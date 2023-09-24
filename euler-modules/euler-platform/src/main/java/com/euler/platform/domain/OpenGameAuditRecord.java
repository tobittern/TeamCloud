package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * 对象 open_game_audit_record
 *
 * @author open
 * @date 2022-02-21
 */
@Data
@TableName("open_game_audit_record")
public class OpenGameAuditRecord extends BaseEntity {

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
     * 游戏ID
     */
    private Integer gameId;
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
