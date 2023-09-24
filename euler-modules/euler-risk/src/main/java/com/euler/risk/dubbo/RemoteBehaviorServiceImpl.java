package com.euler.risk.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.risk.api.RemoteBehaviorService;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.config.WebConfig;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.service.IBehaviorTypeService;
import com.euler.risk.service.ITdDeviceInfoService;
import com.euler.risk.service.ITfDeviceSummaryService;
import com.euler.risk.service.ITfIpSummaryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteBehaviorServiceImpl implements RemoteBehaviorService {

    @Autowired
    private IBehaviorTypeService behaviorTypeService;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private ITdDeviceInfoService deviceInfoService;
    @Autowired
    private ITfDeviceSummaryService deviceSummaryService;
    @Autowired
    private ITfIpSummaryService ipSummaryService;


    /**
     * ip统计
     */
    @Override
    public void ipSummary(FillDataDto fillDataDto) {
        ipSummaryService.fillData(fillDataDto);
    }


    /**
     * 设备统计
     */
    @Override
    public void deviceSummary(FillDataDto fillDataDto) {
        deviceSummaryService.fillData(fillDataDto);
    }

    /**
     * 获取拦截行为列表
     *
     * @return
     */
    @Override
    public List<BehaviorType> getBehaviorList(String platform, String device) {
        return behaviorTypeService.list(Wrappers.<BehaviorType>lambdaQuery().eq(BehaviorType::getType, "0").eq(BehaviorType::getPlatform, platform).eq(BehaviorType::getDevice, device));
    }


    /**
     * 获取拦截行为列表
     *
     * @return
     */
    @Override
    public BehaviorType getBehaviorByCode(String platform, String device, String code) {
        return behaviorTypeService.getOne(Wrappers.<BehaviorType>lambdaQuery().eq(BehaviorType::getType, "1").eq(BehaviorType::getCode, code).eq(BehaviorType::getPlatform, platform).eq(BehaviorType::getDevice, device).last("limit 1"));
    }

    /**
     * 上报用户行为数据
     *
     * @param behaviorType     请求行为
     * @param requestHeaderDto 请求头数据
     * @param reqData          请求体
     * @param userId
     * @param mobile
     * @param userName
     * @return
     */
    @Override
    public void submitUserBehavior(BehaviorType behaviorType, RequestHeaderDto requestHeaderDto, String reqData, Long userId,    String ip) {
        String msgId = rabbitMqHelper.createMsgId("S");

        BehaviorMqMsgDto behaviorMqMsgDto = new BehaviorMqMsgDto();
        behaviorMqMsgDto.setMsgId(msgId)
            .setUserId(userId)
            .setBehaviorType(behaviorType)
            .setRequestHeader(requestHeaderDto)
            .setRequestData(reqData)
            .setIp(ip);
        rabbitMqHelper.sendObj(webConfig.getBehaviorExchange(), webConfig.getBehaviorRoutingkey(), msgId, behaviorMqMsgDto);
    }


    /**
     * 网关上报用户行为数据
     *
     * @param behaviorType     请求行为
     * @param requestHeaderDto 请求头数据
     * @param reqData          请求体
     * @return
     */
    @Override
    public void submitUserBehavior(BehaviorType behaviorType, RequestHeaderDto requestHeaderDto, String reqData, String ip) {
        Long userId = 0L;
        submitUserBehavior(behaviorType, requestHeaderDto, reqData, userId,  ip);
    }

    @Override
    public TdDeviceInfoVo getDeviceInfoByHeader(RequestHeaderDto requestHeaderDto) {
        return deviceInfoService.getDeviceInfoByHeader(requestHeaderDto);
    }


}
