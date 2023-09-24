package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.GiftBagBo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.GiftBagDto;
import com.euler.community.domain.entity.GiftBag;
import com.euler.community.domain.vo.GiftBagVo;

import java.util.Collection;
import java.util.List;

/**
 * 礼包配置Service接口
 *
 * @author euler
 *  2022-06-02
 */
public interface IGiftBagService extends IService<GiftBag> {

    /**
     * 查询礼包配置
     *
     * @param id 礼包配置主键
     * @return 礼包配置
     */
    GiftBagVo queryById(Long id);

    /**
     * 查询礼包配置列表
     *
     * @param giftBagDto 礼包配置
     * @return 礼包配置集合
     */
    TableDataInfo<GiftBagVo> queryPageList(GiftBagDto giftBagDto);

    /**
     * 查询礼包配置列表
     *
     * @param giftBagDto 礼包配置
     * @return 礼包配置集合
     */
    List<GiftBagVo> queryList(GiftBagDto giftBagDto);

    /**
     * 新增添加方法
     * @param bo bag
     * @return boolean
     */
    R<Void> insertByBo(GiftBagBo bo);

    /**
     * 修改礼包配置
     *
     * @param bo 礼包配置
     * @return 结果
     */
    R<Void> updateByBo(GiftBagBo bo);

    /**
     * 校验并批量删除礼包配置信息
     *
     * @param ids 需要删除的礼包配置主键集合
     * @return 结果
     */
    R<Void> deleteWithValidByIds(Collection<Long> ids);

    /**
     * 获取我的礼包
     * @param giftBagCdkBo cdk
     * @return R
     */
    R<Object> receivedBags(GiftBagCdkBo giftBagCdkBo);
    /**
     * 待领取的礼包
     * @param bo cdk
     * @return R
     */
    R<Object> waitBags(GiftBagCdkBo bo);
    /**
     * 领取礼包
     * @param bo cdk
     * @return R
     */
    R<Void> pickBag(GiftBagCdkBo bo);
    /**
     * 礼包兑换
     * @param bo cdk
     * @return R
     */
    R<Void> exchangeBag(GiftBagCdkBo bo);
    /**
     * 修改礼包状态
     * @param bo 礼包
     * @return R
     */
    R<Void> updateStatus(GiftBagBo bo);
}
