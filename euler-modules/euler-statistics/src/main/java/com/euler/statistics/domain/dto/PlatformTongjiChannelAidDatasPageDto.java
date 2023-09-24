package com.euler.statistics.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;

import java.util.Date;

/**
 * 分页业务对象 platform_tongji_channel_aid_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分页业务对象")
public class PlatformTongjiChannelAidDatasPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台")
    private String platform;

    /**
     * 父渠道标识
     */
    @ApiModelProperty(value = "父渠道标识")
    private String preChannel;

    /**
     * 渠道标识
     */
    @ApiModelProperty(value = "渠道标识")
    private String channel;

    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期")
    private Date createDate;

    /**
     * 广告标识
     */
    @ApiModelProperty(value = "广告标识")
    private String aid;

    /**
     * 广告名称
     */
    @ApiModelProperty(value = "广告名称")
    private String aidName;

    /**
     * 广告点击次数
     */
    @ApiModelProperty(value = "广告点击次数")
    private Long clickCnt;

    /**
     * 激活用户数
     */
    @ApiModelProperty(value = "激活用户数")
    private Long activeUsers;

    /**
     * 游戏名
     */
    @ApiModelProperty(value = "游戏名")
    private String gameName;

    /**
     * 注册用户数
     */
    @ApiModelProperty(value = "注册用户数")
    private Long registUsers;

    /**
     * 付费用户数
     */
    @ApiModelProperty(value = "付费用户数")
    private Long payUsers;

    /**
     * 付费金额
     */
    @ApiModelProperty(value = "付费金额")
    private Long totalAmount;

    /**
     * 渠道ID
     */
    @ApiModelProperty(value = "渠道ID")
    private Integer channelId;

    /**
    * 开始时间
    */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
