package com.euler.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.PaymentKeepStatisticsDto;
import com.euler.statistics.domain.entity.PaymentKeepStatistics;
import com.euler.statistics.domain.vo.PaymentKeepStatisticsVo;
import com.euler.statistics.domain.vo.PaymentKeepSummaryVo;
import com.euler.statistics.mapper.PaymentKeepStatisticsMapper;
import com.euler.statistics.service.IPaymentKeepStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 付费留存数据统计Service业务层处理
 *
 * @author euler
 * @date 2022-10-10
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentKeepStatisticsServiceImpl implements IPaymentKeepStatisticsService {

    private final PaymentKeepStatisticsMapper baseMapper;

    @Override
    public void fillPaymentKeepData(FillDataDto dto) {
        //清除数据
        log.info("定时付费留存数据--清除数据--开始");

        var wrapper = new LambdaQueryWrapper<PaymentKeepStatistics>();
        wrapper.between(PaymentKeepStatistics::getPaymentDate, dto.getBeginTime(), dto.getEndTime());
        wrapper.or(a -> a.between(PaymentKeepStatistics::getLoginDate, dto.getBeginTime(), dto.getEndTime()));
        Integer res = baseMapper.delete(wrapper);
        log.info("定时付费留存数据--清除数据--清除数据量：{}", res);

        log.info("定时付费留存数据--填充数据--开始");
        baseMapper.fillPaymentKeepData(dto);
        log.info("定时付费留存数据--填充数据--结束");
    }

    /**
     * 查询付费留存数据统计列表
     *
     * @return 付费留存数据统计
     */
    @Override
    public TableDataInfo<PaymentKeepStatisticsVo> queryPaymentPageList(PaymentKeepStatisticsDto dto) {
        var lqw = buildPaymentQueryWrapper(dto);
        // 付费留存数据汇总
        PaymentKeepSummaryVo summaryVo = new PaymentKeepSummaryVo();
        Page<PaymentKeepStatisticsVo> list = new Page<>();
        // 用于汇总的list
        List<PaymentKeepStatisticsVo> toSummaryList = new ArrayList<>();
        // 新增付费留存判断
        if (Convert.toInt(dto.getPaymentKeepFlag(), 0) > 0) {
            list = baseMapper.getNewRegistPaymentList(dto.build(), lqw);
            toSummaryList = baseMapper.getNewRegistPaymentList(lqw);
        } else {
            list = baseMapper.getPaymentKeepList(dto.build(), lqw);
            toSummaryList = baseMapper.getPaymentKeepList(lqw);
        }

        if (toSummaryList != null && toSummaryList.size() > 0) {
            toSummaryList.forEach(a -> {
                int paymentUserSum = summaryVo.getPaymentUserSum() + a.getPaymentUserNum();
                long day2 = summaryVo.getDay2Sum() + a.getDay2();
                long day3 = summaryVo.getDay3Sum() + a.getDay3();
                long day4 = summaryVo.getDay4Sum() + a.getDay4();
                long day5 = summaryVo.getDay5Sum() + a.getDay5();
                long day6 = summaryVo.getDay6Sum() + a.getDay6();
                long day7 = summaryVo.getDay7Sum() + a.getDay7();
                long day15 = summaryVo.getDay15Sum() + a.getDay15();
                long day30 = summaryVo.getDay30Sum() + a.getDay30();
                long day60 = summaryVo.getDay60Sum() + a.getDay60();
                long day90 = summaryVo.getDay90Sum() + a.getDay90();

                summaryVo.setPaymentUserSum(paymentUserSum);
                summaryVo.setDay2Sum(day2);
                summaryVo.setDay3Sum(day3);
                summaryVo.setDay4Sum(day4);
                summaryVo.setDay5Sum(day5);
                summaryVo.setDay6Sum(day6);
                summaryVo.setDay7Sum(day7);
                summaryVo.setDay15Sum(day15);
                summaryVo.setDay30Sum(day30);
                summaryVo.setDay60Sum(day60);
                summaryVo.setDay90Sum(day90);
            });
        }

        // 日期展示
        if (list != null && list.getSize() > 0) {
            list.getRecords().forEach(a -> {
                a.setDateStr(DateUtil.format(a.getDate(), "yyyy-MM-dd"));
                if (Convert.toInt(dto.getPaymentKeepFlag(), 0) > 0 && Convert.toInt(a.getPaymentUserNum(), 0) == 0) {
                    a.setRemain2("0.00%");
                    a.setRemain3("0.00%");
                    a.setRemain4("0.00%");
                    a.setRemain5("0.00%");
                    a.setRemain6("0.00%");
                    a.setRemain7("0.00%");
                    a.setRemain15("0.00%");
                    a.setRemain30("0.00%");
                    a.setRemain60("0.00%");
                    a.setRemain90("0.00%");
                }
            });
        }

        // 按照付费用户倒序排列
        list.getRecords().stream().sorted(Comparator.comparing(PaymentKeepStatisticsVo::getPaymentUserNum).reversed()).collect(Collectors.toList());

        // 把汇总后的数据放到列表首位
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            PaymentKeepStatisticsVo firstKeepDataVo = new PaymentKeepStatisticsVo();
            firstKeepDataVo.setDateStr("汇总");
            firstKeepDataVo.setChannelName("all");
            firstKeepDataVo.setPackageCode("all");
            firstKeepDataVo.setGameName("all");
            firstKeepDataVo.setOperationPlatform("0");
            firstKeepDataVo.setPaymentUserNum(summaryVo.getPaymentUserSum());

            String remain2 = NumberUtil.round(Convert.toDouble(summaryVo.getDay2Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain3 = NumberUtil.round(Convert.toDouble(summaryVo.getDay3Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain4 = NumberUtil.round(Convert.toDouble(summaryVo.getDay4Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain5 = NumberUtil.round(Convert.toDouble(summaryVo.getDay5Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain6 = NumberUtil.round(Convert.toDouble(summaryVo.getDay6Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain7 = NumberUtil.round(Convert.toDouble(summaryVo.getDay7Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain15 = NumberUtil.round(Convert.toDouble(summaryVo.getDay15Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain30 = NumberUtil.round(Convert.toDouble(summaryVo.getDay30Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain60 = NumberUtil.round(Convert.toDouble(summaryVo.getDay60Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain90 = NumberUtil.round(Convert.toDouble(summaryVo.getDay90Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");

            firstKeepDataVo.setDay2(summaryVo.getDay2Sum());
            firstKeepDataVo.setDay3(summaryVo.getDay3Sum());
            firstKeepDataVo.setDay4(summaryVo.getDay4Sum());
            firstKeepDataVo.setDay5(summaryVo.getDay5Sum());
            firstKeepDataVo.setDay6(summaryVo.getDay6Sum());
            firstKeepDataVo.setDay7(summaryVo.getDay7Sum());
            firstKeepDataVo.setDay15(summaryVo.getDay15Sum());
            firstKeepDataVo.setDay30(summaryVo.getDay30Sum());
            firstKeepDataVo.setDay60(summaryVo.getDay60Sum());
            firstKeepDataVo.setDay90(summaryVo.getDay90Sum());

            firstKeepDataVo.setRemain2(remain2);
            firstKeepDataVo.setRemain3(remain3);
            firstKeepDataVo.setRemain4(remain4);
            firstKeepDataVo.setRemain5(remain5);
            firstKeepDataVo.setRemain6(remain6);
            firstKeepDataVo.setRemain7(remain7);
            firstKeepDataVo.setRemain15(remain15);
            firstKeepDataVo.setRemain30(remain30);
            firstKeepDataVo.setRemain60(remain60);
            firstKeepDataVo.setRemain90(remain90);
            // 把汇总后的数据放到列表首位
            list.getRecords().add(0, firstKeepDataVo);
            list.setTotal(list.getTotal() + 1);
        }
        return TableDataInfo.build(list);
    }

    public LambdaQueryWrapper<PaymentKeepStatistics> buildPaymentQueryWrapper(PaymentKeepStatisticsDto dto) {
        LambdaQueryWrapper<PaymentKeepStatistics> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(dto.getOperationPlatform()) && Convert.toInt(dto.getOperationPlatform(), 0) > 0, PaymentKeepStatistics::getOperationPlatform, dto.getOperationPlatform())
            .eq(dto.getGameId() != null && Convert.toInt(dto.getGameId(), 0) > 0, PaymentKeepStatistics::getGameId, dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), PaymentKeepStatistics::getGameName, dto.getGameName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), PaymentKeepStatistics::getPackageCode, dto.getPackageCode())
            .eq(dto.getChannelId() != null && Convert.toInt(dto.getChannelId(), 0) > 0, PaymentKeepStatistics::getChannelId, dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), PaymentKeepStatistics::getChannelName, dto.getChannelName())
            .ge(StringUtils.isNotBlank(dto.getStartTime()), PaymentKeepStatistics::getPaymentDate, DateUtils.getBeginOfDay(dto.getStartTime()))
            .le(StringUtils.isNotBlank(dto.getEndTime()), PaymentKeepStatistics::getPaymentDate, DateUtils.getEndOfDay(dto.getEndTime()))
            .ne(PaymentKeepStatistics::getPackageCode, "default");
        wrapper.isNotNull(PaymentKeepStatistics::getPackageCode);
        wrapper.orderByDesc(PaymentKeepStatistics::getPaymentDate);
        wrapper.groupBy(PaymentKeepStatistics::getPaymentDate, PaymentKeepStatistics::getChannelId, PaymentKeepStatistics::getPackageCode, PaymentKeepStatistics::getGameId, PaymentKeepStatistics::getOperationPlatform);
        return wrapper;
    }

    /**
     * 导出付费留存数据统计列表
     *
     * @return 付费留存数据统计
     */
    @Override
    public List<PaymentKeepStatisticsVo> queryPaymentList(PaymentKeepStatisticsDto dto) {
        var lqw = buildPaymentQueryWrapper(dto);
        // 付费留存数据汇总
        PaymentKeepSummaryVo summaryVo = new PaymentKeepSummaryVo();
        List<PaymentKeepStatisticsVo> list = new ArrayList<>();
        // 新增付费留存判断
        if (Convert.toInt(dto.getPaymentKeepFlag(), 0) > 0) {
            list = baseMapper.getNewRegistPaymentList(lqw);
        } else {
            list = baseMapper.getPaymentKeepList(lqw);
        }
        if (list != null && list.size() > 0) {
            list.forEach(a -> {
                a.setDateStr(DateUtil.format(a.getDate(), "yyyy-MM-dd"));
                if (Convert.toInt(dto.getPaymentKeepFlag(), 0) > 0 && Convert.toInt(a.getPaymentUserNum(), 0) == 0) {
                    a.setRemain2("0.00%");
                    a.setRemain3("0.00%");
                    a.setRemain4("0.00%");
                    a.setRemain5("0.00%");
                    a.setRemain6("0.00%");
                    a.setRemain7("0.00%");
                    a.setRemain15("0.00%");
                    a.setRemain30("0.00%");
                    a.setRemain60("0.00%");
                    a.setRemain90("0.00%");
                }
                int paymentUserSum = summaryVo.getPaymentUserSum() + a.getPaymentUserNum();
                long day2 = summaryVo.getDay2Sum() + a.getDay2();
                long day3 = summaryVo.getDay3Sum() + a.getDay3();
                long day4 = summaryVo.getDay4Sum() + a.getDay4();
                long day5 = summaryVo.getDay5Sum() + a.getDay5();
                long day6 = summaryVo.getDay6Sum() + a.getDay6();
                long day7 = summaryVo.getDay7Sum() + a.getDay7();
                long day15 = summaryVo.getDay15Sum() + a.getDay15();
                long day30 = summaryVo.getDay30Sum() + a.getDay30();
                long day60 = summaryVo.getDay60Sum() + a.getDay60();
                long day90 = summaryVo.getDay90Sum() + a.getDay90();

                summaryVo.setPaymentUserSum(paymentUserSum);
                summaryVo.setDay2Sum(day2);
                summaryVo.setDay3Sum(day3);
                summaryVo.setDay4Sum(day4);
                summaryVo.setDay5Sum(day5);
                summaryVo.setDay6Sum(day6);
                summaryVo.setDay7Sum(day7);
                summaryVo.setDay15Sum(day15);
                summaryVo.setDay30Sum(day30);
                summaryVo.setDay60Sum(day60);
                summaryVo.setDay90Sum(day90);
            });
        }

        // 按照新增用户倒序排列
        list.stream().sorted(Comparator.comparing(PaymentKeepStatisticsVo::getPaymentUserNum).reversed()).collect(Collectors.toList());

        // 把汇总后的数据放到列表首位
        if (list != null && list.size() > 0) {
            PaymentKeepStatisticsVo firstKeepDataVo = new PaymentKeepStatisticsVo();
            firstKeepDataVo.setDateStr("汇总");
            firstKeepDataVo.setChannelName("all");
            firstKeepDataVo.setPackageCode("all");
            firstKeepDataVo.setGameName("all");
            firstKeepDataVo.setOperationPlatform("0");
            firstKeepDataVo.setPaymentUserNum(summaryVo.getPaymentUserSum());

            String remain2 = NumberUtil.round(Convert.toDouble(summaryVo.getDay2Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain3 = NumberUtil.round(Convert.toDouble(summaryVo.getDay3Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain4 = NumberUtil.round(Convert.toDouble(summaryVo.getDay4Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain5 = NumberUtil.round(Convert.toDouble(summaryVo.getDay5Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain6 = NumberUtil.round(Convert.toDouble(summaryVo.getDay6Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain7 = NumberUtil.round(Convert.toDouble(summaryVo.getDay7Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain15 = NumberUtil.round(Convert.toDouble(summaryVo.getDay15Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain30 = NumberUtil.round(Convert.toDouble(summaryVo.getDay30Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain60 = NumberUtil.round(Convert.toDouble(summaryVo.getDay60Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");
            String remain90 = NumberUtil.round(Convert.toDouble(summaryVo.getDay90Sum()) / Convert.toDouble(summaryVo.getPaymentUserSum()) * 100, 2).toString().concat("%");

            firstKeepDataVo.setRemain2(remain2);
            firstKeepDataVo.setRemain3(remain3);
            firstKeepDataVo.setRemain4(remain4);
            firstKeepDataVo.setRemain5(remain5);
            firstKeepDataVo.setRemain6(remain6);
            firstKeepDataVo.setRemain7(remain7);
            firstKeepDataVo.setRemain15(remain15);
            firstKeepDataVo.setRemain30(remain30);
            firstKeepDataVo.setRemain60(remain60);
            firstKeepDataVo.setRemain90(remain90);
            // 把汇总后的数据放到列表首位
            list.add(0, firstKeepDataVo);
        }
        return list;
    }

}

