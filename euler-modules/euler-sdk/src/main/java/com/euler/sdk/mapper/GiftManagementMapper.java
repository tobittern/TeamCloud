package com.euler.sdk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.GiftManagement;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.domain.vo.GiftManagementVo;
import org.apache.ibatis.annotations.Param;

/**
 * 礼包管理Mapper接口
 *
 * @author euler
 * @date 2022-03-22
 */
public interface GiftManagementMapper extends BaseMapperPlus<GiftManagementMapper, GiftManagement, GiftManagementVo> {

    /**
     * 查看礼包内容详情
     *
     * @param page
     * @param lqw
     * @return 礼包内容详情
     */
    Page<GiftInfoVo> getGiftContentsList(@Param("page") Page<GiftInfoVo> page, @Param(Constants.WRAPPER) Wrapper<GiftInfoVo> lqw);

    /**
     * 根据游戏Id获取礼包组管理详细信息
     *
     * @param gameId 游戏Id
     * @return 礼包组管理详细信息
     */
    GiftManagementVo getGiftGroupInfo(Integer gameId);

}
