package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 动态转发对象 dynamic_forward
 *
 * @author euler
 * @date 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dynamic_forward")
public class DynamicForward extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 动态表主键id
     */
    private Long dynamicId;

    /**
     * 转发用户id
     */
    private Long memberId;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
