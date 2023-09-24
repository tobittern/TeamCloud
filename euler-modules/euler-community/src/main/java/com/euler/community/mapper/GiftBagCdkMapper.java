package com.euler.community.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.GiftBagCdk;
import com.euler.community.domain.vo.GiftBagCdkVo;

import java.util.List;

/**
 * 礼包SDk数据Mapper接口
 *
 * @author euler
 * @date 2022-06-07
 */
public interface GiftBagCdkMapper extends BaseMapperPlus<GiftBagCdkMapper, GiftBagCdk, GiftBagCdkVo> {

    /**
     * 物理删除数据
     * @param ids
     */
    public void physicallyDeleteBatchIds(List<Long> ids);

}
