package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ip设备行为视图对象
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("ip设备行为视图对象")
@ExcelIgnoreUnannotated
public class TdIpBehaviorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ip
     */
    @ExcelProperty(value = "ip")
    @ApiModelProperty("ip")
    private String ip;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 设备id，最后登录设备id
     */
    @ExcelProperty(value = "设备id")
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    @ApiModelProperty("账号")
    private String account;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 用户id，不登录为0
     */
    @ExcelProperty(value = "用户id，不登录为0")
    @ApiModelProperty("用户id，不登录为0")
    private Long userId;

    /**
     * 事件类型名称
     */
    @ExcelProperty(value = "事件类型名称")
    @ApiModelProperty("事件类型名称")
    private String name;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    @ApiModelProperty(value = "时间")
    private Date createTime;

}
