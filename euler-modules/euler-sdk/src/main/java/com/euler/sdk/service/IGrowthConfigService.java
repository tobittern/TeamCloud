package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.sdk.domain.vo.GrowthConfigVo;
import com.euler.sdk.domain.bo.GrowthConfigBo;

/**
 * 成长值配置Service接口
 *
 * @author euler
 * @date 2022-03-25
 */
public interface IGrowthConfigService {

    /**
     * 根据成长类型和等级查询成长值配置信息
     *
     * @param type 成长类型
     * @param grade 成长等级
     * @return 成长值配置信息
     */
    GrowthConfigVo queryByPrams(int type, int grade);

    /**
     * 修改成长值配置
     *
     *
     * @param bo 成长值配置
     * @return 结果
     */
    R updateByBo(GrowthConfigBo bo);

}
