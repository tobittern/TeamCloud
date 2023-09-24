package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 评论对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 关联ID 可能是动态ID 也可能是评论ID
     */
    private Long relationId;
    /**
     * 评论用户id
     */
    private Long memberId;
    /**
     * 被评论人的id
     */
    private Long commentedMemberId;
    /**
     * 归属动态ID
     */
    private Long ascriptionDynamicId;
    /**
     * 归属评论ID 存储最上层的评论ID
     */
    private Long ascriptionCommentId;
    /**
     * 评论类型 1 动态 2评论
     */
    private Integer type;
    /**
     * 评论内容
     */
    private String comments;
    /**
     * 评论内容格式化
     */
    private String commentsRaw;
    /**
     * 点赞量
     */
    private Integer praiseNum;
    /**
     * 举报量
     */
    private Integer reportNum;

    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
