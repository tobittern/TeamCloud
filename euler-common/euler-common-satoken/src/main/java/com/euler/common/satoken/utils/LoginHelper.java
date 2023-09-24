package com.euler.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.enums.LoginPlatformEnum;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.exception.UtilException;
import com.euler.common.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * loginPlatform 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author euler
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";

    /*
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId());
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }


    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser         登录用户信息
     * @param loginPlatformEnum 登录的不同平台
     */
    public static void loginByDevice(LoginUser loginUser, LoginPlatformEnum loginPlatformEnum) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId(), loginPlatformEnum.getLoginPlatformCode());
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 设置用户数据(多级缓存)
     */
    public static void setLoginUser(LoginUser loginUser) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String userId = null;
            for (UserTypeEnum value : UserTypeEnum.values()) {
                if (StringUtils.contains(loginId, value.getUserType())) {
                    String[] strs = StringUtils.split(loginId, JOIN_CODE);
                    // 用户id在总是在最后
                    userId = strs[strs.length - 1];
                }
            }
            if (StringUtils.isBlank(userId)) {
                throw new UtilException("登录用户: LoginId异常 => " + loginId);
            }
            return Long.parseLong(userId);
        }
        return loginUser.getUserId();
    }

    /**
     * 获取用户id - 无论用户是否进行了登录
     */
    public static Long getUserIdOther() {
        if (!LoginHelper.isLogin()) {
            return 0L;
        } else {
            return getUserId();
        }
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }


    /**
     * 获取appid
     */
    public static String getAppId() {
        return getLoginUser().getAppId();
    }

    /**
     * 获取用户类型
     */
    public static UserTypeEnum getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserTypeEnum.getUserType(loginId);
    }


    /**
     * 获取用户登录sdk信息
     */
    public static SdkChannelPackageDto getSdkChannelPackage() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null)
            return new SdkChannelPackageDto();
        SdkChannelPackageDto getSdkChannelPackage = loginUser.getSdkChannelPackage();
        return getSdkChannelPackage == null ? new SdkChannelPackageDto() : getSdkChannelPackage;
    }

    /**
     * 是否前端用户
     *
     * @return
     */
    public static boolean isFront() {
        if (!StpUtil.isLogin())
            return true;

        LoginUser loginUser = getLoginUser();
        if (loginUser == null)
            return true;

        return UserTypeEnum.OPEN_USER.getUserType().equals(loginUser.getUserType());
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (StpUtil.isLogin()) {
            if (getLoginUser() != null) {
                return true;
            }
        }
        return false;
    }

}
