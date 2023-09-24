package com.euler.statistics.service;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.PaymentKeepStatisticsDto;
import com.euler.statistics.domain.vo.PaymentKeepStatisticsVo;

import java.util.List;

/**
 * 付费留存数据Service接口
 *
 * @author euler
 * @date 2022-10-10
 */
public interface IPaymentKeepStatisticsService {

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillPaymentKeepData(FillDataDto dto);

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计列表
     */
    TableDataInfo<PaymentKeepStatisticsVo> queryPaymentPageList(PaymentKeepStatisticsDto dto);

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计列表
     */
    List<PaymentKeepStatisticsVo> queryPaymentList(PaymentKeepStatisticsDto dto);
}
