package com.euler.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("参数配置入参实体")
@Data
public class SysConfigDto implements Serializable {

    @ApiModelProperty(value = "设备，-1：不限设备，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    private Integer device;

}
