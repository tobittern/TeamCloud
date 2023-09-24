package com.euler.risk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.risk.api.domain.LoginConfigVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.util.List;

/**
 * 登录配置业务对象 login_config
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("登录配置业务对象")
public class LoginConfigBo extends BaseEntity {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Integer id;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = {AddGroup.class})
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    @NotBlank(message = "游戏名称不能为空", groups = {AddGroup.class})
    private String gameName;

    /**
     * 平台标识（1:sdk 4:九区玩家app）
     */
    @ApiModelProperty(value = "平台标识（1:sdk 4:九区玩家app）", required = true)
    @NotBlank(message = "平台标识（1:sdk 4:九区玩家app）不能为空", groups = {AddGroup.class})
    private String platformType;

    /**
     * 是否是全局配置（0:不是 1:是）
     */
    @ApiModelProperty(value = "是否是全局配置（0:不是 1:是）", required = true)
    @NotBlank(message = "是否是全局配置（0:不是 1:是）不能为空", groups = {AddGroup.class})
    private String globalConfig;

    /**
     * 平台（1:Android 2:ios 3:h5）
     */
    @ApiModelProperty(value = "平台（1:Android 2:ios 3:h5）", required = true)
    @NotBlank(message = "平台（1:Android 2:ios 3:h5）不能为空", groups = {AddGroup.class})
    private String platform;

    /**
     * 一键登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty(value = "一键登录(0:开 1:关 2:不可用)", required = true)
    @NotBlank(message = "一键登录(0:开 1:关 2:不可用)不能为空", groups = {AddGroup.class})
    private String mobileLogin;

    /**
     * 游客登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty(value = "游客登录(0:开 1:关 2:不可用)", required = true)
    @NotBlank(message = "游客登录(0:开 1:关 2:不可用)不能为空", groups = {AddGroup.class})
    private String idLogin;

    /**
     * 验证码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty(value = "验证码登录(0:开 1:关 2:不可用)", required = true)
    @NotBlank(message = "验证码登录(0:开 1:关 2:不可用)不能为空", groups = {AddGroup.class})
    private String captchaLogin;

    /**
     * 账号密码登录(0:开 1:关 2:不可用)
     */
    @ApiModelProperty(value = "账号密码登录(0:开 1:关 2:不可用)", required = true)
    @NotBlank(message = "账号密码登录(0:开 1:关 2:不可用)不能为空", groups = {AddGroup.class})
    private String passwordLogin;

    /**
     * 九区玩家app的登录配置列表
     */
    private List<LoginConfigVo> appConfigList;

}
