package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉信息的log日志对象 complaint_log
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("complaint_log")
public class ComplaintLog extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 回调内容
     */
    private String content;
    /**
     * 删除状态 0正常 2上传
     */
     @TableLogic
    private String delFlag;

}
