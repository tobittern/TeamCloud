package com.euler.statistics.api;

import com.euler.common.core.domain.dto.FillDataDto;

/**
 * @author euler
 * @date 2022-06-01
 */
public interface RemotePaymentKeepStatService {
    /**
     * 搜集填充数据到同一个基础表
     */
    void fillPaymentKeepData(FillDataDto fillKeepDataDto);
}
