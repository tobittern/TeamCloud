package com.euler.payment.api.domain;

import java.io.Serializable;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 通知记录视图对象 notify_record
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("通知记录视图对象")
@ExcelIgnoreUnannotated
public class NotifyRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知记录ID
     */
    @ExcelProperty(value = "通知记录ID")
    @ApiModelProperty("通知记录ID")
    private String id;

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
     * 通知响应结果
     */
    @ExcelProperty(value = "通知响应结果")
    @ApiModelProperty("通知响应结果")
    private String resResult;


    /**
     * 通知状态,1-通知中,2-通知成功,3-通知失败
     */
    @ExcelProperty(value = "通知状态,1-通知中,2-通知成功,3-通知失败")
    @ApiModelProperty("通知状态,1-通知中,2-通知成功,3-通知失败")
    private Integer state;


    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createdTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    @ApiModelProperty("更新时间")
    private Date updatedTime;


}
