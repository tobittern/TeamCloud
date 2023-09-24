package com.euler.resource.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("OSS对象存储签名")
public class OssSignBo {

    @ApiModelProperty(value = "上传路径",required = true)
    private  String filePath;
    @ApiModelProperty(value = "oss类型，upyun_open：开放平台，upyun_sdk：sdk",required = true)
    private  String ossType;
}
