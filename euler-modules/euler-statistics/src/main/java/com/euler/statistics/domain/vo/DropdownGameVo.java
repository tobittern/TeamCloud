package com.euler.statistics.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DropdownGameVo {
    @ApiModelProperty(value = "游戏id")
    private Integer id;

    @ApiModelProperty(value = "游戏名称")
    private String name;

    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    private Integer type;

    @ApiModelProperty(value = "游戏图标")
    private String icon;
}
