package com.euler.sdk.dubbo;

import com.euler.common.core.domain.R;
import com.euler.sdk.api.RemoteGoodsService;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.service.ICallbackService;
import com.euler.sdk.service.IGoodsService;
import com.euler.sdk.service.IMemberRightsService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteGoodsServiceImpl implements RemoteGoodsService {
    @Autowired
    private IGoodsService goodsService;

    private final ICallbackService iCallbackService;
    @Autowired
    private IMemberRightsService memberRightsService;



    /**
     * 查询商品之后检测当前商品是否可用
     *
     * @param id 商品主键
     * @return 商品
     */
    @Override
    public GoodsVo orderQueryById(Integer id,Long userId) {
        return goodsService.queryById(id, 2,userId);
    }

    /**
     * 商品购买玩之后进行的回调
     *
     * @param goodId
     * @param userId
     * @return
     */
    @Override
    public R buyGoodsCallback(Integer goodsId,Integer gameId, Long userId) {
        return iCallbackService.handleBuyGoodsCallback(goodsId,gameId, userId);
    }

}
