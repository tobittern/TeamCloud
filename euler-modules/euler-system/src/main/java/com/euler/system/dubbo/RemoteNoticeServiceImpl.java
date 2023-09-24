package com.euler.system.dubbo;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.api.RemoteNoticeService;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.api.domain.NoticeTypeDto;
import com.euler.system.api.domain.SysNotice;
import com.euler.system.domain.dto.FrontNoticePageDto;
import com.euler.system.domain.dto.NoticePageDto;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.domain.vo.NoticeToUsersVo;
import com.euler.system.service.INoticeToUsersService;
import com.euler.system.service.ISysNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
@DubboService
public class RemoteNoticeServiceImpl implements RemoteNoticeService {

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private INoticeToUsersService iNoticeToUsersService;

    /**
     * 查询公告信息列表
     *
     * @return 公告信息
     */
    public List<SysNotice> selectNoticeList(){
        return noticeService.selectNoticeList();
    }

    /**
     * 查询公告信息列表
     *
     * @param platformType 推送平台，'1':SDK，'2': 开放平台
     * @param noticeType 公告类型（1通知 2公告）
     * @param userId 用户id
     * @return 公告信息
     */
    public TableDataInfo<SysNotice> selectPageNoticeList(String platformType, String noticeType, Long userId, Integer pageSize, Integer pageNum) {
        NoticePageDto dto = new NoticePageDto();
        dto.setPlatformType(platformType);
        dto.setNoticeType(noticeType);
        dto.setUserId(userId);
        dto.setPageSize(pageSize);
        dto.setPageNum(pageNum);
        return noticeService.selectPageNoticeList(dto);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(Integer noticeId) {
        return noticeService.selectNoticeById(noticeId);
    }

    /**
     * 新增信息公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @SneakyThrows
    public R insertNotice(SysNotice notice) {
        return noticeService.insertNotice(notice);
    }

    /**
     * 修改信息公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @SneakyThrows
    public R updateNotice(SysNotice notice) {
        return noticeService.updateNotice(notice);
    }

    /**
     * 批量删除公告信息
     *
     * @param dto 需要删除的公告ID
     * @return 结果
     */
    public R deleteNoticeByIds(NoticeTypeDto dto) {
        return noticeService.deleteNoticeByIds(dto);
    }

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    public Boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId) {
        return noticeService.toRead(idTypeDto, userId);
    }

    /**
     * 批量置顶公告信息
     *
     * @param idTypeDto
     * @return
     */
    public Boolean toTopNoticeByIds(IdTypeDto<String, String> idTypeDto) {
        return noticeService.toTopNoticeByIds(idTypeDto);
    }

    /**
     * 批量推送
     *
     * @param noticeDto
     * @param userId
     * @return
     */
    public R toPush(NoticeTypeDto noticeDto, Long userId) {
        return noticeService.toPush(noticeDto, userId);
    }

    /**
     * 获取用户未读消息数
     *
     * @param platformType
     * @return
     */
    public Integer getUnReadNoticeTotalCount(String platformType) {
        FrontNoticePageDto dto = new FrontNoticePageDto();
        dto.setPlatformType(platformType);
        dto.setUserId(LoginHelper.getUserId());
        return noticeService.getUnReadNoticeTotalCount(dto);
    }

    /**
     * 查询公告接收列表
     *
     * @param noticeId 消息公告ID
     * @param toUserId 接收人
     * @param readStatus 阅读状态 0:未读 1:已读
     *
     * @return 公告接收集合
     */
    public List<NoticeToUsers> queryList(Integer noticeId, Long toUserId, String readStatus) {
        NoticeToUsersDto dto = new NoticeToUsersDto();
        dto.setNoticeId(noticeId);
        dto.setToUserId(toUserId);
        dto.setReadStatus(readStatus);
        List<NoticeToUsersVo> voList = iNoticeToUsersService.queryList(dto);
        return BeanCopyUtils.copyList(voList, NoticeToUsers.class);
    }
}
