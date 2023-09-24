package com.euler.community.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Shield;
import com.euler.community.domain.vo.ShieldVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 屏蔽信息Mapper接口
 *
 * @author euler
 * @date 2022-09-15
 */
@Mapper
public interface ShieldMapper extends BaseMapperPlus<ShieldMapper, Shield, ShieldVo> {

}
