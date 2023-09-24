package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.community.domain.dto.MessageToUsersDto;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.MessageToUsersVo;
import com.euler.community.enums.MessageTypeEnum;

import java.util.List;

/**
 * 消息接收Service接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface IMessageToUsersService extends IService<MessageToUsers> {

    /**
     * 查询是否有未读消息
     *
     * @param dto 公告接收dto
     * @return 公告接收
     */
    MessageToUsers selectUnreadMessage(MessageToUsersDto dto);

    /**
     * 查询消息接收列表
     *
     * @param dto 消息接收
     * @return 消息接收集合
     */
    List<MessageToUsersVo> queryList(MessageToUsersDto dto);

    /**
     * 新增消息
     *
     * @param entity 消息
     * @return 结果
     */
    Boolean insertMessage(MessageToUsers entity);

    /**
     * 更新消息
     *
     * @param entity 消息
     * @return 结果
     */
    Boolean updateMessage(MessageToUsers entity);

}
