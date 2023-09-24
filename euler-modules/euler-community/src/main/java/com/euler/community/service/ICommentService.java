package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.CommentBo;
import com.euler.community.domain.dto.CommentDto;
import com.euler.community.domain.entity.Comment;
import com.euler.community.domain.vo.CommentFrontVo;
import com.euler.community.domain.vo.CommentVo;
import com.euler.community.domain.vo.NewCommentFrontVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 评论Service接口
 *
 * @author euler
 * @date 2022-06-07
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 获取指定动态的神评
     *
     * @param id 评论主键
     * @return 评论
     */
    CommentVo getCommentDivine(Long id);

    /**
     * 获取一批动态的神评
     *
     * @param dynamicIds
     * @return
     */
    List<CommentVo> selectCommentByMemberIds(List<Long> dynamicIds);

    /**
     * 查询评论列表
     *
     * @return 评论集合
     */
    TableDataInfo<CommentFrontVo> queryPageList(CommentDto dto);

    /**
     * 修改评论
     *
     * @return 结果
     */
    R insertByBo(CommentBo bo);

    /**
     * 校验并批量删除评论信息
     *
     * @param ids     需要删除的评论主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 用户注销之后清空用户的动态数据
     *
     * @param userId
     * @return
     */
    R cancellationClearComment(Long userId);

    /**
     * 查询新评论消息列表
     *
     * @return 评论集合
     */
    TableDataInfo<NewCommentFrontVo> queryNewPageList(CommentDto dto);

    /**
     * 获取用户某天的评论数量
     *
     * @param userId
     * @param date
     * @param type 类型 1动态 2评论
     * @return
     */
    Integer commentCountForDay(Long userId, Date date, String type);

}
