package com.euler.sdk.service;

import com.euler.sdk.domain.bo.SignConfigBo;
import com.euler.sdk.domain.vo.SignConfigVo;

import java.util.List;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface ISignConfigService {

    /**
     * 查询列表
     *
     * @return 集合
     */
    List<SignConfigVo> queryList();

    /**
     * 修改
     *
     * @return 结果
     */
    Boolean updateByBo(SignConfigBo bo);

}
