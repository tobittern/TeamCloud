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
import java.util.Date;

/**
 * 活动业务对象 activity
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("活动业务对象")
public class ActivityBo extends BaseEntity {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称", required = true)
    @NotBlank(message = "活动名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 面向游戏
     */
    @ApiModelProperty(value = "面向游戏", required = true)
    @NotBlank(message = "面向游戏不能为空", groups = {AddGroup.class, EditGroup.class})
    private String gameOriented;

    /**
     * 活动banner
     */
    @ApiModelProperty(value = "活动banner", required = true)
    @NotBlank(message = "活动banner不能为空", groups = {AddGroup.class, EditGroup.class})
    private String activeBanner;

    /**
     * 活动类型 (1：限时折扣模块  2：热门活动模块)
     */
    @ApiModelProperty(value = "活动类型 (1：限时折扣模块  2：热门活动模块)", required = true)
    @NotBlank(message = "活动类型 (1：限时折扣模块  2：热门活动模块)不能为空", groups = {AddGroup.class, EditGroup.class})
    private String activityType;

    /**
     * 跳转地址
     */
    @ApiModelProperty(value = "跳转地址", required = true)
    @NotBlank(message = "跳转地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String jumpAddress;

    /**
     * 活动开启时间
     */
    @ApiModelProperty(value = "活动开启时间", required = true)
    @NotNull(message = "活动开启时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date activityStartTime;

    /**
     * 活动关闭时间
     */
    @ApiModelProperty(value = "活动关闭时间", required = true)
    @NotNull(message = "活动关闭时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date activityClosingTime;

    /**
     * 是否上线 0上线  1下线
     */
    @ApiModelProperty(value = "是否上线 0上线  1下线", required = true)
    private Integer isOnline = 1;

}
