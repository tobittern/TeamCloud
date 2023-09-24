package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.euler.common.core.web.domain.BaseEntity;

/**
 * 用户相关的标准指标统计对象 tf_user_quota
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@TableName("tf_user_quota")
public class TfUserQuota implements Serializable {

private static final long serialVersionUID=1L;

    /**
     *id
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 日期
     */
    private String dateId;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 指标标识
     */
    private String measureId;
    /**
     * 指标值
     */
    private BigDecimal measureValue;

    /**
     * 创建时间
     */
    private Date createTime;

}
