package com.euler.statistics.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 分页业务对象 online_user
 *
 * @author euler
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分页业务对象")
public class OnlineUserPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    /**
     * 渠道号
     */
    @ApiModelProperty(value = "渠道号")
    private String packageCode;

    /**
     * 游戏Id
     */
    @ApiModelProperty(value = "游戏Id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    /**
     * 用户类型，1：sdk，2：开放平台，3：管理后台
     */
    @ApiModelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    private String userType;

    /**
     * 登录平台，1：sdk，2：开放平台，3：管理后台 4：APP
     */
    @ApiModelProperty(value = "登录平台，1：sdk，2：开放平台，3：管理后台 4：APP")
    private Integer platform;

    /**
     * 设备，1：安卓，2：ios，3：h5，4：小程序
     */
    @ApiModelProperty(value = "设备，1：安卓，2：ios，3：h5，4：小程序")
    private Integer device;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
