package com.euler.risk.api;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.risk.api.domain.LoginConfigVo;

/**
 * @author euler
 * @date 2022-06-01
 */
public interface RemoteLoginConfigService {

    /**
     * 查询SDK/APP的登录配置详细信息
     *
     * @param platformType 平台标识（1:sdk 4:九区玩家app）（必填）
     * @param gameId       游戏id，针对sdk，不填的话，默认取全局的登录配置，已填且配置过的话，取该游戏和全局的登录配置的交集
     * @param platform     平台（1:Android 2:ios 3:h5）（必填）
     * @return 登录配置
     */
    LoginConfigVo queryInfo(String platformType, Integer gameId, String platform);

    /**
     * 检测用户封禁状态
     * @param userId
     * @param memberName
     * @param mobile
     * @param ip
     * @param dto
     * @return
     */
    R checkUserStatus(Long userId, String memberName, String mobile, String ip, RequestHeaderDto dto);


}
