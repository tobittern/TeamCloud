package com.euler.community.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("导入礼包码")
@Data
public class CodeDto {
    @ExcelProperty("游戏激活码")
    @ApiModelProperty("礼包码")
    private String code;
}
