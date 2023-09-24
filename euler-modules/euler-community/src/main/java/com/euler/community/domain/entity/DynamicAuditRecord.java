package com.euler.community.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 审核记录对象 dynamic_audit_record
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dynamic_audit_record")
public class DynamicAuditRecord extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 动态id
     */
    private Long dynamicId;
    /**
     * 审核人id
     */
    private Long auditId;
    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    private Integer auditStatus;
    /**
     * 审核原因
     */
    private String auditContent;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
