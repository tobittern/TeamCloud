package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关注对象 attention
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("attention")
public class Attention extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 关注用户id
     */
    private Long attentionUserId;

    /**
     * 状态 1:未关注 2:已关注 3:互相关注
     */
    private String status;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
