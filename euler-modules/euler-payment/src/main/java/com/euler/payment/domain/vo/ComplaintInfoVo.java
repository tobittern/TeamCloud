package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 投诉详情视图对象 complaint_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉详情视图对象")
@ExcelIgnoreUnannotated
public class ComplaintInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 投诉单号
     */
    @ExcelProperty(value = "投诉单号")
    @ApiModelProperty("投诉单号")
    private String complaintId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private String complaintTime;

    /**
     * 投诉详情
     */
    @ExcelProperty(value = "投诉详情")
    @ApiModelProperty("投诉详情")
    private String complaintDetail;

    /**
     * 被诉商户号
     */
    @ExcelProperty(value = "被诉商户号")
    @ApiModelProperty("被诉商户号")
    private String complaintedMchid;

    /**
     * 投诉单状态
     */
    @ExcelProperty(value = "投诉单状态")
    @ApiModelProperty("投诉单状态")
    private String complaintState;

    /**
     * 投诉人联系方式
     */
    @ExcelProperty(value = "投诉人联系方式")
    @ApiModelProperty("投诉人联系方式")
    private String payerPhone;

    /**
     * 投诉人openid
     */
    @ExcelProperty(value = "投诉人openid")
    @ApiModelProperty("投诉人openid")
    private String payerOpenid;

    /**
     * 投诉资料列表
     */
    @ExcelProperty(value = "投诉资料列表")
    @ApiModelProperty("投诉资料列表")
    private List<ComplaintMediaListVo> complaintMediaList;

    /**
     * 投诉单关联订单信息
     */
    @ExcelProperty(value = "投诉单关联订单信息")
    @ApiModelProperty("投诉单关联订单信息")
    private List<ComplaintOrderInfoVo> complaintOrderInfo;

    /**
     * 投诉单关联服务单信息
     */
    @ExcelProperty(value = "投诉单关联服务单信息")
    @ApiModelProperty("投诉单关联服务单信息")
    private List<ServiceOrderInfoVo> serviceOrderInfo;

    /**
     * 投诉单是否已全额退款 0 false  1true
     */
    @ExcelProperty(value = "投诉单是否已全额退款 0 false  1true")
    @ApiModelProperty("投诉单是否已全额退款 0 false  1true")
    private String complaintFullRefunded;

    /**
     * 是否有待回复的用户留言 0 false  1true
     */
    @ExcelProperty(value = "是否有待回复的用户留言 0 false  1true")
    @ApiModelProperty("是否有待回复的用户留言 0 false  1true")
    private String incomingUserResponse;

    /**
     * 问题描述
     */
    @ExcelProperty(value = "问题描述")
    @ApiModelProperty("问题描述")
    private String problemDescription;

    /**
     * 用户投诉次数
     */
    @ExcelProperty(value = "用户投诉次数")
    @ApiModelProperty("用户投诉次数")
    private Integer userComplaintTimes;

    /**
     * 问题类型
     */
    @ExcelProperty(value = "问题类型")
    @ApiModelProperty("问题类型")
    private String problemType;

    /**
     * 申请退款金额(分)
     */
    @ExcelProperty(value = "申请退款金额(分)")
    @ApiModelProperty("申请退款金额(分)")
    private Integer applyRefundAmount;

    /**
     * 用户标签列表
     */
    @ExcelProperty(value = "用户标签列表")
    @ApiModelProperty("用户标签列表")
    private String userTagList;

    /**
     * 微信订单号
     */
    @ExcelProperty(value = "微信订单号")
    @ApiModelProperty("微信订单号")
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

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
