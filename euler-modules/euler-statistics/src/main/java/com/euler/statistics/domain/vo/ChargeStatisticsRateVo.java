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
 * 开放平台充值金额变化率视图对象
 *
 * @author euler
 * 2022-07-13
 */
@Data
@ApiModel("开放平台充值金额变化率视图对象")
@ExcelIgnoreUnannotated
public class ChargeStatisticsRateVo {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日期")
    private String day;

    /**
     * 游戏服务器id
     */
    @ApiModelProperty("游戏服务器id")
    private String serverId;

    /**
     * 新增充值
     */
    @ApiModelProperty("新增充值")
    private String newIncreaseCharge;

    /**
     * 新注册充值
     */
    @ApiModelProperty("新注册充值")
    private String newRegisterCharge;

    /**
     * 新注册平均充值
     */
    @ApiModelProperty("新注册平均充值")
    private String newRegisterAvgCharge;

    /**
     * 总充值数据
     */
    @ApiModelProperty("总充值数据")
    private String totalCharge;

    /**
     * 新增充值的日变化率
     */
    @ApiModelProperty("新增充值的日变化率")
    private String dayNewIncCharge = "0.00%";

    /**
     * 新增充值的日变化标识
     */
    @ApiModelProperty("新增充值的日变化标识")
    private String dayNewIncChargeFlag;

    /**
     * 新增充值的周变化率
     */
    @ApiModelProperty("新增充值的周变化率")
    private String weekNewIncCharge = "0.00%";

    /**
     * 新增充值的周变化标识
     */
    @ApiModelProperty("新增充值的周变化标识")
    private String weekNewIncChargeFlag;

    /**
     * 新增充值的月变化率
     */
    @ApiModelProperty("新增充值的月变化率")
    private String monthNewIncCharge = "0.00%";

    /**
     * 新增充值的月变化标识
     */
    @ApiModelProperty("新增充值的月变化标识")
    private String monthNewIncChargeFlag;

    /**
     * 新注册充值的日变化率
     */
    @ApiModelProperty("新注册充值的日变化率")
    private String dayNewRegCharge = "0.00%";

    /**
     * 新注册充值的日变化标识
     */
    @ApiModelProperty("新注册充值的日变化标识")
    private String dayNewRegChargeFlag;

    /**
     * 新注册充值的周变化率
     */
    @ApiModelProperty("新注册充值的周变化率")
    private String weekNewRegCharge = "0.00%";

    /**
     * 新注册充值的周变化标识
     */
    @ApiModelProperty("新注册充值的周变化标识")
    private String weekNewRegChargeFlag;

    /**
     * 新注册充值的月变化率
     */
    @ApiModelProperty("新注册充值的月变化率")
    private String monthNewRegCharge = "0.00%";

    /**
     * 新注册充值的月变化标识
     */
    @ApiModelProperty("新注册充值的月变化标识")
    private String monthNewRegChargeFlag;

    /**
     * 新注册平均充值的日变化率
     */
    @ApiModelProperty("新注册平均充值的日变化率")
    private String dayNewRegAvgCharge = "0.00%";

    /**
     * 新注册平均充值的日变化标识
     */
    @ApiModelProperty("新注册平均充值的日变化标识")
    private String dayNewRegAvgChargeFlag;

    /**
     * 新注册平均充值的周变化率
     */
    @ApiModelProperty("新注册平均充值的周变化率")
    private String weekNewRegAvgCharge = "0.00%";

    /**
     * 新注册平均充值的周变化标识
     */
    @ApiModelProperty("新注册平均充值的周变化标识")
    private String weekNewRegAvgChargeFlag;

    /**
     * 新注册平均充值的变化率
     */
    @ApiModelProperty("新注册平均充值的变化率")
    private String monthNewRegAvgCharge = "0.00%";

    /**
     * 新注册平均充值的月变化标识
     */
    @ApiModelProperty("新注册平均充值的月变化标识")
    private String monthNewRegAvgChargeFlag;

    /**
     * 总充值的日变化率
     */
    @ApiModelProperty("总充值的日变化率")
    private String dayTotalCharge = "0.00%";

    /**
     * 总充值的日变化标识
     */
    @ApiModelProperty("总充值的日变化标识")
    private String dayTotalChargeFlag;

    /**
     * 总充值的周变化率
     */
    @ApiModelProperty("总充值的周变化率")
    private String weekTotalCharge = "0.00%";

    /**
     * 总充值的周变化标识
     */
    @ApiModelProperty("总充值的周变化标识")
    private String weekTotalChargeFlag;

    /**
     * 总充值的月变化率
     */
    @ApiModelProperty("总充值的月变化率")
    private String monthTotalCharge = "0.00%";

    /**
     * 总充值的月变化标识
     */
    @ApiModelProperty("总充值的月变化标识")
    private String monthTotalChargeFlag;

}
