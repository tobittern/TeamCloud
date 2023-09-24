package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.PlatformTongjiDatas;
import com.euler.statistics.domain.vo.PlatformTongjiDatasVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Mapper接口
 *
 * @author euler
 * @date 2022-09-28
 */
@Mapper
public interface PlatformTongjiDatasMapper extends BaseMapperPlus<PlatformTongjiDatasMapper, PlatformTongjiDatas, PlatformTongjiDatasVo> {

    PlatformTongjiDatasVo selectSummaryForPlatformTongjiDatas(@Param(Constants.WRAPPER) Wrapper<PlatformTongjiDatas> queryWrapper);

}
