package com.euler.risk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * 设备信息视图对象 td_device_info
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("设备信息视图对象")
@ExcelIgnoreUnannotated
public class TdDeviceInfoVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private String id;

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

    private  Boolean isChange=false;


}
