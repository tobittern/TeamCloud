package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉详情对象 complaint_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("complaint_info")
public class ComplaintInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 投诉单号
     */
    private String complaintId;
    /**
     * 创建时间
     */
    private String complaintTime;
    /**
     * 投诉详情
     */
    private String complaintDetail;
    /**
     * 被诉商户号
     */
    private String complaintedMchid;
    /**
     * 投诉单状态
     */
    private String complaintState;
    /**
     * 投诉人联系方式
     */
    private String payerPhone;
    /**
     * 投诉人openid
     */
    private String payerOpenid;
    /**
     * 投诉单是否已全额退款 0 false  1true
     */
    private String complaintFullRefunded;
    /**
     * 是否有待回复的用户留言 0 false  1true
     */
    private String incomingUserResponse;
    /**
     * 问题描述
     */
    private String problemDescription;
    /**
     * 用户投诉次数
     */
    private Integer userComplaintTimes;
    /**
     * 问题类型
     */
    private String problemType;
    /**
     * 申请退款金额(分)
     */
    private Integer applyRefundAmount;
    /**
     * 用户标签列表
     */
    private String userTagList;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单金额 单位是分
     */
    private Integer amount;
    /**
     * 删除状态 0正常 2上传
     */
    @TableLogic
    private String delFlag;

}
