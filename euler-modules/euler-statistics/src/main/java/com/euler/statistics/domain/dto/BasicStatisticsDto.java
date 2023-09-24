package com.euler.statistics.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据统计 - 每日的ltv基础数据统计对象 statistics_everyday_ltv_basedata
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicStatisticsDto extends PageQuery {

private static final long serialVersionUID=1L;

    /**
     * 主渠道id
     */
    private Integer channelId;
    /**
     * 主渠道name
     */
    private String channelName;
    /**
     * 渠道code
     */
    private String packageCode;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名
     */
    private String gameName;
    /**
     * 平台
     */
    private Integer operationPlatform;
    /**
     * 查询开始时间
     */
    private String startTime;
    /**
     * 查询结束时间
     */
    private String endTime;

}
