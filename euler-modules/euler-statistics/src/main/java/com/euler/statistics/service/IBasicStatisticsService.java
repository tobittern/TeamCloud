package com.euler.statistics.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.statistics.domain.dto.BasicStatisticsDto;
import com.euler.statistics.domain.vo.BasicStatisticsReturnDataVo;
import com.euler.statistics.domain.vo.BasicStatisticsReturnVo;
import com.euler.statistics.domain.vo.UserRegisterBaseReturnDataVo;

import java.util.List;

/**
 * 数据统计 - 每日的ltv基础数据统计Service接口
 *
 * @author euler
 * @date 2022-04-27
 */
public interface IBasicStatisticsService {

    R getGameListByName(IdNameTypeDicDto dto);


    List<BasicStatisticsReturnVo> Summary(BasicStatisticsDto dto, Integer type);


    BasicStatisticsReturnDataVo summaryList(BasicStatisticsDto dto);


    BasicStatisticsReturnDataVo queryPageList(BasicStatisticsDto dto, Integer type);


    void getDataIntoMysql();


    UserRegisterBaseReturnDataVo registerList(BasicStatisticsDto dto);


}
