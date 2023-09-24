package com.euler.sdk.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 提现规则对象 cash_out_rule
 *
 * @author euler
 * @date 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cash_out_rule")
public class CashOutRule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 提现规则名称
     */
    private String ruleName;
    /**
     * 提现规则类型，1-积分，2-余额，3-平台币
     */
    private Integer ruleType;
    /**
     * 每天提现限制次数
     */
    private Integer dayNum;
    /**
     * 规则状态
     */
    private Integer ruleStatus;
    /**
     * 提现限制金额
     */
    private BigDecimal cashOutMoney;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
