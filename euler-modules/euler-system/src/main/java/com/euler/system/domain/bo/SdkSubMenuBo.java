package com.euler.system.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * SDK子菜单业务对象 sdk_sub_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("SDK子菜单业务对象")
public class SdkSubMenuBo extends BaseEntity {

    /**
     * sdk子菜单id
     */
    @ApiModelProperty(value = "sdk子菜单id", required = true)
    @NotNull(message = "sdk子菜单id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", required = true)
    @NotBlank(message = "菜单名称不能为空", groups = { EditGroup.class })
    private String name;

    /**
     * sdk主菜单id
     */
    @ApiModelProperty(value = "sdk主菜单id", required = true)
    @NotNull(message = "sdk主菜单id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer mainMenuId;

    /**
     * 选中时的图标
     */
    @ApiModelProperty(value = "选中时的图标")
    private String icon;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort;

    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    private String code;

    /**
     * 小圆点
     */
    @ApiModelProperty(value = "小圆点")
    private String badge;

    private Boolean badgeF;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag = "0";

}
