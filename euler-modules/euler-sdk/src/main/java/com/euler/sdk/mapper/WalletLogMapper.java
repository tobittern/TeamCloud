package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.WalletLog;
import com.euler.sdk.domain.vo.WalletLogVo;
import org.apache.ibatis.annotations.Param;

/**
 * 钱包变动记录Mapper接口
 *
 * @author euler
 * @date 2022-04-15
 */
public interface WalletLogMapper extends BaseMapperPlus<WalletLogMapper, WalletLog, WalletLogVo> {

    // 查询钱包里可提现金额
    WalletLogVo getBalanceTotal(@Param("memberId") Long memberId);
}
