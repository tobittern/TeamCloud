package com.euler.statistics.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.platform.api.domain.OpenGame;
import com.euler.platform.api.domain.OpenGameVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【游戏管理】Mapper接口
 *
 * @author open
 * @date 2022-02-18
 */
@Mapper
public interface OpenGameMapper extends BaseMapperPlus<OpenGameMapper, OpenGame, OpenGameVo> {

    // 查询游戏列表
    List<OpenGame> getGameList(@Param(Constants.WRAPPER) Wrapper<OpenGame> queryWrapper);

}
