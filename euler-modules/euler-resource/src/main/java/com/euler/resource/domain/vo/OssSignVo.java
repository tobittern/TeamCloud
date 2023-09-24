package com.euler.resource.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("OSS对象存储签名出参")
public class OssSignVo {

    @ApiModelProperty(value = "签名")
    private  String sign;

    @ApiModelProperty(value = "策略")
    private  String policy;

    @ApiModelProperty(value = "桶名")
    private  String bucket;

    @ApiModelProperty(value = "域名")
    private  String domain;
}
