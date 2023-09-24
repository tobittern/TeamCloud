package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态收藏业务对象 collect
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("动态收藏业务对象")
public class CollectDto extends PageQuery {


    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    private Long dynamicId;

    /**
     * 收藏用户id
     */
    @ApiModelProperty(value = "收藏用户id", required = true)
    private Long memberId;

}
