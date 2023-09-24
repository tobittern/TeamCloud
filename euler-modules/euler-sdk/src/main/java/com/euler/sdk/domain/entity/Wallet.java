package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 钱包对象 wallet
 *
 * @author euler
 * @date 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet")
@NoArgsConstructor
public class Wallet extends BaseEntity {

private static final long serialVersionUID=1L;
public  Wallet(Long memberId,Integer walletType,Integer gameId){
    this.memberId=memberId;
    this.walletType=walletType;
    this.gameId=gameId;
}

    /**
     * id
     */
    private Integer id;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    /**
     * 余额，元
     */
    private BigDecimal balance=BigDecimal.ZERO;
    /**
     * 积分
     */
    private Long score;
    /**
     * 平台币
     */
    private Long platformCurrency;
    /**
     * 成长值
     */
    private Long growthValue;

    /**
     * 钱包类型，1：正常钱包，2：虚拟钱包
     */
    private  Integer walletType;

    /**
     * 游戏Id
     */
    private  Integer gameId;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
     @TableLogic
    private String delFlag;

}
