package com.euler.platform.dubbo;

import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGame;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.platform.service.IOpenGameService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteGameManagerServiceImpl implements RemoteGameManagerService {

    private final IOpenGameService iOpenGameService;

    /**
     * 通过id查询游戏基础信息
     *
     * @return
     */
    @Override
    public List<OpenGameDubboVo> selectByIds(List<Integer> collect) {
        return iOpenGameService.selectByIds(collect);
    }

    /**
     * 通过id和其他条件
     *
     * @return
     */
    @Override
    public TableDataInfoCoreDto<OpenGameDubboVo> selectByParams(List<Integer> collect, SelectGameDto dto) {
        return iOpenGameService.selectByParams(collect, dto);
    }

    /**
     * 通过id还有一些游戏的查询条件, 不分页
     */
    @Override
    public List<OpenGameDubboVo> selectByChannel(List<Integer> collect, SelectGameDto dto) {
        return iOpenGameService.selectByChannel(collect, dto);
    }

    @Override
    public OpenGameDubboVo selectOpenGameInfo(String appId) {
        return iOpenGameService.selectInfoByAppId(appId);
    }

    @Override
    public TableDataInfoCoreDto<OpenGameDubboVo> selectGameByParam(Map<String,Object> map) {
        return iOpenGameService.selectGameByParam(map);
    }

    @Override
    public OpenGameVo getGameInfo(IdDto<Integer> idDto) {
        return iOpenGameService.getGameInfo(idDto);
    }

    /**
     * 根据用户id查询出审核过且在线的游戏列表
     * @return
     */
    @Override
    public List<OpenGameVo> getGameListByUserId(IdDto<Long> idDto) {
        return iOpenGameService.queryGameListByUserId(idDto);
    }

    /**
     * 根据渠道id查询游戏列表
     */
    @Override
    public List<OpenGame> getGameList(Integer channelId) {
        return iOpenGameService.getGameList(channelId);
    }

    /**
     * 通过游戏名称和平台查询游戏信息
     */
    @Override
    public OpenGameVo getGameInfo(String gameName, String platform) {
        return iOpenGameService.getGameInfo(gameName, platform);
    }
}
