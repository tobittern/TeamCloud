package com.euler.sdk.dubbo;

import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.sdk.service.IGameConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteGameConfigServiceImpl implements RemoteGameConfigService {

    @Autowired
    private IGameConfigService gameConfigService;

    /**
     * 根据条件查询游戏配置信息
     *
     * @return 游戏配置信息
     */
    @Override
    public GameConfigVo selectGameConfigByParam(Integer gameId, String type, String platform) {
        return gameConfigService.selectGameConfigByParam(gameId, type, platform);
    }

}
