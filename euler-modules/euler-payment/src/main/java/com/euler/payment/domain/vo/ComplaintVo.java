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
 * 投诉视图对象 complaint
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉视图对象")
@ExcelIgnoreUnannotated
public class ComplaintVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     *  通知ID
     */
    @ExcelProperty(value = " 通知ID")
    @ApiModelProperty(" 通知ID")
    private String noticeId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private String complaintCreateTime;

    /**
     * 通知类型
     */
    @ExcelProperty(value = "通知类型")
    @ApiModelProperty("通知类型")
    private String eventType;

    /**
     * 通知数据类型
     */
    @ExcelProperty(value = "通知数据类型")
    @ApiModelProperty("通知数据类型")
    private String resourceType;

    /**
     * 回调摘要
     */
    @ExcelProperty(value = "回调摘要")
    @ApiModelProperty("回调摘要")
    private String summary;

    /**
     * 投诉单号
     */
    @ExcelProperty(value = "投诉单号")
    @ApiModelProperty(value = "投诉单号")
    private String complaintId;

    /**
     * 动作类型
     */
    @ExcelProperty(value = "动作类型")
    @ApiModelProperty(value = "动作类型")
    private String actionType;


}
