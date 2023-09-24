package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 对象 online_user
 *
 * @author euler
 * @date 2022-09-14
 */
@Data
@TableName("online_user")
public class OnlineUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 登录IP地址
     */
    private String ipaddr;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 渠道号
     */
    private String packageCode;
    /**
     * 游戏Id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 用户类型，1：sdk，2：开放平台，3：管理后台
     */
    private String userType;
    /**
     * 登录平台，1：sdk，2：开放平台，3：管理后台 4：APP
     */
    private Integer platform;
    /**
     * 设备，1：安卓，2：ios，3：h5，4：小程序
     */
    private Integer device;

    /**
     * 创建时间
     */
    private Date createTime;

}
