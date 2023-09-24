package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.TdMeasurePageDto;
import com.euler.statistics.domain.entity.TdMeasure;
import com.euler.statistics.domain.vo.LineChartVo;
import com.euler.statistics.domain.vo.SummaryIndexVo;
import com.euler.statistics.domain.vo.TdMeasureVo;

import java.util.Collection;
import java.util.List;

/**
 * 指标维Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface ITdMeasureService extends IService<TdMeasure> {

    /**
     * 查询指标维
     *
     * @param id 指标维主键
     * @return 指标维
     */
    TdMeasureVo queryById(Integer id);

    /**
     * 查询指标维列表
     *
     * @param pageDto 指标维
     * @return 指标维集合
     */
    TableDataInfo<TdMeasureVo> queryPageList(TdMeasurePageDto pageDto);

    /**
     * 查询指标维列表
     *
     * @param pageDto 指标维
     * @return 指标维集合
     */
    List<TdMeasureVo> queryList(TdMeasurePageDto pageDto);

    /**
     * 获取首页统计数据
     *
     * @param queryDto
     * @return
     */
    SummaryIndexVo getSummaryIndex(SummaryQueryDto queryDto);

    /**
     * 获取首页统计折线数据
     *
     * @param queryDto
     * @return
     */
    List<LineChartVo> getIndexLineChart(SummaryQueryDto queryDto);


}
