package com.euler.sdk.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("修改省市区dto")
@Data
public class UpdateRegionDto {
    /**
     * 省份id
     */
    @ApiModelProperty("省份id")
    private Integer provinceId;

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String province;

    /**
     * 城市id
     */
    @ApiModelProperty("城市id")
    private Integer cityId;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String city;

    /**
     * 区县id
     */
    @ApiModelProperty("区县id")
    private Integer areaId;

    /**
     * 区县名称
     */
    @ApiModelProperty("区县名称")
    private String area;
}
