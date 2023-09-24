package com.euler.statistics.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.constant.MeasureConstants;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.dto.TdMeasurePageDto;
import com.euler.statistics.domain.entity.TdMeasure;
import com.euler.statistics.domain.vo.LineChartVo;
import com.euler.statistics.domain.vo.SummaryIndexVo;
import com.euler.statistics.domain.vo.TdMeasureVo;
import com.euler.statistics.mapper.TdMeasureMapper;
import com.euler.statistics.service.*;
import com.euler.statistics.utils.StatisticsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 指标维Service业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TdMeasureServiceImpl extends ServiceImpl<TdMeasureMapper, TdMeasure> implements ITdMeasureService {

    @Autowired
    private TdMeasureMapper baseMapper;
    @Autowired
    private IMemberProfileService memberProfileService;
    @Autowired
    private IRechargeStatService rechargeStatService;
    @Autowired
    private IChargeStatisticsService chargeStatService;
    @Autowired
    private IOnlineUserService onlineUserService;

    /**
     * 查询指标维
     *
     * @param id 指标维主键
     * @return 指标维
     */
    @Override
    public TdMeasureVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询指标维列表
     *
     * @param pageDto 指标维
     * @return 指标维
     */
    @Override
    public TableDataInfo<TdMeasureVo> queryPageList(TdMeasurePageDto pageDto) {
        LambdaQueryWrapper<TdMeasure> lqw = buildQueryWrapper(pageDto);
        Page<TdMeasureVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询指标维列表
     *
     * @param pageDto 指标维
     * @return 指标维
     */
    @Override
    public List<TdMeasureVo> queryList(TdMeasurePageDto pageDto) {
        LambdaQueryWrapper<TdMeasure> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TdMeasure> buildQueryWrapper(TdMeasurePageDto pageDto) {
        LambdaQueryWrapper<TdMeasure> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getModelId()), TdMeasure::getModelId, pageDto.getModelId());
        lqw.like(StringUtils.isNotBlank(pageDto.getModelName()), TdMeasure::getModelName, pageDto.getModelName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getMeasureId()), TdMeasure::getMeasureId, pageDto.getMeasureId());
        lqw.like(StringUtils.isNotBlank(pageDto.getMeasureName()), TdMeasure::getMeasureName, pageDto.getMeasureName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getMeasureDesc()), TdMeasure::getMeasureDesc, pageDto.getMeasureDesc());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), TdMeasure::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), TdMeasure::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 获取首页统计数据
     *
     * @param queryDto
     * @return
     */
    public SummaryIndexVo getSummaryIndex(SummaryQueryDto queryDto) {
        SummaryIndexVo summaryIndexVo = new SummaryIndexVo();

        try {
            //查询总数
            SummaryResultDto incMember = memberProfileService.getIncMember(queryDto);
            summaryIndexVo.setTotalUserNum(incMember.getSummaryValue());
            SummaryResultDto incOrderNum = rechargeStatService.getIncOrderNum(queryDto);
            summaryIndexVo.setTotalOrderNum(incOrderNum.getSummaryValue());
            SummaryResultDto incOrderAmount = rechargeStatService.getIncOrderAmount(queryDto);
            summaryIndexVo.setTotalOrderAmount(incOrderAmount.getSummaryValue());

            Date today = new Date();
            //今日数据
            queryDto.setBeginTime(DateUtils.getBeginOfDay(today)).setEndTime(DateUtils.getEndOfDay(today));
            SummaryResultDto tincMember = memberProfileService.getIncMember(queryDto);
            summaryIndexVo.setTodayIncUserNum(tincMember.getSummaryValue());
            SummaryResultDto tincOrderNum = rechargeStatService.getIncOrderNum(queryDto);
            summaryIndexVo.setTodayIncOrderNum(tincOrderNum.getSummaryValue());
            SummaryResultDto tincOrderAmount = rechargeStatService.getIncOrderAmount(queryDto);
            summaryIndexVo.setTodayIncOrderAmount(tincOrderAmount.getSummaryValue());
            SummaryResultDto tincOnlineUser = onlineUserService.getIncOnlineUserNum(queryDto);
            summaryIndexVo.setTodayOnlineUserNum(tincOnlineUser.getSummaryValue());


            //查询昨日数据
            Date yesterday = DateUtil.offsetDay(today, -1);
            queryDto.setBeginTime(DateUtils.getBeginOfDay(yesterday)).setEndTime(DateUtils.getEndOfDay(yesterday));
            SummaryResultDto yincMember = memberProfileService.getIncMember(queryDto);
            summaryIndexVo.setYesterdayIncUserNum(yincMember.getSummaryValue());
            SummaryResultDto yincOrderNum = rechargeStatService.getIncOrderNum(queryDto);
            summaryIndexVo.setYesterdayIncOrderNum(yincOrderNum.getSummaryValue());
            SummaryResultDto yincOrderAmount = rechargeStatService.getIncOrderAmount(queryDto);
            summaryIndexVo.setYesterdayIncOrderAmount(yincOrderAmount.getSummaryValue());
            SummaryResultDto yincOnlineUser = onlineUserService.getIncOnlineUserNum(queryDto);
            summaryIndexVo.setYesterdayOnlineUserNum(yincOnlineUser.getSummaryValue());

        } catch (Exception e) {
            log.error("获取首页统计数据--异常，{}", JsonHelper.toJson(queryDto));
        }
        return summaryIndexVo;

    }

    /**
     * 获取首页统计折线数据
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<LineChartVo> getIndexLineChart(SummaryQueryDto queryDto) {
        List<SummaryResultDto> summaryResultDtos = null;
        String defaultVal = "0";
        String measureUnit="";
        switch (queryDto.getMeasureId()) {
            case MeasureConstants.MEASUREID_USER_DAY:
                summaryResultDtos = memberProfileService.getIncMemberGroup(queryDto);
                measureUnit="人";
                break;
            case MeasureConstants.MEASUREID_ONLINE_USER:
                summaryResultDtos = onlineUserService.getIncOnlineUserGroupNum(queryDto);
                measureUnit="人";
                break;
            case MeasureConstants.MEASUREID_ORDER_DAY:
                summaryResultDtos = rechargeStatService.getIncOrderGroupNum(queryDto);
                measureUnit="笔";
                break;
            case MeasureConstants.MEASUREID_RECHARGE_DAY:
                summaryResultDtos = rechargeStatService.getIncOrderGroupAmount(queryDto);
                defaultVal = "0.00";
                measureUnit="元";
                break;
            case MeasureConstants.MEASUREID_INCREASE_CHARGE:
                summaryResultDtos = chargeStatService.queryNewIncChargeForGroup(queryDto);
                defaultVal = "0.00";
                measureUnit="元";
                break;
            case MeasureConstants.MEASUREID_REGISTER_CHARGE:
                summaryResultDtos = chargeStatService.queryNewRegChargeForGroup(queryDto);
                defaultVal = "0.00";
                measureUnit="元";
                break;
            case MeasureConstants.MEASUREID_REGISTER_AVG_CHARGE:
                summaryResultDtos = chargeStatService.queryNewRegAvgChargeForGroup(queryDto);
                defaultVal = "0.00";
                measureUnit="元";
                break;
        }
        if (summaryResultDtos == null)
            summaryResultDtos = new ArrayList<>();
        List<LineChartVo> lineChartVos = new ArrayList<>();

        if (MeasureConstants.MEASUREID_ONLINE_USER.equals(queryDto.getMeasureId())) {
            var timeList = StatisticsUtils.getTimeList();

            for (var currentDate : timeList) {
                var summaryResultDto = summaryResultDtos.stream().filter(a -> a.getSummaryKey().equals(currentDate.getKey())).findFirst();
                LineChartVo lineChartVo = new LineChartVo();
                lineChartVo.setMeasureId(queryDto.getMeasureId());
                lineChartVo.setDateId(currentDate.getValue());
                lineChartVo.setMeasureUnit(measureUnit);
                lineChartVos.add(lineChartVo);
                if (summaryResultDto.isPresent())
                    lineChartVo.setMeasureValue(summaryResultDto.get().getSummaryValue());
                else
                    lineChartVo.setMeasureValue(defaultVal);
            }

        } else {
            var dateList = StatisticsUtils.getDateList(queryDto.getBeginTime(), queryDto.getEndTime());
            for (var currentDate : dateList) {
                var summaryResultDto = summaryResultDtos.stream().filter(a -> a.getSummaryKey().equals(currentDate.getKey())).findFirst();
                LineChartVo lineChartVo = new LineChartVo();
                lineChartVo.setMeasureId(queryDto.getMeasureId());
                lineChartVo.setDateId(currentDate.getValue());
                lineChartVo.setMeasureUnit(measureUnit);
                lineChartVos.add(lineChartVo);
                if (summaryResultDto.isPresent())
                    lineChartVo.setMeasureValue(summaryResultDto.get().getSummaryValue());
                else
                    lineChartVo.setMeasureValue(defaultVal);
            }
        }
        //计算增长率
        List<LineChartVo> newLineChartVos = new ArrayList<>();
        for (int i = 0; i < lineChartVos.size(); i++) {
            var k1 = lineChartVos.get(i);
            //处理从第二天开始的数据
            if (i > 0) {
                //前一天数据
                var k2 = lineChartVos.get(i - 1);
                var k1Value = new BigDecimal(k1.getMeasureValue());
                var k2Value = new BigDecimal(k2.getMeasureValue());
                k1.setChangeFlag(k1Value.compareTo(k2Value));
                var subtract = k1Value.subtract(k2Value).abs();

                if (BigDecimal.ZERO.compareTo(k2Value) == -1) {
                    k1.setChangeValue(subtract.divide(k2Value, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).toString() + "%");
                } else {
                    k1.setChangeValue("");
                }
            }
            newLineChartVos.add(k1);

        }


        return newLineChartVos;
    }


}
