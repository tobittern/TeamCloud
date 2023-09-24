package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 设备信息分页业务对象 td_device_info
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("设备信息分页业务对象")
public class TdDeviceInfoPageDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private String id;

    /**
     * 手机型号，iphone15，小米18.....
     */
    @ApiModelProperty(value = "手机型号，iphone15，小米18.....", required = true)
    private String mobileType;

    /**
     * 设备mac地址
     */
    @ApiModelProperty(value = "设备mac地址", required = true)
    private String deviceMac;

    /**
     * 设备oaid
     */
    @ApiModelProperty(value = "设备oaid", required = true)
    private String deviceOaid;

    /**
     * 设备imei
     */
    @ApiModelProperty(value = "设备imei", required = true)
    private String deviceImei;

    /**
     * 设备安卓id
     */
    @ApiModelProperty(value = "设备安卓id", required = true)
    private String deviceAndroid;

    /**
     * 设备ios,uuid
     */
    @ApiModelProperty(value = "设备ios,uuid", required = true)
    private String deviceUuid;

    /**
     * 设备ios,idfa
     */
    @ApiModelProperty(value = "设备ios,idfa", required = true)
    private String deviceIdfa;

    /**
     * 设备ios,pushid
     */
    @ApiModelProperty(value = "设备ios,pushid", required = true)
    private String devicePushId;




}
