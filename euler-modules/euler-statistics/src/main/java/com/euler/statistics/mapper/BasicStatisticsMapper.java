package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.BasicStatistics;
import com.euler.statistics.domain.vo.BasicStatisticsVo;
import com.euler.statistics.domain.vo.GameUserBaseDataVo;
import com.euler.statistics.domain.vo.UserRegisterBaseDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据统计 - 每日的ltv基础数据统计Mapper接口
 *
 * @author euler
 * @date 2022-04-27
 */
@Mapper
public interface BasicStatisticsMapper extends BaseMapperPlus<BasicStatisticsMapper, BasicStatistics, BasicStatisticsVo> {

    List<IdNameDto<Integer>> selectBasisGameListByName(@Param(Constants.WRAPPER) Wrapper<BasicStatistics> queryWrapper);

    List<GameUserBaseDataVo> selectPointTimeList(@Param(Constants.WRAPPER) Wrapper<GameUserBaseDataVo> queryWrapper);

//    SearchDataSummaryVo selectSearchOtherDayDataSummary(@Param(Constants.WRAPPER) Wrapper<SearchDataSummaryVo> queryWrapper);

    IdDto<Long> selectBasisForRegisterByCount(@Param(Constants.WRAPPER) Wrapper<BasicStatistics> queryWrapper);

    IdDto<Long> selectBasisForGameUserManagerByCount(@Param(Constants.WRAPPER) Wrapper<BasicStatistics> queryWrapper);

    Page<UserRegisterBaseDataVo> selectBasisForRegister(@Param("page") Page<UserRegisterBaseDataVo> page, @Param(Constants.WRAPPER) Wrapper<BasicStatistics> queryWrapper);

    Page<UserRegisterBaseDataVo> selectBasisForGameUserManagement(@Param("page") Page<UserRegisterBaseDataVo> page, @Param(Constants.WRAPPER) Wrapper<BasicStatistics> queryWrapper);


}
