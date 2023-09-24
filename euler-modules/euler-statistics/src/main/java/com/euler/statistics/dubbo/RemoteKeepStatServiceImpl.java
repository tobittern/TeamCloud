package com.euler.statistics.dubbo;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.statistics.api.RemoteKeepStatService;
import com.euler.statistics.service.IKeepDataStatisticsService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteKeepStatServiceImpl implements RemoteKeepStatService {

    @Autowired
    private IKeepDataStatisticsService keepStatService;

    /**
     * 搜集填充数据到同一个基础表
     */
    @Override
    public void fillKeepDataToSigle(FillDataDto fillKeepDataDto) {
        keepStatService.fillKeepData(fillKeepDataDto);
    }

}
