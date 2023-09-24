package com.euler.sdk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.api.domain.CancellationLog;

import java.util.List;

/**
 * 提现规则Service接口
 *
 * @author euler
 * @date 2022-05-26
 */
public interface ICancellationLogService extends IService<CancellationLog> {

    /**
     * 获取待执行列表
     *
     * @return
     */
    List<CancellationLog> getOpList();
}
