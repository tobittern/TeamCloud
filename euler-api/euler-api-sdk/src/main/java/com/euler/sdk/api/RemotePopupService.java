package com.euler.sdk.api;

import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.api.domain.dto.SdkPopupDto;

import java.util.List;

public interface RemotePopupService {
    /**
     * 查询指定的弹框数据
     *
     * @param dto
     * @return
     */
    List<SdkPopupVo> selectPopupList(SdkPopupDto dto);
}
