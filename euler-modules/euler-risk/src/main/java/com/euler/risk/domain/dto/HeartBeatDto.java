package com.euler.risk.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 心跳业务对象
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@ApiModel("心跳业务对象")
public class HeartBeatDto {
    /**
     * 是否第一次请求
     */
    @ExcelProperty(value = "是否第一次请求")
    @ApiModelProperty("是否第一次请求")
    private Integer firstRequest = 0;
}
