package com.euler.sdk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.LoginMemberVo;
import com.euler.sdk.api.domain.Member;
import com.euler.sdk.domain.vo.MemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息Mapper接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface MemberMapper extends BaseMapperPlus<MemberMapper, Member, MemberVo> {

    LoginMemberVo login(@Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);


    List<LoginMemberVo> loginList(@Param(Constants.WRAPPER) Wrapper<Member> queryWrapper);

}
