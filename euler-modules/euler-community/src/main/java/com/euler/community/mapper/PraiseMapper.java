package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Praise;
import com.euler.community.domain.vo.NewPraiseVo;
import com.euler.community.domain.vo.PraiseVo;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞Mapper接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface PraiseMapper extends BaseMapperPlus<PraiseMapper, Praise, PraiseVo> {

    /**
     * 查询我的新粉丝列表
     *
     * @return 新粉丝列表
     */
    Page<NewPraiseVo> getNewPraiseList(@Param("page") Page<Praise> page, @Param(Constants.WRAPPER)Wrapper<Praise> queryWrapper);

    /**
     * 获取用户点赞的动态列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<PraiseVo> getUserPraiseDynamicList(@Param("page") Page<Praise> page, @Param(Constants.WRAPPER)Wrapper<Praise> queryWrapper);

    /**
     * 获取用户点赞的评论列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<PraiseVo> getUserPraiseCommentList(@Param("page") Page<Praise> page, @Param(Constants.WRAPPER)Wrapper<Praise> queryWrapper);

    /**
     * 获取用户某天动态的点赞数量
     */
    Integer getUseDynamicPraiseCount(@Param(Constants.WRAPPER)Wrapper<Praise> queryWrapper);

    /**
     * 获取用户某天评论的点赞数量
     */
    Integer getUseCommentPraiseCount(@Param(Constants.WRAPPER)Wrapper<Praise> queryWrapper);

}
