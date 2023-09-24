package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 评论业务对象 comment
 *
 * @author euler
 * @date 2022-06-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评论业务对象")
public class CommentDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    /**
     * 关联ID 可能是动态ID 也可能是评论ID
     */
    @ApiModelProperty(value = "关联ID 可能是动态ID 也可能是评论ID", required = true)
    private Long relationId;

    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id", required = true)
    private Long memberId;

    /**
     * 被评论人的id
     */
    @ApiModelProperty(value = "被评论人的id", required = true)
    private Long commentedMemberId;

    /**
     * 评论类型 1 动态 2评论
     */
    @ApiModelProperty(value = "评论类型 1 动态 2评论", required = true)
    private Integer type;


}
