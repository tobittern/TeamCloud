package com.euler.statistics.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 留存数据统计
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeepDataStatisticsDto extends PageQuery {

    private static final long serialVersionUID = 1L;

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
    private String operationPlatform;

    /**
     * 查询开始时间
     */
    private String startTime;

    /**
     * 查询结束时间
     */
    private String endTime;

    /**
     * 单日留存标记
     */
    private Boolean singleFlag = false;

}
