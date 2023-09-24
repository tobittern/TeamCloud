package com.euler.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.RechargeStatPageDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.vo.DiyConsumptionRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRoleRechargeStatVo;
import com.euler.statistics.mapper.GameUserManagementMapper;
import com.euler.statistics.mapper.RechargeStatMapper;
import com.euler.statistics.service.IRechargeStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-04-29
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RechargeStatServiceImpl extends ServiceImpl<RechargeStatMapper, RechargeStat> implements IRechargeStatService {

    private final RechargeStatMapper baseMapper;
    private final GameUserManagementMapper gameUserManagementMapper;


    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public TableDataInfo<DiyRechargeStatVo> queryPageList(RechargeStatPageDto pageDto) {
        pageDto.setDataType("1");
        var lqw = buildQueryWrapper(pageDto);

        var rechargeSum = baseMapper.getRechargeSum(lqw);

        lqw.orderByDesc(RechargeStat::getDateId).orderByDesc(RechargeStat::getId);
        lqw.groupBy(RechargeStat::getMemberId, RechargeStat::getChannelId, RechargeStat::getChannelPackageCode, RechargeStat::getGameId, RechargeStat::getDateId, RechargeStat::getOperationPlatform);
        Page<DiyRechargeStatVo> result = baseMapper.getRechargeList(pageDto.build(), lqw);

        if (rechargeSum != null && result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().add(0, rechargeSum);
        }
        return TableDataInfo.build(result);
    }


    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public TableDataInfo<DiyRoleRechargeStatVo> queryRolePageList(RechargeStatPageDto pageDto) {
        var lqw = buildRoleQueryWrapper(pageDto);
        var rechargeSum = baseMapper.getRoleRechargeSum(lqw);
        lqw.orderByDesc("gu.create_time");

        lqw.groupBy("gu.member_id", "gu.game_id", "gu.role_id", "gu.server_id", "g.operation_platform", "gu.channel_id", "gu.package_code");
        Page<DiyRoleRechargeStatVo> result = baseMapper.getRoleRechargeList(pageDto.build(), lqw);


        if (rechargeSum != null && result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().add(0, rechargeSum);
        }
        return TableDataInfo.build(result);

    }


    /**
     * 搜集填充数据到同一个基础表
     *
     * @param fillDataDto
     */
    @Override
    public void fillRechargeData(FillDataDto fillDataDto) {
        //清除数据
        log.info("定时统计充值数据--清除数据--开始");

        var wrapper = new LambdaQueryWrapper<RechargeStat>();
        wrapper.eq(RechargeStat::getDataType, "1").between(RechargeStat::getDateId, fillDataDto.getBeginTime(), fillDataDto.getEndTime());
        Integer res = baseMapper.delete(wrapper);
        log.info("定时统计充值数据--清除数据--清除数据量：{}", res);

        log.info("定时统计充值数据--填充数据--开始");
        baseMapper.fillRechargeData(fillDataDto);
        log.info("定时统计充值数据--填充数据--结束");

    }

    private QueryWrapper<RechargeStatPageDto> buildRoleQueryWrapper(RechargeStatPageDto pageDto) {
        QueryWrapper<RechargeStatPageDto> lqw = Wrappers.query();

        lqw.eq(Convert.toInt(pageDto.getOperationPlatform(), 0) > 0, "g.operation_platform", pageDto.getOperationPlatform());
        lqw.eq(StringUtils.isNotBlank(pageDto.getChannelName()), "gu.channel_name", pageDto.getChannelName());
        lqw.eq(Convert.toInt(pageDto.getChannelId(), 0) > 0, "gu.channel_id", pageDto.getChannelId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getChannelPackageCode()), "gu.package_code", pageDto.getChannelPackageCode());
        lqw.eq(StringUtils.isNotBlank(pageDto.getServerId()), "gu.server_id", pageDto.getServerId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getRoleId()), "gu.role_id", pageDto.getRoleId());

        lqw.likeRight(StringUtils.isNotBlank(pageDto.getServerName()), "gu.server_name", pageDto.getServerName());
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getRoleName()), "gu.role_name", pageDto.getRoleName());


        lqw.eq(Convert.toInt(pageDto.getGameId(), 0) > 0, "gu.game_id", pageDto.getGameId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getGameName()), "gu.game_name", pageDto.getGameName());
        lqw.eq(Convert.toLong(pageDto.getMemberId(), 0L) > 0L, "gu.member_id", pageDto.getMemberId());


        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginUserDateId()), "gu.create_time", DateUtils.getBeginOfDay(pageDto.getBeginUserDateId()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndUserDateId()), "gu.create_time", DateUtils.getEndOfDay(pageDto.getEndUserDateId()));
        return lqw;
    }


    private LambdaQueryWrapper<RechargeStat> buildQueryWrapper(RechargeStatPageDto bo) {
        LambdaQueryWrapper<RechargeStat> lqw = Wrappers.lambdaQuery();
        lqw.eq(RechargeStat::getDataType, bo.getDataType());
        lqw.eq(Convert.toInt(bo.getOperationPlatform(), 0) > 0, RechargeStat::getOperationPlatform, bo.getOperationPlatform());
        lqw.eq(StringUtils.isNotBlank(bo.getChannelName()), RechargeStat::getChannelName, bo.getChannelName());
        lqw.eq(Convert.toInt(bo.getChannelId(), 0) > 0, RechargeStat::getChannelId, bo.getChannelId());
        lqw.eq(StringUtils.isNotBlank(bo.getChannelPackageCode()), RechargeStat::getChannelPackageCode, bo.getChannelPackageCode());
        lqw.eq(StringUtils.isNotBlank(bo.getServerId()), RechargeStat::getServerId, bo.getServerId());
        lqw.eq(StringUtils.isNotBlank(bo.getRoleId()), RechargeStat::getRoleId, bo.getRoleId());

        lqw.likeRight(StringUtils.isNotBlank(bo.getServerName()), RechargeStat::getServerName, bo.getServerName());
        lqw.likeRight(StringUtils.isNotBlank(bo.getRoleName()), RechargeStat::getRoleName, bo.getRoleName());

        lqw.eq(StringUtils.isNotBlank(bo.getPayChannel()), RechargeStat::getPayChannel, bo.getPayChannel());
        lqw.gt(RechargeStat::getChannelId, 0);

        lqw.eq(Convert.toInt(bo.getGameId(), 0) > 0, RechargeStat::getGameId, bo.getGameId());
        lqw.eq(StringUtils.isNotBlank(bo.getGameName()), RechargeStat::getGameName, bo.getGameName());
        lqw.eq(Convert.toLong(bo.getMemberId(), 0L) > 0L, RechargeStat::getMemberId, bo.getMemberId());

        lqw.ge(StringUtils.isNotBlank(bo.getBeginDateId()), RechargeStat::getDateId, DateUtils.getBeginOfDay(bo.getBeginDateId()));
        lqw.le(StringUtils.isNotBlank(bo.getEndDateId()), RechargeStat::getDateId, DateUtils.getEndOfDay(bo.getEndDateId()));

        lqw.ge(StringUtils.isNotBlank(bo.getBeginUserDateId()), RechargeStat::getUserDateId, DateUtils.getBeginOfDay(bo.getBeginUserDateId()));
        lqw.le(StringUtils.isNotBlank(bo.getEndUserDateId()), RechargeStat::getUserDateId, DateUtils.getEndOfDay(bo.getEndUserDateId()));
        return lqw;
    }


    /**
     * 游戏充值数据
     *
     * @return
     */
    @Override
    public TableDataInfo<DiyConsumptionRechargeStatVo> queryConsumptionPageList(RechargeStatPageDto pageDto) {
        pageDto.setDataType("1");
        var lqw = buildQueryWrapper(pageDto);
        // 强制设置成为游戏消费数据
        lqw.eq(RechargeStat::getOrderType, "G");

        var rechargeSum = baseMapper.getConsumptionRechargeSum(lqw);

        lqw.orderByDesc(RechargeStat::getDateId).orderByDesc(RechargeStat::getId);
        lqw.groupBy(RechargeStat::getMemberId, RechargeStat::getChannelId, RechargeStat::getChannelPackageCode, RechargeStat::getGameId, RechargeStat::getDateId, RechargeStat::getOperationPlatform);
        Page<DiyConsumptionRechargeStatVo> result = baseMapper.getConsumptionRechargeList(pageDto.build(), lqw);

        if (rechargeSum != null && result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().add(0, rechargeSum);
        }
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<RechargeStat> buildSummaryQuery(SummaryQueryDto queryDto) {
        var wrapper = Wrappers.<RechargeStat>lambdaQuery()
            .eq(queryDto.getChannelId() != null, RechargeStat::getChannelId, queryDto.getChannelId())
            .eq(StringUtils.isNotBlank(queryDto.getChannelName()), RechargeStat::getChannelName, queryDto.getChannelName())
            .eq(queryDto.getGameId() != null, RechargeStat::getGameId, queryDto.getGameId())
            .eq(StringUtils.isNotBlank(queryDto.getPackageCode()), RechargeStat::getChannelPackageCode, queryDto.getPackageCode())
            .ge(StringUtils.isNotBlank(queryDto.getBeginTime()), RechargeStat::getDateId, DateUtils.getBeginOfDay(queryDto.getBeginTime()))
            .le(StringUtils.isNotBlank(queryDto.getEndTime()), RechargeStat::getDateId, DateUtils.getEndOfDay(queryDto.getEndTime()));
        return wrapper;
    }

    /**
     * 获取订单数量
     *
     * @param queryDto
     * @return
     */
    @Override
    public SummaryResultDto getIncOrderNum(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        SummaryResultDto sumAmount = baseMapper.selectOrderNum(wrapper);
        return sumAmount;
    }

    /**
     * 获取订单金额
     *
     * @param queryDto
     * @return
     */
    @Override
    public SummaryResultDto getIncOrderAmount(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        SummaryResultDto sumAmount = baseMapper.selectSumAmount(wrapper);
        return sumAmount;
    }



    /**
     * 获取订单数量
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<SummaryResultDto> getIncOrderGroupNum(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        var sumAmount = baseMapper.selectOrderGroupNum(wrapper);
        return sumAmount;
    }

    /**
     * 获取订单金额
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<SummaryResultDto> getIncOrderGroupAmount(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        var sumAmount = baseMapper.selectSumGroupAmount(wrapper);
        return sumAmount;
    }


}
