package com.euler.statistics.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * 视图对象 online_user
 *
 * @author euler
 * @date 2022-09-14
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class OnlineUserVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 登录IP地址
     */
    @ExcelProperty(value = "登录IP地址")
    @ApiModelProperty("登录IP地址")
    private String ipaddr;

    /**
     * 登录时间
     */
    @ExcelProperty(value = "登录时间")
    @ApiModelProperty("登录时间")
    private Date loginTime;

    /**
     * 渠道号
     */
    @ExcelProperty(value = "渠道号")
    @ApiModelProperty("渠道号")
    private String packageCode;

    /**
     * 游戏Id
     */
    @ExcelProperty(value = "游戏Id")
    @ApiModelProperty("游戏Id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 渠道id
     */
    @ExcelProperty(value = "渠道id")
    @ApiModelProperty("渠道id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ExcelProperty(value = "渠道名称")
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 用户类型，1：sdk，2：开放平台，3：管理后台
     */
    @ExcelProperty(value = "用户类型，1：sdk，2：开放平台，3：管理后台")
    @ApiModelProperty("用户类型，1：sdk，2：开放平台，3：管理后台")
    private String userType;

    /**
     * 登录平台，1：sdk，2：开放平台，3：管理后台 4：APP
     */
    @ExcelProperty(value = "登录平台，1：sdk，2：开放平台，3：管理后台 4：APP")
    @ApiModelProperty("登录平台，1：sdk，2：开放平台，3：管理后台 4：APP")
    private Integer platform;

    /**
     * 设备，1：安卓，2：ios，3：h5，4：小程序
     */
    @ExcelProperty(value = "设备，1：安卓，2：ios，3：h5，4：小程序")
    @ApiModelProperty("设备，1：安卓，2：ios，3：h5，4：小程序")
    private Integer device;


}
