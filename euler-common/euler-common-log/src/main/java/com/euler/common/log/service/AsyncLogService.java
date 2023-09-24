package com.euler.common.log.service;

import com.euler.system.api.RemoteLogService;
import com.euler.system.api.domain.SysOperLog;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author euler
 */
@Service
public class AsyncLogService {

    @DubboReference
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) {
        remoteLogService.saveLog(sysOperLog);
    }
}
