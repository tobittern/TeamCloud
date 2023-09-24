package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉单关联服务单信息对象 service_order_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@TableName("service_order_info")
public class ServiceOrderInfo {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 投诉详情ID
     */
    private Long complaintInfoId;
    /**
     * 微信支付服务订单号
     */
    private String orderId;
    /**
     * 商户服务订单号
     */
    private String outOrderNo;
    /**
     * 支付分服务单状态
     */
    private String state;

}
