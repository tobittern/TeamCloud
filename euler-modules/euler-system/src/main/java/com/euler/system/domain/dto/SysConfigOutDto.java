package com.euler.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel("配置出参实体")
@Data
public class SysConfigOutDto implements Serializable {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String configName;
    /**
     * 键名
     */
    @ApiModelProperty("键名")
    private String configKey;
    /**
     * 键值
     */
    @ApiModelProperty("键值")
    private String configValue;

    @ApiModelProperty("备注")
    private String remark;

}
