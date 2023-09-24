package com.euler.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;

/**
 * SDK子菜单分页业务对象 sdk_sub_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("SDK子菜单分页业务对象")
public class SdkSubMenuDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * sdk子菜单id
     */
    @ApiModelProperty(value = "sdk子菜单id")
    private Integer id;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String name;

    /**
     * sdk主菜单id
     */
    @ApiModelProperty(value = "sdk主菜单id")
    private Integer mainMenuId;

}
