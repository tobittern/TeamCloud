package com.euler.sdk.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数据变换dto
 */
@Data
@ApiModel("数据变换dto")
public class StatisticsDto implements Serializable {

    /**截止到昨日各个渠道总人数 **/
    private BigDecimal userTotal;

    /**截止到昨日各个渠道总充值 **/
    private BigDecimal chargeTotal;

    /**截止到昨日各个渠道总订单数 **/
    private BigDecimal orderTotal;

    /**昨日各个渠道总人数 **/
    private BigDecimal yesterdayUser;

    /**昨日各个渠道总充值 **/
    private BigDecimal yesterdayCharge;

    /**昨日各个渠道总订单数 **/
    private BigDecimal yesterdayOrder;

    /**用户变化量 **/
    private BigDecimal userChange;


    /**金额变化量 **/
    private BigDecimal chargeChange;


    /**订单数量变化量 **/
    private BigDecimal orderChange;


    /**用户变化量标志  fall 下降  rise 上升 **/
    private String userFlag;

    /**金额变化标志  fall 下降  rise 上升**/
    private String chargeFlag;


    /**用户变化标志  fall 下降  rise 上升**/
    private String  orderFlag;
}
