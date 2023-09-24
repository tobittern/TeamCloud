package com.euler.risk.dubbo;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.risk.api.RemoteLoginConfigService;
import com.euler.risk.api.domain.LoginConfigVo;
import com.euler.risk.domain.dto.LoginConfigDto;
import com.euler.risk.service.IHeartBeatService;
import com.euler.risk.service.ILoginConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteLoginConfigServiceImpl implements RemoteLoginConfigService {

    @Autowired
    private ILoginConfigService loginConfigService;
    @Autowired
    private IHeartBeatService iHeartBeatService;

    /**
     * 查询SDK/APP的登录配置详细信息
     *
     * @param platformType 平台标识（1:sdk 4:九区玩家app）（必填）
     * @param gameId       游戏id，针对sdk，不填的话，默认取全局的登录配置，已填且配置过的话，取该游戏和全局的登录配置的交集
     * @param platform     平台（1:Android 2:ios 3:h5）（必填）
     * @return 登录配置
     */
    @Override
    public LoginConfigVo queryInfo(String platformType, Integer gameId, String platform) {
        LoginConfigDto dto = new LoginConfigDto();
        dto.setPlatformType(platformType);
        dto.setGameId(gameId);
        dto.setPlatform(platform);
        return loginConfigService.queryInfo(dto);
    }

    /**
     * 检测用户封禁状态
     *
     * @param userId
     * @param memberName
     * @param mobile
     * @param ip
     * @param dto
     * @return
     */
    @Override
    public R checkUserStatus(Long userId, String memberName, String mobile, String ip, RequestHeaderDto dto) {
        return iHeartBeatService.checkUserStatus(userId, memberName, mobile, ip, dto);
    }


}
