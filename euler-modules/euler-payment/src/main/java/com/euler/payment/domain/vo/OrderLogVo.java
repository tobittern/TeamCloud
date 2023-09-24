package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 订单日志视图对象 order_log
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("订单日志视图对象")
@ExcelIgnoreUnannotated
public class OrderLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * 订单日志id
     */
    @ExcelProperty(value = "订单日志id")
    @ApiModelProperty("订单日志id")
    private Long id;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    @ApiModelProperty("订单ID")
    private String businessOrderId;

    /**
     * 订单类型:1-支付,2-退款
     */
    @ExcelProperty(value = "订单类型:1-支付,2-退款")
    @ApiModelProperty("订单类型:1-支付,2-退款")
    private Integer orderType;

    /**
     * 操作内容
     */
    @ExcelProperty(value = "操作内容")
    @ApiModelProperty("操作内容")
    private String opContent;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
