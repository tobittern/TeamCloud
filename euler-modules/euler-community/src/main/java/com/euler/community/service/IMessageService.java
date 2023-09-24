package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.dto.MessageDto;
import com.euler.community.domain.dto.MessageTypeDto;
import com.euler.community.domain.entity.Message;
import com.euler.community.domain.vo.MessageVo;

import java.util.List;

/**
 * 消息Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IMessageService extends IService<Message> {

    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    MessageVo queryById(Long id);

    /**
     * 查询消息列表
     *
     * @param dto 消息
     * @return 消息集合
     */
    TableDataInfo<MessageVo> queryPageList(MessageDto dto);

    /**
     * 前台查询消息列表
     *
     * @param dto 消息
     * @return 消息集合
     */
    TableDataInfo<MessageVo> queryFontPageList(MessageDto dto);

    /**
     * 查询消息列表
     *
     * @param dto 消息
     * @return 消息集合
     */
    List<MessageVo> queryList(MessageDto dto);

    /**
     * 新增消息
     *
     * @param bo 消息
     * @return 结果
     */
    R insertByBo(MessageBo bo);

    /**
     * 修改消息
     *
     * @param bo 消息
     * @return 结果
     */
    R updateByBo(MessageBo bo);

    /**
     * 删除消息信息
     *
     * @param dto 需要删除的消息dto
     * @return 结果
     */
    R deleteMessageById(MessageTypeDto dto);

    /**
     * 批量置顶公告信息
     *
     * @param idTypeDto
     * @return
     */
    Boolean toTopMessageByIds(IdTypeDto<String, String> idTypeDto);

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    Boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId);

    /**
     * 批量推送
     *
     * @param dto
     * @param userId
     * @return
     */
    R toPush(MessageTypeDto dto, Long userId);

    /**
     * 获取用户未读消息数
     *
     * @param dto
     * @param type 消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息
     * @return
     */
    Integer getUnReadCount(MessageDto dto, String type);

    /**
     * 获取用户未读消息数
     *
     * @param dto
     * @return
     */
    Integer getUnReadMessageCount(MessageDto dto);

}
