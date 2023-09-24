package com.euler.sdk.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 游戏用户管理业务对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@ApiModel("游戏用户管理业务对象")
public class GameUserManagementBo {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id")
    private Integer gameId = 0;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 游戏Icon
     */
    @ApiModelProperty(value = "游戏icon")
    private String gameIcon;

    /**
     * 分包渠道号
     */
    @ApiModelProperty(value = "分包渠道号")
    private String packageCode;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    @NotBlank(message = "角色id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String roleId;

    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", required = true)
    @NotBlank(message = "角色不能为空", groups = {AddGroup.class, EditGroup.class})
    private String roleName;

    /**
     * 区服id
     */
    @ApiModelProperty("区服id")
    @NotBlank(message = "区服id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String serverId;

    /**
     * 服务器名称
     */
    @ApiModelProperty("区服名称")
    private String serverName;

    /**
     * 角色等级
     */
    @ApiModelProperty(value = "角色等级")
    private String roleLevel;

    /**
     * VIP等级
     */
    @ApiModelProperty(value = "游戏VIP等级")
    private String vipLevel;


    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "主渠道ID")
    private Integer channelId;

    /**
     * 渠道名
     */
    @ApiModelProperty("主渠道名")
    private String channelName;

    /**
     * 用户注册时间
     */
    @ApiModelProperty("用户注册时间")
    private Date registerTime;


}
