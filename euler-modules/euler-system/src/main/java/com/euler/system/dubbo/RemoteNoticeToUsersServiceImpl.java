package com.euler.system.dubbo;

import com.euler.system.api.RemoteNoticeToUsersService;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.service.INoticeToUsersService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteNoticeToUsersServiceImpl implements RemoteNoticeToUsersService {

    @Autowired
    private INoticeToUsersService noticeToUsersService;

    /**
     * 查询未读的消息
     *
     * @param noticeId
     * @param toUserId
     * @param readStatus
     * @return
     */
    @Override
    public NoticeToUsers selectUnreadNotice(Integer noticeId, Long toUserId, String readStatus) {
        NoticeToUsersDto dto = new NoticeToUsersDto();
        dto.setNoticeId(noticeId);
        dto.setToUserId(toUserId);
        dto.setReadStatus(readStatus);
        return noticeToUsersService.selectUnreadNotice(dto);
    }

    /**
     * 更新消息
     *
     * @param users
     * @return
     */
    public Boolean save(NoticeToUsers users) {
        return noticeToUsersService.save(users);
    }

}
