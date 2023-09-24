package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备行为视图对象
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("设备行为视图对象")
@ExcelIgnoreUnannotated
public class TdDeviceBehaviorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备id
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

    /**
     * 手机型号，iphone15，小米18.....
     */
    @ExcelProperty(value = "手机型号，iphone15，小米18.....")
    @ApiModelProperty("手机型号，iphone15，小米18.....")
    private String mobileType;

    /**
     * 设备mac地址
     */
    @ExcelProperty(value = "设备mac地址")
    @ApiModelProperty("设备mac地址")
    private String deviceMac;

    /**
     * 设备oaid
     */
    @ExcelProperty(value = "设备oaid")
    @ApiModelProperty("设备oaid")
    private String deviceOaid;

    /**
     * 设备imei
     */
    @ExcelProperty(value = "设备imei")
    @ApiModelProperty("设备imei")
    private String deviceImei;

    /**
     * 设备安卓id
     */
    @ExcelProperty(value = "设备安卓id")
    @ApiModelProperty("设备安卓id")
    private String deviceAndroid;

    /**
     * 设备ios,uuid
     */
    @ExcelProperty(value = "设备ios,uuid")
    @ApiModelProperty("设备ios,uuid")
    private String deviceUuid;

    /**
     * 设备ios,idfa
     */
    @ExcelProperty(value = "设备ios,idfa")
    @ApiModelProperty("设备ios,idfa")
    private String deviceIdfa;

    /**
     * 设备ios,pushid
     */
    @ExcelProperty(value = "设备ios,pushid")
    @ApiModelProperty("设备ios,pushid")
    private String devicePushId;

}
