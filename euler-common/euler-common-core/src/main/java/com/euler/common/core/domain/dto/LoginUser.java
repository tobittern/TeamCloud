package com.euler.common.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.euler.common.core.constant.CacheConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息
 *
 * @author euler
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 平台，1：sdk，2：开放平台，3：管理后台 4：APP
     */
    private Integer platform = 1;

    /**
     * 设备，1：安卓，2：ios，3：h5，4：小程序
     */
    private Integer device;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    /**
     * 角色权限
     */
    private Set<String> rolePermission;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 是否登录即注册
     */
    private Boolean fillPassword;
    /**
     * 实名认证状态，1：是，0：否
     */
    private String verifyStatus;

    /**
     * 是否可以玩 0 不可以 1可以玩
     */
    private String isPlay = "0";

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    @JsonIgnore
    private Integer isReg = 0;


    @JsonIgnore
    private Integer isNewGame = 0;


    /**
     * 分包code
     */
    private String packageCode;

    /**
     * 当前appid
     */
    private String appId;

    /**
     * sdk用户渠道信息
     */
    private SdkChannelPackageDto sdkChannelPackage = new SdkChannelPackageDto();


    /**
     * 角色对象
     */
    private List<RoleDTO> roles;

    /**
     * 数据权限 当前角色ID
     */
    private Long roleId;


    /**
     * 获取登录id
     */
    public String getLoginId() {
        if (userType == null) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return userType + CacheConstants.LOGINID_JOIN_CODE + userId;
    }

}
