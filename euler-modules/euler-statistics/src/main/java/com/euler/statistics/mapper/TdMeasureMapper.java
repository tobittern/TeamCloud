package com.euler.statistics.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.TdMeasure;
import com.euler.statistics.domain.vo.TdMeasureVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 指标维Mapper接口
 *
 * @author euler
 * @date 2022-09-05
 */
@Mapper
public interface TdMeasureMapper extends BaseMapperPlus<TdMeasureMapper, TdMeasure, TdMeasureVo> {

}
