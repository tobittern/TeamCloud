package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.sdk.api.domain.LoginMemberVo;
import com.euler.sdk.api.domain.Member;
import com.euler.sdk.domain.vo.MemberVo;

import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface IMemberService extends IService<Member> {

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    MemberVo queryById(Long id);


    Long register(Member member);

    /**
     * 校验是否唯一
     *
     * @param member
     * @return
     */
    String checkUnique(Member member);

    /**
     * 登录
     *
     * @param account
     * @return
     */
    LoginMemberVo login(String account, String password);


    LoginMemberVo getMemberById(Long memberId);

    LoginMemberVo getMemberByMobile(String mobile);

    List<LoginMemberVo> getMutiMemberByMobile(String account);

    /**
     * 检测用户是否可以玩
     *
     * @param idCardNo
     * @return
     */
    Boolean checkUserPlay(String idCardNo);

    /**
     * 注销会员
     *
     * @return
     */
    Boolean cannellation(Integer id, Long memberId);

    /**
     * 下线用户 ，game=-1时，下线当前用户id下所有用户id
     *
     * @param memberId
     * @param gameId
     */
    Integer downLineUser(Long memberId, Integer gameId, Integer platform);

    /**
     * 获取头像
     *
     * @param sex
     * @param avatar
     * @return
     */
    String getAvatar(String sex, String avatar);

}
