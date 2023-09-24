package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分体系对象 score_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("score_system")
public class ScoreSystem extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 游戏用户id
     */
    private Long userId;

    /**
     * 积分
     */
    private Long score;

    /**
     * 类型 0:签到积分 1:首次注册积分
     */
    private String type;

    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;

}
