package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 评论视图对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("评论视图对象")
@ExcelIgnoreUnannotated
public class CommentVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 关联ID 可能是动态ID 也可能是评论ID
     */
    @ExcelProperty(value = "关联ID 可能是动态ID 也可能是评论ID")
    @ApiModelProperty("关联ID 可能是动态ID 也可能是评论ID")
    private Long relationId;

    /**
     * 评论用户id
     */
    @ExcelProperty(value = "评论用户id")
    @ApiModelProperty("评论用户id")
    private Long memberId;

    /**
     * 被评论人的id
     */
    @ExcelProperty(value = "被评论人的id")
    @ApiModelProperty("被评论人的id")
    private Long commentedMemberId;

    /**
     * 评论类型 1 动态 2评论
     */
    @ExcelProperty(value = "评论类型 1 动态 2评论")
    @ApiModelProperty("评论类型 1 动态 2评论")
    private Integer type;

    /**
     * 评论内容
     */
    @ExcelProperty(value = "评论内容")
    @ApiModelProperty("评论内容")
    private String comments;

    /**
     * 评论内容
     */
    @ExcelProperty(value = "评论内容")
    @ApiModelProperty("评论内容")
    private String commentsRaw;

    /**
     * 点赞量
     */
    @ExcelProperty(value = "点赞量")
    @ApiModelProperty("点赞量")
    private Integer praiseNum;

    /**
     * 举报量
     */
    @ExcelProperty(value = "举报量")
    @ApiModelProperty("举报量")
    private Integer reportNum;

    /**
     * 评论的动态ID
     */
    @ExcelProperty(value = "评论的动态ID")
    @ApiModelProperty("评论的动态ID")
    private Long ascriptionDynamicId;

    /**
     * 评论所属用户的基本信息
     */
    @ExcelProperty(value = "评论所属用户的基本信息")
    @ApiModelProperty("评论所属用户的基本信息")
    private MemberVo users;


}
