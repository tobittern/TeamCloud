package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.statistics.domain.dto.SummaryResultDto;
import com.euler.statistics.domain.entity.OnlineUser;
import com.euler.statistics.domain.vo.OnlineUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Mapper接口
 *
 * @author euler
 * @date 2022-09-14
 */
@Mapper
public interface OnlineUserMapper extends BaseMapperPlus<OnlineUserMapper, OnlineUser, OnlineUserVo> {

    SummaryResultDto selectOnlineUserNum(@Param(Constants.WRAPPER) Wrapper<OnlineUser> queryWrapper);

    List<SummaryResultDto> selectOnlineUserGroupNum(@Param(Constants.WRAPPER) Wrapper<OnlineUser> queryWrapper);

}
