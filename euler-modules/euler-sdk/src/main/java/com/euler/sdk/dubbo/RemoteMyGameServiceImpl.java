package com.euler.sdk.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.sdk.api.RemoteMyGameService;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.dto.MyGameDto;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.service.IGameUserManagementService;
import com.euler.sdk.service.IMyGameService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteMyGameServiceImpl implements RemoteMyGameService {
    @Autowired
    private IMyGameService myGameService;
@Autowired
private IGameUserManagementService gameUserManagementService;

    /**
     * 获取登录的游戏信息
     *
     * @param gameId
     * @param memberId
     * @param channelId
     * @return
     */
    @Override
    public MyGameVo getCurrentGameInfo(Integer gameId, Long memberId, Integer channelId) {
        MyGameDto myGameDto=new MyGameDto();
        myGameDto.setGameId(gameId);
        myGameDto.setUserId(memberId);
        myGameDto.setChannelId(channelId);
        return myGameService.getCurrentGameInfo(myGameDto);
    }


    @Override
    public GameUserManagement getGameUserInfo(Integer gameId, Long memberId, String serverId,String roleId) {
        GameUserManagement gameUser=new GameUserManagement();
        gameUser.setGameId(gameId);
        gameUser.setMemberId(memberId);
        gameUser.setRoleId(roleId);
        gameUser.setServerId(serverId);

        return gameUserManagementService.getGameUserInfo(gameUser);
    }

    @Override
    public List<GameUserManagementVo> getAllGameService() {
        List<GameUserManagementVo> rList = new ArrayList<>();
        List<GameUserManagementVo> allGameService = gameUserManagementService.getAllGameService();
        for (GameUserManagementVo v : allGameService) {
            GameUserManagementVo v2 = BeanUtil.toBean(v, GameUserManagementVo.class);
            rList.add(v2);
        }
        return rList;
    }


    /**
     * 获取游戏用户数据列表
     *
     * @param memberIds
     * @param gameId
     * @return
     */
    @Override
  public List<GameUserManagement> getGameUserInfoList(List<Long> memberIds, Integer gameId){
        return gameUserManagementService.getGameUserInfoList(memberIds,gameId);
    }

    /**
     * 根据区服名称查询游戏区服列表
     *
     * @param idNameDto
     * @return
     */
    public List<GameUserManagementVo> getServerListByName(IdNameDto<Long> idNameDto) {
        return gameUserManagementService.getServerListByName(idNameDto);
    }

}
