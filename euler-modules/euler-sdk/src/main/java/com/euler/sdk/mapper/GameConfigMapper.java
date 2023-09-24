package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.api.domain.GameConfigVo;
import com.euler.sdk.domain.entity.GameConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 游戏配置Mapper接口
 *
 * @author euler
 * @date 2023-03-23
 */
@Mapper
public interface GameConfigMapper extends BaseMapperPlus<GameConfigMapper, GameConfig, GameConfigVo> {

}

