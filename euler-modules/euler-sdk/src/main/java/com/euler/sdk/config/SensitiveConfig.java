package com.euler.sdk.config;

import com.euler.common.core.service.SensitiveService;
import com.euler.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 * @author euler
 */
@Service
public class SensitiveConfig implements SensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive() {
        return !LoginHelper.isAdmin();
    }

}
