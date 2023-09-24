package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关注业务对象 attention
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("关注业务对象")
public class AttentionDto extends PageQuery {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 被关注用户id
     */
    @ApiModelProperty(value = "被关注用户id")
    private Long attentionUserId;

    /**
     * 状态 1:未关注 2:已关注 3:互相关注
     */
    @ApiModelProperty(value = "状态 1:未关注 2:已关注 3:互相关注")
    private String status;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

}
