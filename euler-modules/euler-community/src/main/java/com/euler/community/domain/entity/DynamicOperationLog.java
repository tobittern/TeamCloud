package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态操作错误日志对象 dynamic_operation_log
 *
 * @author euler
 * @date 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dynamic_operation_log")
public class DynamicOperationLog extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 操作用户ID
     */
    private Long memberId;
    /**
     * 动态id
     */
    private Long dynamicId;
    /**
     * 操作类型
     */
    private Integer operationType;
    /**
     * 内容
     */
    private String operationContent;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
