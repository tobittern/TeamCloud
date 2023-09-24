package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 动态收藏业务对象 collect
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("首页动态展示")
public class IdPageDto extends PageQuery {


    /**
     * 动态ID
     */
    @ApiModelProperty(value = "动态ID")
    private Long dynamicId;


}
