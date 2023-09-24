package com.euler.common.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 字典
 **/

@Data
@ApiModel("id入参")
@NoArgsConstructor
@AllArgsConstructor
public class IdTypeDto<T,K> {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private T id;

    @ApiModelProperty(value = "类型")
    private K type;
}
