package com.euler.sdk.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 视图对象 statistics_charge
 *
 * @author euler
 * @date 2022-07-06
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class StatisticsChargeDto extends PageQuery{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

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

    /**
     * 总人数
     */
    @ApiModelProperty("总人数")
    private BigDecimal userTotal;

    /**
     * 总充值
     */
    @ApiModelProperty("总充值")
    private BigDecimal chargeTotal;

    /**
     * 总订单
     */
    @ApiModelProperty("总订单")
    private BigDecimal orderTotal;

    /**
     * 统计日期，所在渠道人数
     */
    @ApiModelProperty("统计日期，所在渠道人数")
    private BigDecimal userNum;

    /**
     * 统计日期，所在渠道充值金额
     */
    @ApiModelProperty("统计日期，所在渠道充值金额")
    private BigDecimal chargeNum;

    /**
     * 统计日期，所在渠道订单数目
     */
    @ApiModelProperty("统计日期，所在渠道订单数目")
    private BigDecimal orderNum;

    /**
     * 统计日期
     */
    @ApiModelProperty("统计日期")
    private Date day;

    /**
     * id 集合
     */
    private Long[] ids;

    /**
     * 日期，例如 2022-07-06
     */
    private String date;


}
