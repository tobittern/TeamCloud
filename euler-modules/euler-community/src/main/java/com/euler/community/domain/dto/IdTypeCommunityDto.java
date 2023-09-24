package com.euler.community.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典
 **/

@Data
@ApiModel("id入参")
@NoArgsConstructor
@AllArgsConstructor
public class IdTypeCommunityDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "类型")
    private String type;
}
