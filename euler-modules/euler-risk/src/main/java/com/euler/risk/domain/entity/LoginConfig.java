package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录配置对象 login_config
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("login_config")
public class LoginConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 平台标识（1:sdk 4:九区玩家app）
     */
    private String platformType;

    /**
     * 是否是全局配置（0:不是 1:是）
     */
    private String globalConfig;

    /**
     * 平台（1:Android 2:ios 3:h5）
     */
    private String platform;

    /**
     * 一键登录(0:开 1:关 2:不可用)
     */
    private String mobileLogin;

    /**
     * 游客登录(0:开 1:关 2:不可用)
     */
    private String idLogin;

    /**
     * 验证码登录(0:开 1:关 2:不可用)
     */
    private String captchaLogin;

    /**
     * 账号密码登录(0:开 1:关 2:不可用)
     */
    private String passwordLogin;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
