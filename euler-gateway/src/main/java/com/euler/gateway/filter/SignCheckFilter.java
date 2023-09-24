package com.euler.gateway.filter;

import cn.dev33.satoken.router.SaRouter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.exception.SignBadException;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.gateway.config.WebConfig;
import com.euler.gateway.config.properties.IgnoreWhiteProperties;
import com.euler.gateway.utils.RequestHeaderUtils;
import com.euler.gateway.utils.WebFluxUtils;
import com.euler.risk.api.RemoteBehaviorService;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.system.api.RemoteSignService;
import com.euler.system.api.domain.AppConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

/**
 * 签名过滤器
 *
 * @author euler
 */
@Slf4j
@Component
public class SignCheckFilter extends AbstractGatewayFilterFactory<Object> {

    @DubboReference
    private RemoteSignService remoteSignService;
    @DubboReference
    private RemoteBehaviorService remoteBehaviorService;


    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RequestHeaderUtils requestHeaderUtils;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getURI().getPath();
            String reqData = WebFluxUtils.resolveBodyFromRequest(request);
            RequestHeaderDto requestHeaderDto = requestHeaderUtils.getFromRequest(request);
            //是否上报数据
            if (webConfig.getIsSubmitBehavior()) {
                submitUserBehaviorData(url, requestHeaderDto, reqData, request);
            }
            //是否校验签名
            if (!webConfig.getIsCheckSign())
                return chain.filter(exchange);

            try {
                //白名单校验
                List<String> signWhites = ignoreWhite.getSignWhites();
                if (SaRouter.isMatch(signWhites, url)) {
                    log.info("签名白名单通过：url:{}", url);
                    return chain.filter(exchange);
                }

                //签名校验
                boolean b = validateSign(requestHeaderDto, reqData, url);
                if (!b) {
                    log.error("签名错误,请求url:{},requestHeaderDto:{},请求数据：{}", url, JsonHelper.toJson(requestHeaderDto), reqData);
                    return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), "签名错误");
                }
                return chain.filter(exchange);


            } catch (Exception e) {
                log.error("签名异常,请求url:{},requestHeaderDto:{},请求数据：{}", url, JsonHelper.toJson(requestHeaderDto), reqData, e);
                return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }

        };
    }

    /**
     * 上报用户行为数据
     *
     * @param path             请求地址
     * @param requestHeaderDto 请求头数据
     * @param reqData          请求体
     */
    private void submitUserBehaviorData(String path, RequestHeaderDto requestHeaderDto, String reqData, ServerHttpRequest request) {
        try {
            if (StringUtils.isBlank(path) || path.equals("/") || path.equals("//"))
                return;

            if (requestHeaderDto == null || StringUtils.isBlank(requestHeaderDto.getPlatform()) || StringUtils.isBlank(requestHeaderDto.getDevice()))
                return;

            List<BehaviorType> behaviorTypeList = getBehaviorTypeList(requestHeaderDto.getPlatform(), requestHeaderDto.getDevice());

            if (behaviorTypeList == null || behaviorTypeList.isEmpty())
                return;
            Optional<BehaviorType> first = behaviorTypeList.stream().filter(a -> a.getPath().equalsIgnoreCase(path)).findFirst();


            if (first.isPresent()) {
                String ip = requestHeaderUtils.getIpAddress(request);
                remoteBehaviorService.submitUserBehavior(first.get(), requestHeaderDto, reqData, ip);
                log.info("拦截上报--成功，path:{},platform:{},device:{}", path, requestHeaderDto.getPlatform(), requestHeaderDto.getDevice());
                return;
            }
            log.info("拦截上报--不在上报范围，path:{},platform:{},device:{}", path, requestHeaderDto.getPlatform(), requestHeaderDto.getDevice());

        } catch (Exception e) {
            log.error("拦截上报--异常：path：{},reqHeader,reqData:{}", path, JsonHelper.toJson(requestHeaderDto), reqData);
        }

    }


    private String genSign(RequestHeaderDto requestHeaderDto, String reqData, String appSecret, String url) {

        Map reqMap = JsonUtils.parseMap(reqData);
        Map<String, String> treeMap = new TreeMap<>();
        if (reqMap != null)
            treeMap.putAll(reqMap);


        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append(requestHeaderDto.getAppId());
        if (treeMap != null) {
            for (var entry : treeMap.entrySet()) {
                Object valueObject = entry.getValue();
                String value = null;

                if (valueObject instanceof LinkedHashMap) {
                    //特殊对象不参与签名
                } else if (valueObject instanceof List) {
                    //集合不参与签名
                } else {
                    value = Convert.toStr(valueObject);
                    //空值不参与签名
                    if (StringUtils.isNotBlank(value)) {
                        stringBuilder.append(entry.getKey());
                        stringBuilder.append("=");
                        stringBuilder.append(value);

                    }
                }
            }
        }
        stringBuilder.append(requestHeaderDto.getTicks());
        stringBuilder.append(appSecret);
        stringBuilder.append(requestHeaderDto.getNonce());
        stringBuilder.append(requestHeaderDto.getPackageCode());

        stringBuilder.append(requestHeaderDto.getDevice());
        stringBuilder.append(requestHeaderDto.getPlatform());
        stringBuilder.append(requestHeaderDto.getVersion());


        String sign = SecureUtil.md5(stringBuilder.toString());
        if (webConfig.getSignLog())
            log.info("请求地址：{}，header：{}，reqData:{}，待加密字符串：{}，加密后sign：{}", url, JsonHelper.toJson(requestHeaderDto), reqData, stringBuilder.toString(), sign);

        return sign;
    }


    @SneakyThrows
    private boolean validateSign(RequestHeaderDto requestHeaderDto, String reqData, String url) {


        if (StringUtils.isEmpty(requestHeaderDto.getAppId()) || requestHeaderDto.getTicks() <= 0L || StringUtils.isEmpty(requestHeaderDto.getSign()) || StringUtils.isEmpty(requestHeaderDto.getNonce()))
            throw new SignBadException("缺少请求参数");


        DateTime beginTime = DateTime.now();
        DateTime reqTime = DateUtil.date(requestHeaderDto.getTicks());
        long secondTick = DateUtil.between(reqTime, beginTime, DateUnit.SECOND);

        if (secondTick > webConfig.getTickExpireTime()) {
            throw new SignBadException("时间戳已过期");
        }

        if (!checkCallTimes(requestHeaderDto.getSign())) {
            log.error("签名重复使用：" + requestHeaderDto.getSign());
            throw new SignBadException("不允许重复调用");

        }

        var appConfig = getAppConfig(requestHeaderDto.getAppId());
        String signResult = genSign(requestHeaderDto, reqData, appConfig.getAppSecret(), url);
        if (signResult.equals(requestHeaderDto.getSign())) {
            return true;
        }

        return false;
    }

    /**
     * 校验是否运行调用多次
     *
     * @param sign
     * @return
     */
    private boolean checkCallTimes(String sign) {
        if (!webConfig.getCallOnce()) {
            return true;
        }
        String redisKey = Constants.BASE_KEY + "checksign:" + sign;
        if (RedisUtils.hasKey(redisKey)) {
            return false;
        }
        RedisUtils.setCacheObject(redisKey, sign, Duration.ofMinutes(10));
        return true;

    }

    /**
     * 防止缓存穿透获取appid信息
     *
     * @param appId
     * @return
     */
    private AppConfig getAppConfig(String appId) {
        String key = StringUtils.format("{}appconfig:appid:{}", Constants.BASE_KEY, appId);
        String json = RedisUtils.getCacheObject(key);
        AppConfig appConfig = null;
        if (json == null) {
            appConfig = remoteSignService.getAppConfig(appId);
            String jsonStr = appConfig == null ? "" : JsonHelper.toJson(appConfig);
            RedisUtils.setCacheObject(key, jsonStr, Duration.ofHours(24));
        } else {
            appConfig = JsonHelper.toObject(json, AppConfig.class);

        }
        if (appConfig == null) {
            log.error("根据 appid 查询 appconfig 失败，appid不合法，appid：" + appId);
            throw new SignBadException("appid 不合法");
        }
        return appConfig;
    }


    /**
     * 获取拦截行为数据
     *
     * @return
     */
    private List<BehaviorType> getBehaviorTypeList(String platform, String device) {
        String key = StringUtils.format("{}behaviorType:platform_{}:device_{}", Constants.BASE_KEY, platform, device);
        String cacheJson = RedisUtils.getCacheObject(key);
        if (cacheJson == null) {
            List<BehaviorType> list = remoteBehaviorService.getBehaviorList(platform, device);
            RedisUtils.setCacheObject(key, JsonHelper.toJson(list), Duration.ofHours(24));
            return list;
        } else {
            return JsonHelper.toList(cacheJson, BehaviorType.class);
        }

    }


}
