package com.euler.sdk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.domain.vo.MemberProfileVo;
import com.euler.sdk.domain.vo.StatisticsChargeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员详细信息Mapper接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface MemberProfileMapper extends BaseMapperPlus<MemberProfileMapper, MemberProfile, MemberProfileVo> {

    Page<MemberDetailVo> getMemberDetailPageList(@Param("page") Page<MemberDetailVo> page, @Param(Constants.WRAPPER) Wrapper<MemberDetailVo> queryWrapper);

    MemberDetailVo getMemberDetailByMemberId(@Param(Constants.WRAPPER) Wrapper<MemberDetailVo> queryWrapper);

    List<StatisticsChargeVo> selectUserNumByParam(Map<String, Object> userMap);
}
