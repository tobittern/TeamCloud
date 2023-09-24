package com.euler.common.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 字典
 **/

@Data
@ApiModel("id入参")
public class IdNameTypeDicDto<T> {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private T id;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "类型")
    private Integer type;
}
