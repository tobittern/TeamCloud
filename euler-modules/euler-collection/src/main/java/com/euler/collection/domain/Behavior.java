/**
 *
 * Copyright From 2018.
 *
 * Behavior.java
 *
 */
package com.euler.collection.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 表 : behavior的 model 类
 *
 * @author 	$author$
 * @date 	2021年08月06日
 */

/**
 * Created by 2021-08-06 17:32:40
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("behavior")
public class Behavior extends BaseEntity {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:id，行为类型id */
    private Integer id;

    /** 字段:biz_id，业务id */
    private Integer bizId;

    /** 字段:biz_type，内容类型 */
    private String bizType;

    /** 字段:behavior_type，行为类型 */
    private String behaviorType;

    /** 字段:behavior_value，行为结果,扩展字段 */
    private String behaviorValue;

    /** 字段:user_id，用户id，不登录为0 */
    private Long userId;

    /** 字段:longitude，位置经度 */
    private String longitude;

    /** 字段:latitude，用户纬度 */
    private String latitude;

    /** 字段:ip，ip地址 */
    private String ip;

    /** 字段:client_ip 本机获取的IP*/
    private String clientIp;

    /** 字段:app_version，app版本 */
    private String appVersion;

    /** 字段:app_id，上报来源，appid */
    private String appId;

    /** 字段:imei，设备id */
    private String imei;

    /** 字段:os，设备类型，安卓，ios，小程序，pc，未知 */
    private String os;

    /** 字段:osVersion，手机型号，iphoneX，小米11..... */
    private String osVersion;

    /** 字段:model，终端操作系统，操作系统，版本信息 */
    private String model;

    /** 字段:network，网络类型，wifi，数据网络 */
    private String network;

    /** 字段:session_id，用户一次访问的标识ID */
    private String sessionId;

    /** 字段:trace_id，行为唯一标识 */
    private String traceId;

    /** 字段:parent_trace_id，父行为标识 */
    private String parentTraceId;

    /** 字段:page_id，页面标识 */
    private String pageId;

    /** 字段:idfa */
    private String idfa;

    /** 字段:android_id */
    private String androidid;

    /** 字段:oa_id */
    private String oaid;

    /** 字段:mac */
    private String mac;

    /** 字段:ua*/
    private String ua;

    /**
     * 分包号
     */
    private String packageCode;

}
