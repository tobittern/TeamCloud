package com.euler.statistics.service;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.KeepDataStatisticsDto;
import com.euler.statistics.domain.vo.KeepDataStatisticsVo;

import java.util.List;

/**
 * 留存数据统计Service接口
 *
 * @author euler
 * @date 2022-04-27
 */
public interface IKeepDataStatisticsService {

    /**
     * 查询留存数据统计列表
     *
     * @return 留存数据统计列表
     */
    TableDataInfo<KeepDataStatisticsVo> queryPageList(KeepDataStatisticsDto dto);

    /**
     * 查询留存数据统计列表
     *
     * @return 留存数据统计列表
     */
    List<KeepDataStatisticsVo> queryList(KeepDataStatisticsDto dto);

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillKeepData(FillDataDto dto);

}
