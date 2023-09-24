package com.euler.common.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("key-value入参")
public class KeyValueDto<T> implements Serializable {
    @ApiModelProperty(value = "key")
    private T key;
    @ApiModelProperty(value = "value")
    private String value;

}
