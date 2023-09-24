package com.euler.sdk.dubbo;

import com.euler.sdk.api.RemoteChannelService;
import com.euler.sdk.api.domain.ChannelPackageVo;
import com.euler.sdk.service.IChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteChannelServiceImpl implements RemoteChannelService {
    @Autowired
    private IChannelService iChannelService;

    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @return 渠道分包信息
     */
    @Override
    public ChannelPackageVo selectChannelPackageByCode(String codeKey, String appId) {
        return iChannelService.selectChannelPackageByCode(codeKey, appId);
    }

//    /**
//     * 根据渠道code查询出分包渠道的基础信息
//     *
//     * @return 渠道分包信息
//     */
//    @Override
//    public Boolean createChannelPackageDefault(String gameName, Integer gameId) {
//        return iChannelService.createChannelPackageDefault(gameName, gameId);
//    }

    /**
     * 获取一批游戏的分包基础信息
     * @param gameIds
     * @return
     */
    @Override
    public List<ChannelPackageVo> getGamePackageInfoByIds(List<Integer> gameIds) {
        return iChannelService.getGamePackageInfoByIds(gameIds);
    }

    /**
     * 根据游戏名查询出指定游戏列表
     * @return
     */
    @Override
    public List<ChannelPackageVo> getGameListByName() {
        return iChannelService.getGameListByName();
    }
}
