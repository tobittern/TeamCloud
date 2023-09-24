package com.euler.sdk.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.sdk.domain.entity.GrowthConfig;
import com.euler.sdk.domain.vo.GrowthConfigVo;

/**
 * 成长值配置Mapper接口
 *
 * @author euler
 * @date 2022-03-25
 */
public interface GrowthConfigMapper extends BaseMapperPlus<GrowthConfigMapper, GrowthConfig, GrowthConfigVo> {

    /**
     * 根基成长值类型和等级查询成长值配置信息
     *
     * @param type 成长类型
     * @param grade 成长等级
     * @return 成长值配置信息
     */
    GrowthConfigVo selectVoByPrams(int type, int grade);
}
