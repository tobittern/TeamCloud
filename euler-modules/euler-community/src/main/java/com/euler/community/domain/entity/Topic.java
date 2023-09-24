package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 话题对象 topic
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("topic")
public class Topic extends BaseEntity {

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
     * 话题名称
     */
    private String topicName;

    /**
     * 话题访问次数
     */
    private Long searchCount;

    /**
     * 话题曝光次数
     */
    private Long exposureCount;

    /**
     * 状态： 0:未发布 1:已发布
     */
    private String status;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
