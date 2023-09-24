package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.sdk.domain.entity.SignConfig;
import com.euler.sdk.domain.bo.SignConfigBo;
import com.euler.sdk.domain.vo.SignConfigVo;
import com.euler.sdk.mapper.SignConfigMapper;
import com.euler.sdk.service.ISignConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@RequiredArgsConstructor
@Service
public class SignConfigServiceImpl implements ISignConfigService {

    private final SignConfigMapper baseMapper;


    /**
     * 查询签到配置列表
     *
     * @return
     */
    @Override
    public List<SignConfigVo> queryList() {
        LambdaQueryWrapper<SignConfig> lqw = Wrappers.lambdaQuery();
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 修改签到配置
     *
     * @param bo 修改签到配置
     * @return 结果
     */
    @Override
    public Boolean updateByBo(SignConfigBo bo) {
        SignConfig update = BeanUtil.toBean(bo, SignConfig.class);
        if (update.getScore() <= 0) {
            return Boolean.FALSE;
        }
        return baseMapper.updateById(update) > 0;
    }

}
