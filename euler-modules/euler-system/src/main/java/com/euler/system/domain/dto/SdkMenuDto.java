package com.euler.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("sdk菜单对象")
public class SdkMenuDto {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "代码")
    private String code;
    @ApiModelProperty(value = "选中时图标")
    private String icon;
    @ApiModelProperty(value = "未选中时图标")
    private String wicon;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "小圆点")
    private String badge;
    @ApiModelProperty(value = "子菜单")
    private List<SdkMenuDto> children;
}
