package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备信息对象 td_device_info
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("td_device_info")
public class TdDeviceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 手机型号，iphone15，小米18.....
     */
    private String mobileType;
    /**
     * 设备mac地址
     */
    private String deviceMac;
    /**
     * 设备oaid
     */
    private String deviceOaid;
    /**
     * 设备imei
     */
    private String deviceImei;
    /**
     * 设备安卓id
     */
    private String deviceAndroid;
    /**
     * 设备ios,uuid
     */
    private String deviceUuid;
    /**
     * 设备ios,idfa
     */
    private String deviceIdfa;
    /**
     * 设备ios,pushid
     */
    private String devicePushId;

}
