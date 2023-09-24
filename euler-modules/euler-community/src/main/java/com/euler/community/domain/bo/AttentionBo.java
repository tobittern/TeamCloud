package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
/**
 * 关注业务对象 attention
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("关注业务对象")
public class AttentionBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 被关注用户id
     */
    @ApiModelProperty(value = "被关注用户id", required = true)
    @NotNull(message = "被关注用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long attentionUserId;

    /**
     * 状态 1:未关注 2:已关注 3:互相关注
     */
    @ApiModelProperty(value = "状态 1:未关注 2:已关注 3:互相关注")
    private String status;

    /**
     * 是否是官方账号  0不是  1是
     */
    @ApiModelProperty("是否是官方账号")
    private Integer isOfficial = 0;

}
