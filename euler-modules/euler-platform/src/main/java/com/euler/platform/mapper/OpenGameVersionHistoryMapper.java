package com.euler.platform.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.domain.OpenGameVersionHistory;
import com.euler.platform.domain.vo.OpenGameVersionHistoryVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 游戏版本历史Mapper接口
 *
 * @author euler
 * @date 2022-03-31
 */
public interface OpenGameVersionHistoryMapper extends BaseMapperPlus<OpenGameVersionHistoryMapper, OpenGameVersionHistory, OpenGameVersionHistoryVo> {

    List<OpenGameVersionHistory> selectVersionNewOne(@Param("gameIds") List<Integer> gameIds);

}
