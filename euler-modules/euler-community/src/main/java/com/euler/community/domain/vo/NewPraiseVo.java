package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 新点赞消息视图对象 praise
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("新点赞消息视图对象")
@ExcelIgnoreUnannotated
public class NewPraiseVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 动态表主键id
     */
    @ExcelProperty(value = "动态表主键id")
    @ApiModelProperty("动态表主键id")
    private Long relationId;

    /**
     * 点赞用户id
     */
    @ExcelProperty(value = "点赞用户id")
    @ApiModelProperty("点赞用户id")
    private Long memberId;

    /**
     * 类型 1动态 2评论
     */
    @ApiModelProperty(value = "点赞类型")
    private Integer type;

    /**
     * 点赞人的昵称
     */
    @ExcelProperty(value = "点赞人的昵称")
    @ApiModelProperty("点赞人的昵称")
    private String praiseNickName;

    /**
     * 点赞人的头像
     */
    @ExcelProperty(value = "点赞人的头像")
    @ApiModelProperty("点赞人的头像")
    private String praiseUserAvatar;

    /**
     * 点赞时间
     */
    @ExcelProperty(value = "点赞时间")
    @ApiModelProperty("点赞时间")
    private Date createTime;

    /**
     * 我的昵称
     */
    @ExcelProperty(value = "我的昵称")
    @ApiModelProperty("我的昵称")
    private String nickName;

    /**
     * 我的头像
     */
    @ExcelProperty(value = "我的头像")
    @ApiModelProperty("我的头像")
    private String avatar;

    /**
     * 我的动态/评论内容
     */
    @ExcelProperty(value = "我的动态/评论内容")
    @ApiModelProperty("我的动态/评论内容")
    private String content;

    /**
     * 点赞人是否是官方账号
     */
    @ExcelProperty(value = "点赞人是否是官方账号")
    @ApiModelProperty("点赞人是否是官方账号")
    private Integer praiseUserOfficial = 0;

    /**
     * 我是否是官方账号
     */
    @ExcelProperty(value = "我是否是官方账号")
    @ApiModelProperty("我是否是官方账号")
    private Integer isOfficial = 0;

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
