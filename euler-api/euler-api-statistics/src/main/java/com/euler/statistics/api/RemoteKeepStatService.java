package com.euler.statistics.api;

import com.euler.common.core.domain.dto.FillDataDto;

public interface RemoteKeepStatService {

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillKeepDataToSigle(FillDataDto fillKeepDataDto);

}
