package com.euler.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;

/**
 * SDK菜单分页业务对象 sdk_main_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("SDK菜单分页业务对象")
public class SdkMainMenuDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * sdk菜单ID
     */
    @ApiModelProperty(value = "sdk菜单ID")
    private Integer id;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    /**
     * 是否上下架（1上架 2下架）
     */
    @ApiModelProperty(value = "是否上下架（1上架 2下架）")
    private String isUp;

}

