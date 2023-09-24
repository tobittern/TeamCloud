package com.euler.statistics.domain.dto;


import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 业务对象 douyin_channel_aid_datas
 *
 * @author euler
 * @date 2022-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class DouyinChannelAidDatasDto extends PageQuery {

    /**
     * 自增id
     */
    @ApiModelProperty(value = " 自增id", required = true)
    private Long id;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台", required = true)
    private String platform;

    /**
     * 渠道标识
     */
    @ApiModelProperty(value = "渠道标识", required = true)
    private String channel;

    /**
     * 广告标识
     */
    @ApiModelProperty(value = "广告标识", required = true)
    private String aid;

    /**
     * 广告名称
     */
    @ApiModelProperty(value = "广告名称", required = true)
    private String aidName;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    private String gameName;

    /**
     * 注册用户数
     */
    @ApiModelProperty(value = "注册用户数", required = true)
    private Long users;

    /**
     * 付费用户数
     */
    @ApiModelProperty(value = "付费用户数", required = true)
    private Long payUsers;

    /**
     * 付费总金额
     */
    @ApiModelProperty(value = "付费总金额", required = true)
    private BigDecimal totalAmount;

    /**
     * 广告点击次数
     */
    @ApiModelProperty(value = "广告点击次数", required = true)
    private Long clickCnt;

    /**
     * 激活用户数
     */
    @ApiModelProperty(value = "激活用户数", required = true)
    private Long activeUsers;

    /**
     * 父渠道标识
     */
    @ApiModelProperty(value = "父渠道标识", required = true)
    private String preChannel;
    /**
     * 新增创角数
     */
    @ApiModelProperty(value = "新增创角数", required = true)
    private Long newRoles;

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
