package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 后端用户行为上报数据视图对象 behavior
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("后端用户行为上报数据视图对象")
@ExcelIgnoreUnannotated
public class BehaviorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行为类型id
     */
    @ExcelProperty(value = "行为类型id")
    @ApiModelProperty("行为类型id")
    private Long id;

    /**
     * 行为类型
     */
    @ExcelProperty(value = "行为类型")
    @ApiModelProperty("行为类型")
    private Integer behaviorTypeId;

    /**
     * 设备Id
     */
    @ExcelProperty(value = "设备Id")
    @ApiModelProperty("设备Id")
    private  String deviceId;

    /**
     * 行为数据,请求body数据
     */
    @ExcelProperty(value = "行为数据,请求body数据")
    @ApiModelProperty("行为数据,请求body数据")
    private String behaviorData;

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
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * appid，header数据
     */
    @ExcelProperty(value = "appid，header数据")
    @ApiModelProperty("appid，header数据")
    private String appId;

    /**
     * 平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    @ExcelProperty(value = "平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    @ApiModelProperty("平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    private String platform;

    /**
     * 设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序
     */
    @ExcelProperty(value = "设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    @ApiModelProperty("设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序")
    private String device;

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

    @ApiModelProperty("处理状态，0：已处理，1：待处理")
    private String opFlag = "0";
}
