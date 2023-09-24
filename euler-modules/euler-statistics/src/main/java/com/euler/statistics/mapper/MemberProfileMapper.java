package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.statistics.api.domain.RechargeStat;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.vo.MemberProfileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员详细信息Mapper接口
 *
 * @author euler
 * @date 2022-03-21
 */
@Mapper
public interface MemberProfileMapper extends BaseMapperPlus<MemberProfileMapper, MemberProfile, MemberProfileVo> {

    /**
     * @param queryWrapper 统计人数
     * @return
     */
    SummaryResultDto selectMemberNum(@Param(Constants.WRAPPER) Wrapper<MemberProfile> queryWrapper);

    List<SummaryResultDto> selectMemberGroupNum(@Param(Constants.WRAPPER) Wrapper<MemberProfile> queryWrapper);
}
