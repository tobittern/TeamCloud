package com.euler.risk.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.Ipv4Util;
import com.baomidou.lock.LockInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.enums.DeviceEnum;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.LockHelper;
import com.euler.risk.api.domain.Behavior;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.domain.dto.BehaviorUserDto;
import com.euler.risk.domain.entity.TdDeviceInfo;
import com.euler.risk.domain.entity.TdDeviceMember;
import com.euler.risk.domain.entity.TdIpMember;
import com.euler.risk.mapper.BehaviorMapper;
import com.euler.risk.service.IBehaviorService;
import com.euler.risk.service.ITdDeviceInfoService;
import com.euler.risk.service.ITdDeviceMemberService;
import com.euler.risk.service.ITdIpMemberService;
import com.euler.risk.service.reflect.IBehaviorUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后端用户行为上报数据Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BehaviorServiceImpl extends ServiceImpl<BehaviorMapper, Behavior> implements IBehaviorService {
    @Autowired
    private BehaviorMapper baseMapper;

    @Autowired
    private ApplicationContext appctx;
    @Autowired
    @Qualifier("defaultBehaviorUserServiceImpl")
    private IBehaviorUserService behaviorUserService;

    @Autowired
    private ITdDeviceInfoService deviceInfoService;
    @Autowired
    private ITdDeviceMemberService deviceMemberService;
    @Autowired
    private ITdIpMemberService ipMemberService;
    @Autowired
    private LockHelper lockHelper;


    @Override
    public void save(BehaviorMqMsgDto msgDto) {
        try {
            //获取上报的行为数据类型
            BehaviorType behaviorType = msgDto.getBehaviorType();

            //获取用户数据
            var res = getBehaviorUserInfo(msgDto);
            if (R.FAIL.equals(res.getCode())) {
                log.error("{},{}", res.getMsg(), JsonHelper.toJson(msgDto));
                return;
            }
            BehaviorUserDto result = res.getData();

            if (R.SUCCESS.equals(res.getCode())) {
                if (result == null || result.getUserId() == null || result.getUserId() == 0L) {
                    log.info("数据上报--入库,上报数据无用户信息，不处理,{}", JsonHelper.toJson(msgDto));
                    return;
                }

            }
            Behavior behavior = new Behavior();

            behavior.setMsgId(msgDto.getMsgId());
            behavior.setIp(msgDto.getIp());
            behavior.setBehaviorTypeId(behaviorType.getId());
            behavior.setBehaviorData(msgDto.getRequestData());

            behavior.setUserId(result.getUserId());
            behavior.setAccount(result.getAccount());
            behavior.setMobile(result.getMobile());

            behavior = fillDeviceData(behavior, msgDto.getRequestHeader());
            behavior.setCreateTime(new Date());
            behavior = opBehaviorData(behavior, msgDto.getRequestHeader());
            if (StringUtils.isNotBlank(behavior.getDeviceId())) {
                boolean flag = save(behavior);
                log.info("数据上报--入库，msgId：{}，behaviorType：{}，userId：{}，入库结果：{}", msgDto.getMsgId(), behaviorType.getId(), behavior.getUserId(), flag);
            } else {
                log.info("数据上报--入库,获取设备Id失败，不入库，msgId：{}，behaviorType：{}，userId：{}，header：{}", msgDto.getMsgId(), behaviorType.getId(), behavior.getUserId(), JsonHelper.toJson(msgDto.getRequestHeader()));
            }


        } catch (Exception e) {
            log.error("数据上报--入库--异常,data:{}", JsonHelper.toJson(msgDto), e);

        }


    }

    @SneakyThrows
    private R<BehaviorUserDto> getBehaviorUserInfo(BehaviorMqMsgDto msgDto) {

        //获取上报的行为数据类型
        BehaviorType behaviorType = msgDto.getBehaviorType();
        //获取处理数据的方式
        String reflectExpression = behaviorType.getReflectExpression();

        if (StringUtils.isBlank(reflectExpression))
            return R.ok(behaviorUserService.getUserInfo(msgDto));

        String[] beanMethod = reflectExpression.split("\\.");
        if (beanMethod == null || beanMethod.length < 2) {
            return R.fail("数据上报--入库--失败，反射处理表达式不正确");

        }
        Object operateServices = appctx.getBean(beanMethod[0]);
        //设置方法参数类型
        List<Class> classList = new ArrayList<>();
        //我们定义的方法只有一个参数
        classList.add(BehaviorMqMsgDto.class);


        //设置方法参数值
        List<Object> parameterList = new ArrayList<>();
        parameterList.add(msgDto);
        //获取方法
        Method method = operateServices.getClass().getMethod(beanMethod[1], classList.toArray(new Class[classList.size()]));
        //调用方法
        BehaviorUserDto result = (BehaviorUserDto) method.invoke(operateServices, parameterList.toArray());


        return R.ok(result);


    }


    private Behavior fillDeviceData(Behavior behavior, RequestHeaderDto requestHeaderDto) {
        if (requestHeaderDto == null) {
            log.info("数据上报--入库,请求头信息为空，msgId:{}", behavior.getMsgId());
            return behavior;
        }
        behavior.setDevice(requestHeaderDto.getDevice());
        behavior.setPlatform(requestHeaderDto.getPlatform());
        behavior.setAppId(requestHeaderDto.getAppId());
        behavior.setVersion(requestHeaderDto.getVersion());
        var deviceInfo = requestHeaderDto.getDeviceInfo();

        if (requestHeaderDto.getDeviceInfo() != null) {
            behavior.setDeviceIdfa(deviceInfo.getIdfa());
            behavior.setDeviceUuid(deviceInfo.getUuid());
            behavior.setDevicePushId(deviceInfo.getPushId());
            behavior.setDeviceAndroid(deviceInfo.getAndroid());
            behavior.setDeviceImei(deviceInfo.getImei());
            behavior.setDeviceOaid(deviceInfo.getOaid());
            behavior.setDeviceMac(deviceInfo.getMac());
            behavior.setMobileType(deviceInfo.getMobileType());
        }
        return behavior;
    }

    /**
     * 拆分处理数据
     *
     * @param behavior
     * @return
     */
    private Behavior opBehaviorData(Behavior behavior, RequestHeaderDto headerDto) {
        //处理设备信息
        TdDeviceInfoVo tdDeviceInfoVo = deviceInfoService.getDeviceInfoByHeader(headerDto);
        String deviceId = tdDeviceInfoVo != null ? tdDeviceInfoVo.getId() : null;

        //处理ip用户信息
        boolean opIpMemberFlag = false;
        if (StringUtils.isBlank(behavior.getIp())) {
            opIpMemberFlag = true;
            log.info("数据上报--记录用户IP，IP为空，msgId：{}", behavior.getMsgId());
        } else {
            opIpMemberFlag = opIpMember(behavior, deviceId);
        }


        if (StringUtils.isBlank(deviceId)) {
            log.info("数据上报--拆分处理数据，设备id获取失败,msgId:{}", behavior.getMsgId());
            return behavior;
        }
        behavior.setDeviceId(deviceId);

        //处理设备用户信息
        boolean opDeviceMemberFlag = opDeviceMember(behavior, deviceId);


        behavior.setOpFlag(opDeviceMemberFlag && opIpMemberFlag ? "0" : "1");
        return behavior;
    }

    /**
     * ipmember处理
     *
     * @param behavior
     * @param deviceId
     * @return
     */
    private boolean opIpMember(Behavior behavior, String deviceId) {
        Long ipNumber = Ipv4Util.ipv4ToLong(behavior.getIp());
        String lockKey = StringUtils.format("{}lock:ipmember:member_{}:ip_{}", Constants.BASE_KEY, behavior.getUserId(), ipNumber);
        LockInfo lockInfo = lockHelper.lock(lockKey);
        try {
            if (lockInfo != null) {
                var ipMember = ipMemberService.getByUserAndIp(behavior.getUserId(), behavior.getIp());
                if (ipMember == null) {
                    ipMember = new TdIpMember();
                    ipMember.setAccount(behavior.getAccount());
                    ipMember.setMobile(behavior.getMobile());
                    ipMember.setUserId(behavior.getUserId());
                    ipMember.setIp(behavior.getIp());
                    ipMember.setDeviceId(deviceId);
                    ipMemberService.save(ipMember);

                }
                return true;
            }
        } catch (Exception e) {
            log.error("数据上报--记录用户IP，ipmember异常，msgId:{},userId:{},ip:{}", behavior.getMsgId(), behavior.getUserId(), behavior.getIp(), e);
            return false;
        } finally {
            lockHelper.unLock(lockInfo);
        }
        return false;
    }

    /**
     * 记录用户设备信息
     *
     * @param behavior
     * @param deviceId
     * @return
     */
    private boolean opDeviceMember(Behavior behavior, String deviceId) {
        String lockKey = StringUtils.format("{}risk:lock:devicemember:member_{}:ip_{}", Constants.BASE_KEY, behavior.getUserId(), deviceId);
        LockInfo lockInfo = lockHelper.lock(lockKey);
        try {
            if (lockInfo != null) {
                TdDeviceMember deviceMember = deviceMemberService.getByDeviceIdAndUserId(behavior.getUserId(), deviceId);
                if (deviceMember == null) {
                    deviceMember = new TdDeviceMember();
                    deviceMember.setDeviceId(deviceId);
                    deviceMember.setUserId(behavior.getUserId());
                    deviceMember.setMobile(behavior.getMobile());
                    deviceMember.setAccount(behavior.getAccount());
                    deviceMemberService.save(deviceMember);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("数据上报--记录用户设备，devicemember异常，msgId:{},userId:{},deviceId:{}", behavior.getMsgId(), behavior.getUserId(), deviceId, e);
            return false;
        } finally {
            lockHelper.unLock(lockInfo);
        }
        return false;
    }

}
