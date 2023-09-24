package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * 投诉单关联订单信息视图对象 complaint_order_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉单关联订单信息视图对象")
@ExcelIgnoreUnannotated
public class ComplaintOrderInfoVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private String transactionId;

    /**
     * 商户订单号
     */
    @ExcelProperty(value = "商户订单号")
    @ApiModelProperty("商户订单号")
    private String outTradeNo;

    /**
     * 订单金额 单位是分
     */
    @ExcelProperty(value = "订单金额 单位是分")
    @ApiModelProperty("订单金额 单位是分")
    private Integer amount;


}
