package com.euler.community.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Discover;
import com.euler.community.domain.vo.DiscoverVo;
import java.util.List;
import java.util.Map;

/**
 * 发现配置Mapper接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface DiscoverMapper extends BaseMapperPlus<DiscoverMapper, Discover, DiscoverVo> {

    List<Discover> queryDiscover(Map<String, Object> paraMap);
}
