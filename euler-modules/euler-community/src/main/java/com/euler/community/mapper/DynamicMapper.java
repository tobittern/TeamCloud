package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Dynamic;
import com.euler.community.domain.vo.DynamicVo;
import org.apache.ibatis.annotations.Param;

/**
 * 动态Mapper接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface DynamicMapper extends BaseMapperPlus<DynamicMapper, Dynamic, DynamicVo> {

    Page<DynamicVo> selectDynamic(@Param("page") Page<DynamicVo> page, @Param(Constants.WRAPPER) Wrapper<Dynamic> queryWrapper);

}
