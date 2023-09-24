package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 礼包SDk数据业务对象 gift_bag_cdk
 *
 * @author euler
 * @date 2022-06-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包SDk数据业务对象")
public class GiftBagCdkBo extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 礼包表id
     */
    @ApiModelProperty(value = "礼包表id", required = true)
    @NotNull(message = "礼包表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long giftBagId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long gameId;

    /**
     * 领取礼包的用户id
     */
    @ApiModelProperty(value = "领取礼包的用户id")
    private Long memberId;

    /**
     * 礼包码
     */
    @ApiModelProperty(value = "礼包码", required = true)
    @NotBlank(message = "礼包码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 礼包状态，0：未使用，1：已使用
     */
    @ApiModelProperty(value = "礼包状态，0：未使用，1：已使用")
    private Integer status;

    /**
     * 领取时间
     */
    @ApiModelProperty(value = "领取时间")
    private Date receiveTime;

    /**
     * 兑换时间
     */
    @ApiModelProperty(value = "兑换时间")
    private Date exchangeTime;

    /**
     * 应用平台 1：android 2：ios 3：h5
     */
    @ApiModelProperty(value = "应用平台")
    private String applicationType;

}
