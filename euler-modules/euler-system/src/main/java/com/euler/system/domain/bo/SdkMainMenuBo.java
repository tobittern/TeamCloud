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
 * SDK菜单业务对象 sdk_main_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("SDK菜单业务对象")
public class SdkMainMenuBo extends BaseEntity {

    /**
     * sdk菜单ID
     */
    @ApiModelProperty(value = "sdk菜单ID", required = true)
    @NotNull(message = "sdk菜单ID不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称", required = true)
    @NotBlank(message = "菜单名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 字典键值
     */
    @ApiModelProperty(value = "字典键值", required = true)
    @NotBlank(message = "字典键值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dictValue;

    /**
     * 选中时的图标
     */
    @ApiModelProperty(value = "选中时的图标")
    private String icon;

    /**
     * 未选中时的图标
     */
    @ApiModelProperty(value = "未选中时的图标")
    private String wicon;

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
     * 是否上下架（1上架 2下架）
     */
    @ApiModelProperty(value = "是否上下架（1上架 2下架）")
    private String isUp = "2";

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag = "0";

}
