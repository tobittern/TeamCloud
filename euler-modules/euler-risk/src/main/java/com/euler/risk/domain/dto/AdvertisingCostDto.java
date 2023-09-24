package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 广告成本管理业务对象 advertising_cost
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告成本管理业务对象")
public class AdvertisingCostDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 广告成本日期开始区间
     */
    @ApiModelProperty(value = "广告成本日期开始区间")
    private Date advertisingDateFrom;

    /**
     * 广告成本日期结束区间
     */
    @ApiModelProperty(value = "广告成本日期结束区间")
    private Date advertisingDateTo;

    /**
     * 广告平台
     */
    @ApiModelProperty(value = "广告平台")
    private String advertisingPlatform;

    /**
     * 成本开始区间
     */
    @ApiModelProperty(value = "成本开始区间")
    private BigDecimal costFrom;

    /**
     * 成本结束区间
     */
    @ApiModelProperty(value = "成本结束区间")
    private BigDecimal costTo;


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
     * 媒体id
     */
    @ApiModelProperty("媒体id")
    private Integer mediaId;


    /**
     * 媒体名称
     */
    @ApiModelProperty("游戏名称")
    private String mediaName;

    /**
     * 游戏运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty("游戏运行平台 (1 安卓  2 ios  3 h5)")
    private Integer gameOperationPlatform;


}
