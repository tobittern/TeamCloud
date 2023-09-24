package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉对象 complaint
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
public class ComplaintJsonObject {

    private static final long serialVersionUID = 1L;

    /**
     * 通知ID
     */
    private String id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 通知类型
     */
    private String eventType;
    /**
     * 通知数据类型
     */
    private String resourceType;
    /**
     * 回调摘要
     */
    private String summary;
    /**
     * 投诉单号
     */
    private String complaintId;

    /**
     * 动作类型
     */
    private String actionType;

}
