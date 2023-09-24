package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告成本管理视图对象 advertising_cost
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@ApiModel("广告成本管理视图对象")
@ExcelIgnoreUnannotated
public class AdvertisingCostVo {

  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @ExcelProperty(value = "主键id")
  @ApiModelProperty("主键id")
  private Long id;

  /**
   * 广告平台
   */
  @ExcelProperty(value = "广告平台 ")
  @ApiModelProperty("广告平台 ")
  private String advertisingPlatform;

  /**
   * 广告成本日期
   */
  @ExcelProperty(value = "广告成本日期")
  @ApiModelProperty("广告成本日期")
  private String costDate;

  /**
   * 成本，精确到小数点后两位
   */
  @ExcelProperty(value = "成本")
  @ApiModelProperty("成本，精确到小数点后两位")
  private Float cost;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏Id")
    private Integer gameId;


    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    @ExcelProperty(value = "游戏名称")
    private String gameName;


    /**
     * 媒体id
     */
    @ApiModelProperty("媒体id")
    private Integer mediaId;


    /**
     * 媒体名称
     */
    @ApiModelProperty("媒体名称")
    @ExcelProperty(value = "媒体名称")
    private String mediaName;

    /**
     * 游戏运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty("游戏运行平台 (1 安卓  2 ios  3 h5)")
    private Integer gameOperationPlatform;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
