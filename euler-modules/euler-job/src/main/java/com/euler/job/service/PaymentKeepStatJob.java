package com.euler.job.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.statistics.api.RemotePaymentKeepStatService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author euler
 * @date 2022-10-10
 */
@Slf4j
@Component
public class PaymentKeepStatJob {
    @DubboReference
    private RemotePaymentKeepStatService paymentKeepStatService;

    @XxlJob("fillPaymentKeepData")
    public void fillPaymentKeepData() {
        log.info("定时统计付费留存数据--执行开始");
        Date date = new Date();
        FillDataDto dto = new FillDataDto();
        dto.setBatchNo(DateUtil.format(date, DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date)).setEndTime(DateUtil.endOfDay(date));

        paymentKeepStatService.fillPaymentKeepData(dto);
        log.info("定时统计付费留存数据--执行结束");

    }
}
