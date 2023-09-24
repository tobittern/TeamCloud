package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.GiftBagCdkBo;
import com.euler.community.domain.dto.GiftBagCdkDto;
import com.euler.community.domain.entity.GiftBagCdk;
import com.euler.community.domain.vo.GiftBagCdkVo;

import java.util.Collection;
import java.util.List;

/**
 * 礼包兑换码数据Service接口
 *
 * @author euler
 *  2022-06-07
 */
public interface IGiftBagCdkService extends IService<GiftBagCdk> {

    /**
     * 查询礼包兑换码数据
     *
     * @param id 礼包兑换码数据主键
     * @return 礼包兑换码数据
     */
    GiftBagCdkVo queryById(Long id);

    /**
     * 查询礼包兑换码数据列表
     *
     * @param giftBagCdkDto 礼包兑换码数据
     * @return 礼包SDk数据集合
     */
    TableDataInfo<GiftBagCdkVo> queryPageList(GiftBagCdkDto giftBagCdkDto);

    /**
     * 查询礼包兑换码数据列表
     *
     * @param giftBagCdkDto 礼包兑换码数据
     * @return 礼包兑换码数据集合
     */
    List<GiftBagCdkVo> queryList(GiftBagCdkDto giftBagCdkDto);

    /**
     * 修改礼包兑换码数据
     *
     * @param bo 礼包SDk数据
     * @return 结果
     */
    R insertByBo(GiftBagCdkBo bo);

    /**
     * 修改礼包兑换码数据
     *
     * @param bo 礼包兑换码数据
     * @return 结果
     */
    R updateByBo(GiftBagCdkBo bo);

    /**
     * 校验并批量删除礼包兑换码数据信息
     *
     * @param ids 需要删除的礼包兑换码数据主键集合
     * @param isValid 是否校验, true-删除前校验, false-不校验
     * @return 结果
     */
    R<Void> deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
