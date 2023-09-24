package com.euler.sdk.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 游戏用户管理对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_user_management")
public class GameUserManagement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏用户id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 游戏Id
     */
    private Integer gameId;

    /**
     * 游戏名称
     */
    private String gameName;



    /**
     * packageCode
     */
    private String packageCode;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色等级
     */
    private String roleLevel;

    /**
     * 游戏VIP等级
     */
    private String vipLevel;

    /**
     * 服务器id
     */
    private String serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;


    /**
     * 游戏天数
     */
    private Integer gameDays;
    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    private Float gameDuration;

    /**
     * 数据唯一索引，md5(member_id+game_id+role_id+server_id)
     */
    private String uniqueId;

    /**
     * 渠道ID
     */
    private Integer channelId;

    /**
     * 渠道名
     */
    private String channelName;


    /**
     * 用户注册时间
     */
    private Date registerTime;



}
