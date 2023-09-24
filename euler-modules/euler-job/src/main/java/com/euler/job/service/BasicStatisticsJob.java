package com.euler.job.service;


import com.euler.statistics.api.RemoteRechargeStatService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BasicStatisticsJob {

    @DubboReference
    private RemoteRechargeStatService rechargeStatService;

    @XxlJob("fillBasicStatistics")
    public void fillBasicStatistics() {
        rechargeStatService.fillBasicStatistics();
    }
}
