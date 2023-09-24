package com.euler.community.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("微信用户对象")
public class WxUserDto {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间", required = true)
    private Date date;

}
