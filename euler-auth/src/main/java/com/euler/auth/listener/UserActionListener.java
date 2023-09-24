package com.euler.auth.listener;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.api.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 用户行为 侦听器的实现
 *
 * @author euler
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserActionListener implements SaTokenListener {

    private final SaTokenConfig tokenConfig;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        //  UserTypeEnum userTypeEnum = UserTypeEnum.getUserType(loginId.toString());
        UserAgent userAgent = UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = ServletUtils.getClientIP();
        LoginUser user = LoginHelper.getLoginUser();
        SysUserOnline userOnline = new SysUserOnline();
        userOnline.setIpaddr(ip);
        // userOnline.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        userOnline.setBrowser(userAgent.getBrowser().getName());
        userOnline.setOs(userAgent.getOs().getName());
        userOnline.setLoginTime(System.currentTimeMillis());
        userOnline.setTokenId(tokenValue);
        userOnline.setUserName(user.getUsername());
        userOnline.setUserId(user.getUserId());
        userOnline.setPlatform(user.getPlatform());
        userOnline.setDevice(user.getDevice());
        userOnline.setUserType(user.getUserType());
        if (UserTypeEnum.SDK_USER.getUserType().equals(user.getUserType())) {
            SdkChannelPackageDto sdkChannelPackageDto = user.getSdkChannelPackage();
            if (sdkChannelPackageDto != null && Convert.toInt(sdkChannelPackageDto.getGameId(), 0) > 0) {
                userOnline.setGameId(sdkChannelPackageDto.getGameId());
                userOnline.setGameName(sdkChannelPackageDto.getGameName());
                userOnline.setChannelId(sdkChannelPackageDto.getChannelId());
                userOnline.setChannelName(sdkChannelPackageDto.getChannelName());
                userOnline.setPackageCode(sdkChannelPackageDto.getPackageCode());
            }

        }


        RedisUtils.setCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue, userOnline, Duration.ofSeconds(tokenConfig.getTimeout()));
        //log.info("user doLogin, useId:{}, token:{}", loginId, tokenValue);

    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        //log.info("user doLogout, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doLogoutByLoginId, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doReplaced, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }


}
