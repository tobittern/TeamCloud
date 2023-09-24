package com.euler.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.dto.SummaryResultDto;

import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author euler
 * @date 2022-09-05
 */
public interface IMemberProfileService extends IService<MemberProfile> {
    /**
     * 获取用户数量
     *
     * @param queryDto
     * @return
     */
    SummaryResultDto getIncMember(SummaryQueryDto queryDto);

    List<SummaryResultDto> getIncMemberGroup(SummaryQueryDto queryDto);


}
