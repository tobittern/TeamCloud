package com.euler.sdk.service.Impl;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.domain.entity.MemberRights;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.service.ICallbackService;
import com.euler.sdk.service.IGoodsService;
import com.euler.sdk.service.IMemberRightsService;
import com.euler.sdk.service.IWalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 回调总入口
 *
 * @author euler
 * @date 2022-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CallbackServiceImpl implements ICallbackService {

    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private IWalletService iWalletService;
    @Autowired
    private IMemberRightsService iMemberRightsService;

    /**
     * 商品购买之后的回调
     */
    @Override
    public R handleBuyGoodsCallback(Integer goodsId,Integer gameId, Long userId) {
        // 获取商品的基础信息
        GoodsVo goodsVo = iGoodsService.queryById(goodsId, 1,userId);

        // 基础验证通过 根据商品的类型进行不通的操作
        if (goodsVo.getGoodsType().equals(1)) {
            // TODO 年费商品 我们直接给指定用户分配支付之后的权益
            MemberRights memberRightsBo = new MemberRights();
            memberRightsBo.setId(userId);
            memberRightsBo.setName(goodsVo.getGoodsName());
            memberRightsBo.setGoodsId(goodsVo.getId());
            memberRightsBo.setName(goodsVo.getGoodsName());
            memberRightsBo.setLever(goodsVo.getGoodsLevel());
            memberRightsBo.setGoodsPrice(goodsVo.getGoodsPrice());
            memberRightsBo.setPayPrice(goodsVo.getGoodsDiscountPrice());
            return iMemberRightsService.openMember(memberRightsBo);
        } else {
            // 普通充值商品  proportion赠送比例  get_type获取商品的类型 (1平台币 2余额)  platform_currency 获取的平台币 - 立即获取多少平台币
            // 计算需要获取多少个平台币和余额
            double floor = Math.floor(goodsVo.getPlatformCurrency() * goodsVo.getProportion());
            Integer realNums = Convert.toInt(floor);
            RechargeTypeEnum platformCurrency = null;
            if (goodsVo.getGetType().equals(1)) {
                platformCurrency = RechargeTypeEnum.find(3);
            } else {
                platformCurrency = RechargeTypeEnum.find(2);
            }
            boolean b = iWalletService.modifyWallet(userId,gameId,1,realNums, platformCurrency,1,"充值");
            if (b) {
                return R.ok();
            }
            return R.fail("数据更新失败");
        }
    }
}
