package com.euler.system.api;

import com.euler.system.api.domain.NoticeToUsers;

public interface RemoteNoticeToUsersService {

    /**
     * 查询未读的消息
     *
     * @param noticeId
     * @param toUserId
     * @param readStatus
     * @return
     */
    NoticeToUsers selectUnreadNotice(Integer noticeId, Long toUserId, String readStatus);

    /**
     * 更新消息
     *
     * @param users
     * @return
     */
    Boolean save(NoticeToUsers users);

}
