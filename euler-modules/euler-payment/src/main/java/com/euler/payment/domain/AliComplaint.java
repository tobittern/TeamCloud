package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 阿里支付的投诉对象 ali_complaint
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ali_complaint")
public class AliComplaint extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 应用id
     */
    private String targetId;
    /**
     * 应用类型
     */
    private String targetType;
    /**
     * 支付宝侧投诉单号
     */
    private String complainEventId;
    /**
     * 状态
     */
    private String status;
    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 商家订单号
     */
    private String merchantOrderNo;
    /**
     * 投诉单创建时间
     */
    private String gmtCreate;
    /**
     * 投诉单修改时间
     */
    private String gmtModified;
    /**
     * 投诉单完结时间
     */
    private String gmtFinished;
    /**
     * 用户投诉诉求
     */
    private String leafCategoryName;
    /**
     * 用户投诉原因
     */
    private String complainReason;
    /**
     * 用户投诉内容
     */
    private String content;
    /**
     * 投诉图片
     */
    private String imagesString;
    /**
     * 投诉人电话号码
     */
    private String phoneNo;
    /**
     * 交易金额，单位元
     */
    private String tradeAmount;
    /**
     * 删除状态
     */
     @TableLogic
    private String delFlag;

}
