package com.euler.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * SDK菜单视图对象 sdk_main_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@ApiModel("SDK菜单视图对象")
@ExcelIgnoreUnannotated
public class SdkMainMenuVo implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * sdk菜单ID
     */
    @ExcelProperty(value = "sdk菜单ID")
    @ApiModelProperty("sdk菜单ID")
    private Integer id;

    /**
     * 菜单名称
     */
    @ExcelProperty(value = "菜单名称")
    @ApiModelProperty("菜单名称")
    private String name;

    /**
     * 字典键值
     */
    @ExcelProperty(value = "字典键值")
    @ApiModelProperty("字典键值")
    private String dictValue;

    /**
     * 选中时的图标
     */
    @ExcelProperty(value = "选中时的图标")
    @ApiModelProperty("选中时的图标")
    private String icon;

    /**
     * 未选中时的图标
     */
    @ExcelProperty(value = "未选中时的图标")
    @ApiModelProperty("未选中时的图标")
    private String wicon;

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

    /**
     * 是否上下架（1上架 2下架）
     */
    @ExcelProperty(value = "是否上下架", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=上架,2=下架")
    @ApiModelProperty("是否上下架（1上架 2下架）")
    private String isUp;


}
