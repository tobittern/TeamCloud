package com.euler.risk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.domain.entity.AdvertisingMedia;
import com.euler.risk.domain.vo.AdvertisingMediaVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 广告媒体Mapper接口
 *
 * @author euler
 * @date 2022-09-22
 */
@Mapper
public interface AdvertisingMediaMapper extends BaseMapperPlus<AdvertisingMediaMapper, AdvertisingMedia, AdvertisingMediaVo> {

}
