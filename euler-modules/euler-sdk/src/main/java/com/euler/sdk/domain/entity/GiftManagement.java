package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 礼包管理对象 gift_management
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_management")
public class GiftManagement extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 礼包组id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 礼包组名
     */
    private String giftGroupName;

    /**
     * 游戏id
     */
    private String gameId;

    /**
     * 礼包数量
     */
    private Integer giftAmount;

    /**
     * 礼包领取等级
     */
    private String receiveGrade;

    /**
     * 是否上架 1:上架 2:下架
     */
    private String isUp;

    /**
     * 礼包组图标
     */
    private String giftGroupIcon;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
