package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.dto.WalletLogPageDto;
import com.euler.sdk.domain.entity.WalletLog;
import com.euler.sdk.domain.vo.WalletLogVo;

import java.util.List;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-04-15
 */
public interface IWalletLogService extends IService<WalletLog> {



    /**
     * 查询列表
     *
     * @param walletLog
     * @return 集合
     */
    TableDataInfo<WalletLogVo> queryPageList(WalletLogPageDto walletLog);

    /**
     * 查询列表
     *
     * @param walletLog
     * @return 集合
     */
    List<WalletLogVo> queryList(WalletLogPageDto walletLog);


}
