package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 点赞业务对象 praise
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("点赞业务对象")
public class PraiseBo extends BaseEntity {

    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    @NotNull(message = "主键不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long relationId;

    /**
     * 点赞用户id
     */
    @ApiModelProperty(value = "点赞用户id", required = true)
    private Long memberId;

    /**
     * 点赞类型 1点赞 2取消点赞
     */
    @ApiModelProperty(value = "点赞类型", required = true)
    private Integer clickType = 1;

    /**
     * 类型 1动态 2评论
     */
    @ApiModelProperty(value = "类型", required = true)
    private Integer type = 1;

    /**
     * 点赞所属用户的基本信息
     */
    @ApiModelProperty("点赞所属用户的基本信息")
    private Long praiseUserId;

}
