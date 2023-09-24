package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 礼包对象 gift_info
 *
 * @author euler
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_info")
public class GiftInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 礼包id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 礼包名称
     */
    private String giftName;

    /**
     * 礼包组id
     */
    private Integer giftGroupId;

    /**
     * 礼包组名称
     */
    private String giftGroupName;

    /**
     * 游戏id
     */
    private String gameId;

    /**
     * 礼包领取等级
     */
    private Integer receiveGrade;

    /**
     * 礼包介绍
     */
    private String giftIntroduce;

    /**
     * 礼包类型 1:红包 2:平台币 3:积分
     */
    private String type;

    /**
     * 奖励数量
     */
    private Integer rewardAmount;

    /**
     * 礼包图标
     */
    private String giftIcon;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
