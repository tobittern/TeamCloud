package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.euler.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.euler.sdk.domain.bo.GrowthConfigBo;
import com.euler.sdk.domain.vo.GrowthConfigVo;
import com.euler.sdk.domain.entity.GrowthConfig;
import com.euler.sdk.mapper.GrowthConfigMapper;
import com.euler.sdk.service.IGrowthConfigService;

/**
 * 成长值配置Service业务层处理
 *
 * @author euler
 * @date 2022-03-25
 */
@RequiredArgsConstructor
@Service
public class GrowthConfigServiceImpl implements IGrowthConfigService {

    private final GrowthConfigMapper baseMapper;

    /**
     * 根基成长值类型和等级查询成长值配置信息
     *
     * @param type 成长类型
     * @param grade 成长等级
     * @return 成长值配置信息
     */
    @Override
    public GrowthConfigVo queryByPrams(int type, int grade){
        return baseMapper.selectVoByPrams(type, grade);
    }

    /**
     * 修改成长值配置
     *
     * @param bo 成长值配置
     * @return 结果
     */
    @Override
    public R updateByBo(GrowthConfigBo bo) {
        GrowthConfig update = BeanUtil.toBean(bo, GrowthConfig.class);
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail("修改失败");
    }
}
