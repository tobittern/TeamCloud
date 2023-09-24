package com.euler.job.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.job.config.PayMqConfig;
import com.euler.payment.api.RemoteOrderService;
import com.euler.payment.api.domain.TradeResultDto;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class OrderJob {

    @DubboReference
    private RemoteOrderService remoteOrderService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @Autowired
    private PayMqConfig payMqConfig;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;

    @XxlJob("queryOrderState")
    public void queryOrderState() {
        //获取过期未支付的订单

        log.info("定时处理过期未支付订单--执行开始");

        var expiredList = remoteOrderService.queryExpiredList(1000);

        if (expiredList != null && !expiredList.isEmpty()) {
            log.info("定时处理过期未支付订单--订单数量为：{}", expiredList.size());
            for (var expired : expiredList) {
                try {
                    TradeResultDto tradeResult = remoteOrderService.queryPayOrderState(expired.getBusinessOrderId());
                    log.info("定时处理过期未支付订单--获取支付渠道订单状态结果：{}", JsonHelper.toJson(tradeResult));

                    // 校验游戏配置里是否配置了事件广播
                    if(checkIsOpenBroadcast()) {
                        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
                        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
                        String msgId = "pay_fail_" + expired.getBusinessOrderId();
                        Map<String, Object> map = new HashMap<>();
                        map.put("member_id", expired.getPayChannelUser());
                        map.put("good_id", expired.getBusinessOrderId());
                        map.put("good_name", expired.getSubject() == null? expired.getBody() : expired.getSubject());
                        map.put("price", expired.getAmount());
                        map.put("device_info", deviceInfo);
                        map.put("status", "fail");
                        // 超时未支付的订单，在消息队列里发送一条失败的消息
                        rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
                    }
                } catch (Exception e) {
                    log.info("定时处理过期未支付订单--处理异常：订单数据：{}，", JsonHelper.toJson(expired), e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.info("休眠失败");
                }
            }
        } else {
            log.info("定时处理过期未支付订单--暂无数据需要处理");
        }
        log.info("定时处理过期未支付订单--处理结束");
    }

    @XxlJob("supplyGameOrder")
    public void supplyGameOrder() {
        log.info("定时处理游戏通知失败的订单--执行开始");
        var failList = remoteOrderService.queryGameNotifyFailList(1000);

        if (failList != null && !failList.isEmpty()) {
            log.info("定时处理游戏通知失败的订单--订单数量为：{}", failList.size());
            for (var expired : failList) {
                try {
                    R r = remoteOrderService.autoSupplyOrder(expired.getBusinessOrderId(),"system","系统自动补单");
                    log.info("定时处理游戏通知失败的订单--获取支付渠道订单状态结果：{},msg:{}", R.SUCCESS.equals(r.getCode()), r.getMsg());
                } catch (Exception e) {
                    log.info("定时处理游戏通知失败的订单--处理异常：订单数据：{}，", JsonHelper.toJson(expired), e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.info("休眠失败");
                }
            }
        } else {
            log.info("定时处理游戏通知失败的订单--暂无数据需要处理");

        }
        log.info("定时处理游戏通知失败的订单--处理结束");
    }

    /**
     * 校验游戏配置里是否配置了事件广播
     */
    private Boolean checkIsOpenBroadcast() {
        AtomicReference<Boolean> isOpenPay = new AtomicReference<>(false);
        // 事件广播，默认false
        Boolean hasEventBroadcast = false;
        JSONObject jsonObject = new JSONObject();
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
            JSONObject finalJsonObject = jsonObject;
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
        log.info("--job---支付失败开关：{}", isOpenPay.get());
        return isOpenPay.get();
    }
}
