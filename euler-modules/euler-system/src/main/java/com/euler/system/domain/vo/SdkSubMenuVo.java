package com.euler.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * SDK子菜单视图对象 sdk_sub_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@ApiModel("SDK子菜单视图对象")
@ExcelIgnoreUnannotated
public class SdkSubMenuVo implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * sdk子菜单id
     */
    @ExcelProperty(value = "sdk子菜单id")
    @ApiModelProperty("sdk子菜单id")
    private Integer id;

    /**
     * 菜单名称
     */
    @ExcelProperty(value = "菜单名称")
    @ApiModelProperty("菜单名称")
    private String name;

    /**
     * sdk主菜单id
     */
    @ExcelProperty(value = "sdk主菜单id")
    @ApiModelProperty("sdk主菜单id")
    private Integer mainMenuId;

    /**
     * 选中时的图标
     */
    @ExcelProperty(value = "选中时的图标")
    @ApiModelProperty("选中时的图标")
    private String icon;

    /**
     * 路径
     */
    @ExcelProperty(value = "路径")
    @ApiModelProperty("路径")
    private String path;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 代码
     */
    @ExcelProperty(value = "代码")
    @ApiModelProperty("代码")
    private String code;

    /**
     * 小圆点
     */
    @ExcelProperty(value = "小圆点")
    @ApiModelProperty("小圆点")
    private String badge;

    private Boolean badgeF;

}
