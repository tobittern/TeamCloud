package com.euler.sdk.mapper;

import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.ChannelPackageVo;
import com.euler.sdk.domain.entity.ChannelPackage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 渠道分组Mapper接口
 *
 * @author euler
 * @date 2022-04-01
 */
@Mapper
public interface ChannelPackageMapper extends BaseMapperPlus<ChannelPackageMapper, ChannelPackage, ChannelPackageVo> {

    List<KeyValueDto> selectGameIdHavePackageNums(int channelId);
}
