package com.euler.risk.api;

import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.api.domain.TdDeviceInfoVo;

import java.util.List;

public interface RemoteBehaviorService {


    /**
     * ip统计
     */
    void ipSummary(FillDataDto fillDataDto);


    /**
     * 设备统计
     */
    void deviceSummary(FillDataDto fillDataDto);

    /**
     * 获取拦截行为列表
     *
     * @return
     */
    List<BehaviorType> getBehaviorList(String platform, String device);

    BehaviorType getBehaviorByCode(String platform, String device, String code);


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
    void submitUserBehavior(BehaviorType behaviorType, RequestHeaderDto requestHeaderDto, String reqData, Long userId, String ip);


    /**
     * 上报用户行为数据
     *
     * @param behaviorType     请求行为
     * @param requestHeaderDto 请求头数据
     * @param reqData          请求体
     * @return
     */
    void submitUserBehavior(BehaviorType behaviorType, RequestHeaderDto requestHeaderDto, String reqData, String ip);

    TdDeviceInfoVo getDeviceInfoByHeader(RequestHeaderDto requestHeaderDto);

}
