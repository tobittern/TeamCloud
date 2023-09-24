package com.euler.risk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.entity.TfDeviceSummary;
import com.euler.risk.domain.vo.TfDeviceSummaryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备数据汇总Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface TfDeviceSummaryMapper extends BaseMapperPlus<TfDeviceSummaryMapper, TfDeviceSummary, TfDeviceSummaryVo> {


    void fillData(FillDataDto fillDataDto);

    /**
     * 查询设备行为异常预警列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<TfDeviceSummaryVo> selectDeviceSummaryList(@Param("page") Page<TfDeviceSummary> page, @Param(Constants.WRAPPER) Wrapper<TfDeviceSummary> queryWrapper);

}
