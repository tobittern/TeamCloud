package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.AttentionBo;
import com.euler.community.domain.dto.AttentionDto;
import com.euler.community.domain.entity.Attention;
import com.euler.community.domain.vo.AttentionVo;

import java.util.List;

/**
 * 关注Service接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface IAttentionService extends IService<Attention> {

    /**
     * 查询我的关注列表
     *
     * @return 自己的关注列表
     */
    List<AttentionVo> queryMyAttentionList();

    /**
     * 查询我的粉丝列表
     *
     * @return 自己的粉丝列表
     */
    List<AttentionVo> queryMyFansList();

    /**
     * 查询我的关注列表
     *
     * @return 自己的关注列表
     */
    TableDataInfo<AttentionVo> queryMyAttentionList(AttentionDto dto);

    /**
     * 查询我的粉丝列表
     *
     * @return 自己的粉丝列表
     */
    TableDataInfo<AttentionVo> queryMyFansList(AttentionDto dto);

    /**
     * 查询我的新粉丝列表
     *
     * @return 自己的新粉丝列表
     */
    TableDataInfo<AttentionVo> queryMyNewFansList(AttentionDto dto);

    /**
     * 查询他人的关注列表
     *
     * @param dto
     * @return 他人的关注列表
     */
    TableDataInfo<AttentionVo> queryOthersAttentionList(AttentionDto dto);

    /**
     * 查询他人的粉丝列表
     *
     * @param dto
     * @return 他人的粉丝列表
     */
    TableDataInfo<AttentionVo> queryOthersFansList(AttentionDto dto);

    /**
     * 查询关注的数量
     *
     * @param memberId 用户id
     * @return
     */
    Integer queryAttentionCountById(Long memberId);

    /**
     * 查询粉丝的数量
     *
     * @param memberId 用户id
     * @return
     */
    Integer queryFansCountById(Long memberId);

    /**
     * 新增关注
     *
     * @param bo 关注
     * @return 结果
     */
    R insertByBo(AttentionBo bo);

    /**
     * 取消关注
     *
     * @param bo 关注
     * @return 结果
     */
    R updateByBo(AttentionBo bo);

    /**
     * 查询用户是否已关注
     *
     * @param memberId        用户id
     * @param attentionUserId 被关注用户id
     * @return true: 已关注 false: 未关注
     */
    Boolean isAttention(Long memberId, Long attentionUserId);

    /**
     * 查询用户是否关注一批用户
     *
     * @param userId
     * @param relationId
     * @return
     */
    List<Long> checkUserIsAttention(Long userId, List<Long> relationId);

    /**
     * 根据昵称检索我的关注列表
     *
     * @return 我的关注列表
     */
    TableDataInfo<AttentionVo> queryMyAttentionListByName(AttentionDto dto);

    /**
     * 根据昵称检索我的粉丝列表
     *
     * @return 我的关注列表
     */
    TableDataInfo<AttentionVo> queryMyFansListByName(AttentionDto dto);

    /**
     * 根据昵称检索他人的关注列表
     *
     * @return 他人的关注列表
     */
    TableDataInfo<AttentionVo> queryOthersAttentionListByName(AttentionDto dto);

    /**
     * 根据昵称检索他人的粉丝列表
     *
     * @return 他人的粉丝列表
     */
    TableDataInfo<AttentionVo> queryOthersFansListByName(AttentionDto dto);

}
