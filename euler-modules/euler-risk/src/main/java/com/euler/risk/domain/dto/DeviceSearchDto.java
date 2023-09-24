package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("设备信息检索对象")
public class DeviceSearchDto extends PageQuery {

    /**
     * 设备类型(1:mac 2:oaid 3:imei 4:android 5:uuid 6:idfa 7:pushId)
     */
    @ApiModelProperty("设备类型(1:mac 2:oaid 3:imei 4:android 5:uuid 6:idfa 7:pushId)")
    private String deviceType = "1";

    /**
     * 目标
     */
    @ApiModelProperty("目标")
    private String target;

    /**
     * 选择开始日期
     */
    @ApiModelProperty(value = "选择开始日期")
    private Date startTime;

    /**
     * 选择结束日期
     */
    @ApiModelProperty(value = "选择结束日期")
    private Date endTime;

    /**
     * 注册开始次数
     */
    @ApiModelProperty("注册开始次数")
    private Integer registerStartNum;

    /**
     * 注册结束次数
     */
    @ApiModelProperty("注册结束次数")
    private Integer registerEndNum;

    /**
     * 登录开始次数
     */
    @ApiModelProperty("登录开始次数")
    private Integer loginStartNum;

    /**
     * 登录结束次数
     */
    @ApiModelProperty("登录结束次数")
    private Integer loginEndNum;

    /**
     * 角色创建开始次数
     */
    @ApiModelProperty("角色创建开始次数")
    private Integer roleCreateStartNum;

    /**
     * 角色创建结束次数
     */
    @ApiModelProperty("角色创建结束次数")
    private Integer roleCreateEndNum;

}
