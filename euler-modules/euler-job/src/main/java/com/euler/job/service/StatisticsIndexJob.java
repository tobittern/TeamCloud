package com.euler.job.service;

import com.euler.sdk.api.RemoteStatisticsService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatisticsIndexJob {

    @DubboReference
    private RemoteStatisticsService remoteStatisticsService;

    @XxlJob("dataSummary")
    public void dataSummary() {
        log.info("定时统计后台管理首页数据--执行开始");
        remoteStatisticsService.insertDataSummary(null);
        log.info("定时统计后台管理首页数据--执行结束");
    }
}
