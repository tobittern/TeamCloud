package com.euler.platform.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * captcha_code
 *
 * @author open
 * @date 2022-02-28
 */
@Data
@TableName("captcha_code")
public class CaptchaCode extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键自增
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "ID")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 接收方
     */
    @ApiModelProperty(value = "接收方")
    private String receiver;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 使用类型 1注册 2登录 3更换邮箱地址
     */
    @ApiModelProperty(value = "使用类型 1注册 2登录 3更换邮箱地址")
    private String type;


    /**
     * 发送类型 1邮箱 2手机
     */
    @ApiModelProperty(value = "发送类型 1邮箱 2手机")
    private String sendType="1";


    /**
     * 是否使用 0未使用  1已用
     */
    @ApiModelProperty(value = "是否使用 0未使用  1已用")
    private Integer isUse;

    /**
     * 删除flag
     */
    @TableLogic
    private String delFlag;

}
