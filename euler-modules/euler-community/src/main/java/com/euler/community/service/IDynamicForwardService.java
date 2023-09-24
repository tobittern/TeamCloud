package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.community.domain.bo.DynamicForwardBo;
import com.euler.community.domain.entity.DynamicForward;

/**
 * 动态转发Service接口
 *
 * @author euler
 * @date 2022-06-20
 */
public interface IDynamicForwardService extends IService<DynamicForward> {

    /**
     * 新增动态转发
     *
     * @param bo 动态转发
     * @return 结果
     */
    R insertByBo(DynamicForwardBo bo);

}
