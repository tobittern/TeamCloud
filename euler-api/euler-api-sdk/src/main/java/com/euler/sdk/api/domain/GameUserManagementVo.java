package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏用户管理视图对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@ApiModel("游戏用户管理视图对象")
public class GameUserManagementVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 渠道Id
     */
    @ApiModelProperty("渠道Id")
    private String channelId;

    /**
     * 渠道组
     */
    @ApiModelProperty("渠道组")
    private String channelName;

    /**
     * 渠道号
     */
    @ApiModelProperty("渠道号")
    private String packageCode;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer operationPlatform;


    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private String roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色")
    private String roleName;

    /**
     * 角色等级
     */
    @ApiModelProperty("角色等级")
    private String roleLevel;

    /**
     * 游戏VIP等级
     */
    @ApiModelProperty("游戏VIP等级")
    private String vipLevel;

    /**
     * 服务器id
     */
    @ApiModelProperty("服务器id")
    private String serverId;

    /**
     * 服务器名称
     */
    @ApiModelProperty("服务器名称")
    private String serverName;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Date createTime;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Date loginDate;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
