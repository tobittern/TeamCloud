package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 商家处理交易投诉对象 ali_complaint_ feedback
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ali_complaint_feedback")
public class AliComplaintFeedback extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 支付宝侧投诉单
     */
    private String complainEventId;
    /**
     * 反馈类目ID
     */
    private String feedbackCode;
    /**
     * 反馈内容，字数不超过200个字
     */
    private String feedbackContent;
    /**
     * 商家处理投诉时反馈凭证的图片id，多个逗号隔开
     */
    private String feedbackImages;
    /**
     * 处理投诉人，字数不超过6个字
     */
    private String operator;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除状态
     */
    @TableLogic
    private String delFlag;

}
