package com.euler.common.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfoDto implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 手机型号，iphone15，小米18.....
     */
    private String mobileType;
    /**
     * 设备mac地址
     */
    private String mac;
    /**
     * 设备oaid
     */
    private String oaid;
    /**
     * 设备imei
     */
    private String imei;
    /**
     * 设备安卓id
     */
    private String android;
    /**
     * 设备ios,uuid
     */
    private String uuid;
    /**
     * 设备ios,idfa
     */
    private String idfa;
    /**
     * 设备ios,pushid
     */
    private String pushId;

}
