package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象 statistics_charge
 *
 * @author euler
 * @date 2022-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("statistics_charge")
public class StatisticsCharge extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 总人数
     */
    private BigDecimal userTotal;
    /**
     * 总充值
     */
    private BigDecimal chargeTotal;
    /**
     * 总订单
     */
    private BigDecimal orderTotal;
    /**
     * 统计日期，所在渠道人数
     */
    private BigDecimal userNum;
    /**
     * 统计日期，所在渠道充值金额
     */
    private BigDecimal chargeNum;
    /**
     * 统计日期，所在渠道订单数目
     */
    private BigDecimal orderNum;
    /**
     * 统计日期
     */
    private Date day;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
