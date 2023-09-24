package com.euler.community.api;

import com.euler.common.core.domain.R;

public interface RemoteCommonService {

    /**
     * @param userId
     * @return
     */
    R userCancellationClear(Long userId);

}
