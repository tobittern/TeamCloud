package com.euler.statistics.dubbo;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.statistics.api.RemotePaymentKeepStatService;
import com.euler.statistics.service.IPaymentKeepStatisticsService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemotePaymentKeepStatServiceImpl implements RemotePaymentKeepStatService {

    @Autowired
    private IPaymentKeepStatisticsService iPaymentKeepStatService;

    /**
     * 搜集填充数据到同一个基础表
     */
    @Override
    public void fillPaymentKeepData(FillDataDto dto) {
        iPaymentKeepStatService.fillPaymentKeepData(dto);
    }

}
