package com.euler.sdk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.api.domain.MemberProfileBasics;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import com.euler.sdk.domain.dto.MemberPageDto;
import com.euler.sdk.domain.dto.MobileDto;
import com.euler.sdk.domain.dto.SearchMemberlDto;
import com.euler.sdk.domain.vo.MemberProfileVo;

import java.util.List;
import java.util.Map;

/**
 * 会员详细信息Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface IMemberProfileService extends IService<MemberProfile> {

    /**
     * 会员详情列表
     *
     * @param memberPageDto
     * @return
     */
    TableDataInfo<MemberDetailVo> getMemberDetailPageList(MemberPageDto memberPageDto);


    /**
     * 会员详情
     *
     * @param memberId
     * @return
     */
    MemberDetailVo getMemberDetailByMemberId(long memberId);


    /**
     * 根据memberId获取memberProfile
     *
     * @param memberId
     * @return
     */
    MemberProfile getByMemberId(long memberId);

    /**
     * 根据List 获取一批用户的基础信息
     *
     * @param memberIds
     * @return
     */
    List<MemberProfile> getByMemberIds(List<Long> memberIds);

    /**
     * 根据memberId获取根据memberId获取memberProfileVo
     *
     * @param memberId
     * @return
     */
    MemberProfileVo getVoByMemberId(long memberId);

    /**
     * 修改会员实名认证信息
     *
     * @param memberProfile 实名认证信息
     * @return 结果
     */
    Boolean updateMemberDetail(MemberProfile memberProfile);

    /**
     * 绑定手机号
     *
     * @return
     * @Param dto
     */
    R updateMobileByMemberId(MobileDto dto);

    /**
     * 按照条件查询用户
     *
     * @param dto
     * @return
     */
    R searchMember(SearchMemberlDto dto);


    List<MemberDetailVo> selectMemberByParam(Map<String, Object> orderMap);

    /**
     * 通过指定数据获取用户的id列表
     *
     * @return
     */
    TableDataInfo<MemberProfileBasics> getMemberIdsByParams(SearchBanDto dto);

    /**
     * 一批身份证号进行加密处理
     * @param idCardNos
     * @return
     */
    List<KeyValueDto<String>> getUserIdCardNoEncryption(List<String> idCardNos, Integer type);

    /**
     * 获取身份证的随机秘钥
     */
    byte[] getIdCardAesKey();

}
