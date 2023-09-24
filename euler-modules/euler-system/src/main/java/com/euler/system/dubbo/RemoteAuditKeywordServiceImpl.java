package com.euler.system.dubbo;

import com.euler.common.core.domain.R;
import com.euler.system.api.RemoteAuditKeywordService;
import com.euler.system.service.ISysAuditKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@RequiredArgsConstructor
@Service
@DubboService
@Slf4j
public class RemoteAuditKeywordServiceImpl implements RemoteAuditKeywordService {

    @Autowired
    private ISysAuditKeywordService iSysAuditKeywordService;

    /**
     * 校验是否存在敏感词
     *
     * @return
     */
    @Override
    public R systemCheck(String checkString, Integer type) {
        return iSysAuditKeywordService.check(checkString, type);
    }


}
