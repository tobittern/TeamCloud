package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.GiftInfo;
import com.euler.sdk.domain.vo.GiftInfoVo;

/**
 * 礼包Mapper接口
 *
 * @author euler
 * @date 2022-03-24
 */
public interface GiftInfoMapper extends BaseMapperPlus<GiftInfoMapper, GiftInfo, GiftInfoVo> {

    /**
     * 根据礼包组id查询礼包的数量
     *
     * @param giftGroupId 礼包组id
     * @return 礼包数量
     */
    int giftInfoListCount(Integer giftGroupId);

}
