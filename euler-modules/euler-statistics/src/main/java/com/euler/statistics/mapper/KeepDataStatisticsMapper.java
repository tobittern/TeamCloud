package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.api.domain.KeepDataStatistics;
import com.euler.statistics.domain.vo.KeepDataStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留存数据统计Mapper接口
 *
 * @author euler
 * @date 2022-04-27
 */
public interface KeepDataStatisticsMapper extends BaseMapperPlus<KeepDataStatisticsMapper, KeepDataStatistics, KeepDataStatisticsVo> {

    /**
     * 搜集填充数据到同一个基础表
     */
    void fillKeepData(FillDataDto dto);

    /**
     * 查询留存数据统计列表
     *
     * @return 留存数据统计列表
     */
    Page<KeepDataStatisticsVo> getKeepList(@Param("page") Page<KeepDataStatisticsVo> page, @Param(Constants.WRAPPER) Wrapper<KeepDataStatistics> queryWrapper);

    /**
     * 查询留存数据统计列表
     *
     * @return 留存数据统计列表
     */
    List<KeepDataStatisticsVo> getKeepList(@Param(Constants.WRAPPER) Wrapper<KeepDataStatistics> queryWrapper);

}
