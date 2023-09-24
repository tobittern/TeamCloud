package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 礼包业务对象 gift_info
 *
 * @author euler
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包业务对象")
public class GiftInfoBo extends BaseEntity {

    /**
     * 礼包id
     */
    @ApiModelProperty(value = "礼包id", required = true)
    @NotNull(message = "礼包id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 礼包名称
     */
    @ApiModelProperty(value = "礼包名称", required = true)
    @NotBlank(message = "礼包名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftName;

    /**
     * 礼包组id
     */
    @ApiModelProperty(value = "礼包组id", required = true)
    @NotNull(message = "礼包组id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer giftGroupId;

    /**
     * 礼包组名称
     */
    @ApiModelProperty(value = "礼包组名称", required = true)
    @NotBlank(message = "礼包组名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftGroupName;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotBlank(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameId;

    /**
     * 礼包领取等级
     */
    @ApiModelProperty(value = "礼包领取等级", required = true)
    @NotNull(message = "礼包领取等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer receiveGrade;

    /**
     * 礼包介绍
     */
    @ApiModelProperty(value = "礼包介绍", required = true)
    @NotBlank(message = "礼包介绍不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftIntroduce;

    /**
     * 礼包类型 1:红包 2:平台币 3:积分
     */
    @ApiModelProperty(value = "礼包类型 1:红包 2:平台币 3:积分", required = true)
    @NotBlank(message = "礼包类型 1:红包 2:平台币 3:积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 奖励数量
     */
    @ApiModelProperty(value = "奖励数量", required = true)
    @NotNull(message = "奖励数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer rewardAmount;

    /**
     * 礼包图标
     */
    @ApiModelProperty(value = "礼包图标", required = true)
    @NotBlank(message = "礼包图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftIcon;

}
