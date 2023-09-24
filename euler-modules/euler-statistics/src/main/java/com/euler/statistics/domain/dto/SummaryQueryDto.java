package com.euler.statistics.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 汇总数据获取参数
 */
@Data
@Accessors(chain = true)
public class SummaryQueryDto {

    /**
     * 渠道号
     */
    @ApiModelProperty("渠道号")
    private String packageCode;

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
     * 渠道id
     */
    @ApiModelProperty("渠道id")
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;

    @ApiModelProperty("统计指维度标识,用户折线图")
    private  String measureId;

    /**
     * 开始时间，yyyy-MM-dd
     */
    @ApiModelProperty("开始时间，yyyy-MM-dd")
    private String beginTime;


    /**
     * 结束时间，yyyy-MM-dd
     */
    @ApiModelProperty("结束时间，yyyy-MM-dd")
    private String endTime;

    /**
     * 区服id
     */
    @ApiModelProperty("区服id")
    private String serverId;

    /**
     * 区服名称
     */
    @ApiModelProperty("区服名称")
    private String serverName;

}
