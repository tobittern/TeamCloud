package com.euler.platform.dubbo;

import com.euler.common.core.domain.R;
import com.euler.platform.api.RemoteCaptchaCodeService;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.service.ICaptchaCodeService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteCaptchaCodeServiceImpl implements RemoteCaptchaCodeService {
    @Autowired
    private ICaptchaCodeService captchaCodeService;

    @Override
    public CaptchaCode selectByReceiver(String receiver) {
        return captchaCodeService.selectByReceiver(receiver);
    }

    /**
     * 修改邮箱对象信息
     *
     * @param bo 邮箱bo
     * @return 更新成功与否的结果
     */
    @Override
    public Boolean updateByReceiver(CaptchaCode bo) {
        return captchaCodeService.updateByReceiver(bo);
    }

    /**
     * 验证码校验
     *
     * @param userName 邮箱或手机号
     * @param code     验证码
     */
    @Override
    public void checkCode(String userName, String code) {
        captchaCodeService.checkCode(userName, code);

    }

    /**
     * 一键登录获取手机号
     *
     * @param token
     * @return
     */
    @Override
    public R getPhoneCode(String token) {
        return captchaCodeService.getPhoneCode(token);
    }

}
