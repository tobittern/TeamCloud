package com.euler.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.system.api.domain.SysNotice;
import com.euler.system.api.domain.UserNoticeVo;
import org.apache.ibatis.annotations.Param;

/**
 * 通知公告表 数据层
 *
 * @author euler
 */
public interface SysNoticeMapper extends BaseMapperPlus<SysNoticeMapper, SysNotice, SysNotice> {



    Integer getUnReadCount(@Param(Constants.WRAPPER) Wrapper<SysNotice> queryWrapper, @Param("userId") Long userId);


    Page<UserNoticeVo> selectFrontPageNoticeList(@Param("page") Page<SysNotice> page, @Param(Constants.WRAPPER) Wrapper<SysNotice> queryWrapper, @Param("userId") Long userId, @Param("noticeType")String noticeType);
}
