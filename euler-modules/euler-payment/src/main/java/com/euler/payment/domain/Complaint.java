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
@EqualsAndHashCode(callSuper = true)
@TableName("complaint")
public class Complaint extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 通知ID
     */
    private String noticeId;
    /**
     * 创建时间
     */
    private String complaintCreateTime;
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

    /**
     * 删除状态 0正常 2上传
     */
    @TableLogic
    private String delFlag;

}
