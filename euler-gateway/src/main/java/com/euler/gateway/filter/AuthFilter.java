package com.euler.gateway.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ServletUtils;
import com.euler.gateway.config.WebConfig;
import com.euler.gateway.config.properties.IgnoreWhiteProperties;
import com.euler.common.core.constant.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

/**
 * [Sa-Token 权限认证] 拦截器
 *
 * @author euler
 */
@Configuration
@Slf4j
public class AuthFilter {
    @Autowired
    private WebConfig webConfig;

    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter(IgnoreWhiteProperties ignoreWhite) {

        SaReactorFilter saReactorFilter = new SaReactorFilter();
        // 拦截地址
        saReactorFilter.addInclude("/**")
            .addExclude("/favicon.ico", "/actuator/**")
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                SaRouter.match("/**")
                    .notMatch(ignoreWhite.getWhites())
                    .check(r -> {
                        // 检查是否登录 是否有token
                        StpUtil.checkLogin();
                    });
            }).setError(e -> {
                String token = StpUtil.getTokenValue();
                var requestPath = SaHolder.getRequest().getRequestPath();
                log.error("校验用户信息异常，url：{},token:{}", requestPath, token, e);
                if (webConfig.getIsDebug()) {
                    return SaResult.error(e.getMessage()).setCode(HttpStatus.ERROR);
                } else {
                    return SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED);
                }

            }
        );


        return saReactorFilter;
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true); // 允许cookies跨域
//        List<String> allowedOriginPatterns = new ArrayList<>();
//        allowedOriginPatterns.add("*");
//        config.setAllowedOriginPatterns(allowedOriginPatterns);


        config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
        config.addAllowedMethod("*");// 允许提交请求的方法类型，*表示全部允许
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
