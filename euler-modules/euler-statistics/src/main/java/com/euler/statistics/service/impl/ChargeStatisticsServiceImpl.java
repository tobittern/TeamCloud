package com.euler.statistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.sdk.api.RemoteMyGameService;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.ChargeStatisticsDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.ChargeStatistics;
import com.euler.statistics.domain.vo.ChargeStatisticsRateVo;
import com.euler.statistics.domain.vo.ChargeStatisticsVo;
import com.euler.statistics.domain.vo.GameInfoVo;
import com.euler.statistics.mapper.ChargeStatisticsMapper;
import com.euler.statistics.service.IChargeStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 开放平台充值金额统计Service业务层处理
 *
 * @author euler
 * 2022-07-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ChargeStatisticsServiceImpl extends ServiceImpl<ChargeStatisticsMapper, ChargeStatistics> implements IChargeStatisticsService {

    @Autowired
    private ChargeStatisticsMapper baseMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteMyGameService remoteMyGameService;

    /**
     * 查询开放平台充值金额统计列表
     *
     * @param dto 开放平台充值金额统计
     * @return 开放平台充值金额统计
     */
    @Override
    public TableDataInfo<ChargeStatisticsVo> queryChargeStatPageList(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = buildChargeStatQueryWrapper(dto);
        Page<ChargeStatisticsVo> list = baseMapper.getChargeStatPageList(dto.build(), lqw);
        // 获取新注册充值金额数据
        SummaryQueryDto queryDto = BeanUtil.toBean(dto, SummaryQueryDto.class);
        List<SummaryResultDto> result = queryNewRegChargeForGroup(queryDto);
        // 获取新注册人数
        List<SummaryResultDto> count = queryNewRegCountForGroup(queryDto);

        // 某日期之前的充值汇总
        AtomicReference<BigDecimal> totalBeforeSum = new AtomicReference<>(new BigDecimal(0));
        List<SummaryResultDto> totalBefore = queryBeforeTotalChargeForGroup(queryDto);
        for (SummaryResultDto tt : totalBefore) {
            totalBeforeSum.set(NumberUtil.add(totalBeforeSum.get(), Convert.toBigDecimal(tt.getSummaryValue())));
        }
        // 总充值
        List<SummaryResultDto> total = queryTotalChargeForGroup(queryDto);

        if (list != null && !list.getRecords().isEmpty()) {
            list.getRecords().forEach(a -> {
                String day = DateUtil.format(a.getDay(), "yyyy-MM-dd");
                // 新注册充值金额
                if (result != null && !result.isEmpty()) {
                    var regResult = result.stream().filter(b -> b.getSummaryKey().equals(day)).findFirst();
                    if (regResult.isPresent()) {
                        a.setNewRegisterCharge(Convert.toBigDecimal(regResult.get().getSummaryValue()));
                    }
                }
                // 新注册人数
                if (count != null && !count.isEmpty()) {
                    var countResult = count.stream().filter(c -> c.getSummaryKey().equals(day)).findFirst();
                    if (countResult.isPresent()) {
                        a.setNewRegisterCount(Convert.toInt(countResult.get().getSummaryValue()));
                    }
                }
                // 新注册平均充值 = 新注册充值金额/新注册人数(保留2位小数)
                if (a.getNewRegisterCount() > 0) {
                    BigDecimal avgCharge = NumberUtil.div(a.getNewRegisterCharge(), a.getNewRegisterCount(), 2);
                    a.setNewRegisterAvgCharge(avgCharge);
                } else {
                    a.setNewRegisterAvgCharge(BigDecimal.ZERO);
                }
                // 总充值
                if (total != null && !total.isEmpty()) {
                    var totalResult = total.stream().filter(d -> d.getSummaryKey().equals(day)).findFirst();
                    if (totalResult.isPresent()) {
                        totalBeforeSum.set(NumberUtil.add(totalBeforeSum.get(), Convert.toBigDecimal(totalResult.get().getSummaryValue())));
                    }
                }
                a.setTotalCharge(totalBeforeSum.get());
            });
        }
        return TableDataInfo.build(list);
    }

    /**
     * 查询开放平台充值金额统计列表
     *
     * @param dto 开放平台充值金额统计
     * @return 开放平台充值金额统计
     */
    @Override
    public List<ChargeStatisticsVo> queryChargeStatList(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = buildChargeStatQueryWrapper(dto);
        List<ChargeStatisticsVo> list = baseMapper.getChargeStatList(lqw);

        // 获取新注册充值金额数据
        SummaryQueryDto queryDto = BeanUtil.toBean(dto, SummaryQueryDto.class);
        List<SummaryResultDto> result = queryNewRegChargeForGroup(queryDto);
        // 获取新注册人数
        List<SummaryResultDto> count = queryNewRegCountForGroup(queryDto);

        // 某日期之前的充值汇总
        AtomicReference<BigDecimal> totalBeforeSum = new AtomicReference<>(new BigDecimal(0));
        List<SummaryResultDto> totalBefore = queryBeforeTotalChargeForGroup(queryDto);
        for (SummaryResultDto tt : totalBefore) {
            totalBeforeSum.set(NumberUtil.add(totalBeforeSum.get(), Convert.toBigDecimal(tt.getSummaryValue())));
        }
        // 总充值
        List<SummaryResultDto> total = queryTotalChargeForGroup(queryDto);

        if (list != null && !list.isEmpty()) {
            list.forEach(a -> {
                String day = DateUtil.format(a.getDay(), "yyyy-MM-dd");
                // 新注册充值金额
                if (result != null && !result.isEmpty()) {
                    var regResult = result.stream().filter(b -> b.getSummaryKey().equals(day)).findFirst();
                    if (regResult.isPresent()) {
                        a.setNewRegisterCharge(Convert.toBigDecimal(regResult.get().getSummaryValue()));
                    }
                }
                // 新注册人数
                if (count != null && !count.isEmpty()) {
                    var countResult = count.stream().filter(c -> c.getSummaryKey().equals(day)).findFirst();
                    if (countResult.isPresent()) {
                        a.setNewRegisterCount(Convert.toInt(countResult.get().getSummaryValue()));
                    }
                }
                // 新注册平均充值 = 新注册充值金额/新注册人数(保留2位小数)
                if (a.getNewRegisterCount() > 0) {
                    BigDecimal avgCharge = NumberUtil.div(a.getNewRegisterCharge(), a.getNewRegisterCount(), 2);
                    a.setNewRegisterAvgCharge(avgCharge);
                } else {
                    a.setNewRegisterAvgCharge(BigDecimal.ZERO);
                }
                // 总充值
                if (total != null && !total.isEmpty()) {
                    var totalResult = total.stream().filter(d -> d.getSummaryKey().equals(day)).findFirst();
                    if (totalResult.isPresent()) {
                        totalBeforeSum.set(NumberUtil.add(totalBeforeSum.get(), Convert.toBigDecimal(totalResult.get().getSummaryValue())));
                    }
                }
                a.setTotalCharge(totalBeforeSum.get());
            });
        }

        return list;
    }

    /**
     * 获取新增充值的检索条件
     *
     * @param dto
     * @return
     */
    private QueryWrapper<RechargeStat> buildChargeStatQueryWrapper(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = Wrappers.query();
        lqw.eq(dto.getGameId() != null, "game_id", dto.getGameId());
        lqw.like(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName());
        lqw.eq(StringUtils.isNotBlank(dto.getServerId()), "server_id", dto.getServerId());
        lqw.like(StringUtils.isNotBlank(dto.getServerName()), "server_name", dto.getServerName());
        lqw.ge(StringUtils.isNotBlank(dto.getBeginTime()), "date_id", DateUtils.getBeginOfDay(dto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), "date_id", DateUtils.getEndOfDay(dto.getEndTime()));
        // 排除default渠道
        lqw.gt("channel_id", 0);
        return lqw;
    }

    /**
     * 获取注册充值的检索条件
     *
     * @param dto
     * @return
     */
    private QueryWrapper<RechargeStat> buildChargeStatRegQueryWrapper(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = Wrappers.query();
        lqw.eq(dto.getGameId() != null, "game_id", dto.getGameId());
        lqw.like(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName());
        lqw.eq(StringUtils.isNotBlank(dto.getServerId()), "server_id", dto.getServerId());
        lqw.like(StringUtils.isNotBlank(dto.getServerName()), "server_name", dto.getServerName());
        lqw.ge(StringUtils.isNotBlank(dto.getBeginTime()), "date_id", DateUtils.getBeginOfDay(dto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), "date_id", DateUtils.getEndOfDay(dto.getEndTime()));
        lqw.ge(StringUtils.isNotBlank(dto.getBeginTime()), "member_date_id", DateUtils.getBeginOfDay(dto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), "member_date_id", DateUtils.getEndOfDay(dto.getEndTime()));
        // 排除default渠道
        lqw.gt("channel_id", 0);
        return lqw;
    }

    /**
     * 获取某日期之前的总充值的检索条件
     *
     * @param dto
     * @return
     */
    private QueryWrapper<RechargeStat> buildChargeStatBeforeTotalQueryWrapper(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = Wrappers.query();
        lqw.eq(dto.getGameId() != null, "game_id", dto.getGameId());
        lqw.like(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName());
        lqw.eq(StringUtils.isNotBlank(dto.getServerId()), "server_id", dto.getServerId());
        lqw.like(StringUtils.isNotBlank(dto.getServerName()), "server_name", dto.getServerName());
        lqw.lt(StringUtils.isNotBlank(dto.getBeginTime()), "date_id", DateUtils.getBeginOfDay(dto.getBeginTime()));
        // 排除default渠道
        lqw.gt("channel_id", 0);
        return lqw;
    }

    /**
     * 获取总充值的检索条件
     *
     * @param dto
     * @return
     */
    private QueryWrapper<RechargeStat> buildChargeStatTotalQueryWrapper(ChargeStatisticsDto dto) {
        QueryWrapper<RechargeStat> lqw = Wrappers.query();
        lqw.eq(dto.getGameId() != null, "game_id", dto.getGameId());
        lqw.like(StringUtils.isNotBlank(dto.getGameName()), "game_name", dto.getGameName());
        lqw.eq(StringUtils.isNotBlank(dto.getServerId()), "server_id", dto.getServerId());
        lqw.like(StringUtils.isNotBlank(dto.getServerName()), "server_name", dto.getServerName());
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), "date_id", DateUtils.getEndOfDay(dto.getEndTime()));
        // 排除default渠道
        lqw.gt("channel_id", 0);
        return lqw;
    }

    /**
     * 开放平台充值金额的变化率详情
     * （统计的是昨天0-24点的数据）
     */
    public R queryChargeStatInfo(ChargeStatisticsDto dto) {
        ChargeStatisticsRateVo vo = new ChargeStatisticsRateVo();
        if (ObjectUtil.isNull(dto.getGameId())) {
            return R.fail("游戏id不能为空");
        }
        // 昨天
        String yesterday = Convert.toStr(DateUtil.yesterday());
        dto.setBeginTime(yesterday);
        dto.setEndTime(yesterday);
        List<ChargeStatisticsVo> yesterdayList = queryChargeStatList(dto);
        Map<String, Object> sumMap = chargeStatSum(yesterdayList);
        vo.setDay(yesterday);
        vo.setServerId(dto.getServerId());
        vo.setNewIncreaseCharge(Convert.toStr(sumMap.get("newIncreaseCharge")));
        vo.setNewRegisterCharge(Convert.toStr(sumMap.get("newRegisterCharge")));
        vo.setNewRegisterAvgCharge(Convert.toStr(sumMap.get("newRegisterAvgCharge")));
        vo.setTotalCharge(Convert.toStr(sumMap.get("totalCharge")));

        // 前天，日期偏移
        String newDate = Convert.toStr(DateUtil.offsetDay(DateUtil.yesterday(), -1));
        dto.setBeginTime(newDate);
        dto.setEndTime(newDate);
        List<ChargeStatisticsVo> newDateList = queryChargeStatList(dto);
        // 计算日变化率
        Map<String, Object> dayMap = calChargeStat(yesterdayList, newDateList);
        // 设置日变化率
        vo.setDayNewIncCharge(Convert.toStr(dayMap.get("newIncreaseChargeResult")));
        vo.setDayNewIncChargeFlag(Convert.toStr(dayMap.get("newIncreaseChargeFlag")));
        vo.setDayNewRegCharge(Convert.toStr(dayMap.get("newRegisterChargeResult")));
        vo.setDayNewRegChargeFlag(Convert.toStr(dayMap.get("newRegisterChargeFlag")));
        vo.setDayNewRegAvgCharge(Convert.toStr(dayMap.get("newRegisterAvgChargeResult")));
        vo.setDayNewRegAvgChargeFlag(Convert.toStr(dayMap.get("newRegisterAvgChargeFlag")));
        vo.setDayTotalCharge(Convert.toStr(dayMap.get("totalChargeResult")));
        vo.setDayTotalChargeFlag(Convert.toStr(dayMap.get("totalChargeFlag")));

        // 上周
        String lastWeek = Convert.toStr(DateUtil.offsetDay(DateUtil.lastWeek(), -1));
        dto.setBeginTime(lastWeek);
        dto.setEndTime(yesterday);
        List<ChargeStatisticsVo> lastWeekList = queryChargeStatList(dto);

        String lastWeek2 = Convert.toStr(DateUtil.offsetDay(DateUtil.lastWeek(), -8));
        dto.setBeginTime(lastWeek2);
        dto.setEndTime(lastWeek);
        List<ChargeStatisticsVo> lastWeekList2 = queryChargeStatList(dto);
        // 计算周变化率
        Map<String, Object> weekMap = calChargeStat(lastWeekList, lastWeekList2);
        // 设置周变化率
        vo.setWeekNewIncCharge(Convert.toStr(weekMap.get("newIncreaseChargeResult")));
        vo.setWeekNewIncChargeFlag(Convert.toStr(weekMap.get("newIncreaseChargeFlag")));
        vo.setWeekNewRegCharge(Convert.toStr(weekMap.get("newRegisterChargeResult")));
        vo.setWeekNewRegChargeFlag(Convert.toStr(weekMap.get("newRegisterChargeFlag")));
        vo.setWeekNewRegAvgCharge(Convert.toStr(weekMap.get("newRegisterAvgChargeResult")));
        vo.setWeekNewRegAvgChargeFlag(Convert.toStr(weekMap.get("newRegisterAvgChargeFlag")));
        vo.setWeekTotalCharge(Convert.toStr(weekMap.get("totalChargeResult")));
        vo.setWeekTotalChargeFlag(Convert.toStr(weekMap.get("totalChargeFlag")));

        // 上个月
        String lastMonth = Convert.toStr(DateUtil.offsetDay(DateUtil.lastMonth(), -1));
        dto.setBeginTime(lastMonth);
        dto.setEndTime(yesterday);
        List<ChargeStatisticsVo> lastMonthList = queryChargeStatList(dto);

        String lastMonth2 = Convert.toStr(DateUtil.offsetDay(DateUtil.offsetMonth(DateUtil.lastMonth(), -1), -1));
        dto.setBeginTime(lastMonth2);
        dto.setEndTime(lastMonth);
        List<ChargeStatisticsVo> lastMonthList2 = queryChargeStatList(dto);

        // 计算月变化率
        Map<String, Object> monthMap = calChargeStat(lastMonthList, lastMonthList2);
        // 设置月变化率
        vo.setMonthNewIncCharge(Convert.toStr(monthMap.get("newIncreaseChargeResult")));
        vo.setMonthNewIncChargeFlag(Convert.toStr(monthMap.get("newIncreaseChargeFlag")));
        vo.setMonthNewRegCharge(Convert.toStr(monthMap.get("newRegisterChargeResult")));
        vo.setMonthNewRegChargeFlag(Convert.toStr(monthMap.get("newRegisterChargeFlag")));
        vo.setMonthNewRegAvgCharge(Convert.toStr(monthMap.get("newRegisterAvgChargeResult")));
        vo.setMonthNewRegAvgChargeFlag(Convert.toStr(monthMap.get("newRegisterAvgChargeFlag")));
        vo.setMonthTotalCharge(Convert.toStr(monthMap.get("totalChargeResult")));
        vo.setMonthTotalChargeFlag(Convert.toStr(monthMap.get("totalChargeFlag")));

        return R.ok(vo);
    }

    /**
     * 充值金额数据汇总
     *
     * @return
     */
    private Map<String, Object> chargeStatSum(List<ChargeStatisticsVo> list) {
        Map<String, Object> map = new HashMap<>();
        BigDecimal newIncreaseCharge = BigDecimal.ZERO;
        BigDecimal newRegisterCharge = BigDecimal.ZERO;
        BigDecimal newRegisterAvgCharge = BigDecimal.ZERO;
        BigDecimal totalCharge = BigDecimal.ZERO;
        for (ChargeStatisticsVo v : list) {
            if (ObjectUtil.isNull(v.getNewRegisterAvgCharge())) {
                v.setNewRegisterAvgCharge(Convert.toBigDecimal("0.00"));
            }
            if (ObjectUtil.isNotNull(v.getNewIncreaseCharge())) {
                newIncreaseCharge = newIncreaseCharge.add(v.getNewIncreaseCharge());
            }
            if (ObjectUtil.isNotNull(v.getNewRegisterCharge())) {
                newRegisterCharge = newRegisterCharge.add(v.getNewRegisterCharge());
            }
            if (ObjectUtil.isNotNull(v.getNewRegisterAvgCharge()) && v.getNewRegisterAvgCharge().compareTo(BigDecimal.ZERO) > 0) {
                newRegisterAvgCharge = newRegisterAvgCharge.add(v.getNewRegisterAvgCharge());
            }
            if (ObjectUtil.isNotNull(v.getTotalCharge())) {
                totalCharge = totalCharge.add(v.getTotalCharge());
            }
        }

        map.put("newIncreaseCharge", newIncreaseCharge);
        map.put("newRegisterCharge", newRegisterCharge);
        map.put("newRegisterAvgCharge", newRegisterAvgCharge);
        map.put("totalCharge", totalCharge);

        return map;
    }

    /**
     * 计算充值金额的变化率
     *
     * @param list     本日期的充值金额列表
     * @param lastList 上个日期的充值金额列表
     * @return
     */
    private Map<String, Object> calChargeStat(List<ChargeStatisticsVo> list, List<ChargeStatisticsVo> lastList) {
        Map<String, Object> map = new HashMap<>();
        // 新增充值
        Map<String, Object> newIncreaseMap = chargeRateCal(chargeStatSum(list).get("newIncreaseCharge"),
            chargeStatSum(lastList).get("newIncreaseCharge"));
        // 新注册充值
        Map<String, Object> newRegisterMap = chargeRateCal(chargeStatSum(list).get("newRegisterCharge"),
            chargeStatSum(lastList).get("newRegisterCharge"));
        // 新注册平均充值
        Map<String, Object> newRegisterAvgMap = chargeRateCal(chargeStatSum(list).get("newRegisterAvgCharge"),
            chargeStatSum(lastList).get("newRegisterAvgCharge"));
        // 总充值数据
        Map<String, Object> totalMap = chargeRateCal(chargeStatSum(list).get("totalCharge"),
            chargeStatSum(lastList).get("totalCharge"));

        //对上面数据进行封装
        map.put("newIncreaseChargeResult", newIncreaseMap.get("result"));
        map.put("newIncreaseChargeFlag", newIncreaseMap.get("flag"));
        map.put("newRegisterChargeResult", newRegisterMap.get("result"));
        map.put("newRegisterChargeFlag", newRegisterMap.get("flag"));
        map.put("newRegisterAvgChargeResult", newRegisterAvgMap.get("result"));
        map.put("newRegisterAvgChargeFlag", newRegisterAvgMap.get("flag"));
        map.put("totalChargeResult", totalMap.get("result"));
        map.put("totalChargeFlag", totalMap.get("flag"));

        return map;
    }

    /**
     * 金额增长率的计算
     */
    private Map<String, Object> chargeRateCal(Object charge, Object lastCharge) {
        Map<String, Object> map = new HashMap<>();
        BigDecimal bcharge = Convert.toBigDecimal(charge);
        BigDecimal bLastCharge = Convert.toBigDecimal(lastCharge);

        // 变化金额
        BigDecimal result = BigDecimal.ZERO;
        // 增长率标识
        String flag = "0";

        // 金额增长率的计算
        if (bcharge.compareTo(bLastCharge) > 0) {
            if (bLastCharge.compareTo(BigDecimal.ZERO) > 0) {
                flag = "1";
                result = NumberUtil.div(NumberUtil.sub(bcharge, bLastCharge), bLastCharge, 2);
            }
        } else if (bcharge.compareTo(bLastCharge) < 0) {
            flag = "-1";
            result = NumberUtil.div(NumberUtil.sub(bLastCharge, bcharge), bLastCharge, 2);
        }

        map.put("result", result.multiply(BigDecimal.valueOf(100L)) + "%");
        map.put("flag", flag);

        return map;
    }

    /**
     * 获取新注册充值数据
     *
     * @param queryDto
     * @return
     */
    public List<SummaryResultDto> queryNewRegChargeForGroup(SummaryQueryDto queryDto) {

        ChargeStatisticsDto dto = BeanUtil.toBean(queryDto, ChargeStatisticsDto.class);
        var wrapper = buildChargeStatRegQueryWrapper(dto);
        return baseMapper.getNewRegChargeForGroup(wrapper);
    }

    /**
     * 获取新增充值数据
     *
     * @param queryDto
     * @return
     */
    public List<SummaryResultDto> queryNewIncChargeForGroup(SummaryQueryDto queryDto) {

        ChargeStatisticsDto dto = BeanUtil.toBean(queryDto, ChargeStatisticsDto.class);
        var wrapper = buildChargeStatQueryWrapper(dto);
        return baseMapper.getNewIncChargeForGroup(wrapper);
    }

    /**
     * 获取新注册平均充值数据
     *
     * @param queryDto
     * @return
     */
    public List<SummaryResultDto> queryNewRegAvgChargeForGroup(SummaryQueryDto queryDto) {
        // 获取新注册充值金额数据
        List<SummaryResultDto> result = queryNewRegChargeForGroup(queryDto);
        // 获取新注册人数
        List<SummaryResultDto> count = queryNewRegCountForGroup(queryDto);

        if (result != null && !result.isEmpty() && count != null && !count.isEmpty()) {
            result.forEach(a -> {
                var resultDto = count.stream().filter(b -> b.getSummaryKey().equals(a.getSummaryKey())).findFirst();
                // 新注册平均充值 = 新注册充值金额/新注册人数(保留2位小数)
                if (resultDto.isPresent() && Convert.toInt(resultDto.get().getSummaryValue()) > 0) {
                    String avgCharge = Convert.toStr(NumberUtil.div(a.getSummaryValue(), resultDto.get().getSummaryValue(), 2));
                    a.setSummaryValue(avgCharge);
                } else {
                    a.setSummaryValue("0.00");
                }
            });
        }
        return result;
    }

    /**
     * 获取新注册人数
     */
    public List<SummaryResultDto> queryNewRegCountForGroup(SummaryQueryDto queryDto) {

        ChargeStatisticsDto dto = BeanUtil.toBean(queryDto, ChargeStatisticsDto.class);
        var wrapper = buildChargeStatRegQueryWrapper(dto);
        return baseMapper.getNewRegCountForGroup(wrapper);
    }

    /**
     * 获取某日期之前的总充值数据
     *
     * @param queryDto
     * @return
     */
    public List<SummaryResultDto> queryBeforeTotalChargeForGroup(SummaryQueryDto queryDto) {

        ChargeStatisticsDto dto = BeanUtil.toBean(queryDto, ChargeStatisticsDto.class);
        var wrapper = buildChargeStatBeforeTotalQueryWrapper(dto);
        return baseMapper.getTotalChargeForGroup(wrapper);
    }

    /**
     * 获取总充值数据
     *
     * @param queryDto
     * @return
     */
    public List<SummaryResultDto> queryTotalChargeForGroup(SummaryQueryDto queryDto) {

        ChargeStatisticsDto dto = BeanUtil.toBean(queryDto, ChargeStatisticsDto.class);
        var wrapper = buildChargeStatTotalQueryWrapper(dto);
        return baseMapper.getTotalChargeForGroup(wrapper);
    }

    /**
     * 根据用户id查询出审核过，在线的游戏列表
     */
    public R getGameListByUserId(IdDto<Long> idDto) {

        List<OpenGameVo> list = remoteGameManagerService.getGameListByUserId(idDto);
        var resList = JsonHelper.copyList(list, GameInfoVo.class);
        return R.ok(resList);
    }

    /**
     * 根据区服名称查询游戏区服列表
     */
    public R getServerListByName(IdNameDto<Long> idNameDto) {

        List<GameUserManagementVo> list = remoteMyGameService.getServerListByName(idNameDto);
        return R.ok(list);
    }

}
