package com.euler.risk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import com.euler.common.mybatis.core.page.PageQuery;


/**
 * ip账号信息分页业务对象 td_ip_member
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("ip账号信息分页业务对象")
public class TdIpMemberPageDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址", required = true)
    private String ip;

    /**
     * ip区域地址
     */
    @ApiModelProperty(value = "ip区域地址", required = true)
    private String ipAddress;
    /**
     * 设备id，最后登录设备id
     */
    @ApiModelProperty(value = "设备id，最后登录设备id", required = true)
    private String deviceId;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 用户id，不登录为0
     */
    @ApiModelProperty(value = "用户id，不登录为0", required = true)
    private Long userId;



}
