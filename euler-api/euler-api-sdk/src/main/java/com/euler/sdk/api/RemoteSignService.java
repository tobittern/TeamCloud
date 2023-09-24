package com.euler.sdk.api;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.SignInVo;
import com.euler.sdk.api.domain.dto.SignInDto;

public interface RemoteSignService {

    TableDataInfo<SignInVo> queryPageList(SignInDto signInDto);

    R insertByBo(Long userId);

    R checkClick(Long userId);
}
