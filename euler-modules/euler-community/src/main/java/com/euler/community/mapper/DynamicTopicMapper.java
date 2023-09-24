package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.DynamicTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态关联话题Mapper接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface DynamicTopicMapper extends BaseMapperPlus<DynamicTopicMapper, DynamicTopic, DynamicTopic> {

    List<IdNameDto<Long>> selectTopicNameByDynamicId(@Param(Constants.WRAPPER) Wrapper<IdNameDto<Long>> queryWrapper);

}
