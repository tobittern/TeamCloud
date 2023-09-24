package com.euler.collection.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 behavior
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class BehaviorVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 业务id
     */
    @ExcelProperty(value = "业务id")
    @ApiModelProperty("业务id")
    private Integer bizId;


    /**
     * 内容类型
     */
    @ExcelProperty(value = "内容类型")
    @ApiModelProperty("内容类型")
    private String bizType;

    /**
     * 行为类型 （1设备 2网络 3行为）
     */
    @ExcelProperty(value = "行为类型 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=设备,2=网络,3=行为")
    @ApiModelProperty("行为类型 （1设备 2网络 3行为）")
    private String behaviorType;

    /**
     * 行为结果,扩展字段
     */
    @ExcelProperty(value = "行为结果,扩展字段")
    @ApiModelProperty("行为结果,扩展字段")
    private String behaviorValue;

    /**
     * 用户id，不登录为0
     */
    @ExcelProperty(value = "用户id，不登录为0")
    @ApiModelProperty("用户id，不登录为0")
    private Long userId;

    /**
     * 位置经度
     */
    @ExcelProperty(value = "位置经度")
    @ApiModelProperty("位置经度")
    private String longitude;

    /**
     * 用户纬度
     */
    @ExcelProperty(value = "用户纬度")
    @ApiModelProperty("用户纬度")
    private String latitude;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * 本机获取的IP
     */
    @ExcelProperty(value = "本机获取的IP")
    @ApiModelProperty("本机获取的IP")
    private String clientIp;

    /**
     * app版本
     */
    @ExcelProperty(value = "app版本")
    @ApiModelProperty("app版本")
    private String appVersion;

    /**
     * 上报来源，appid
     */
    @ExcelProperty(value = "上报来源，appid")
    @ApiModelProperty("上报来源，appid")
    private String appId;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 设备类型，安卓，ios，小程序，pc，未知
     */
    @ExcelProperty(value = "设备类型，安卓，ios，小程序，pc，未知")
    @ApiModelProperty("设备类型，安卓，ios，小程序，pc，未知")
    private String deviceType;

    /**
     * 网络类型，wifi，数据网络 4G  5G
     */
    @ExcelProperty(value = "网络类型，wifi，数据网络 4G  5G")
    @ApiModelProperty("网络类型，wifi，数据网络 4G  5G")
    private String network;

    /**
     * 手机型号，iphoneX，小米11.....
     */
    @ExcelProperty(value = "手机型号，iphoneX，小米11.....")
    @ApiModelProperty("手机型号，iphoneX，小米11.....")
    private String mobileType;

    /**
     * 终端操作系统，操作系统，版本信息
     */
    @ExcelProperty(value = "终端操作系统，操作系统，版本信息")
    @ApiModelProperty("终端操作系统，操作系统，版本信息")
    private String os;

    /**
     * 用户一次访问的标识ID
     */
    @ExcelProperty(value = "用户一次访问的标识ID")
    @ApiModelProperty("用户一次访问的标识ID")
    private String sessionId;

    /**
     * 行为唯一标识
     */
    @ExcelProperty(value = "行为唯一标识")
    @ApiModelProperty(value = "行为唯一标识", required = true)
    private String traceId;

    /**
     * 父行为标识
     */
    @ExcelProperty(value = "父行为标识")
    @ApiModelProperty(value = "父行为标识", required = true)
    private String parentTraceId;

    /**
     * 页面标识
     */
    @ExcelProperty(value = "页面标识")
    @ApiModelProperty(value = "页面标识", required = true)
    private String pageId;


}
