package com.euler.gateway.utils;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RequestHeaderUtils {

    public RequestHeaderDto getFromRequest(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        RequestHeaderDto requestHeaderDto = new RequestHeaderDto();

        String appId = headers.getFirst("appid");
        Long ticks = Convert.toLong(headers.getFirst("ticks"), 0L);
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String packageCode = Convert.toStr(headers.getFirst("packagecode"), "");
        String platform = Convert.toStr(headers.getFirst("platform"), "");
        String device = Convert.toStr(headers.getFirst("device"), "");
        String version = Convert.toStr(headers.getFirst("version"), "");
        String deviceInfoStr = Convert.toStr(headers.getFirst("deviceinfo"), "");
        DeviceInfoDto dto = JsonHelper.toObject(deviceInfoStr, DeviceInfoDto.class);
        requestHeaderDto.setAppId(appId)
            .setTicks(ticks)
            .setSign(sign)
            .setNonce(nonce)
            .setPackageCode(packageCode)
            .setPlatform(platform)
            .setDevice(device)
            .setVersion(version).setDeviceInfo(dto);
        return requestHeaderDto;
    }


    public  String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        return ip;
    }


}
