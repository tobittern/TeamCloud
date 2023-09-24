package com.euler.sdk.service;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.dto.SignInDto;
import com.euler.sdk.api.domain.SignInVo;

/**
 * Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface ISignInService {

    /**
     * 查询列表
     *
     * @return 集合
     */
    TableDataInfo<SignInVo> queryPageList(SignInDto signInDto);

    /**
     * 修改
     *
     * @return 结果
     */
    R insertByBo(Long userId);

    /**
     * 检查用户是否签到过
     *
     * @return
     */
    R checkClick(Long userId);

}
