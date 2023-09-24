package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 邮箱视图对象
 *
 * @author open
 * @date 2022-02-28
 */
@Data
@ApiModel("验证码视图对象")
@ExcelIgnoreUnannotated
public class CaptchaCodeVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ExcelProperty(value = "主键自增")
    @ApiModelProperty("主键自增")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 验证码
     */
    @ExcelProperty(value = "验证码")
    @ApiModelProperty("验证码")
    private String code;

    /**
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 使用类型 1注册 2登录 3更换邮箱地址
     */
    @ExcelProperty(value = "使用类型 1注册 2登录 3更换邮箱地址")
    @ApiModelProperty(value = "使用类型 1注册 2登录 3更换邮箱地址")
    private String type;


    /**
     * 发送类型 1邮箱 2手机
     */
    @ExcelProperty(value = "发送类型 1邮箱 2手机")

    @ApiModelProperty(value = "发送类型 1邮箱 2手机")
    private String sendType;

    /**
     * 是否使用 0未使用  1已用
     */
    @ExcelProperty(value = "是否使用 0未使用  1已用")
    @ApiModelProperty(value = "是否使用 0未使用  1已用")
    private Integer isUse;


}
