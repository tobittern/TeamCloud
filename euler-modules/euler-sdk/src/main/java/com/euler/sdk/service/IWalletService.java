package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.sdk.domain.dto.CashOutDto;
import com.euler.sdk.domain.dto.GetWalletDto;
import com.euler.sdk.domain.dto.WalletDto;
import com.euler.sdk.domain.entity.Wallet;
import com.euler.sdk.api.domain.WalletVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.sdk.domain.vo.WalletLogVo;

/**
 * 钱包Service接口
 *
 * @author euler
 * @date 2022-03-28
 */
public interface IWalletService extends IService<Wallet> {

    /**
     * 查询个人钱包
     *
     * @param idTypeDto
     * @return
     */
    WalletVo queryByMemberId(GetWalletDto idTypeDto);

    /**
     * 钱包金额变动
     *
     * @param memberId         会员id
     * @param walletType       钱包类型，1：正常钱包，2：虚拟钱包
     * @param numValue         变动金额，负数为减
     * @param rechargeTypeEnum 变动类型，1：积分，2：余额，3：平台币，4：成长值
     * @param isAdd            增减，1：增加，2：减少
     * @param modifyDesc       修改备注
     * @return
     */
    boolean modifyWallet(Long memberId, Integer gameId,Integer walletType, Number numValue, RechargeTypeEnum rechargeTypeEnum, Integer isAdd, String modifyDesc);

    /**
     * 钱包金额变动
     *
     * @param walletDto
     * @return
     */
    boolean modifyWallet(WalletDto walletDto);

    /**
     * 提取虚拟钱包金额到真实钱包
     *
     * @param idTypeDto
     * @return
     */
    R<Boolean> cashOutToWallet(IdTypeDto<Long, Integer> idTypeDto);

    /**
     * 获取sdk钱包菜单
     *
     * @param walletMenuType 钱包菜单，sdk_wallet_menu:真实钱包，sdk_virtual_wallet_menu
     * @return sdk钱包菜单
     */
    R getGamePayTypeList(String walletMenuType);

    /**
     * 获取提现按钮开关状态
     *
     * @param withdrawalType 提现按钮开关
     * @return 提现按钮开关状态
     */
    R getWithdrawalSwitch(String withdrawalType);

    /**
     * 查询钱包里可提现金额
     */
    WalletLogVo getBalanceTotal(Long memberId);

    /**
     * 获取当日提现次数
     */
    long getCashOutCount(Long memberId, Integer walletType, Integer walletOpType, Integer changeType);

    /**
     * 提现到微信
     *
     * @param dto
     * @return
     */
    R<String> cashOutToWX(CashOutDto dto);

}
