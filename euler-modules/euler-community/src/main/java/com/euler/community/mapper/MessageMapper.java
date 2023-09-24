package com.euler.community.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.community.domain.entity.Message;
import com.euler.community.domain.vo.MessageVo;
import org.apache.ibatis.annotations.Param;

/**
 * 消息Mapper接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface MessageMapper extends BaseMapperPlus<MessageMapper, Message, MessageVo> {

    /**
     * 查询未读消息的件数
     * @param queryWrapper
     * @param userId
     * @return
     */
    Integer getUnReadCount(@Param(Constants.WRAPPER) Wrapper<Message> queryWrapper, @Param("userId") Long userId);

    /**
     * 查询消息列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<MessageVo> selectMessageList(@Param("page") Page<Message> page, @Param(Constants.WRAPPER) Wrapper<Message> queryWrapper);

    /**
     * 查询消息列表(前台展示用)
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<MessageVo> selectFontMessageList(@Param("page") Page<Message> page, @Param(Constants.WRAPPER) Wrapper<Message> queryWrapper, @Param("type") String type, @Param("userId") Long userId);

    /**
     * 查询消息列表(前台展示用-系统消息，未登录)
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<MessageVo> selectFontMessageListNoLogin(@Param("page") Page<Message> page, @Param(Constants.WRAPPER) Wrapper<Message> queryWrapper, @Param("type") String type);

}
