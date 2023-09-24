package com.euler.community.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.HotGame;
import com.euler.community.domain.vo.HotGameVo;

import java.util.List;

/**
 * Mapper接口
 *
 * @author euler
 * @date 2022-06-17
 */
public interface HotGameMapper extends BaseMapperPlus<HotGameMapper, HotGame, HotGameVo> {

    List<HotGame> getHotGames(Integer operationPlatform);
}
