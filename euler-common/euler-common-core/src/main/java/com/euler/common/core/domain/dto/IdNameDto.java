package com.euler.common.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字典
 **/

@Data
@ApiModel("id入参")
@NoArgsConstructor
@AllArgsConstructor
public class IdNameDto<T> implements Serializable {

    @ApiModelProperty(value = "id")
    private T id;

    @ApiModelProperty(value = "name")
    private String name;
}
