package com.euler.sdk.api;

import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.api.domain.MyGameVo;

import java.util.List;

public interface RemoteMyGameService {

    /**
     * 获取登录的游戏信息
     *
     * @param gameId
     * @param memberId
     * @param channelId
     * @return
     */
    MyGameVo getCurrentGameInfo(Integer gameId, Long memberId, Integer channelId);

    GameUserManagement getGameUserInfo(Integer gameId, Long memberId, String serverId, String roleId);

    /**
     * 获取游戏用户数据列表
     *
     * @param memberIds
     * @param gameId
     * @return
     */
    List<GameUserManagement> getGameUserInfoList(List<Long> memberIds, Integer gameId);
    List<GameUserManagementVo> getAllGameService();

    /**
     * 根据区服名称查询游戏区服列表
     *
     * @param idNameDto
     * @return
     */
    List<GameUserManagementVo> getServerListByName(IdNameDto<Long> idNameDto);

}
