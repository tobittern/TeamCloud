package com.euler.statistics.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.TfUserQuota;
import com.euler.statistics.mapper.MemberProfileMapper;
import com.euler.statistics.service.IMemberProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MemberProfileService业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MemberProfileServiceImpl extends ServiceImpl<MemberProfileMapper, MemberProfile> implements IMemberProfileService {

    @Autowired
    private MemberProfileMapper baseMapper;


    /**
     * 获取用户数量
     *
     * @param queryDto
     * @return
     */
    @Override
    public SummaryResultDto getIncMember(SummaryQueryDto queryDto) {
        var wrapper=buildQuery(queryDto);
        SummaryResultDto selectCount = baseMapper.selectMemberNum(wrapper);
        return selectCount;
    }

    private LambdaQueryWrapper<MemberProfile> buildQuery(SummaryQueryDto queryDto){
        var wrapper = Wrappers.<MemberProfile>lambdaQuery()
            .eq(queryDto.getChannelId() != null, MemberProfile::getChannelId, queryDto.getChannelId())
            .eq(StringUtils.isNotBlank(queryDto.getChannelName()), MemberProfile::getChannelName, queryDto.getChannelName())

            .eq(queryDto.getGameId() != null, MemberProfile::getGameId, queryDto.getGameId())
            .eq(StringUtils.isNotBlank(queryDto.getPackageCode()), MemberProfile::getPackageCode, queryDto.getPackageCode())
            .ge(StringUtils.isNotBlank(queryDto.getBeginTime()), MemberProfile::getCreateTime, DateUtils.getBeginOfDay(queryDto.getBeginTime()))
            .le(StringUtils.isNotBlank(queryDto.getEndTime()), MemberProfile::getCreateTime, DateUtils.getEndOfDay(queryDto.getEndTime()));
        return  wrapper;
    }


    /**
     * 获取用户数量
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<SummaryResultDto> getIncMemberGroup(SummaryQueryDto queryDto) {
        var wrapper=buildQuery(queryDto);

       List<SummaryResultDto> groupNum = baseMapper.selectMemberGroupNum(wrapper);
        return groupNum;
    }


}
