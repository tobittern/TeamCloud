package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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
public class ChargeStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏服务器id
     */
    @ExcelProperty(value = "游戏服务器id")
    @ApiModelProperty("游戏服务器id")
    private String serverId;

    /**
     * 游戏服务器名称
     */
    @ExcelProperty(value = "游戏服务器名称")
    @ApiModelProperty("游戏服务器名称")
    private String serverName;

    /**
     * 新增充值
     */
    @ExcelProperty(value = "新增充值")
    @ApiModelProperty("新增充值")
    private BigDecimal newIncreaseCharge;

    /**
     * 新注册充值
     */
    @ExcelProperty(value = "新注册充值")
    @ApiModelProperty("新注册充值")
    private BigDecimal newRegisterCharge;

    /**
     * 新注册平均充值
     */
    @ExcelProperty(value = "新注册平均充值")
    @ApiModelProperty("新注册平均充值")
    private BigDecimal newRegisterAvgCharge;

    /**
     * 总充值数据
     */
    @ExcelProperty(value = "总充值数据")
    @ApiModelProperty("总充值数据")
    private BigDecimal totalCharge;

    /**
     * 统计日期
     */
    @JsonFormat(pattern ="yyyy-MM-dd")
    @ExcelProperty(value = "统计日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty("统计日期")
    private Date day;

    /**
     * 用户注册时间
     */
    @ExcelProperty(value = "用户注册时间")
    @ApiModelProperty("用户注册时间")
    private Date registerTime;

    /**
     * 新注册用户人数
     */
    @ExcelProperty(value = "新注册用户人数")
    @ApiModelProperty("新注册用户人数")
    private int newRegisterCount;

}
