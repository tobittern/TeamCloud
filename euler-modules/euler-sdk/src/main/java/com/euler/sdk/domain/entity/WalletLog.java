package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 钱包变动记录对象 wallet_log
 *
 * @author euler
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wallet_log")
public class WalletLog extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 钱包id
     */
    private Integer walletId;
    /**
     * 钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值
     */
    private Integer changeType;
    /**
     * 1：增加，2：减少
     */
    private Integer isAdd;
    /**
     * 描述
     */
    private String description;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
     @TableLogic
    private String delFlag;
    /**
     * 变动数额
     */
    private BigDecimal changeValue;

    /**
     * 钱包类型，1：正常钱包，2：虚拟钱包
     */
    private  Integer walletType;

    /**
     * 游戏Id
     */
    private  Integer gameId;

    /**
     * 钱包操作类型，1：正常增减，2：提现
     */
    private  Integer walletOpType;

    /**
     * 余额类型 1:可提现 2:不可提现 3:达到额度可提现
     */
    private  Integer balanceType;

}
