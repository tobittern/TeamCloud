package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * ip账号信息视图对象 td_ip_member
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("ip账号信息视图对象")
@ExcelIgnoreUnannotated
public class TdIpMemberVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * ip区域地址
     */
    @ExcelProperty(value = "ip区域地址")
    @ApiModelProperty("ip区域地址")
    private String ipAddress;

    /**
     * 设备id，最后登录设备id
     */
    @ExcelProperty(value = "设备id，最后登录设备id")
    @ApiModelProperty("设备id，最后登录设备id")
    private String deviceId;

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


}
