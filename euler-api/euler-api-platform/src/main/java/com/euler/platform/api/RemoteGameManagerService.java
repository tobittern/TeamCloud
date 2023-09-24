package com.euler.platform.api;

import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.platform.api.domain.OpenGame;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;

import java.util.List;
import java.util.Map;

/**
 * @author euler
 */
public interface RemoteGameManagerService {

    /**
     * 通过id查询游戏基础信息
     */
    List<OpenGameDubboVo> selectByIds(List<Integer> collect);

    /**
     * 通过id还有一些游戏的查询条件
     */
    TableDataInfoCoreDto<OpenGameDubboVo> selectByParams(List<Integer> collect, SelectGameDto dto);

    /**
     * 通过id还有一些游戏的查询条件, 不分页
     */
    List<OpenGameDubboVo> selectByChannel(List<Integer> collect, SelectGameDto dto);

    /**
     * 通过id查询游戏基础信息
     */
    OpenGameDubboVo selectOpenGameInfo(String appId);

    TableDataInfoCoreDto<OpenGameDubboVo> selectGameByParam(Map<String, Object> map);

    /**
     * 用户访问游戏详情
     *
     * @param idDto
     * @return
     */
    OpenGameVo getGameInfo(IdDto<Integer> idDto);

    /**
     * 根据用户id查询出审核过且在线的游戏列表
     * @return
     */
    List<OpenGameVo> getGameListByUserId(IdDto<Long> idDto);

    /**
     * 根据渠道id查询游戏列表
     */
    List<OpenGame> getGameList(Integer channelId);

    /**
     * 通过游戏名称和平台查询游戏信息
     */
    OpenGameVo getGameInfo(String gameName, String platform);

}
