package com.euler.system.api;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;

/**
 * 敏感词服务
 *
 * @author euler
 */
public interface RemoteAuditKeywordService {

    R systemCheck(String checkString, Integer type);

}
