package com.euler.sdk.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.dto.WalletLogPageDto;
import com.euler.sdk.domain.entity.WalletLog;
import com.euler.sdk.domain.vo.WalletLogVo;
import com.euler.sdk.mapper.WalletLogMapper;
import com.euler.sdk.service.IWalletLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-04-15
 */
@RequiredArgsConstructor
@Service
public class WalletLogServiceImpl extends ServiceImpl<WalletLogMapper, WalletLog> implements IWalletLogService {

    private final WalletLogMapper baseMapper;

    /**
     * 查询列表
     *
     * @param bo
     * @return
     */
    @Override
    public TableDataInfo<WalletLogVo> queryPageList(WalletLogPageDto bo) {
        LambdaQueryWrapper<WalletLog> lqw = buildQueryWrapper(bo);
        Page<WalletLogVo> result = baseMapper.selectVoPage(bo.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询列表
     *
     * @param bo
     * @return
     */
    @Override
    public List<WalletLogVo> queryList(WalletLogPageDto bo) {
        LambdaQueryWrapper<WalletLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WalletLog> buildQueryWrapper(WalletLogPageDto dto) {
        LambdaQueryWrapper<WalletLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, WalletLog::getMemberId, dto.getMemberId());
        lqw.eq(dto.getWalletType() != null, WalletLog::getWalletType, dto.getWalletType());
        lqw.eq(dto.getChangeType() != null, WalletLog::getChangeType, dto.getChangeType());
        lqw.eq(1 != dto.getWalletType() && dto.getGameId() != null, WalletLog::getGameId, dto.getGameId());

        lqw.orderByDesc(WalletLog::getCreateTime);
        return lqw;
    }


}
