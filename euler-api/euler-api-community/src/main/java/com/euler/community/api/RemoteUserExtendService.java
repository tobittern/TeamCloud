package com.euler.community.api;

import com.euler.common.core.domain.R;
import com.euler.community.api.domain.UserExtend;

/**
 * @author euler
 * @date 2022-06-01
 */
public interface RemoteUserExtendService {

    /**
     * 修改用户信息
     */
    R updateUserDetail(UserExtend entity);

}
