package com.euler.sdk.domain.dto;

import com.euler.sdk.api.enums.RechargeTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WalletDto {
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 变动金额，负数为减
     */
    private Number numValue;

    /**
     * 变动类型，1：积分，2：余额
     */
    private RechargeTypeEnum rechargeTypeEnum;

    /**
     * 增减，1：增加，2：减少
     */
    private Integer isAdd;
    /**
     * 修改备注
     */
    private String modifyDesc;

    /**
     * 钱包类型
     */
    private Integer walletType;
    /**
     * 游戏Id
     */
    private  Integer gameId;

    /**
     * 钱包操作类型，1：正常增减，2：提现
     */
    private  Integer walletOpType=1;

}
