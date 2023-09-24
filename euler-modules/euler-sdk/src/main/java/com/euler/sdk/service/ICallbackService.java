package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;

/**
 * 回调总入口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface ICallbackService {

    /**
     * 商品购买之后的回调
     */
    R handleBuyGoodsCallback(Integer goodsId, Integer gameId,Long userId);
}
