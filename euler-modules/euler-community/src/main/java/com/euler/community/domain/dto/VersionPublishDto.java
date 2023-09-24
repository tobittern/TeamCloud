package com.euler.community.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 版本业务对象 version
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("版本业务对象")
public class VersionPublishDto {

    /**
     * 应用系统，'1': android '2': ios
     */
    @ApiModelProperty(value = "应用系统，'1': android '2': ios")
    private String systemType;

}
