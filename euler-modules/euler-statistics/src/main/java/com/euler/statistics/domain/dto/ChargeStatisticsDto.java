package com.euler.statistics.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 开放平台充值金额统计视图对象 charge_statistics
 *
 * @author euler
 *  2022-07-13
 */
@Data
@ApiModel("开放平台充值金额统计视图对象")
@ExcelIgnoreUnannotated
public class ChargeStatisticsDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 统计日期
     */
    private Date day;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏服务器id
     */
    private String serverId;

    /**
     * 游戏服务器名称
     */
    private String serverName;

    /**
     * 开始日期
     */
    private String beginTime;

    /**
     * 结束日期
     */
    private String endTime;

}
