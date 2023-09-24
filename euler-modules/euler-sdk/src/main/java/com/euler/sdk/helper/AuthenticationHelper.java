package com.euler.sdk.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.euler.common.core.utils.AESUtil;
import com.euler.common.core.utils.JsonUtils;
import com.euler.sdk.config.Authentication;
import com.euler.sdk.domain.dto.CertDto;
import com.euler.sdk.domain.vo.AuthenticationVo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class AuthenticationHelper {
    @Autowired
    private Authentication authentication;


    /**
     * 签名
     *
     * @param reqMap
     * @param appSecret
     * @return
     */
    private String genSign(Map<String, String> reqMap, String data, String appSecret) {
        Map<String, String> treeMap = new TreeMap<>();
        if (reqMap != null)
            treeMap.putAll(reqMap);

        StringBuilder stringBuilder = new StringBuilder(128);
        stringBuilder.append(appSecret);
        for (var entry : reqMap.entrySet()) {
            Object valueObject = entry.getValue();
            String value = null;

            if (valueObject instanceof LinkedHashMap) {
                //特殊对象不参与签名
            } else if (valueObject instanceof List) {
                //集合不参与签名
            } else {
                value = Convert.toStr(valueObject);
                //空值不参与签名
                if (StringUtils.isNotEmpty(value)) {
                    stringBuilder.append(entry.getKey());
                    stringBuilder.append(value);
                }
            }
        }
        stringBuilder.append(data);
        String sign = SecureUtil.sha256(stringBuilder.toString());
        //log.info("待加密字符串：{}，加密后sign：{}", stringBuilder.toString(), sign);
        return sign;
    }


    /**
     * 二要素验证
     *
     * @return
     */
    public AuthenticationVo check(CertDto certDto) {
        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put("ai", SecureUtil.md5(certDto.getMemberId().toString()));
        paramMap.put("name", certDto.getRealName());
        paramMap.put("idNum", certDto.getIdCardNo());
        String content = JsonUtils.toJsonString(paramMap);

        String data = AESUtil.Encrypt(content, authentication.getSecretKey(), 1);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("data", data);
        String timestamps = String.valueOf(new Date().getTime());
        // 设置签名参数
        Map<String, String> reqMap = new TreeMap<>();
        reqMap.put("appId", authentication.getAppId());
        reqMap.put("bizId", authentication.getBizId());
        reqMap.put("timestamps", timestamps);

        String result = HttpRequest.post(authentication.getSendUrl())
            .header(Header.CONTENT_TYPE, "application/json; charset=utf-8")
            .header("appId", authentication.getAppId())
            .header("bizId", authentication.getBizId())
            .header("timestamps", timestamps)
            .header("sign", genSign(reqMap, JsonUtils.toJsonString(dataMap), authentication.getSecretKey()))
            .body(JsonUtils.toJsonString(dataMap))
            .timeout(20000)//超时，毫秒
            .execute().body();
        log.info("实名认证返回结果:{}", JsonUtils.toJsonString(result));

        AuthenticationVo authenticationVo = JsonUtils.parseObject(result, AuthenticationVo.class);

        return authenticationVo;
    }
}
