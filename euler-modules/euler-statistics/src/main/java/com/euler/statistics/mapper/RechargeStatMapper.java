package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.RechargeStatPageDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充值数据统计Mapper接口
 *
 * @author euler
 * @date 2022-04-29
 */
public interface RechargeStatMapper extends BaseMapperPlus<RechargeStatMapper, RechargeStat, RechargeStatVo> {

    Page<DiyRechargeStatVo> getRechargeList(@Param("page") Page<DiyRechargeStatVo> page, @Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    DiyRechargeStatVo getRechargeSum(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    void fillRechargeData(FillDataDto fillRechargeDataDto);


    Page<DiyRoleRechargeStatVo> getRoleRechargeList(@Param("page") Page<DiyRoleRechargeStatVo> page, @Param(Constants.WRAPPER) Wrapper<RechargeStatPageDto> queryWrapper);

    DiyRoleRechargeStatVo getRoleRechargeSum(@Param(Constants.WRAPPER) Wrapper<RechargeStatPageDto> queryWrapper);


    DiyConsumptionRechargeStatVo getConsumptionRechargeSum(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    Page<DiyConsumptionRechargeStatVo> getConsumptionRechargeList(@Param("page") Page<DiyRechargeStatVo> page, @Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);


    SummaryResultDto selectSumAmount(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    List<SummaryResultDto> selectSumGroupAmount(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);


    SummaryResultDto selectOrderNum(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

    List<SummaryResultDto> selectOrderGroupNum(@Param(Constants.WRAPPER) Wrapper<RechargeStat> queryWrapper);

}
