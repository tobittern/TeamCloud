package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.RechargeStatPageDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.vo.DiyConsumptionRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRoleRechargeStatVo;

import java.util.List;

/**
 * 充值数据Service接口
 *
 * @author euler
 * @date 2022-04-29
 */
public interface IRechargeStatService extends IService<RechargeStat> {


    /**
     * 营收数据
     *
     * @param rechargeStat
     * @return
     */
    TableDataInfo<DiyRechargeStatVo> queryPageList(RechargeStatPageDto rechargeStat);

    TableDataInfo<DiyRoleRechargeStatVo> queryRolePageList(RechargeStatPageDto pageDto);

    /**
     * 搜集填充数据到同一个基础表
     *
     * @param fillDataDto
     */
    void fillRechargeData(FillDataDto fillDataDto);

    /**
     * 游戏充值数据
     *
     * @param rechargeStat
     * @return
     */
    TableDataInfo<DiyConsumptionRechargeStatVo> queryConsumptionPageList(RechargeStatPageDto rechargeStat);


    /**
     * 获取订单数量
     *
     * @param queryDto
     * @return
     */
    SummaryResultDto getIncOrderNum(SummaryQueryDto queryDto);

    /**
     * 获取订单金额
     *
     * @param queryDto
     * @return
     */
    SummaryResultDto getIncOrderAmount(SummaryQueryDto queryDto);


    /**
     * 获取订单数量
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> getIncOrderGroupNum(SummaryQueryDto queryDto);

    /**
     * 获取订单金额
     *
     * @param queryDto
     * @return
     */
    List<SummaryResultDto> getIncOrderGroupAmount(SummaryQueryDto queryDto);


}
