package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 评论业务对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评论业务对象")
public class CommentBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 关联ID 可能是动态ID 也可能是评论ID
     */
    @ApiModelProperty(value = "关联ID 可能是动态ID 也可能是评论ID", required = true)
    @NotNull(message = "评论ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long relationId;

    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id")
    private Long memberId;

    /**
     * 被评论人的id
     */
    @ApiModelProperty(value = "被评论人的id")
    private Long commentedMemberId;
    /**
     * 归属动态ID
     */
    @ApiModelProperty(value = "归属动态ID")
    private Long ascriptionDynamicId;
    /**
     * 归属评论ID 存储最上层的评论ID
     */
    @ApiModelProperty(value = "归属评论ID 存储最上层的评论ID")
    private Long ascriptionCommentId;
    /**
     * 评论类型 1 动态 2评论
     */
    @ApiModelProperty(value = "评论类型 1 动态 2评论", required = true)
    private Integer type = 1;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String comments;

}
