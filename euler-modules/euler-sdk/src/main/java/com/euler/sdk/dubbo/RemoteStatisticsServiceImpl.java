package com.euler.sdk.dubbo;

import com.euler.sdk.api.RemoteStatisticsService;
import com.euler.sdk.service.IStatisticsChargeService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteStatisticsServiceImpl implements RemoteStatisticsService {

    @Resource
    private IStatisticsChargeService iStatisticsChargeService;

    @Override
    public void insertDataSummary(Date date) {
        iStatisticsChargeService.insertDataSummary(date);
    }
}
