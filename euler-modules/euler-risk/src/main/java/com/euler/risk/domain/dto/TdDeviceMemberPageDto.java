package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 设备账号信息分页业务对象 td_device_member
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("设备账号信息分页业务对象")
public class TdDeviceMemberPageDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", required = true)
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
