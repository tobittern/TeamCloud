package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.entity.DouyinChannelAidDatas;
import com.euler.statistics.domain.vo.DouyinChannelAidDatasVo;
import org.apache.ibatis.annotations.Param;

/**
 * 平台渠道计划统计数据Mapper接口
 *
 * @author euler
 * @date 2022-07-12
 */
public interface DouyinChannelAidDatasMapper extends BaseMapperPlus<DouyinChannelAidDatasMapper, DouyinChannelAidDatas, DouyinChannelAidDatasVo> {

    DouyinChannelAidDatasVo selectSummarySimpleData(@Param(Constants.WRAPPER) Wrapper<DouyinChannelAidDatas> queryWrapper);
}
