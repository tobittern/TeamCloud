package com.euler.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.system.api.domain.NoticeToUsers;
import com.euler.system.domain.dto.NoticeToUsersDto;
import com.euler.system.domain.vo.NoticeToUsersVo;
import com.euler.system.domain.bo.NoticeToUsersBo;

import java.util.List;

/**
 * 公告接收Service接口
 *
 * @author euler
 * @date 2022-04-08
 */
public interface INoticeToUsersService extends IService<NoticeToUsers> {

    /**
     * 查询是否有未读消息
     *
     * @param dto 公告接收dto
     * @return 公告接收
     */
    NoticeToUsers selectUnreadNotice(NoticeToUsersDto dto);

    /**
     * 查询公告接收
     *
     * @param id 公告接收主键
     * @return 公告接收
     */
    NoticeToUsersVo queryById(Integer id);

    /**
     * 根据公告id查询公告接收者列表
     *
     * @param noticeId 公告id
     * @return 公告接收
     */
    List<NoticeToUsersVo> selectVoByNoticeId(Integer noticeId);

    /**
     * 查询公告接收列表
     *
     * @param dto 公告接收
     * @return 公告接收集合
     */
    List<NoticeToUsersVo> queryList(NoticeToUsersDto dto);

    /**
     * 修改公告接收
     *
     * @param bo 公告接收
     * @return 结果
     */
    Boolean insertByBo(NoticeToUsersBo bo);

    /**
     * 修改公告接收
     *
     * @param bo 公告接收
     * @return 结果
     */
    Boolean updateByBo(NoticeToUsersBo bo);


}
