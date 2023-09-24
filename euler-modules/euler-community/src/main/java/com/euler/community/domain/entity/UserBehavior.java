package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 用户行为记录对象 user_behavior
 *
 * @author euler
 * @date 2022-07-14
 */
@Data
@TableName("user_behavior")
public class UserBehavior {

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
     * 类型
     */
    private Integer type;
    /**
     * 动态ID
     */
    private Long relationId;
    /**
     * 创建时间
     */
    private String time;

    /**
     * 更新时间
     */
    private Date updateTime;

}
