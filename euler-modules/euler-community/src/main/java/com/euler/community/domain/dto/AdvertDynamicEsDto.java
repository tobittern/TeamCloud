package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.community.domain.entity.DynamicEs;
import com.euler.community.domain.entity.DynamicFrontEs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告转动态Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告转动态")
public class AdvertDynamicEsDto extends PageQuery {

    /** 广告所在位置 第几条 **/
    @ApiModelProperty("广告所在位置第几条")
    private Integer row;

    /** 对应的es 广告 **/
    @ApiModelProperty("es存储的动态")
    private DynamicFrontEs dynamicEs;
}
