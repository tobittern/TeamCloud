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
 * 投诉单关联服务单信息视图对象 service_order_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉单关联服务单信息视图对象")
@ExcelIgnoreUnannotated
public class ServiceOrderInfoVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 微信支付服务订单号
     */
    @ExcelProperty(value = "微信支付服务订单号")
    @ApiModelProperty("微信支付服务订单号")
    private String orderId;

    /**
     * 商户服务订单号
     */
    @ExcelProperty(value = "商户服务订单号")
    @ApiModelProperty("商户服务订单号")
    private String outOrderNo;

    /**
     * 支付分服务单状态
     */
    @ExcelProperty(value = "支付分服务单状态")
    @ApiModelProperty("支付分服务单状态")
    private String state;


}
