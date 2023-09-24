package com.euler.risk.service.reflect;

import com.euler.risk.domain.dto.BehaviorMqMsgDto;
import com.euler.risk.domain.dto.BehaviorUserDto;

/**
 * 解析用户行为数据中的用户信息
 */
public interface IBehaviorUserService {

    /**从用户行为中获取用户信息
     * @param msgDto
     * @return
     */
    BehaviorUserDto getUserInfo(BehaviorMqMsgDto msgDto);
}
