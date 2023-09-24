package com.euler.common.core.utils;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

/**
 * 获取请求头信息
 */
@Component
public class HttpRequestHeaderUtils {
    /**
     * 普通http请求使用
     * @return
     */
    public static RequestHeaderDto getFromHttpRequest() {
        TreeMap<String, String> headers = ServletUtils.getHeaders();
        RequestHeaderDto requestHeaderDto = new RequestHeaderDto();
        String appId = headers.get("appid") == null? headers.get("appId"): headers.get("appid");
        Long ticks = Convert.toLong(headers.get("ticks"), 0L);
        String sign = headers.get("sign");
        String nonce = headers.get("nonce");
        String packageCode = Convert.toStr(headers.get("packagecode"), "");
        String platform = Convert.toStr(headers.get("platform"), "");
        String device = Convert.toStr(headers.get("device"), "");
        String version = Convert.toStr(headers.get("version"), "");
        String deviceInfoStr=Convert.toStr(headers.get("deviceinfo"), "");
        String userAgent=Convert.toStr(headers.get("User-Agent"), "");
        DeviceInfoDto dto= JsonHelper.toObject(deviceInfoStr,DeviceInfoDto.class);
        requestHeaderDto
            .setAppId(appId)
            .setTicks(ticks)
            .setSign(sign)
            .setNonce(nonce)
            .setPackageCode(packageCode)
            .setPlatform(platform)
            .setDevice(device)
            .setVersion(version)
            .setDeviceInfo(dto)
            .setUserAgent(userAgent);
        return requestHeaderDto;
    }
}
