package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.ChargeStatistics;
import com.euler.statistics.domain.vo.ChargeStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开放平台充值金额统计Mapper接口
 *
 * @author euler
 *  2022-07-13
 */
public interface ChargeStatisticsMapper extends BaseMapperPlus<ChargeStatisticsMapper, ChargeStatistics, ChargeStatisticsVo> {

    /**
     * 获取开放平台充值金额统计列表
     */
    Page<ChargeStatisticsVo> getChargeStatPageList(@Param("page") Page<ChargeStatisticsVo> page, @Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    /**
     * 获取开放平台充值金额统计列表
     */
    List<ChargeStatisticsVo> getChargeStatList(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    /**
     * 获取新注册充值数据
     */
    List<SummaryResultDto> getNewRegChargeForGroup(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    /**
     * 获取新增充值数据
     */
    List<SummaryResultDto> getNewIncChargeForGroup(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    /**
     * 获取新注册人数
     */
    List<SummaryResultDto> getNewRegCountForGroup(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    /**
     * 获取总充值数据
     */
    List<SummaryResultDto> getTotalChargeForGroup(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

}
