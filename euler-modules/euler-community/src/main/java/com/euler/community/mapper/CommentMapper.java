package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Comment;
import com.euler.community.domain.vo.CommentFrontVo;
import com.euler.community.domain.vo.CommentVo;
import com.euler.community.domain.vo.NewCommentFrontVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 *
 * @author euler
 * @date 2022-06-07
 */
public interface CommentMapper extends BaseMapperPlus<CommentMapper, Comment, CommentVo> {

    Page<CommentFrontVo> selectCommentForDynamicList(@Param("page") Page<CommentFrontVo> page, @Param(Constants.WRAPPER) Wrapper<CommentFrontVo> queryWrapper);

    Page<CommentFrontVo> selectCommentForCommentList(@Param("page") Page<CommentFrontVo> page, @Param(Constants.WRAPPER) Wrapper<CommentFrontVo> queryWrapper);

    List<CommentVo> selectDynamicDivineEvaluation(@Param(Constants.WRAPPER) Wrapper<CommentVo> queryWrapper);

    Page<NewCommentFrontVo> getNewCommentList(@Param("page") Page<NewCommentFrontVo> page, @Param(Constants.WRAPPER) Wrapper<NewCommentFrontVo> queryWrapper, @Param("type") Integer type);


    List<IdTypeDto<Long, Integer>> selectSumPraise(@Param(Constants.WRAPPER) Wrapper<CommentVo> queryWrapper);

    CommentVo selectDivineComment(@Param("max") Integer max, @Param(Constants.WRAPPER) Wrapper<CommentVo> queryWrapper);


    List<IdTypeDto<Long, Integer>> getDynamicCommentReportNum(@Param(Constants.WRAPPER) Wrapper<IdNameDto<Long>> queryWrapper);

    /**
     * 获取用户某天动态的评论数量
     */
    Integer getUseDynamicCommentCount(@Param(Constants.WRAPPER)Wrapper<Comment> queryWrapper);

    /**
     * 获取用户某天评论的评论数量
     */
    Integer getUseCommentComCount(@Param(Constants.WRAPPER)Wrapper<Comment> queryWrapper);

}
