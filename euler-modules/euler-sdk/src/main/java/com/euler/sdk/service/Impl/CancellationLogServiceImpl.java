package com.euler.sdk.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.sdk.api.domain.CancellationLog;
import com.euler.sdk.mapper.CancellationLogMapper;
import com.euler.sdk.service.ICancellationLogService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class CancellationLogServiceImpl extends ServiceImpl<CancellationLogMapper, CancellationLog> implements ICancellationLogService {
    @Autowired
    private CancellationLogMapper baseMapper;


    /**
     * 获取待执行列表
     *
     * @return
     */
    @Override
    public List<CancellationLog> getOpList() {
        Date date = DateUtil.offsetDay(new Date(), -3);
        var lqw = Wrappers.<CancellationLog>lambdaQuery().eq(CancellationLog::getStatus, 0).lt(CancellationLog::getOpNums,3).lt(CancellationLog::getCreateTime, date);


        return list(lqw);
    }
}
