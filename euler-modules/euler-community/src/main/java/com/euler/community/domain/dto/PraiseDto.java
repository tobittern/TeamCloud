package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 点赞业务对象 praise
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("点赞业务对象")
public class PraiseDto extends PageQuery {

    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    private Long relationId;

    /**
     * 点赞用户id
     */
    @ApiModelProperty(value = "点赞用户id", required = true)
    private Long memberId;

    /**
     * 类型 1动态 2评论
     */
    @ApiModelProperty(value = "点赞类型", required = true)
    private Integer type;


}
