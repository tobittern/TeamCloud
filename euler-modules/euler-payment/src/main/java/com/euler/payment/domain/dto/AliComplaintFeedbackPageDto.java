package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 商家处理交易投诉分页业务对象 ali_complaint_ feedback
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("商家处理交易投诉分页业务对象")
public class AliComplaintFeedbackPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 支付宝侧投诉单
     */
    @ApiModelProperty(value = "支付宝侧投诉单")
    private String complainEventId;

    /**
     * 反馈类目ID
     */
    @ApiModelProperty(value = "反馈类目ID")
    private String feedbackCode;

    /**
     * 反馈内容，字数不超过200个字
     */
    @ApiModelProperty(value = "反馈内容，字数不超过200个字")
    private String feedbackContent;

    /**
     * 商家处理投诉时反馈凭证的图片id，多个逗号隔开
     */
    @ApiModelProperty(value = "商家处理投诉时反馈凭证的图片id，多个逗号隔开")
    private String feedbackImages;

    /**
     * 处理投诉人，字数不超过6个字
     */
    @ApiModelProperty(value = "处理投诉人，字数不超过6个字")
    private String operator;

    /**
    * 开始时间
    */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
