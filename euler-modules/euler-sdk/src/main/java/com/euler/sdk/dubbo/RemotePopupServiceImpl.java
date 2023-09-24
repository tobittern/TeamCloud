package com.euler.sdk.dubbo;

import com.euler.sdk.api.RemotePopupService;
import com.euler.sdk.api.domain.dto.SdkPopupDto;
import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.service.IPopupService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemotePopupServiceImpl implements RemotePopupService {
    @Autowired
    private IPopupService iPopupService;

    /**
     * 根据渠道code查询出分包渠道的基础信息
     *
     * @return 渠道分包信息
     */
    @Override
    public List<SdkPopupVo> selectPopupList(SdkPopupDto dto) {
        return iPopupService.queryList(dto);
    }
}
