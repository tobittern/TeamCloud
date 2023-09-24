package com.euler.risk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录配置视图对象 login_config
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@ApiModel("登录配置视图对象")
@ExcelIgnoreUnannotated
public class LoginConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ApiModelProperty("自增主键")
    private Integer id;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 平台标识（1:sdk 4:九区玩家app）
     */
    @ApiModelProperty("平台标识（1:sdk 4:九区玩家app）")
    private String platformType;

    /**
     * 是否是全局配置（0:不是 1:是）
     */
    @ApiModelProperty("是否是全局配置 （0:不是 1:是）")
    private String globalConfig;

    /**
     * 平台（1:Android 2:ios 3:h5）
     */
    @ApiModelProperty("平台（1:Android 2:ios 3:h5）")
    private String platform;

    /**
     * 一键登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("一键登录(0:开 1:关 2:不可用)")
    private String mobileLogin;

    /**
     * 游客登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("游客登录(0:开 1:关 2:不可用)")
    private String idLogin;

    /**
     * 验证码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("验证码登录(0:开 1:关 2:不可用)")
    private String captchaLogin;

    /**
     * 账号密码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty("账号密码登录(0:开 1:关 2:不可用)")
    private String passwordLogin;

}
