package com.euler.collection.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.collection.config.ActivateMqConfig;
import com.euler.collection.domain.Behavior;
import com.euler.collection.domain.bo.BehaviorBo;
import com.euler.collection.domain.dto.BehaviorDto;
import com.euler.collection.domain.vo.BehaviorVo;
import com.euler.collection.mapper.BehaviorMapper;
import com.euler.collection.service.IBehaviorService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BehaviorServiceImpl implements IBehaviorService {

    private final BehaviorMapper baseMapper;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private ActivateMqConfig activateMqConfig;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;
    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    @Override
    public BehaviorVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public TableDataInfo<BehaviorVo> queryPageList(BehaviorDto dto) {
        LambdaQueryWrapper<Behavior> lqw = buildQueryWrapper(dto);
        Page<BehaviorVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<Behavior> buildQueryWrapper(BehaviorDto dto) {
        LambdaQueryWrapper<Behavior> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getBizId() != null, Behavior::getBizId, dto.getBizId());
        lqw.eq(dto.getUserId() != null, Behavior::getUserId, dto.getUserId());
        lqw.eq(StringUtils.isNotBlank(dto.getSessionId()), Behavior::getSessionId, dto.getSessionId());
        lqw.orderByDesc(Behavior::getId);
        return lqw;
    }

    /**
     * 新增
     *
     * @param bo
     * @return 结果
     */
    @Override
    public R insertByBo(BehaviorBo bo) {
        String packageCode = ServletUtils.getHeader(ServletUtils.getRequest(), "packagecode");

        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
        // 从请求头获取数据
        if (deviceInfo != null) {
            // 系统类型
            bo.setOs(ObjectUtils.isEmpty(bo.getOs())? deviceInfo.getMobileType(): bo.getOs());
            // mac:安卓，ios都需要
            bo.setMac(ObjectUtils.isEmpty(bo.getMac())? deviceInfo.getMac(): bo.getMac());
            // 设备imei，安卓，ios都需要
            bo.setImei(ObjectUtils.isEmpty(bo.getImei())? deviceInfo.getImei(): bo.getImei());
            // 设备ios,idfa
            if(ObjectUtils.isEmpty(bo.getIdfa())) {
                bo.setIdfa(!ObjectUtils.isEmpty(deviceInfo.getIdfa())? deviceInfo.getIdfa(): !ObjectUtils.isEmpty(deviceInfo.getPushId())? deviceInfo.getPushId(): deviceInfo.getUuid());
            }
            // 设备安卓oaid
            bo.setOaid(ObjectUtils.isEmpty(bo.getOaid())? deviceInfo.getOaid(): bo.getOaid());
            // 设备安卓id
            bo.setAndroidid(ObjectUtils.isEmpty(bo.getAndroidid())? deviceInfo.getAndroid(): bo.getAndroidid());
            log.info("请求头里的设备信息：手机型号----" + bo.getOs() + "，mac：" + bo.getMac() + "，imei：" + bo.getImei() + "，idfa：" + bo.getIdfa() + "，安卓id：" + bo.getAndroidid()+ "，安卓oaid：" + bo.getOaid());
        }
        Behavior add = BeanUtil.toBean(bo, Behavior.class);
        validEntityBeforeSave(add);
        add.setPackageCode(packageCode);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());

            // 存储到Redis中的key
            String collectionKey = StringUtils.format("euler:cloud:collection:collect:{}:{}:{}:{}", bo.getAndroidid(), bo.getOaid(), bo.getImei(), bo.getIdfa());
            BehaviorVo cacheObject = RedisUtils.getCacheObject(collectionKey);
            // 缓存里如果没数据的话
            if (cacheObject == null) {
                // 查询最近30天的行为表
                LambdaQueryWrapper<Behavior> lqw = Wrappers.lambdaQuery();
                lqw.eq(StringUtils.isNotBlank(bo.getAppId()), Behavior::getAppId, bo.getAppId());
                lqw.eq(StringUtils.isNotBlank(bo.getAndroidid()), Behavior::getAndroidid, bo.getAndroidid());
                lqw.eq(StringUtils.isNotBlank(bo.getOaid()), Behavior::getOaid, bo.getOaid());
                lqw.eq(StringUtils.isNotBlank(bo.getImei()), Behavior::getImei, bo.getImei());
                lqw.eq(StringUtils.isNotBlank(bo.getIdfa()), Behavior::getIdfa, bo.getIdfa());
                lqw.ge(Behavior::getCreateTime, DateUtils.getBeginOfDay(DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, -30)));
                lqw.orderByDesc(Behavior::getId);
                lqw.last("limit 1");
                BehaviorVo vo = baseMapper.selectVoOne(lqw);
                if(vo == null) {
                    // 校验游戏配置里是否配置了事件广播
                    if(checkIsOpenBroadcast()) {
                        String msgId = "activate" + bo.getOs();
                        if(StringUtils.equals("1", bo.getOs())) {
                            msgId = msgId + (!ObjectUtils.isEmpty(bo.getAndroidid()) ? bo.getAndroidid() : !ObjectUtils.isEmpty(bo.getOaid()) ? bo.getOaid() : bo.getImei());
                        } else if(StringUtils.equals("2", bo.getOs())) {
                            msgId = msgId + bo.getIdfa();
                        } else {
                            msgId = msgId + bo.getClientIp();
                        }
                        Map<String, Object> map = new HashMap<>();
                        map.put("device_info", deviceInfo);
                        map.put("status", "success");
                        // 在消息队列里发送一条激活消息
                        rabbitMqHelper.sendObj(activateMqConfig.getActivateExchange(), activateMqConfig.getActivateRoutingKey(), msgId, map);
                    }
                } else {
                    // 数据存储到缓存中, 30天有效期
                    RedisUtils.setCacheObject(collectionKey, vo, Duration.ofDays(30));
                }
            }
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Behavior entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验游戏配置里是否配置了事件广播
     */
    private Boolean checkIsOpenBroadcast() {
        AtomicReference<Boolean> isOpenActivate = new AtomicReference<>(false);
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
            isOpenActivate.set(Convert.toBool(finalJsonObject.get("激活")));
        } else {
            // 查询全局字典
            List<SysDictData> data = remoteDictService.selectDictDataByType("event_broadcast");
            if (data != null && !data.isEmpty()) {
                data.forEach(a -> {
                    // 判断开关是否开启
                    if (a.getDictLabel().equals("激活") && StringUtils.equals("0", a.getStatus())) {
                        isOpenActivate.set(true);
                    }
                });
            }
        }
        log.info("激活开关:{}", isOpenActivate.get());
        return isOpenActivate.get();
    }

}
