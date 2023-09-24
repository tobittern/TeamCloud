package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 评论视图对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("评论视图对象")
@ExcelIgnoreUnannotated
public class CommentFrontVo {

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
     * 评论内容 格式化
     */
    @ExcelProperty(value = "评论内容")
    @ApiModelProperty("评论内容")
    private String commentsRaw;
    /**
     * 点赞量
     */
    @ExcelProperty(value = "点赞量")
    @ApiModelProperty("点赞量")
    private Integer praiseNum = 0;
    /**
     * 举报量
     */
    @ExcelProperty(value = "举报量")
    @ApiModelProperty("举报量")
    private Integer reportNum = 0;
    /**
     * 是否点赞 0 没有点赞  1点赞过
     */
    @ExcelProperty(value = "是否点赞")
    @ApiModelProperty("是否点赞")
    private Integer isPraise = 0;
    /**
     * 评论数量
     */
    @ExcelProperty(value = "评论数量")
    @ApiModelProperty("评论数量")
    private Integer nums = 0;

    /**
     * 当前评论的第一个人的用户ID
     */
    @ExcelProperty(value = "当前评论的第一个人的用户ID")
    @ApiModelProperty("当前评论的第一个人的用户ID")
    private Long commentMemberId;

    /**
     * 当前评论的第一个人的用户昵称
     */
    @ExcelProperty(value = "当前评论的第一个人的用户昵称")
    @ApiModelProperty("当前评论的第一个人的用户昵称")
    private String commentMemberNickName;

    /**
     * 当前评论的第一个人的用户头像
     */
    @ExcelProperty(value = "当前评论的第一个人的用户头像")
    @ApiModelProperty("当前评论的第一个人的用户头像")
    private String commentMemberAvatar;

    /**
     * 是否是官方账号
     */
    @ExcelProperty(value = "是否是官方账号")
    @ApiModelProperty("是否是官方账号")
    private Integer commentMemberIsOfficial = 0;

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

    /**
     * 评论时间
     */
    @ExcelProperty(value = "评论时间")
    @ApiModelProperty("评论时间")
    private Date createTime;

    /**
     * 是否是神评
     */
    @ExcelProperty(value = "是否是神评")
    @ApiModelProperty("是否是神评")
    private Integer isDivine = 0;

}
