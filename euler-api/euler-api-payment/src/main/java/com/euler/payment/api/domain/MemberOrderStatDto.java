package com.euler.payment.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberOrderStatDto implements Serializable {
    /**
     * 充值次数
     */
    private Integer rechargeNum = 0;
    /**
     * 充值金额
     */
    private BigDecimal rechargeAmount = new BigDecimal(0);


    /**
     * 单笔充值是否大于某个金额
     */
    private Boolean checkRechargeAmount = false;
}
