package com.euler.statistics.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 业务对象 platform_datas
 *
 * @author euler
 * @date 2022-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class PlatformDatasDto extends PageQuery {

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id", required = true)
    private Long id;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    private String gameName;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台", required = true)
    private String platform;

    /**
     * 本地后台注册用户数
     */
    @ApiModelProperty(value = "本地后台注册用户数", required = true)
    private Long bdUsers;

    /**
     * 广告平台注册用户数
     */
    @ApiModelProperty(value = "广告平台注册用户数", required = true)
    private Long ptUsers;

    /**
     * 本地后台付费用户数
     */
    @ApiModelProperty(value = "本地后台付费用户数", required = true)
    private Long bdPayUsers;

    /**
     * 广告平台付费用户数
     */
    @ApiModelProperty(value = "广告平台付费用户数", required = true)
    private Long ptPayUsers;

    /**
     * 本地后台付费总金额
     */
    @ApiModelProperty(value = "本地后台付费总金额", required = true)
    private BigDecimal bdTotalAmount;

    /**
     * 广告平台付费总金额
     */
    @ApiModelProperty(value = "广告平台付费总金额", required = true)
    private BigDecimal ptTotalAmount;

    /**
     * 广告平台新增创角数
     */
    @ApiModelProperty("广告平台新增创角数")
    private Long ptNewRoles;

    /**
     * 本地后台新增创角数
     */
    @ApiModelProperty("本地后台新增创角数")
    private Long bdNewRoles;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    private String startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    private String endDate;


}
