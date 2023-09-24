package com.euler.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.api.domain.KeepDataStatistics;
import com.euler.statistics.domain.dto.KeepDataStatisticsDto;
import com.euler.statistics.domain.vo.KeepDataStatisticsVo;
import com.euler.statistics.domain.vo.KeepDataSummaryVo;
import com.euler.statistics.mapper.KeepDataStatisticsMapper;
import com.euler.statistics.service.IKeepDataStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 留存数据统计Service业务层处理
 *
 * @author euler
 * @date 2022-04-27
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KeepDataStatisticsServiceImpl implements IKeepDataStatisticsService {

    private final KeepDataStatisticsMapper baseMapper;

    /**
     * 查询留存数据统计列表
     *
     * @return 留存数据统计
     */
    @Override
    public TableDataInfo<KeepDataStatisticsVo> queryPageList(KeepDataStatisticsDto pageDto) {
        var lqw = buildQueryWrapper(pageDto);
        lqw.orderByDesc(KeepDataStatistics::getRegistTime);
        if (pageDto.getSingleFlag()) {
            lqw.groupBy(KeepDataStatistics::getRegistTime);
        } else {
            lqw.groupBy(KeepDataStatistics::getRegistTime, KeepDataStatistics::getChannelId, KeepDataStatistics::getPackageCode, KeepDataStatistics::getGameId, KeepDataStatistics::getOperationPlatform);
        }
        Page<KeepDataStatisticsVo> list = baseMapper.getKeepList(pageDto.build(), lqw);

        // 用于汇总的list
        List<KeepDataStatisticsVo> toSummaryList = baseMapper.getKeepList(lqw);
        // 留存数据汇总
        KeepDataSummaryVo summaryVo = new KeepDataSummaryVo();
        if (toSummaryList != null && toSummaryList.size() > 0) {
            toSummaryList.forEach(a -> {
                int newUserSum = summaryVo.getNewUserSum() + a.getNewUserNum();
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

                summaryVo.setNewUserSum(newUserSum);
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
                // 单日留存
                if (pageDto.getSingleFlag()) {
                    if (ObjectUtil.isEmpty(pageDto.getChannelId()) && ObjectUtil.isEmpty(pageDto.getChannelName())) {
                        a.setChannelName("all");
                    }
                    if (ObjectUtil.isEmpty(pageDto.getPackageCode())) {
                        a.setPackageCode("all");
                    }
                    if (ObjectUtil.isEmpty(pageDto.getGameId()) && ObjectUtil.isEmpty(pageDto.getGameName())) {
                        a.setGameName("all");
                    }
                    if (ObjectUtil.isEmpty(pageDto.getOperationPlatform())) {
                        a.setOperationPlatform("0");
                    }
                }
            });
        }

        // 按照新增用户倒序排列
        list.getRecords().stream().sorted(Comparator.comparing(KeepDataStatisticsVo::getNewUserNum).reversed()).collect(Collectors.toList());

        // 把汇总后的数据放到列表首位
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            KeepDataStatisticsVo firstKeepDataVo = new KeepDataStatisticsVo();
            firstKeepDataVo.setDateStr("汇总");
            firstKeepDataVo.setChannelName("all");
            firstKeepDataVo.setPackageCode("all");
            firstKeepDataVo.setGameName("all");
            firstKeepDataVo.setOperationPlatform("0");
            firstKeepDataVo.setNewUserNum(summaryVo.getNewUserSum());

            String remain2 = NumberUtil.round(Convert.toDouble(summaryVo.getDay2Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain3 = NumberUtil.round(Convert.toDouble(summaryVo.getDay3Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain4 = NumberUtil.round(Convert.toDouble(summaryVo.getDay4Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain5 = NumberUtil.round(Convert.toDouble(summaryVo.getDay5Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain6 = NumberUtil.round(Convert.toDouble(summaryVo.getDay6Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain7 = NumberUtil.round(Convert.toDouble(summaryVo.getDay7Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain15 = NumberUtil.round(Convert.toDouble(summaryVo.getDay15Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain30 = NumberUtil.round(Convert.toDouble(summaryVo.getDay30Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain60 = NumberUtil.round(Convert.toDouble(summaryVo.getDay60Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain90 = NumberUtil.round(Convert.toDouble(summaryVo.getDay90Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");

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

    /**
     * 查询列表
     *
     * @param dto
     * @return
     */
    public LambdaQueryWrapper<KeepDataStatistics> buildQueryWrapper(KeepDataStatisticsDto dto) {
        // 查询条件
        LambdaQueryWrapper<KeepDataStatistics> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotBlank(dto.getOperationPlatform()) && Convert.toInt(dto.getOperationPlatform(), 0) > 0, KeepDataStatistics::getOperationPlatform, dto.getOperationPlatform())
            .eq(dto.getGameId() != null && Convert.toInt(dto.getGameId(), 0) > 0, KeepDataStatistics::getGameId, dto.getGameId())
            .eq(StringUtils.isNotBlank(dto.getGameName()), KeepDataStatistics::getGameName, dto.getGameName())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), KeepDataStatistics::getPackageCode, dto.getPackageCode())
            .eq(dto.getChannelId() != null && Convert.toInt(dto.getChannelId(), 0) > 0, KeepDataStatistics::getChannelId, dto.getChannelId())
            .eq(StringUtils.isNotBlank(dto.getChannelName()), KeepDataStatistics::getChannelName, dto.getChannelName())
            .ge(StringUtils.isNotBlank(dto.getStartTime()), KeepDataStatistics::getRegistTime, DateUtils.getBeginOfDay(dto.getStartTime()))
            .le(StringUtils.isNotBlank(dto.getEndTime()), KeepDataStatistics::getRegistTime, DateUtils.getEndOfDay(dto.getEndTime()))
            .ne(KeepDataStatistics::getPackageCode, "default");
        wrapper.isNotNull(KeepDataStatistics::getPackageCode);
        return wrapper;
    }

    /**
     * 填充数据到同一个基础表
     */
    public void fillKeepData(FillDataDto dto) {
        //清除数据
        log.info("定时用户留存数据--清除数据--开始");

        var wrapper = new LambdaQueryWrapper<KeepDataStatistics>();
        wrapper.between(KeepDataStatistics::getRegistTime,dto.getBeginTime(), dto.getEndTime());
        wrapper.or(a -> a.between(KeepDataStatistics::getLoginTime, dto.getBeginTime(),dto.getEndTime()));
        Integer res = baseMapper.delete(wrapper);
        log.info("定时用户留存数据--清除数据--清除数据量：{}", res);

        log.info("定时用户留存数据--填充数据--开始");
        baseMapper.fillKeepData(dto);
        log.info("定时用户留存数据--填充数据--结束");
    }

    /**
     * 导出留存数据统计列表
     *
     * @return 留存数据统计
     */
    @Override
    public List<KeepDataStatisticsVo> queryList(KeepDataStatisticsDto dto) {
        var lqw = buildQueryWrapper(dto);
        lqw.orderByDesc(KeepDataStatistics::getRegistTime);
        if (dto.getSingleFlag()) {
            lqw.groupBy(KeepDataStatistics::getRegistTime);
        } else {
            lqw.groupBy(KeepDataStatistics::getRegistTime, KeepDataStatistics::getChannelId, KeepDataStatistics::getPackageCode, KeepDataStatistics::getGameId, KeepDataStatistics::getOperationPlatform);
        }
        List<KeepDataStatisticsVo> list = baseMapper.getKeepList(lqw);

        // 留存数据汇总
        KeepDataSummaryVo summaryVo = new KeepDataSummaryVo();
        if (list != null && list.size() > 0) {
            list.forEach(a -> {
                a.setDateStr(DateUtil.format(a.getDate(), "yyyy-MM-dd"));
                // 单日留存
                if (dto.getSingleFlag()) {
                    if (ObjectUtil.isEmpty(dto.getChannelId()) && ObjectUtil.isEmpty(dto.getChannelName())) {
                        a.setChannelName("all");
                    }
                    if (ObjectUtil.isEmpty(dto.getPackageCode())) {
                        a.setPackageCode("all");
                    }
                    if (ObjectUtil.isEmpty(dto.getGameId()) && ObjectUtil.isEmpty(dto.getGameName())) {
                        a.setGameName("all");
                    }
                    if (ObjectUtil.isEmpty(dto.getOperationPlatform())) {
                        a.setOperationPlatform("0");
                    }
                }
                int newUserSum = summaryVo.getNewUserSum() + a.getNewUserNum();
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

                summaryVo.setNewUserSum(newUserSum);
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
        list.stream().sorted(Comparator.comparing(KeepDataStatisticsVo::getNewUserNum).reversed()).collect(Collectors.toList());

        // 把汇总后的数据放到列表首位
        if (list != null && list.size() > 0) {
            KeepDataStatisticsVo firstKeepDataVo = new KeepDataStatisticsVo();
            firstKeepDataVo.setDateStr("汇总");
            firstKeepDataVo.setChannelName("all");
            firstKeepDataVo.setPackageCode("all");
            firstKeepDataVo.setGameName("all");
            firstKeepDataVo.setOperationPlatform("0");
            firstKeepDataVo.setNewUserNum(summaryVo.getNewUserSum());

            String remain2 = NumberUtil.round(Convert.toDouble(summaryVo.getDay2Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain3 = NumberUtil.round(Convert.toDouble(summaryVo.getDay3Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain4 = NumberUtil.round(Convert.toDouble(summaryVo.getDay4Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain5 = NumberUtil.round(Convert.toDouble(summaryVo.getDay5Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain6 = NumberUtil.round(Convert.toDouble(summaryVo.getDay6Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain7 = NumberUtil.round(Convert.toDouble(summaryVo.getDay7Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain15 = NumberUtil.round(Convert.toDouble(summaryVo.getDay15Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain30 = NumberUtil.round(Convert.toDouble(summaryVo.getDay30Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain60 = NumberUtil.round(Convert.toDouble(summaryVo.getDay60Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");
            String remain90 = NumberUtil.round(Convert.toDouble(summaryVo.getDay90Sum()) / Convert.toDouble(summaryVo.getNewUserSum()) * 100, 2).toString().concat("%");

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
