package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 活动对象 activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity")
public class Activity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动banner
     */
    private String activeBanner;
    /**
     * 活动类型( 1：限时折扣模块  2：热门活动模块)
     */
    private String activityType;
    /**
     * 跳转地址
     */
    private String jumpAddress;
    /**
     * 活动开启时间
     */
    private Date activityStartTime;
    /**
     * 活动关闭时间
     */
    private Date activityClosingTime;
    /**
     * 是否上线 0上线  1下线
     */
    private Integer isOnline;
    /**
     * 删除状态 0正常 2删除
     */
    @TableLogic
    private String delFlag;

}
