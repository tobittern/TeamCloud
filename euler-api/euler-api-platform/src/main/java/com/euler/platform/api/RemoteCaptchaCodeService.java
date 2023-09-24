
package com.euler.platform.api;

import com.euler.common.core.domain.R;
import com.euler.platform.api.domain.CaptchaCode;

/**
 * @author euler
 */
public interface RemoteCaptchaCodeService {

    /**
     * 通过接收方查询
     *
     * @param receiver 接收方
     * @return 验证码对象信息
     */
    CaptchaCode selectByReceiver(String receiver);

    /**
     * 修改验证码对象信息
     *
     * @param bo 验证码
     * @return 更新成功与否的结果
     */
    Boolean updateByReceiver(CaptchaCode bo);

    /**
     * 验证码校验
     *
     * @param userName 邮箱或手机号
     * @param code     验证码
     */
    void checkCode(String userName, String code);


    /**
     * 一键登录获取手机号
     *
     * @param token
     * @return
     */
    R getPhoneCode(String token);


}
