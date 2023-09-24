package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 设备数据汇总视图对象 tf_device_summary
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("设备数据汇总视图对象")
@ExcelIgnoreUnannotated
public class TfDeviceSummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 汇总日期
     */
    @ExcelProperty(value = "汇总日期")
    @ApiModelProperty("汇总日期")
    private Date dateId;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 注册次数
     */
    @ExcelProperty(value = "注册次数")
    @ApiModelProperty("注册次数")
    private Integer registerNum;

    /**
     * 登录次数
     */
    @ExcelProperty(value = "登录次数")
    @ApiModelProperty("登录次数")
    private Integer loginNum;

    /**
     * 角色创建次数
     */
    @ExcelProperty(value = "角色创建次数")
    @ApiModelProperty("角色创建次数")
    private Integer roleCreateNum;

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

    /**
     * 设备信息
     */
    @ExcelProperty(value = "设备信息")
    @ApiModelProperty("设备信息")
    private String target;

    /**
     * 设备类型名称
     */
    @ExcelProperty(value = "设备类型名称")
    @ApiModelProperty("设备类型名称")
    private String deviceTypeName;

}
