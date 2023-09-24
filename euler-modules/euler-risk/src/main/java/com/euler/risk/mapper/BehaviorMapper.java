package com.euler.risk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.risk.api.domain.Behavior;
import com.euler.risk.domain.vo.BehaviorVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后端用户行为上报数据Mapper接口
 *
 * @author euler
 * @date 2022-08-24
 */
@Mapper
public interface BehaviorMapper extends BaseMapperPlus<BehaviorMapper, Behavior, BehaviorVo> {

}
