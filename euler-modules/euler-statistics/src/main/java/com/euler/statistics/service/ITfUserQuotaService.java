package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.TfUserQuotaPageDto;
import com.euler.statistics.domain.entity.TfUserQuota;
import com.euler.statistics.domain.vo.TfUserQuotaVo;

import java.util.List;

/**
 * 用户相关的标准指标统计Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface ITfUserQuotaService extends IService<TfUserQuota> {

    /**
     * 查询用户相关的标准指标统计
     *
     * @param id 用户相关的标准指标统计主键
     * @return 用户相关的标准指标统计
     */
    TfUserQuotaVo queryById(Integer id);

    /**
     * 查询用户相关的标准指标统计列表
     *
     * @param pageDto 用户相关的标准指标统计
     * @return 用户相关的标准指标统计集合
     */
    TableDataInfo<TfUserQuotaVo> queryPageList(TfUserQuotaPageDto pageDto);

    /**
     * 查询用户相关的标准指标统计列表
     *
     * @param pageDto 用户相关的标准指标统计
     * @return 用户相关的标准指标统计集合
     */
    List<TfUserQuotaVo> queryList(TfUserQuotaPageDto pageDto);

}
