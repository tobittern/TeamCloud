package com.euler.system.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 当前在线会话
 *
 * @author euler
 */

@Data
@NoArgsConstructor
@ApiModel("当前在线会话业务对象")
public class SysUserOnline implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 会话编号
     */
    @ApiModelProperty(value = "会话编号")
    private String tokenId;



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
     * 登录地址
     */
    @ApiModelProperty(value = "登录地址")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    private String os;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    private Long loginTime;


    /**
     * 渠道号
     */
    @ApiModelProperty("渠道号")
    private String packageCode;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏Id")
    private Integer gameId=0;

    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 渠道id
     */
    @ApiModelProperty("渠道id")
    private Integer channelId=0;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;


    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    private String userType;


    /**
     * 平台
     */
    @ApiModelProperty(value = "登录平台，1：sdk，2：开放平台，3：管理后台 4：APP")
    private Integer platform = 1;

    /**
     * 设备，1：安卓，2：ios，3：h5，4：小程序
     */
    @ApiModelProperty(value = "设备，1：安卓，2：ios，3：h5，4：小程序")
    private  Integer device;





}
