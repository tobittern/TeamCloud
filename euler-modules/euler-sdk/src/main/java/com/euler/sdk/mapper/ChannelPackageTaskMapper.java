package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.ChannelPackageTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 渠道分组Mapper接口
 *
 * @author euler
 * @date 2022-04-01
 */
@Mapper
public interface ChannelPackageTaskMapper extends BaseMapperPlus<ChannelPackageTaskMapper, ChannelPackageTask, ChannelPackageTask> {

}
