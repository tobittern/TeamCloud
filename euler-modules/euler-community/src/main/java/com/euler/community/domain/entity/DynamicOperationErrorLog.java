package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态操作错误日志对象 dynamic_operation_error_log
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dynamic_operation_error_log")
public class DynamicOperationErrorLog extends BaseEntity {

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
     * 操作类型  A 数据入es报错
     */
    private String operationType;
    /**
     * 错误内容
     */
    private String errorContent;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
