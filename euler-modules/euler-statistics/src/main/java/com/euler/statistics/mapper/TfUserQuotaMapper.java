package com.euler.statistics.mapper;

import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.TfUserQuota;
import com.euler.statistics.domain.vo.TfUserQuotaVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户相关的标准指标统计Mapper接口
 *
 * @author euler
 * @date 2022-09-05
 */
@Mapper
public interface TfUserQuotaMapper extends BaseMapperPlus<TfUserQuotaMapper, TfUserQuota, TfUserQuotaVo> {

}
