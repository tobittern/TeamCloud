package com.euler.community.instantiator;

import cn.dev33.satoken.stp.StpUtil;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberBanVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInstantiator implements HandlerInterceptor {

    @DubboReference
    private RemoteMemberService remoteMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        RequestHeaderDto fromHttpRequest = HttpRequestHeaderUtils.getFromHttpRequest();
        DeviceInfoDto deviceInfo = fromHttpRequest.getDeviceInfo();
        if (authorization != null && StringUtils.isNotBlank(authorization) && deviceInfo == null) {
            Long userId = 0L;
            Integer gameId = 0;
            String username = "";
            if (StpUtil.isLogin()) {
                LoginUser loginUser = LoginHelper.getLoginUser();
                userId = loginUser.getUserId();
                username = loginUser.getUsername();
                SdkChannelPackageDto sdkChannelPackage = loginUser.getSdkChannelPackage();
                if (sdkChannelPackage != null) {
                    gameId = sdkChannelPackage.getGameId();
                }
            }
            // 判断是否存在用户ID
            if (userId != null && userId > 0) {
                // 调取黑名单检测服务
                MemberBanVo memberBanVo = remoteMemberService.checkUserBlacklist(userId, username, 1, gameId);
                if (memberBanVo.getMemberId() != null) {
                    // 封禁
                    request.setAttribute("returnMessage", memberBanVo);
                    try {
                        request.getRequestDispatcher("/common/userBlockingDispatcher").forward(request, response);
                    } catch (Exception e) {
                        log.error("sdk写的拦截器出现了错误", e);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
        modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
        ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
