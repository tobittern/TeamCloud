package com.euler.statistics.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.constant.MeasureConstants;
import com.euler.statistics.domain.dto.TfUserQuotaPageDto;
import com.euler.statistics.domain.entity.TfUserQuota;
import com.euler.statistics.domain.vo.TfUserQuotaVo;
import com.euler.statistics.mapper.TfUserQuotaMapper;
import com.euler.statistics.service.ITfUserQuotaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户相关的标准指标统计Service业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TfUserQuotaServiceImpl extends ServiceImpl<TfUserQuotaMapper, TfUserQuota> implements ITfUserQuotaService {

    @Autowired
    private TfUserQuotaMapper baseMapper;

    /**
     * 查询用户相关的标准指标统计
     *
     * @param id 用户相关的标准指标统计主键
     * @return 用户相关的标准指标统计
     */
    @Override
    public TfUserQuotaVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询用户相关的标准指标统计列表
     *
     * @param pageDto 用户相关的标准指标统计
     * @return 用户相关的标准指标统计
     */
    @Override
    public TableDataInfo<TfUserQuotaVo> queryPageList(TfUserQuotaPageDto pageDto) {
        LambdaQueryWrapper<TfUserQuota> lqw = buildQueryWrapper(pageDto);
        Page<TfUserQuotaVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询用户相关的标准指标统计列表
     *
     * @param pageDto 用户相关的标准指标统计
     * @return 用户相关的标准指标统计
     */
    @Override
    public List<TfUserQuotaVo> queryList(TfUserQuotaPageDto pageDto) {
        LambdaQueryWrapper<TfUserQuota> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TfUserQuota> buildQueryWrapper(TfUserQuotaPageDto pageDto) {
        LambdaQueryWrapper<TfUserQuota> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getDateId()), TfUserQuota::getDateId, pageDto.getDateId());
        lqw.eq(pageDto.getChannelId() != null, TfUserQuota::getChannelId, pageDto.getChannelId());
        lqw.like(StringUtils.isNotBlank(pageDto.getChannelName()), TfUserQuota::getChannelName, pageDto.getChannelName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getMeasureId()), TfUserQuota::getMeasureId, pageDto.getMeasureId());
        lqw.eq(pageDto.getMeasureValue() != null, TfUserQuota::getMeasureValue, pageDto.getMeasureValue());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), TfUserQuota::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), TfUserQuota::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }


//    /**
//     * 定时统计用户数据
//     */
//    @Override
//    public void fillData(FillDataDto fillDataDto) {
//        //清除数据
//        log.info("定时统计用户数据--清除数据--开始");
//
//        var wrapper = new LambdaQueryWrapper<TfUserQuota>();
//        wrapper.eq(TfUserQuota::getMeasureId, MeasureConstants.MEASUREID_USER_DAY).between(TfUserQuota::getDateId, DateUtil.parse(fillDataDto.getBeginTime()).toDateStr(), DateUtil.parse(fillDataDto.getEndTime()).toDateStr());
//        Integer res = baseMapper.delete(wrapper);
//        log.info("定时统计用户数据--清除数据--清除数据量：{}", res);
//
//        log.info("定时统计用户数据--填充数据--开始");
//        baseMapper.fillData(fillDataDto);
//        log.info("定时统计用户数据--填充数据--结束");
//
//    }


}
