package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.risk.api.domain.Behavior;
import com.euler.risk.domain.dto.BehaviorMqMsgDto;

/**
 * 后端用户行为上报数据Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface IBehaviorService extends IService<Behavior> {


    void save(BehaviorMqMsgDto behaviorMqMsgDto);
}
