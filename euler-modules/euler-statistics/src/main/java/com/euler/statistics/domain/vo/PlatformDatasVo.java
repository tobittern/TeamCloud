package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 视图对象 platform_datas
 * @author euler
 * @date 2022-07-13
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class PlatformDatasVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ExcelProperty(value = "自增id")
    @ApiModelProperty("自增id")
    private Long id;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 平台
     */
    @ExcelProperty(value = "平台")
    @ApiModelProperty("平台")
    private String platform;

    /**
     * 本地后台注册用户数
     */
    @ExcelProperty(value = "本地后台注册用户数")
    @ApiModelProperty("本地后台注册用户数")
    private Long bdUsers;

    /**
     * 广告平台注册用户数
     */
    @ExcelProperty(value = "广告平台注册用户数")
    @ApiModelProperty("广告平台注册用户数")
    private Long ptUsers;

    /**
     * 本地后台付费用户数
     */
    @ExcelProperty(value = "本地后台付费用户数")
    @ApiModelProperty("本地后台付费用户数")
    private Long bdPayUsers;

    /**
     * 广告平台付费用户数
     */
    @ExcelProperty(value = "广告平台付费用户数")
    @ApiModelProperty("广告平台付费用户数")
    private Long ptPayUsers;

    /**
     * 本地后台付费总金额
     */
    @ExcelProperty(value = "本地后台付费总金额")
    @ApiModelProperty("本地后台付费总金额")
    private BigDecimal bdTotalAmount;

    /**
     * 广告平台付费总金额
     */
    @ExcelProperty(value = "广告平台付费总金额")
    @ApiModelProperty("广告平台付费总金额")
    private BigDecimal ptTotalAmount;

    /**
     * 统计日期
     */
    @ExcelProperty(value = "统计日期")
    @ApiModelProperty("统计日期")
    private String createDate;

    /**
     * 广告平台新增创角数
     */
    @ExcelProperty(value = "广告平台新增创角数")
    @ApiModelProperty("广告平台新增创角数")
    private Long ptNewRoles;

    /**
     * 本地后台新增创角数
     */
    @ExcelProperty(value = "本地后台新增创角数")
    @ApiModelProperty("本地后台新增创角数")
    private Long bdNewRoles;


}
