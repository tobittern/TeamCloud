package com.euler.system.api;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RemoteNoticeService {

    /**
     * 查询公告信息列表
     *
     * @return 公告信息
     */
    List<SysNotice> selectNoticeList();

    /**
     * 查询公告信息列表
     *
     * @param platformType 推送平台，'1':SDK，'2': 开放平台
     * @param noticeType 公告类型（1通知 2公告）
     * @param userId 用户id
     * @return 公告信息
     */
    TableDataInfo<SysNotice> selectPageNoticeList(String platformType, String noticeType, Long userId, Integer pageSize, Integer pageNum);

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNotice selectNoticeById(Integer noticeId);

    /**
     * 新增信息公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    R insertNotice(SysNotice notice);

    /**
     * 修改信息公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    R updateNotice(SysNotice notice);

    /**
     * 批量删除公告信息
     *
     * @param noticeDto 需要删除的公告ID
     * @return 结果
     */
    R deleteNoticeByIds(NoticeTypeDto noticeDto);

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    Boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId);

    /**
     * 批量置顶公告信息
     *
     * @param idTypeDto
     * @return
     */
    Boolean toTopNoticeByIds(IdTypeDto<String, String> idTypeDto);

    /**
     * 批量推送
     *
     * @param noticeDto
     * @param userId
     * @return
     */
    R toPush(NoticeTypeDto noticeDto, Long userId);

    /**
     * 获取用户未读消息数
     *
     * @param platformType
     * @return
     */
    Integer getUnReadNoticeTotalCount(String platformType);

    /**
     * 查询公告接收列表
     *
     * @param noticeId 消息公告ID
     * @param toUserId 接收人
     * @param readStatus 阅读状态 0:未读 1:已读
     * @return 公告接收集合
     */
    List<NoticeToUsers> queryList(Integer noticeId, Long toUserId, String readStatus);

}
