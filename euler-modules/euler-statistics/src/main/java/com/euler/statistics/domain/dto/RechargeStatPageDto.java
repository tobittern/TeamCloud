package com.euler.statistics.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@ApiModel("充值数据统计搜索对象")
@EqualsAndHashCode(callSuper = true)
public class RechargeStatPageDto extends PageQuery {
    private static final long serialVersionUID=1L;
    /**
     * 渠道号
     */
    @ApiModelProperty("渠道号")
    private String channelPackageCode;
    /**
     * 游戏平台：1、安卓，2、ios，3、h5
     */
    @ApiModelProperty("游戏平台：1、安卓，2、ios，3、h5")
    private String operationPlatform;
    /**
     * 游戏id
     */
    @ApiModelProperty("游戏Id")
    private Integer gameId;


    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private String roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;


    /**
     * 服务器id
     */
    @ApiModelProperty("服务器id")
    private String serverId;

    /**
     * 服务器名称
     */
    @ApiModelProperty("服务器名称")
    private String serverName;


    /**
     * 渠道id
     */
    @ApiModelProperty("渠道id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 角色注册开始时间，yyyy-MM-dd
     */
    @ApiModelProperty("角色注册开始时间，yyyy-MM-dd")
    private String beginUserDateId;


    /**
     * 角色注册结束时间，yyyy-MM-dd
     */
    @ApiModelProperty("角色注册结束时间，yyyy-MM-dd")
    private String endUserDateId;


    /**
     * 充值开始时间
     */
    @ApiModelProperty("充值开始时间，yyyy-MM-dd")
    private String beginDateId;

    /**
     * 充值结束时间
     */
    @ApiModelProperty("充值结束时间，yyyy-MM-dd")
    private String endDateId;

    /**
     * 数据类型，1：充值数据，2：角色充值数据
     */
    @ApiModelProperty("数据类型，1：充值数据，2：角色充值数据")
    private  String dataType;


    /**
     * 支付渠道
     */
    @ApiModelProperty("支付渠道")
    private  String payChannel;


}
