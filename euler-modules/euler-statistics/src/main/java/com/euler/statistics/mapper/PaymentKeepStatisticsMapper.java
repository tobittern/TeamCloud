package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.PaymentKeepStatistics;
import com.euler.statistics.domain.vo.PaymentKeepStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 付费留存数据统计Mapper接口
 *
 * @author euler
 * @date 2022-10-10
 */
public interface PaymentKeepStatisticsMapper extends BaseMapperPlus<PaymentKeepStatisticsMapper, PaymentKeepStatistics, PaymentKeepStatisticsVo> {

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillPaymentKeepData(FillDataDto dto);

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计列表
     */
    Page<PaymentKeepStatisticsVo> getPaymentKeepList(@Param("page") Page<PaymentKeepStatisticsVo> page, @Param(Constants.WRAPPER) Wrapper<PaymentKeepStatistics> queryWrapper);

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计列表
     */
    List<PaymentKeepStatisticsVo> getPaymentKeepList(@Param(Constants.WRAPPER) Wrapper<PaymentKeepStatistics> queryWrapper);

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计列表
     */
    Page<PaymentKeepStatisticsVo> getNewRegistPaymentList(@Param("page") Page<PaymentKeepStatisticsVo> page, @Param(Constants.WRAPPER) Wrapper<PaymentKeepStatistics> queryWrapper);

    /**
     * 查询新增付费留存数据列表
     *
     * @return 新增付费留存数据列表
     */
    List<PaymentKeepStatisticsVo> getNewRegistPaymentList(@Param(Constants.WRAPPER) Wrapper<PaymentKeepStatistics> queryWrapper);

}
