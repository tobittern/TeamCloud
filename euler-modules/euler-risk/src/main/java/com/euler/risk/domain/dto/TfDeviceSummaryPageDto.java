package com.euler.risk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import com.euler.common.mybatis.core.page.PageQuery;

import java.util.Date;


/**
 * 设备数据汇总分页业务对象 tf_device_summary
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("设备数据汇总分页业务对象")
public class TfDeviceSummaryPageDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 汇总日期
     */
    @ApiModelProperty(value = "汇总日期", required = true)
    private Date dateId;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", required = true)
    private String deviceId;

    /**
     * 注册次数
     */
    @ApiModelProperty(value = "注册次数", required = true)
    private Integer registerNum;

    /**
     * 登录次数
     */
    @ApiModelProperty(value = "登录次数", required = true)
    private Integer loginNum;

    /**
     * 角色创建次数
     */
    @ApiModelProperty(value = "角色创建次数", required = true)
    private Integer roleCreateNum;



}
