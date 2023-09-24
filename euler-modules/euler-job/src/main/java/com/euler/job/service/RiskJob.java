package com.euler.job.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.risk.api.RemoteBehaviorService;
import com.euler.common.core.domain.dto.FillDataDto;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class RiskJob {
    @DubboReference
    private RemoteBehaviorService remoteBehaviorService;

    @XxlJob("deviceSummary")
    public void deviceSummary() {
        log.info("定时统计设备信息--执行开始");
        Date date = new Date();
        FillDataDto fillDataDto = new FillDataDto();
        fillDataDto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
        remoteBehaviorService.deviceSummary(fillDataDto);
        log.info("定时统计设备信息--执行结束");

    }


    @XxlJob("ipSummary")
    public void ipSummary() {
        log.info("定时统计IP信息--执行开始");
        Date date = new Date();
        FillDataDto fillDataDto = new FillDataDto();
        fillDataDto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));
        remoteBehaviorService.ipSummary(fillDataDto);
        log.info("定时统计IP信息--执行结束");
    }
}
