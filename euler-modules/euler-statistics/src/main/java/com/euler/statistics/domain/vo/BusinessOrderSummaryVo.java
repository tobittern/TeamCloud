package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 订单视图对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("订单汇总数据")
@ExcelIgnoreUnannotated
public class BusinessOrderSummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新增付费次数
     */
    @ExcelProperty(value = "新增付费次数")
    @ApiModelProperty("新增付费次数")
    private Integer newIncrPayNum = 0;

    /**
     * 新增付费订单数
     */
    @ExcelProperty(value = "新增付费订单数")
    @ApiModelProperty("新增付费订单数")
    private Integer newIncrPayOrderNum = 0;

    /**
     * 新增付费金额
     */
    @ExcelProperty(value = "新增付费金额")
    @ApiModelProperty("新增付费金额")
    private BigDecimal newIncrPayAmount = new BigDecimal(0);


    /**
     * 当前时间 当前渠道里面 新增的充值用户的id集合
     */
    private NewIncrOrderUserVo newIncrOrderUserVos;


}
