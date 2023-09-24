package com.euler.payment.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 投诉详情分页业务对象 complaint_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("投诉详情分页业务对象")
public class ComplaintInfoPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 投诉单号
     */
    @ApiModelProperty(value = "投诉单号")
    private String complaintId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String complaintTime;

    /**
     * 投诉详情
     */
    @ApiModelProperty(value = "投诉详情")
    private String complaintDetail;

    /**
     * 被诉商户号
     */
    @ApiModelProperty(value = "被诉商户号")
    private String complaintedMchid;

    /**
     * 投诉单状态
     */
    @ApiModelProperty(value = "投诉单状态")
    private String complaintState;

    /**
     * 投诉人联系方式
     */
    @ApiModelProperty(value = "投诉人联系方式")
    private String payerPhone;

    /**
     * 投诉人openid
     */
    @ApiModelProperty(value = "投诉人openid")
    private String payerOpenid;

    /**
     * 投诉单是否已全额退款 0 false  1true
     */
    @ApiModelProperty(value = "投诉单是否已全额退款 0 false  1true")
    private String complaintFullRefunded;

    /**
     * 是否有待回复的用户留言 0 false  1true
     */
    @ApiModelProperty(value = "是否有待回复的用户留言 0 false  1true")
    private String incomingUserResponse;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    /**
     * 用户投诉次数
     */
    @ApiModelProperty(value = "用户投诉次数")
    private Integer userComplaintTimes;

    /**
     * 问题类型
     */
    @ApiModelProperty(value = "问题类型")
    private String problemType;

    /**
     * 申请退款金额(分)
     */
    @ApiModelProperty(value = "申请退款金额(分)")
    private Integer applyRefundAmount;

    /**
     * 用户标签列表
     */
    @ApiModelProperty(value = "用户标签列表")
    private String userTagList;
    /**
     * 微信订单号
     */
    @ApiModelProperty("微信订单号")
    private String transactionId;

    /**
     * 商户订单号
     */
    @ApiModelProperty("商户订单号")
    private String outTradeNo;

    /**
     * 订单金额 单位是分
     */
    @ApiModelProperty("订单金额 单位是分")
    private Integer amount;

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
