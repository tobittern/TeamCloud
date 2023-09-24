package com.euler.sdk.api;

import com.euler.sdk.api.domain.ChannelPackageVo;

import java.util.List;

public interface RemoteChannelService {

    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @return 渠道分包信息
     */
    ChannelPackageVo selectChannelPackageByCode(String codeKey, String appId);

//    /**
//     * 开放平台创建游戏的时候默认创建一个默认的default渠道
//     * @param gameName
//     * @param gameId
//     * @return
//     */
//    Boolean createChannelPackageDefault(String gameName, Integer gameId);

    List<ChannelPackageVo> getGamePackageInfoByIds(List<Integer> gameIds);

    /**
     * 根据游戏名查询出指定游戏列表
     * @return
     */
    List<ChannelPackageVo> getGameListByName();
}
