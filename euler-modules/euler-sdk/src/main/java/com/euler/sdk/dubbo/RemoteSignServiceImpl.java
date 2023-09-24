package com.euler.sdk.dubbo;

import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.RemoteSignService;
import com.euler.sdk.api.domain.SignInVo;
import com.euler.sdk.api.domain.dto.SignInDto;
import com.euler.sdk.service.ISignInService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteSignServiceImpl implements RemoteSignService {

    @Autowired
    private ISignInService iSignInService;

    /**
     * 获取签到列表
     *
     * @param signInDto
     * @return
     */
    public TableDataInfo<SignInVo> queryPageList(SignInDto signInDto) {
        return iSignInService.queryPageList(signInDto);
    }

    /**
     * 签到
     *
     * @param userId
     * @return
     */
    public R insertByBo(Long userId) {
        return iSignInService.insertByBo(userId);
    }

    /**
     * 检查用户是否签到过
     *
     * @param userId
     * @return
     */
    public R checkClick(Long userId) {
        return iSignInService.checkClick(userId);
    }

}
