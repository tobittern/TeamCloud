package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 订单日志对象 order_log
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@TableName("order_log")
public class OrderLog   {

private static final long serialVersionUID=1L;

    public static final int ORDERTYPE_PAY = 1; //支付订单
    public static final int ORDERTYPE_REFUND = 2; //退款订单
    public static final int ORDERTYPE_TRANSFER = 3; //转账订单

    /**
     * 订单日志id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 业务订单id
     */
    private String businessOrderId;
    /**
     * 订单类型:1-支付,2-退款
     */
    private Integer orderType;

    /**
     * 操作内容
     */
    private String opContent;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
