package com.euler.risk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.BanlistBo;
import com.euler.risk.domain.dto.BanlistDto;
import com.euler.risk.domain.entity.Banlist;
import com.euler.risk.domain.vo.BanlistVo;
import com.euler.sdk.api.domain.MemberProfileBasics;
import com.euler.sdk.api.domain.dto.SearchBanDto;

import java.util.Collection;
import java.util.List;

/**
 * 封号列Service接口
 *
 * @author euler
 * @date 2022-08-23
 */
public interface IBanlistService extends IService<Banlist> {

    /**
     * 按照指定条件查询用户和设备关联列表
     *
     * @return 封号列
     */
    TableDataInfo<MemberProfileBasics> search(SearchBanDto dto);

    /**
     * 查询封号列
     *
     * @param id 封号列主键
     * @return 封号列
     */
    BanlistVo queryById(Integer id);

    /**
     * 查询封号列
     *
     * @param id 用户ID
     * @return 封号列
     */
    BanlistVo queryByMemberId(Long id);

    /**
     * 查询封号列列表
     *
     * @param dto 封号列
     * @return 封号列集合
     */
    TableDataInfo<BanlistVo> queryPageList(BanlistDto dto);

    /**
     * 修改封号列
     *
     * @return 结果
     */
    R insertByBo(BanlistBo bo);

    /**
     * 修改封号列
     *
     * @return 结果
     */
    R updateByBo(BanlistBo bo);

    /**
     * 校验并批量删除封号列信息
     *
     * @param ids     需要删除的封号列主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid);

    /**
     * 删除掉指定的封号Redis key
     *
     * @param userId
     */
    void deleteRedisKey(Long userId);

    /**
     * 检测用户的封禁状态 返回的是没有被封的用户列表
     * @param memberIds
     * @return
     */
    List<Long> checkUserBanStatus(List<Long> memberIds);

}
