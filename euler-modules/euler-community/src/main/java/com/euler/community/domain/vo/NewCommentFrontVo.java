package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 新评论消息的视图对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("新评论消息的视图对象")
@ExcelIgnoreUnannotated
public class NewCommentFrontVo {

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
     * 评论的动态ID
     */
    @ExcelProperty(value = "评论的动态ID")
    @ApiModelProperty("评论的动态ID")
    private Long ascriptionDynamicId;

    /**
     * 评论人的基本信息
     */
    @ExcelProperty(value = "评论人的基本信息")
    @ApiModelProperty("评论人的基本信息")
    private MemberVo users;

    /**
     * 评论时间
     */
    @ExcelProperty(value = "评论时间")
    @ApiModelProperty("评论时间")
    private Date createTime;

    /**
     * 我的基本信息
     */
    @ExcelProperty(value = "我的基本信息")
    @ApiModelProperty("我的基本信息")
    private MemberVo myUser;

    /**
     * 我的关联的动态/评论内容
     */
    @ExcelProperty(value = "我的关联的动态/评论内容")
    @ApiModelProperty("我的关联的动态/评论内容")
    private String relationContent;

    /**
     * 动态id
     */
    @ExcelProperty(value = "动态id")
    @ApiModelProperty("动态id")
    private Long dynamicId;

    /**
     * 动态内容
     */
    @ExcelProperty(value = "动态内容")
    @ApiModelProperty("动态内容")
    private String dynamicContent;

    /**
     * 动态的昵称
     */
    @ExcelProperty(value = "动态的昵称")
    @ApiModelProperty("动态的昵称")
    private String dynamicNickName;
}
