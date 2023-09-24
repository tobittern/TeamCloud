package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.ChargeStatisticsDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.ChargeStatistics;
import com.euler.statistics.domain.vo.ChargeStatisticsRateVo;
import com.euler.statistics.domain.vo.ChargeStatisticsVo;

import java.util.List;

/**
 * 开放平台充值金额统计Service接口
 *
 * @author euler
 *  2022-07-13
 */
public interface IChargeStatisticsService extends IService<ChargeStatistics> {

    /**
     * 查询开放平台充值金额统计列表
     *
     * @param dto 开放平台充值金额统计
     * @return 开放平台充值金额统计集合
     */
    TableDataInfo<ChargeStatisticsVo> queryChargeStatPageList(ChargeStatisticsDto dto );

    /**
     * 查询开放平台充值金额统计列表
     *
     * @param dto 开放平台充值金额统计
     * @return 开放平台充值金额统计集合
     */
    List<ChargeStatisticsVo> queryChargeStatList(ChargeStatisticsDto dto);

    /**
     * 开放平台充值金额的变化率详情
     */
    R queryChargeStatInfo(ChargeStatisticsDto dto);

    /**
     * 获取新注册充值数据
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> queryNewRegChargeForGroup(SummaryQueryDto queryDto);

    /**
     * 获取新增充值数据
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> queryNewIncChargeForGroup(SummaryQueryDto queryDto);

    /**
     * 获取新注册平均充值数据
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> queryNewRegAvgChargeForGroup(SummaryQueryDto queryDto);

    /**
     * 获取总充值数据
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> queryTotalChargeForGroup(SummaryQueryDto queryDto);

    /**
     * 根据用户id查询出审核过，在线的游戏列表
     */
    R getGameListByUserId(IdDto<Long> idDto);

    /**
     * 根据区服名称查询游戏区服列表
     */
    R getServerListByName(IdNameDto<Long> idNameDto);

}
