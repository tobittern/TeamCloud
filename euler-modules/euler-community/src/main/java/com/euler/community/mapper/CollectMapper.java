package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Collect;
import com.euler.community.domain.vo.CollectVo;
import org.apache.ibatis.annotations.Param;

/**
 * 动态收藏Mapper接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface CollectMapper extends BaseMapperPlus<CollectMapper, Collect, CollectVo> {
    /**
     * 获取用户收藏的动态数
     *
     * @param memberId
     * @return
     */
    Integer getUserCollectDynamicNum(Long memberId);

    /**
     * 获取用户某天收藏的动态数
     *
     * @return
     */
    Integer getUserCollectCount(@Param(Constants.WRAPPER) Wrapper<Collect> queryWrapper);

    /**
     * 分页获取用户收藏的动态数
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<CollectVo> getUserCollectDynamicList(@Param("page") Page<Collect> page, @Param(Constants.WRAPPER) Wrapper<Collect> queryWrapper);
}
