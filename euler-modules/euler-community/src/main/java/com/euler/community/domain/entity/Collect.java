package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态收藏对象 collect
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("collect")
public class Collect extends BaseEntity {

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
     * 收藏用户id
     */
    private Long memberId;
    /**
     * 收藏状态 1 收藏中  2取消收藏
     */
    private String status;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
