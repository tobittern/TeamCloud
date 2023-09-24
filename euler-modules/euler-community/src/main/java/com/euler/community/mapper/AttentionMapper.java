package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.dto.AttentionDto;
import com.euler.community.domain.entity.Attention;
import com.euler.community.domain.vo.AttentionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注Mapper接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface AttentionMapper extends BaseMapperPlus<AttentionMapper, Attention, AttentionVo> {

    /**
     * 查询我的关注列表
     *
     * @return 我的关注列表
     */
    Page<AttentionVo> getMyAttentionList(@Param("page") Page<Attention> page, @Param("memberId") Long memberId);

    /**
     * 查询我的粉丝列表
     *
     * @return 我的粉丝列表
     */
    Page<AttentionVo> getMyFansList(@Param("page") Page<Attention> page, @Param("attentionUserId") Long attentionUserId);

    /**
     * 查询我的关注列表
     *
     * @return 我的关注列表
     */
    List<AttentionVo> getMyAttentionList(@Param("memberId") Long memberId);

    /**
     * 查询我的粉丝列表
     *
     * @return 我的粉丝列表
     */
    List<AttentionVo> getMyFansList(@Param("attentionUserId") Long attentionUserId);

    /**
     * 查询关注的数量
     *
     * @param memberId 用户id
     * @return 关注的数量
     */
    Integer getAttentionCount(Long memberId);

    /**
     * 查询粉丝的数量
     *
     * @param memberId 用户id
     * @return 粉丝的数量
     */
    Integer getFansCount(Long memberId);

    /**
     * 查询我的新粉丝列表
     *
     * @return 新粉丝列表
     */
    Page<AttentionVo> getMyNewAttentionList(@Param("page") Page<Attention> page, @Param(Constants.WRAPPER) Wrapper<Attention> queryWrapper);

    /**
     * 查询他人的关注列表
     *
     * @return 他人的关注列表
     */
    List<AttentionVo> getOthersAttentionList(Long memberId);

    /**
     * 查询他人的粉丝列表
     *
     * @return 他人的粉丝列表
     */
    List<AttentionVo> getOthersFansList(Long memberId);

    /**
     * 根据昵称检索我的关注列表
     *
     * @return 我的关注列表
     */
    Page<AttentionVo> getMyAttentionListByName(@Param("page") Page<Attention> page, @Param(Constants.WRAPPER) Wrapper<Attention> queryWrapper);

    /**
     * 根据昵称检索我的粉丝列表
     *
     * @return 我的粉丝列表
     */
    Page<AttentionVo> getMyFansListByName(@Param("page") Page<Attention> page, @Param(Constants.WRAPPER) Wrapper<Attention> queryWrapper);

    /**
     * 根据昵称检索他人的关注列表
     *
     * @return 他人的关注列表
     */
    List<AttentionVo> getOthersAttentionListByName(AttentionDto dto);

    /**
     * 根据昵称检索他人的粉丝列表
     *
     * @return 他人的粉丝列表
     */
    List<AttentionVo> getOthersFansListByName(AttentionDto dto);

}
