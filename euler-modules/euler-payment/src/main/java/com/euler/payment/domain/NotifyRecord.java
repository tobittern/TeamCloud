package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 通知记录对象 notify_record
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@TableName("notify_record")
public class NotifyRecord  {

    private static final long serialVersionUID = 1L;


    public final static Integer NOTIFY_STATE_NO = 0;
    public final static Integer NOTIFY_STATE_ING = 1;
    public final static Integer NOTIFY_STATE_SUCCESS = 2;
    public final static Integer NOTIFY_STATE_FAILED = 3;


    /**
     * 通知记录ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 业务订单id
     */
    private String businessOrderId;
    /**
     * 订单类型:1-支付,2-退款
     */
    private Integer orderType;

    /**
     * 通知响应结果
     */
    private String resResult;


    /**
     * 通知状态,0-未通知，1-通知中,2-通知成功,3-通知失败
     */
    private Integer state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
