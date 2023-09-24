package com.euler.common.payment.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class PayUtils {

    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;
    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 校验游戏配置里是否配置了事件广播
     */
    public Boolean checkIsOpenBroadcast() {
        Map<String, Boolean> retMap = new HashMap<>();
        AtomicReference<Boolean> isOpenPay= new AtomicReference<>(false);
        // 事件广播，默认false
        Boolean hasEventBroadcast = false;
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        // 通过appid获取游戏信息
        if (headerDto != null && headerDto.getAppId() != null) {
            // 通过appid查询游戏信息
            OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
            if (openGameDubboVo != null && openGameDubboVo.getId() > 0) {
                // 查询是否有单游戏配置了事件广播
                // 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6: 事件广播
                GameConfigVo vo = remoteGameConfigService.selectGameConfigByParam(openGameDubboVo.getId(), "6", Convert.toStr(openGameDubboVo.getOperationPlatform()));
                if (vo != null) {
                    hasEventBroadcast = true;
                    jsonObject = JSONUtil.parseObj(vo.getData());
                }
            }
        }

        // 如果有单个游戏配置，返回游戏配置信息
        if(hasEventBroadcast) {
            cn.hutool.json.JSONObject finalJsonObject = jsonObject;
            isOpenPay.set(Convert.toBool(finalJsonObject.get("支付")));
        } else {
            // 查询全局字典
            List<SysDictData> data = remoteDictService.selectDictDataByType("event_broadcast");
            if (data != null && !data.isEmpty()) {
                data.forEach(a -> {
                    // 判断开关是否开启
                    if (a.getDictLabel().equals("支付") && StringUtils.equals("0", a.getStatus())) {
                        isOpenPay.set(true);
                    }
                });
            }
        }

        log.info("支付开关：{}", isOpenPay.get());

        return isOpenPay.get();
    }

}
