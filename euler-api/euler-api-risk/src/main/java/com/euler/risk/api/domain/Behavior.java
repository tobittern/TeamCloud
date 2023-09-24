package com.euler.risk.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 后端用户行为上报数据对象 behavior
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@TableName("behavior")
public class Behavior implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行为类型id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息Id，唯一Id
     */
    private String msgId;

    /**
     * 设备Id
     */
    private String deviceId;

    /**
     * 行为类型
     */
    private Integer behaviorTypeId;
    /**
     * 行为数据,请求body数据
     */
    private String behaviorData;
    /**
     * 账号
     */
    private String account;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户id，不登录为0
     */
    private Long userId;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * ip地址
     */
    private String ip;
    /**
     * appid，header数据
     */
    private String appId;
    /**
     * 应用版本，header数据
     */
    @Version
    private String version;
    /**
     * 平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    private String platform;
    /**
     * 设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序
     */
    private String device;
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

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 处理状态，0：已处理，1：待处理
     */
    private String opFlag = "0";
}
