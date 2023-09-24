package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 礼包活动管理业务对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包活动管理业务对象")
public class GiftActivityBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    @NotNull(message = "游戏ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameId;

    /**
     * 礼包名
     */
    @ApiModelProperty(value = "礼包名", required = true)
    @NotBlank(message = "礼包名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftName;

    /**
     * 礼包介绍
     */
    @ApiModelProperty(value = "礼包介绍", required = true)
    @NotBlank(message = "礼包介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftIntroduction;

    /**
     * 礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包
     */
    @ApiModelProperty(value = "礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包", required = true)
    @NotNull(message = "礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包 不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer giftType;

    /**
     * 礼包的一个icon
     */
    @ApiModelProperty(value = "礼包的一个icon", required = true)
    @NotBlank(message = "礼包的一个icon不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftIcon;

    /**
     * 礼包的奖励条件
     */
    @ApiModelProperty(value = "礼包的奖励条件", required = true)
    @NotNull(message = "礼包的奖励条件不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer rewardConditions;

    /**
     * 礼包奖励类型 1 平台币 2余额  3积分
     */
    @ApiModelProperty(value = "礼包奖励类型 1 平台币 2余额  3积分", required = true)
    @NotNull(message = "礼包奖励类型 1 平台币 2余额  3积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer giftRewardType;

    /**
     * 礼包的奖励数量
     */
    @ApiModelProperty(value = "礼包的奖励数量", required = true)
    @NotNull(message = "礼包的奖励数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer giftRewardNums;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    private String sort;

    /**
     * 是否上线 0上线 1下线
     */
    @ApiModelProperty(value = "是否上线 0上线 1下线", required = true)
    private Integer isOnline;

}
