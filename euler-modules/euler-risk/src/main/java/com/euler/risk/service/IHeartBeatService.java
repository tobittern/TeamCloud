package com.euler.risk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.risk.domain.dto.HeartBeatDto;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-08-24
 */
public interface IHeartBeatService {

    R heartJumpCollection(HeartBeatDto dto);

    R checkUserStatus(Long userId, String memberName, String mobile, String ip, RequestHeaderDto dto);
}
