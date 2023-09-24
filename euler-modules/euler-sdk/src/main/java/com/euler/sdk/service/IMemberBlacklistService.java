package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MemberBanVo;
import com.euler.sdk.domain.bo.MemberBlacklistBo;
import com.euler.sdk.domain.dto.CommonIdPageDto;
import com.euler.sdk.domain.dto.MemberBlacklistDto;
import com.euler.sdk.domain.vo.MemberBlacklistRecordVo;
import com.euler.sdk.domain.vo.MemberBlacklistVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户黑名单列Service接口
 *
 * @author euler
 * @date 2022-06-13
 */
public interface IMemberBlacklistService {

    /**
     * 查询用户黑名单列列表
     *
     * @return 用户黑名单列集合
     */
    TableDataInfo<MemberBlacklistRecordVo> queryPageList(CommonIdPageDto<Long> dto);

    /**
     * 查询用户黑名单列列表
     *
     * @return 用户黑名单列集合
     */
    List<MemberBlacklistVo> queryList(MemberBlacklistDto dto);

    /**
     * 修改用户黑名单列
     *
     * @return 结果
     */
    R insertByBo(MemberBlacklistBo bo);

    /**
     * 修改用户黑名单列
     *
     * @return 结果
     */
    R updateByBo(MemberBlacklistBo bo);

    /**
     * 校验并批量删除用户黑名单列信息
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 校验并批量删除用户黑名单列信息
     * @return 结果
     */
    Boolean remove(KeyValueDto<Long> dto);

    /**
     * 一键解封
     *
     * @return 结果
     */
    R unseal(IdTypeDto<Long, Integer> dto);

    /**
     * 检测用户是否被封禁
     */
    MemberBanVo checkUserIsBlacklist(Long userId, String username, Integer platform, Integer gameId);
}
