package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ActivityDto extends PageQuery {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id", required = true)
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 活动banner
     */
    @ApiModelProperty(value = "活动banner", required = true)
    private String activeBanner;

    /**
     * 活动类型 (1：限时折扣模块  2：热门活动模块)
     */
    @ApiModelProperty(value = "活动类型 (1：限时折扣模块  2：热门活动模块)", required = true)
    private String activityType;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称", required = true)
    private String name;


    /**
     * 面向游戏
     */
    @ApiModelProperty(value = "面向游戏", required = true)
    private String gameOriented;

    /**
     * 活动开启时间
     */
    @ApiModelProperty(value = "活动开启时间", required = true)
    private Date activityStartTime;

    /**
     * 活动关闭时间
     */
    @ApiModelProperty(value = "活动关闭时间", required = true)
    private Date activityClosingTime;

    /**
     * 是否上线 1上线  2下线
     */
    @ApiModelProperty(value = "是否上线 1上线  2下线", required = true)
    private Integer isOnline;


}
