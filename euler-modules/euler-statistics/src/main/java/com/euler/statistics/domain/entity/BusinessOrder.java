package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("business_order_1")
public class BusinessOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 业务订单号
     */
    @TableId(value = "id")
    private String id;
    private Integer orderState;
    private BigDecimal orderAmount;
    private Long memberId;
    private Integer gameId;
    private String gameName;
    private String gameRoleId;
    private String gameRoleName;
    private Integer gameChannelId;
    private String gamePackageCode;
}
