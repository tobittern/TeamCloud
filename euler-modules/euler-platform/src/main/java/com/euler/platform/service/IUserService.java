package com.euler.platform.service;

import com.euler.common.core.domain.R;
import com.euler.platform.domain.dto.FrontUserUpdateDto;
import com.euler.platform.domain.vo.UserVo;

/**
 * 【用户管理】Service接口
 *
 * @author open
 * @date 2022-02-18
 */
public interface IUserService {

    /**
     * 获取用户基础信息
     *
     * @param userId
     * @return
     */
    R<UserVo> getInfo(Long userId);

    /**
     * 修改用户基础信息
     *
     * @param frontUserUpdateDto
     * @return
     */
    R edit(FrontUserUpdateDto frontUserUpdateDto);
}
