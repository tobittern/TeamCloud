package com.euler.sdk.api;

import com.euler.common.core.domain.R;
import com.euler.sdk.api.domain.GoodsVo;

public interface RemoteGoodsService {

    /**
     * 商品详情查询 - 进行了商品可用的判断
     * @param id
     * @return
     */
    GoodsVo orderQueryById(Integer id,Long userId);

    /**
     * 商品购买玩之后进行的回调
     * @param goodsId
     * @param gameId
     * @param userId
     * @return
     */
    R buyGoodsCallback(Integer goodsId, Integer gameId, Long userId);

}
