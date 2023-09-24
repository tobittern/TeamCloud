package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 视图对象 platform_tongji_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class PlatformTongjiDatasVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建日期
     */
    @ExcelProperty(value = "创建日期")
    @ApiModelProperty("创建日期")
    private String createDate;

    /**
     * 自增主键标识(不显示)
     */
    @ExcelProperty(value = "自增主键标识(不显示)")
    @ApiModelProperty("自增主键标识(不显示)")
    private Long id;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 广告平台名称
     */
    @ExcelProperty(value = "广告平台名称")
    @ApiModelProperty("广告平台名称")
    private String platform;

    /**
     * 分包号
     */
    @ExcelProperty(value = "分包号")
    @ApiModelProperty("分包号")
    private String packageCode;

    /**
     * 当日新增用户数
     */
    @ExcelProperty(value = "当日新增用户数")
    @ApiModelProperty("当日新增用户数")
    private Long registerUsers;

    /**
     * 付费次数
     */
    @ExcelProperty(value = "付费次数")
    @ApiModelProperty("付费次数")
    private Long payCnt;

    /**
     * 首日付费次数
     */
    @ExcelProperty(value = "首日付费次数")
    @ApiModelProperty("首日付费次数")
    private Long firstPayCnt;

    /**
     * 首日付费用户数
     */
    @ExcelProperty(value = "首日付费用户数")
    @ApiModelProperty("首日付费用户数")
    private Long firstPayUsers;

    /**
     * 首日付费总金额
     */
    @ExcelProperty(value = "首日付费总金额")
    @ApiModelProperty("首日付费总金额")
    private BigDecimal firstRelTotal;

    /**
     * 实际成本
     */
    @ExcelProperty(value = "实际成本")
    @ApiModelProperty("实际成本")
    private BigDecimal relCost;

    /**
     * 首日LTV
     */
    @ExcelProperty(value = "首日LTV")
    @ApiModelProperty("首日LTV")
    private String ltv;

    /**
     * 首日ROI
     */
    @ExcelProperty(value = "首日ROI")
    @ApiModelProperty("首日ROI")
    private String roi;

    /**
     * 渠道ID
     */
    @ExcelProperty(value = "渠道ID")
    @ApiModelProperty(value = "渠道ID")
    private Integer channelId;


}
