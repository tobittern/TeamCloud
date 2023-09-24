package com.euler.statistics.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.ListUtil;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.config.WebConfig;
import com.euler.statistics.domain.dto.OnlineUserPageDto;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.OnlineUser;
import com.euler.statistics.domain.vo.OnlineUserVo;
import com.euler.statistics.mapper.OnlineUserMapper;
import com.euler.statistics.service.IOnlineUserService;
import com.euler.system.api.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-09-14
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OnlineUserServiceImpl extends ServiceImpl<OnlineUserMapper, OnlineUser> implements IOnlineUserService {

    @Autowired
    private OnlineUserMapper baseMapper;
    @Autowired
    private WebConfig webConfig;

    /**
     * 查询
     *
     * @param id 主键
     * @return
     */
    @Override
    public OnlineUserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public TableDataInfo<OnlineUserVo> queryPageList(OnlineUserPageDto pageDto) {
        LambdaQueryWrapper<OnlineUser> lqw = buildQueryWrapper(pageDto);
        Page<OnlineUserVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public List<OnlineUserVo> queryList(OnlineUserPageDto pageDto) {
        LambdaQueryWrapper<OnlineUser> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OnlineUser> buildQueryWrapper(OnlineUserPageDto pageDto) {
        LambdaQueryWrapper<OnlineUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(pageDto.getUserId() != null, OnlineUser::getUserId, pageDto.getUserId());
        lqw.like(StringUtils.isNotBlank(pageDto.getUserName()), OnlineUser::getUserName, pageDto.getUserName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getIpaddr()), OnlineUser::getIpaddr, pageDto.getIpaddr());
        lqw.eq(pageDto.getLoginTime() != null, OnlineUser::getLoginTime, pageDto.getLoginTime());
        lqw.eq(StringUtils.isNotBlank(pageDto.getPackageCode()), OnlineUser::getPackageCode, pageDto.getPackageCode());
        lqw.eq(pageDto.getGameId() != null, OnlineUser::getGameId, pageDto.getGameId());
        lqw.like(StringUtils.isNotBlank(pageDto.getGameName()), OnlineUser::getGameName, pageDto.getGameName());
        lqw.eq(pageDto.getChannelId() != null, OnlineUser::getChannelId, pageDto.getChannelId());
        lqw.like(StringUtils.isNotBlank(pageDto.getChannelName()), OnlineUser::getChannelName, pageDto.getChannelName());
        lqw.eq(StringUtils.isNotBlank(pageDto.getUserType()), OnlineUser::getUserType, pageDto.getUserType());
        lqw.eq(pageDto.getPlatform() != null, OnlineUser::getPlatform, pageDto.getPlatform());
        lqw.eq(pageDto.getDevice() != null, OnlineUser::getDevice, pageDto.getDevice());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), OnlineUser::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), OnlineUser::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 获取在线用户
     */
    @Override
    public void getCurrentOnlineUser(List<SysUserOnline> userOnlineList) {

            List<OnlineUser> onlineUserList = JsonHelper.copyList(userOnlineList, OnlineUser.class);
            try {
                saveBatch(onlineUserList);
            } catch (Exception e) {
                log.error("定时获取在线用户--入库--异常", e);
            }


    }

    private LambdaQueryWrapper<OnlineUser> buildSummaryQuery(SummaryQueryDto queryDto) {
        var wrapper = Wrappers.<OnlineUser>lambdaQuery()
            .eq(queryDto.getChannelId() != null, OnlineUser::getChannelId, queryDto.getChannelId())
            .eq(queryDto.getGameId() != null, OnlineUser::getGameId, queryDto.getGameId())
            .eq(StringUtils.isNotBlank(queryDto.getChannelName()), OnlineUser::getChannelName, queryDto.getChannelName())
            .eq(StringUtils.isNotBlank(queryDto.getPackageCode()), OnlineUser::getPackageCode, queryDto.getPackageCode())
            .ge(StringUtils.isNotBlank(queryDto.getBeginTime()), OnlineUser::getCreateTime, DateUtils.getBeginOfDay(queryDto.getBeginTime()))
            .le(StringUtils.isNotBlank(queryDto.getEndTime()), OnlineUser::getCreateTime, DateUtils.getEndOfDay(queryDto.getEndTime()));
        return wrapper;
    }


    /**
     * 获取在线用户统计
     *
     * @param queryDto
     * @return
     */
    @Override
    public SummaryResultDto getIncOnlineUserNum(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        SummaryResultDto sumAmount = baseMapper.selectOnlineUserNum(wrapper);
        return sumAmount;
    }


    /**
     * 获取在线用户折线
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<SummaryResultDto> getIncOnlineUserGroupNum(SummaryQueryDto queryDto) {
        var wrapper = buildSummaryQuery(queryDto);
        var sumAmount = baseMapper.selectOnlineUserGroupNum(wrapper);
        return sumAmount;
    }


}
