package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.StatisticsCharge;
import com.euler.sdk.domain.vo.StatisticsChargeVo;


/**
 * Mapper接口
 *
 * @author euler
 *  2022-07-06
 */
public interface StatisticsChargeMapper extends BaseMapperPlus<StatisticsChargeMapper, StatisticsCharge, StatisticsChargeVo> {

    public void deleteTrue(StatisticsCharge s);
}
