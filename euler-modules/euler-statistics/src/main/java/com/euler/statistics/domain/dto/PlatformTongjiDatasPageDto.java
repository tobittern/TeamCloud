package com.euler.statistics.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 分页业务对象 platform_tongji_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分页业务对象")
public class PlatformTongjiDatasPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private String createDate;

    /**
     * 自增主键标识(不显示)
     */
    @ApiModelProperty(value = "自增主键标识(不显示)")
    private Long id;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 广告平台名称
     */
    @ApiModelProperty(value = "广告平台名称")
    private String platform;

    /**
     * 分包号
     */
    @ApiModelProperty(value = "分包号")
    private String packageCode;

    /**
     * 当日新增用户数
     */
    @ApiModelProperty(value = "当日新增用户数")
    private Long registerUsers;

    /**
     * 付费次数
     */
    @ApiModelProperty(value = "付费次数")
    private Long payCnt;

    /**
     * 首日付费次数
     */
    @ApiModelProperty(value = "首日付费次数")
    private Long firstPayCnt;

    /**
     * 首日付费用户数
     */
    @ApiModelProperty(value = "首日付费用户数")
    private Long firstPayUsers;

    /**
     * 首日付费总金额
     */
    @ApiModelProperty(value = "首日付费总金额")
    private Long firstRelTotal;

    /**
     * 首日LTV
     */
    @ApiModelProperty(value = "首日LTV")
    private BigDecimal ltv;

    /**
     * 首日ROI
     */
    @ApiModelProperty(value = "首日ROI")
    private BigDecimal roi;

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
