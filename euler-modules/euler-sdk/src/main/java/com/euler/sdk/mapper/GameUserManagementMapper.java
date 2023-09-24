package com.euler.sdk.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.GameUserManagementVo;
import org.apache.ibatis.annotations.Param;

/**
 * 游戏用户管理Mapper接口
 *
 * @author euler
 * @date 2022-04-02
 */
public interface GameUserManagementMapper extends BaseMapperPlus<GameUserManagementMapper, GameUserManagement, GameUserManagementVo> {

    Page<GameUserManagementVo> getGameUserDetailPageList(@Param("page") Page<GameUserManagementVo> page, @Param(Constants.WRAPPER) Wrapper<GameUserManagementVo> queryWrapper);

    GameUserManagement getGameUserInfo(@Param(Constants.WRAPPER) Wrapper<Object> queryWrapper);

    boolean updateGameUserDataById(GameUserManagement entity);
}
