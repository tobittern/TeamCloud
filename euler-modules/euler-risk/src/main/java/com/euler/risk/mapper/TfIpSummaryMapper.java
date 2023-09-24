package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.entity.TfIpSummary;
import com.euler.risk.domain.vo.TfIpSummaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ip汇总Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface TfIpSummaryMapper extends BaseMapperPlus<TfIpSummaryMapper, TfIpSummary, TfIpSummaryVo> {

    void fillData(FillDataDto fillDataDto);

    /**
     * 查询设备行为异常预警列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<TfIpSummaryVo> selectIpSummaryList(@Param("page") Page<TfIpSummary> page, @Param(Constants.WRAPPER) Wrapper<TfIpSummary> queryWrapper);

}
