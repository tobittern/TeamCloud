package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 礼包活动管理对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_activity")
public class GiftActivity extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 礼包名
     */
    private String giftName;
    /**
     * 礼包介绍
     */
    private String giftIntroduction;
    /**
     * 礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包
     */
    private Integer giftType;
    /**
     * 礼包的一个icon
     */
    private String giftIcon;
    /**
     * 礼包的奖励条件
     */
    private Integer rewardConditions;
    /**
     * 礼包奖励类型 1 平台币 2余额  3积分
     */
    private Integer giftRewardType;
    /**
     * 礼包的奖励数量
     */
    private Integer giftRewardNums;
    /**
     * 排序
     */
    private String sort;
    /**
     * 是否上线 0上线 1下线
     */
    private Integer isOnline;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
