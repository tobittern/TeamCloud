package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.StatisticsChargeBo;
import com.euler.sdk.domain.dto.StatisticsChargeDto;
import com.euler.sdk.domain.entity.StatisticsCharge;
import com.euler.sdk.domain.vo.StatisticsChargeVo;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Service接口
 *
 * @author euler
 *  2022-07-06
 */
public interface IStatisticsChargeService extends IService<StatisticsCharge> {

    /**
     * 查询
     *
     * @param id 主键
     * @return 实体
     */
    StatisticsChargeVo queryById(Long id);

    /**
     * 查询列表
     *
     * @param bo 数据
     * @return 集合
     */
    TableDataInfo<StatisticsChargeVo> queryPageList(StatisticsChargeDto bo);

    /**
     * 查询列表
     *
     * @param bo 数据
     * @return 集合
     */
    List<StatisticsChargeVo> queryList(StatisticsChargeDto bo);

    /**
     * 修改
     *
     * @param bo 数据
     * @return 结果
     */
    Boolean insertByBo(StatisticsChargeBo bo);

    /**
     * 修改
     *
     * @param bo 数据
     * @return 结果
     */
    Boolean updateByBo(StatisticsChargeBo bo);

    /**
     * 校验并批量删除信息
     *
     * @param ids     需要删除的主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取管理后台汇总数据
     * @param date 查询日期
     */
    public void insertDataSummary(Date date);

    /**
     * 根据渠道id查询汇总数据
     * @param channelId 渠道id
     * @return 汇总数据
     */
    Object getStatisticsInfo(Integer channelId);

    /**
     * 初始化近30天数据
     * @return 返回结果
     */
    Object init30DayData();
}
