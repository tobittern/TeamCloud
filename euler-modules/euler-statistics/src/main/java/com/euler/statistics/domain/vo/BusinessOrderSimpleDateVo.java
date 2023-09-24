package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 订单视图对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("业务订单视图对象")
@ExcelIgnoreUnannotated
public class BusinessOrderSimpleDateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单数量
     */
    @ExcelProperty(value = "订单数量")
    @ApiModelProperty("订单数量")
    private Integer orderNums;


    /**
     * 订单金额
     */
    @ExcelProperty(value = "订单金额")
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmounts;

    /**
     * 格式化的时间
     */
    @ExcelProperty(value = "格式化的时间")
    @ApiModelProperty("格式化的时间")
    private String dateFormat;



    /**
     * 格式化的时间 数字格式
     */
    @ExcelProperty(value = "格式化的时间 数字格式")
    @ApiModelProperty("格式化的时间 数字格式")
    private Integer dateFormatNumbers;

}
