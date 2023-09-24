package com.euler.system.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import com.euler.system.api.domain.UserNoticeVo;
import com.euler.system.domain.dto.FrontNoticePageDto;
import com.euler.system.domain.dto.NoticePageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author euler
 */
public interface ISysNoticeService {

    /**
     * 查询公告信息列表
     *
     * @return 公告信息
     */
    List<SysNotice> selectNoticeList();

    /**
     * 查询公告信息列表
     *
     * @param noticePageDto 公告信息
     * @return 公告信息
     */
    TableDataInfo<SysNotice> selectPageNoticeList(NoticePageDto noticePageDto);

    TableDataInfo<UserNoticeVo> selectFrontPageNoticeList(FrontNoticePageDto noticePageDto);

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNotice selectNoticeById(Integer noticeId);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    R insertNotice(SysNotice notice);

    /**
     * 修改公告
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
     * 批量置顶公告信息
     *
     * @param idTypeDto
     * @return
     */
    boolean toTopNoticeByIds(IdTypeDto<String, String> idTypeDto);

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId);

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
     * @param noticePageDto
     * @return
     */
    Integer getUnReadNoticeTotalCount(FrontNoticePageDto noticePageDto);

}
