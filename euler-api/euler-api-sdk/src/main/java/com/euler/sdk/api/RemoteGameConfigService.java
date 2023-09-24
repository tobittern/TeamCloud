package com.euler.sdk.api;

import com.euler.sdk.api.domain.GameConfigVo;

public interface RemoteGameConfigService {

    /**
     * 根据条件查询游戏配置信息
     *
     * @return 游戏配置信息
     */
    GameConfigVo selectGameConfigByParam(Integer gameId, String type, String platform);

}
