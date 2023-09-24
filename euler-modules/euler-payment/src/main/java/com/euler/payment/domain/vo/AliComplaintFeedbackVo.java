package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 商家处理交易投诉视图对象 ali_complaint_ feedback
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@ApiModel("商家处理交易投诉视图对象")
@ExcelIgnoreUnannotated
public class AliComplaintFeedbackVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 支付宝侧投诉单
     */
    @ExcelProperty(value = "支付宝侧投诉单")
    @ApiModelProperty("支付宝侧投诉单")
    private String complainEventId;

    /**
     * 反馈类目ID
     */
    @ExcelProperty(value = "反馈类目ID")
    @ApiModelProperty("反馈类目ID")
    private String feedbackCode;

    /**
     * 反馈内容，字数不超过200个字
     */
    @ExcelProperty(value = "反馈内容，字数不超过200个字")
    @ApiModelProperty("反馈内容，字数不超过200个字")
    private String feedbackContent;

    /**
     * 商家处理投诉时反馈凭证的图片id，多个逗号隔开
     */
    @ExcelProperty(value = "商家处理投诉时反馈凭证的图片id，多个逗号隔开")
    @ApiModelProperty("商家处理投诉时反馈凭证的图片id，多个逗号隔开")
    private String feedbackImages;

    /**
     * 处理投诉人，字数不超过6个字
     */
    @ExcelProperty(value = "处理投诉人，字数不超过6个字")
    @ApiModelProperty("处理投诉人，字数不超过6个字")
    private String operator;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
