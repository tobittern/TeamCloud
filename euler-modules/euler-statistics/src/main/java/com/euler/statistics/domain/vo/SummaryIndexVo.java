package com.euler.statistics.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 后台首页数据统计
 */
@ApiModel("后台首页数据统计")
@Data
@Accessors(chain = true)
public class SummaryIndexVo implements Serializable {

    /**
     * 总用户数
     */
    @ApiModelProperty("总用户数")
    private String totalUserNum = "0";
    /**
     * 总订单金额
     */
    @ApiModelProperty("总订单金额")
    private String totalOrderAmount = "0.00";
    /**
     * 总订单数
     */
    @ApiModelProperty("总订单数")
    private String totalOrderNum = "0";
    /**
     * 昨日新增用户数
     */
    @ApiModelProperty("昨日新增用户数")
    private String yesterdayIncUserNum = "0";
    /**
     * 昨日新增订单金额
     */
    @ApiModelProperty("昨日新增订单金额")
    private String yesterdayIncOrderAmount = "0.00";
    /**
     * 昨日新增订单数
     */
    @ApiModelProperty("昨日新增订单数")
    private String yesterdayIncOrderNum = "0";
    /**
     * 昨日在线用户数
     */
    @ApiModelProperty("昨日在线用户数")
    private String yesterdayOnlineUserNum = "0";
    /**
     * 今日新增用户数
     */
    @ApiModelProperty("今日新增用户数")
    private String todayIncUserNum = "0";
    /**
     * 今日新增订单金额
     */
    @ApiModelProperty("今日新增订单金额")
    private String todayIncOrderAmount = "0.00";
    /**
     * 今日新增订单数
     */
    @ApiModelProperty("今日新增订单数")
    private String todayIncOrderNum = "0";
    /**
     * 今日在线用户数
     */
    @ApiModelProperty("今日在线用户数")
    private String todayOnlineUserNum = "0";
}
