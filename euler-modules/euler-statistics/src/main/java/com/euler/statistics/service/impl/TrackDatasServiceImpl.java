package com.euler.statistics.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.statistics.domain.dto.*;
import com.euler.statistics.domain.entity.*;
import com.euler.statistics.domain.vo.*;
import com.euler.statistics.mapper.*;
import com.euler.statistics.service.ITrackDatasService;
import com.euler.statistics.utils.StatisticsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 平台汇总统计数据Service业务层处理
 *
 * @author euler
 * @date 2022-07-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
@DS("trackdatas")
public class TrackDatasServiceImpl implements ITrackDatasService {

    private final PlatformDatasMapper baseMapper;
    @Autowired
    private DouyinChannelAidDatasMapper douyinChannelAidDatasMapper;
    @Autowired
    private DouyinChannelDatasMapper douyinChannelDatasMapper;
    @Autowired
    private PlatformTongjiDatasMapper platformTongjiDatasMapper;
    @Autowired
    private PlatformTongjiChannelAidDatasMapper platformTongjiChannelAidDatasMapper;

    /**
     * 查询平台汇总统计数据列表
     *
     * @return 平台汇总统计数据
     */
    @Override
    @DS("trackdatas")
    public TableDataInfo<PlatformDatasVo> queryPageList(PlatformDatasDto bo) {
        LambdaQueryWrapper<PlatformDatas> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(bo.getGameName()), PlatformDatas::getGameName, bo.getGameName());
        lqw.eq(bo.getBdUsers() != null, PlatformDatas::getBdUsers, bo.getBdUsers());
        lqw.eq(bo.getPtUsers() != null, PlatformDatas::getPtUsers, bo.getPtUsers());
        lqw.eq(bo.getBdPayUsers() != null, PlatformDatas::getBdPayUsers, bo.getBdPayUsers());
        lqw.eq(bo.getPtPayUsers() != null, PlatformDatas::getPtPayUsers, bo.getPtPayUsers());
        lqw.eq(bo.getBdTotalAmount() != null, PlatformDatas::getBdTotalAmount, bo.getBdTotalAmount());
        lqw.eq(bo.getPtTotalAmount() != null, PlatformDatas::getPtTotalAmount, bo.getPtTotalAmount());
        lqw.eq(bo.getPtNewRoles() != null, PlatformDatas::getPtNewRoles, bo.getPtNewRoles());
        lqw.eq(bo.getBdNewRoles() != null, PlatformDatas::getBdNewRoles, bo.getBdNewRoles());
        if (bo.getStartDate() != null && bo.getEndDate() != null) {
            lqw.between(PlatformDatas::getCreateDate, bo.getStartDate(), bo.getEndDate());
        }
        lqw.orderByDesc(PlatformDatas::getId);
        Page<PlatformDatasVo> result = baseMapper.selectVoPage(bo.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    @Override
    @DS("trackdatas")
    public TableDataInfo<DouyinChannelAidDatasVo> douyinChannelAidDataList(DouyinChannelAidDatasDto bo) {
        // 首先进行汇总的查询
        // 我们需要将这批角色按照他们的注册时间进行数据分组 为了我们后面做汇总的ltv数据
        QueryWrapper<DouyinChannelAidDatas> orderWrapper = Wrappers.query();
        if (bo.getStartDate() != null && bo.getEndDate() != null) {
            orderWrapper.between("create_date", bo.getStartDate(), bo.getEndDate());
        }
        orderWrapper.eq(StringUtils.isNotBlank(bo.getPlatform()), "platform", bo.getPlatform())
            .eq(StringUtils.isNotBlank(bo.getChannel()), "channel", bo.getChannel())
            .eq(StringUtils.isNotBlank(bo.getAid()), "aid", bo.getAid())
            .likeRight(StringUtils.isNotBlank(bo.getAidName()), "aid_name", bo.getAidName())
            .likeRight(StringUtils.isNotBlank(bo.getGameName()), "game_name", bo.getGameName())
            .eq(StringUtils.isNotBlank(bo.getPreChannel()), "pre_channel", bo.getPreChannel())
            .eq(bo.getPreChannel() != null, "new_roles", bo.getNewRoles());

        DouyinChannelAidDatasVo douyinChannelAidDatasVo = douyinChannelAidDatasMapper.selectSummarySimpleData(orderWrapper);

        LambdaQueryWrapper<DouyinChannelAidDatas> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getPlatform()), DouyinChannelAidDatas::getPlatform, bo.getPlatform());
        lqw.eq(StringUtils.isNotBlank(bo.getChannel()), DouyinChannelAidDatas::getChannel, bo.getChannel());
        lqw.eq(StringUtils.isNotBlank(bo.getAid()), DouyinChannelAidDatas::getAid, bo.getAid());
        lqw.likeRight(StringUtils.isNotBlank(bo.getAidName()), DouyinChannelAidDatas::getAidName, bo.getAidName());
        lqw.likeRight(StringUtils.isNotBlank(bo.getGameName()), DouyinChannelAidDatas::getGameName, bo.getGameName());
        lqw.eq(bo.getUsers() != null, DouyinChannelAidDatas::getUsers, bo.getUsers());
        lqw.eq(bo.getPayUsers() != null, DouyinChannelAidDatas::getPayUsers, bo.getPayUsers());
        lqw.eq(bo.getTotalAmount() != null, DouyinChannelAidDatas::getTotalAmount, bo.getTotalAmount());
        lqw.eq(bo.getClickCnt() != null, DouyinChannelAidDatas::getChannel, bo.getChannel());
        lqw.eq(bo.getActiveUsers() != null, DouyinChannelAidDatas::getActiveUsers, bo.getActiveUsers());
        lqw.eq(StringUtils.isNotBlank(bo.getPreChannel()), DouyinChannelAidDatas::getPreChannel, bo.getPreChannel());
        lqw.eq(bo.getNewRoles() != null, DouyinChannelAidDatas::getNewRoles, bo.getNewRoles());
        if (bo.getStartDate() != null && bo.getEndDate() != null) {
            lqw.between(DouyinChannelAidDatas::getCreateDate, bo.getStartDate(), bo.getEndDate());
        }
        lqw.orderByDesc(DouyinChannelAidDatas::getId);
        Page<DouyinChannelAidDatasVo> result = douyinChannelAidDatasMapper.selectVoPage(bo.build(), lqw);
        // 汇总数据获取完毕之后 我们需要讲这些数据累加到返回数据中
        if (douyinChannelAidDatasVo != null) {
            DouyinChannelAidDatasVo insertDouyinChannelAidDatasVo = new DouyinChannelAidDatasVo();
            insertDouyinChannelAidDatasVo.setId(0L);
            insertDouyinChannelAidDatasVo.setPlatform(StringUtils.isNotBlank(bo.getPlatform()) ? bo.getPlatform() : "all");
            insertDouyinChannelAidDatasVo.setChannel(StringUtils.isNotBlank(bo.getChannel()) ? bo.getChannel() : "all");
            insertDouyinChannelAidDatasVo.setAid(StringUtils.isNotBlank(bo.getAid()) ? bo.getAid() : "all");
            insertDouyinChannelAidDatasVo.setAidName(StringUtils.isNotBlank(bo.getAidName()) ? bo.getAidName() : "all");
            insertDouyinChannelAidDatasVo.setGameName(StringUtils.isNotBlank(bo.getGameName()) ? bo.getGameName() : "all");
            insertDouyinChannelAidDatasVo.setUsers(douyinChannelAidDatasVo.getUsers());
            insertDouyinChannelAidDatasVo.setPayUsers(douyinChannelAidDatasVo.getPayUsers());
            insertDouyinChannelAidDatasVo.setTotalAmount(douyinChannelAidDatasVo.getTotalAmount());
            insertDouyinChannelAidDatasVo.setCreateDate("汇总");
            insertDouyinChannelAidDatasVo.setClickCnt(douyinChannelAidDatasVo.getClickCnt());
            insertDouyinChannelAidDatasVo.setActiveUsers(douyinChannelAidDatasVo.getActiveUsers());
            insertDouyinChannelAidDatasVo.setPreChannel(StringUtils.isNotBlank(bo.getPreChannel()) ? bo.getPreChannel() : "all");
            insertDouyinChannelAidDatasVo.setNewRoles(douyinChannelAidDatasVo.getNewRoles());
            result.getRecords().add(0, insertDouyinChannelAidDatasVo);
        }
        return TableDataInfo.build(result);
    }

    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    @Override
    @DS("trackdatas")
    public TableDataInfo<DouyinChannelDatasVo> douyinChannelDataList(DouyinChannelDatasDto bo) {
        LambdaQueryWrapper<DouyinChannelDatas> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(bo.getGameName()), DouyinChannelDatas::getGameName, bo.getGameName());
        lqw.eq(bo.getPlatform() != null, DouyinChannelDatas::getPlatform, bo.getPlatform());
        lqw.eq(bo.getPackageCode() != null, DouyinChannelDatas::getPackageCode, bo.getPackageCode());
        lqw.eq(bo.getBdUsers() != null, DouyinChannelDatas::getBdUsers, bo.getBdUsers());
        lqw.eq(bo.getPtUsers() != null, DouyinChannelDatas::getPtUsers, bo.getPtUsers());
        lqw.eq(bo.getPtPayUsers() != null, DouyinChannelDatas::getPtPayUsers, bo.getPtPayUsers());
        lqw.eq(bo.getBdTotalAmount() != null, DouyinChannelDatas::getBdTotalAmount, bo.getBdTotalAmount());
        lqw.eq(bo.getPtTotalAmount() != null, DouyinChannelDatas::getPtTotalAmount, bo.getPtTotalAmount());
        lqw.eq(bo.getPtNewRoles() != null, DouyinChannelDatas::getPtNewRoles, bo.getPtNewRoles());
        lqw.eq(bo.getBdNewRoles() != null, DouyinChannelDatas::getBdNewRoles, bo.getBdNewRoles());
        if (bo.getStartDate() != null && bo.getEndDate() != null) {
            lqw.between(DouyinChannelDatas::getCreateDate, bo.getStartDate(), bo.getEndDate());
        }
        lqw.orderByDesc(DouyinChannelDatas::getId);
        Page<DouyinChannelDatasVo> result = douyinChannelDatasMapper.selectVoPage(bo.build(), lqw);
        return TableDataInfo.build(result);
    }


    /*********************** 上面的按照数据平台同学要求已经不用了 *****************************/
    /**
     * 平台渠道统计数据
     *
     * @param pageDto
     * @return
     */
    @Override
    @DS("trackdatas")
    public TableDataInfo<PlatformTongjiDatasVo> platformTongjiDatas(PlatformTongjiDatasPageDto pageDto) {

        // 首先进行汇总的查询
        // 我们需要将这批角色按照他们的注册时间进行数据分组 为了我们后面做汇总的ltv数据
        QueryWrapper<PlatformTongjiDatas> orderWrapper = Wrappers.query();
        List<String> searchInDate = null;
        if (pageDto.getBeginTime() != null && pageDto.getEndTime() != null) {
            searchInDate = StatisticsUtils.getDate(pageDto.getBeginTime(), pageDto.getEndTime());
        }
        orderWrapper.in(searchInDate != null, "create_date", searchInDate)
            .eq(StringUtils.isNotBlank(pageDto.getPlatform()), "platform", pageDto.getPlatform())
            .eq(StringUtils.isNotBlank(pageDto.getPlatform()), "platform", pageDto.getPlatform())
            .eq(StringUtils.isNotBlank(pageDto.getPackageCode()), "package_code", pageDto.getPackageCode())
            .eq((pageDto.getChannelId() != null && pageDto.getChannelId() != 0), "channel_id", pageDto.getChannelId())
            .likeRight(StringUtils.isNotBlank(pageDto.getGameName()), "game_name", pageDto.getGameName());

        PlatformTongjiDatasVo vo = platformTongjiDatasMapper.selectSummaryForPlatformTongjiDatas(orderWrapper);

        LambdaQueryWrapper<PlatformTongjiDatas> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getCreateDate()), PlatformTongjiDatas::getCreateDate, pageDto.getCreateDate());
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getGameName()), PlatformTongjiDatas::getGameName, pageDto.getGameName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPlatform()), PlatformTongjiDatas::getPlatform, pageDto.getPlatform());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPackageCode()), PlatformTongjiDatas::getPackageCode, pageDto.getPackageCode());
        lqw.eq(pageDto.getRegisterUsers() != null, PlatformTongjiDatas::getRegisterUsers, pageDto.getRegisterUsers());
        lqw.eq(pageDto.getPayCnt() != null, PlatformTongjiDatas::getPayCnt, pageDto.getPayCnt());
        lqw.eq(pageDto.getFirstPayCnt() != null, PlatformTongjiDatas::getFirstPayCnt, pageDto.getFirstPayCnt());
        lqw.eq(pageDto.getFirstPayUsers() != null, PlatformTongjiDatas::getFirstPayUsers, pageDto.getFirstPayUsers());
        lqw.eq(pageDto.getFirstRelTotal() != null, PlatformTongjiDatas::getFirstRelTotal, pageDto.getFirstRelTotal());
        lqw.eq(pageDto.getLtv() != null, PlatformTongjiDatas::getLtv, pageDto.getLtv());
        lqw.eq(pageDto.getRoi() != null, PlatformTongjiDatas::getRoi, pageDto.getRoi());
        lqw.eq((pageDto.getChannelId() != null && pageDto.getChannelId() != 0), PlatformTongjiDatas::getChannelId, pageDto.getChannelId());
        lqw.in(searchInDate != null, PlatformTongjiDatas::getCreateDate, searchInDate);
        lqw.orderByDesc(PlatformTongjiDatas::getId);
        Page<PlatformTongjiDatasVo> result = platformTongjiDatasMapper.selectVoPage(pageDto.build(), lqw);

        // 汇总数据获取完毕之后 我们需要讲这些数据累加到返回数据中
        if (vo != null) {
            PlatformTongjiDatasVo insertVo = new PlatformTongjiDatasVo();
            insertVo.setId(0L);
            insertVo.setGameName(StringUtils.isNotBlank(pageDto.getGameName()) ? pageDto.getGameName() : "all");
            insertVo.setPlatform(StringUtils.isNotBlank(pageDto.getPlatform()) ? pageDto.getPlatform() : "all");
            insertVo.setChannelId((pageDto.getChannelId() != null && pageDto.getChannelId() != 0) ? pageDto.getChannelId() : 0);
            insertVo.setPackageCode(StringUtils.isNotBlank(pageDto.getPackageCode()) ? pageDto.getPackageCode() : "all");
            insertVo.setChannelId((pageDto.getChannelId() != null && pageDto.getChannelId() != 0) ? pageDto.getChannelId() : 0);
            insertVo.setRegisterUsers(vo.getRegisterUsers());
            insertVo.setPayCnt(vo.getPayCnt());
            insertVo.setFirstPayCnt(vo.getFirstPayCnt());
            insertVo.setFirstPayUsers(vo.getFirstPayUsers());
            insertVo.setFirstRelTotal(vo.getFirstRelTotal());
            insertVo.setRelCost(vo.getRelCost());
            insertVo.setCreateDate("汇总");
            result.getRecords().add(0, insertVo);
        }
        log.info("sss:{}", JsonUtils.toJsonString(result.getRecords()));
        // 数据获取完毕之后我们需要对一些字段进行一下处理的
        if (result.getRecords().size() > 0) {
            result.getRecords().stream().forEach(a -> {
                if (a.getFirstRelTotal() != null && a.getRegisterUsers() != null) {
                    BigDecimal mul = NumberUtil.mul(a.getFirstRelTotal(), 100);
                    BigDecimal div = NumberUtil.div(NumberUtil.div(mul, a.getRegisterUsers()), 100.0);
                    DecimalFormat df1 = new DecimalFormat("0.00");
                    a.setLtv(df1.format(div));
                } else {
                    a.setLtv("0.0");
                }
                if (a.getFirstRelTotal() != null && a.getRelCost() != null && !a.getRelCost().equals(new BigDecimal("0.00"))) {
                    BigDecimal div = NumberUtil.div(a.getFirstRelTotal(), a.getRelCost());
                    a.setRoi(String.format("%.2f", div) + "%");
                } else {
                    a.setRoi("0.0%");
                }
            });
        }
        return TableDataInfo.build(result);

    }


    /**
     * 平台渠道统计数据
     *
     * @param pageDto
     * @return
     */
    @Override
    @DS("trackdatas")
    public TableDataInfo<PlatformTongjiChannelAidDatasVo> platformTongjiChannelAidDatas(PlatformTongjiChannelAidDatasPageDto pageDto) {

        // 首先进行汇总的查询
        // 我们需要将这批角色按照他们的注册时间进行数据分组 为了我们后面做汇总的ltv数据
        QueryWrapper<PlatformTongjiChannelAidDatas> orderWrapper = Wrappers.query();
        if (pageDto.getBeginTime() != null && pageDto.getEndTime() != null) {
            orderWrapper.between("create_date", pageDto.getBeginTime(), pageDto.getBeginTime());
        }
        orderWrapper.eq(StringUtils.isNotBlank(pageDto.getPlatform()), "platform", pageDto.getPlatform())
            .eq(StringUtils.isNotBlank(pageDto.getPreChannel()), "pre_channel", pageDto.getPreChannel())
            .eq(StringUtils.isNotBlank(pageDto.getChannel()), "channel", pageDto.getChannel())
            .eq((pageDto.getChannelId() != null && pageDto.getChannelId() != 0), "channel_id", pageDto.getChannelId())
            .eq(StringUtils.isNotBlank(pageDto.getAid()), "aid", pageDto.getAid())
            .likeRight(StringUtils.isNotBlank(pageDto.getAidName()), "aid_name", pageDto.getAidName())
            .likeRight(StringUtils.isNotBlank(pageDto.getGameName()), "game_name", pageDto.getGameName());

        PlatformTongjiChannelAidDatasVo vo = platformTongjiChannelAidDatasMapper.selectSummaryForPlatformTongjiChannelAidDatas(orderWrapper);

        LambdaQueryWrapper<PlatformTongjiChannelAidDatas> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getPlatform()), PlatformTongjiChannelAidDatas::getPlatform, pageDto.getPlatform());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPreChannel()), PlatformTongjiChannelAidDatas::getPreChannel, pageDto.getPreChannel());
        lqw.eq(StringUtils.isNotBlank(pageDto.getChannel()), PlatformTongjiChannelAidDatas::getChannel, pageDto.getChannel());
        lqw.eq((pageDto.getChannelId() != null && pageDto.getChannelId() != 0), PlatformTongjiChannelAidDatas::getChannelId, pageDto.getChannelId());
        lqw.eq(pageDto.getCreateDate() != null, PlatformTongjiChannelAidDatas::getCreateDate, pageDto.getCreateDate());
        lqw.eq(StringUtils.isNotBlank(pageDto.getAid()), PlatformTongjiChannelAidDatas::getAid, pageDto.getAid());
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getAidName()), PlatformTongjiChannelAidDatas::getAidName, pageDto.getAidName());
        lqw.eq(pageDto.getClickCnt() != null, PlatformTongjiChannelAidDatas::getClickCnt, pageDto.getClickCnt());
        lqw.eq(pageDto.getActiveUsers() != null, PlatformTongjiChannelAidDatas::getActiveUsers, pageDto.getActiveUsers());
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getGameName()), PlatformTongjiChannelAidDatas::getGameName, pageDto.getGameName());
        lqw.eq(pageDto.getRegistUsers() != null, PlatformTongjiChannelAidDatas::getRegistUsers, pageDto.getRegistUsers());
        lqw.eq(pageDto.getPayUsers() != null, PlatformTongjiChannelAidDatas::getPayUsers, pageDto.getPayUsers());
        lqw.eq(pageDto.getTotalAmount() != null, PlatformTongjiChannelAidDatas::getTotalAmount, pageDto.getTotalAmount());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), PlatformTongjiChannelAidDatas::getCreateDate, pageDto.getBeginTime());
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), PlatformTongjiChannelAidDatas::getCreateDate, pageDto.getEndTime());
        lqw.orderByDesc(PlatformTongjiChannelAidDatas::getId);

        IPage<PlatformTongjiChannelAidDatasVo> result = platformTongjiChannelAidDatasMapper.selectVoPage(pageDto.build(), lqw);
        // 汇总数据获取完毕之后 我们需要讲这些数据累加到返回数据中
        if (vo != null) {
            PlatformTongjiChannelAidDatasVo insertVo = new PlatformTongjiChannelAidDatasVo();
            insertVo.setId(0L);
            insertVo.setPlatform(StringUtils.isNotBlank(pageDto.getPlatform()) ? pageDto.getPlatform() : "all");
            insertVo.setChannel(StringUtils.isNotBlank(pageDto.getChannel()) ? pageDto.getChannel() : "all");
            insertVo.setChannelId((pageDto.getChannelId() != null && pageDto.getChannelId() != 0) ? pageDto.getChannelId() : 0);
            insertVo.setAid(StringUtils.isNotBlank(pageDto.getAid()) ? pageDto.getAid() : "all");
            insertVo.setAidName(StringUtils.isNotBlank(pageDto.getAidName()) ? pageDto.getAidName() : "all");
            insertVo.setGameName(StringUtils.isNotBlank(pageDto.getGameName()) ? pageDto.getGameName() : "all");
            insertVo.setPreChannel(StringUtils.isNotBlank(pageDto.getPreChannel()) ? pageDto.getPreChannel() : "all");
            insertVo.setClickCnt(vo.getClickCnt());
            insertVo.setActiveUsers(vo.getActiveUsers());
            insertVo.setRegistUsers(vo.getRegistUsers());
            insertVo.setPayUsers(vo.getPayUsers());
            insertVo.setTotalAmount(vo.getTotalAmount());
            insertVo.setCreateDate("汇总");
            result.getRecords().add(0, insertVo);
        }
        return TableDataInfo.build(result);
    }


}
