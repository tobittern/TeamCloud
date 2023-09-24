package com.euler.collection.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 业务对象 behavior
 *
 * @author euler
 * @date 2022-03-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class BehaviorBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;

    /**
     * 内容类型
     */
    @ApiModelProperty(value = "内容类型")
    private String bizType;

    /**
     * 行为类型 （1设备 2网络 3行为）
     */
    @ApiModelProperty(value = "行为类型 （1设备 2网络 3行为）")
    @NotBlank(message = "行为类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer behaviorType;

    /**
     * 行为结果,扩展字段
     */
    @ApiModelProperty(value = "行为结果,扩展字段")
    private String behaviorValue;

    /**
     * 用户id，不登录为0
     */
    @ApiModelProperty(value = "用户id，不登录为0")
    private Long userId;

    /**
     * 位置经度
     */
    @ApiModelProperty(value = "位置经度")
    private String longitude;

    /**
     * 用户纬度
     */
    @ApiModelProperty(value = "用户纬度")
    private String latitude;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    @NotBlank(message = "ip地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String ip;

    /**
     * 本机获取的IP
     */
    @ApiModelProperty(value = "本机获取的IP")
    private String clientIp;

    /**
     * app版本
     */
    @ApiModelProperty(value = "app版本")
    private String appVersion;

    /**
     * 上报来源，appid
     */
    @ApiModelProperty(value = "上报来源，appid")
    @NotBlank(message = "appid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appId;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String imei;

    /**
     * 设备类型，安卓，ios，小程序，pc，未知
     */
    @ApiModelProperty(value = "设备类型，安卓，ios，小程序，pc，未知")
    private String os;

    /**
     * 网络类型，wifi，数据网络 4G  5G
     */
    @ApiModelProperty(value = "网络类型，wifi，数据网络 4G  5G")
    private String network;

    /**
     * 手机型号，iphoneX，小米11.....
     */
    @ApiModelProperty(value = "手机型号，iphoneX，小米11.....")
    private String model;

    /**
     * 终端操作系统，操作系统，版本信息
     */
    @ApiModelProperty(value = "终端操作系统，操作系统，版本信息")
    private String osVersion;

    /**
     * 用户一次访问的标识ID
     */
    @ApiModelProperty(value = "用户一次访问的标识ID")
    @NotBlank(message = "用户一次访问的标识ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String sessionId;

    /**
     * 行为唯一标识
     */
    @ApiModelProperty(value = "行为唯一标识")
    private String traceId;

    /**
     * 父行为标识
     */
    @ApiModelProperty(value = "父行为标识")
    private String parentTraceId;

    /**
     * 页面标识
     */
    @ApiModelProperty(value = "页面标识")
    private String pageId;

    /**
     * idfa
     */
    @ApiModelProperty(value = "idfa")
    private String idfa;

    /**
     * androidid
     */
    @ApiModelProperty(value = "androidid")
    private String androidid;

    /**
     * oaid
     */
    @ApiModelProperty(value = "oaid")
    private String oaid;

    /**
     * mac
     */
    @ApiModelProperty(value = "mac")
    private String mac;

    /**
     * ua
     */
    @ApiModelProperty(value = "ua")
    private String ua;

}
