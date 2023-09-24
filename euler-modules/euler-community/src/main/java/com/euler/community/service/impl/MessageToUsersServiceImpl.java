package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.community.domain.dto.MessageToUsersDto;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.MessageToUsersVo;
import com.euler.community.mapper.MessageToUsersMapper;
import com.euler.community.service.IMessageToUsersService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息接收Service业务层处理
 *
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
public class MessageToUsersServiceImpl extends ServiceImpl<MessageToUsersMapper, MessageToUsers> implements IMessageToUsersService {

    private final MessageToUsersMapper baseMapper;

    /**
     * 查询是否有未读消息
     *
     * @param dto 消息接收dto
     * @return 消息接收
     */
    @Override
    public MessageToUsers selectUnreadMessage(MessageToUsersDto dto) {
        MessageToUsers users = baseMapper.selectOne(new LambdaQueryWrapper<MessageToUsers>()
            .eq(dto.getMessageId()!= null, MessageToUsers::getMessageId, dto.getMessageId())
            .eq(dto.getToUserId() != null, MessageToUsers::getToUserId, dto.getToUserId())
            .eq(StringUtils.isNotBlank(dto.getType()), MessageToUsers::getType, dto.getType())
            .eq(StringUtils.isNotBlank(dto.getReadStatus()), MessageToUsers::getReadStatus, dto.getReadStatus()).last("limit 1"));

        return users;
    }

    /**
     * 查询消息接收列表
     *
     * @param dto 消息接收
     * @return 消息接收集合
     */
    public List<MessageToUsersVo> queryList(MessageToUsersDto dto){
        LambdaQueryWrapper<MessageToUsers> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MessageToUsers> buildQueryWrapper(MessageToUsersDto dto) {
        LambdaQueryWrapper<MessageToUsers> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMessageId() != null, MessageToUsers::getMessageId, dto.getMessageId());
        lqw.eq(dto.getToUserId() != null, MessageToUsers::getToUserId, dto.getToUserId());
        lqw.eq(StringUtils.isNotBlank(dto.getType()), MessageToUsers::getType, dto.getType());
        lqw.eq(StringUtils.isNotBlank(dto.getReadStatus()), MessageToUsers::getReadStatus, dto.getReadStatus());
        // 按时间倒序排列
        lqw.orderByDesc(MessageToUsers::getCreateTime);
        return lqw;
    }

    /**
     * 新增消息
     *
     * @param entity 消息
     * @return 结果
     */
    public Boolean insertMessage(MessageToUsers entity){
        Boolean flag = baseMapper.insert(entity) > 0;
        return flag;
    }

    /**
     * 更新消息
     *
     * @param entity 消息
     * @return 结果
     */
    public Boolean updateMessage(MessageToUsers entity){

        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(entity.getToUserId() != null, MessageToUsers::getToUserId, entity.getToUserId())
            .eq(MessageToUsers::getType, entity.getType())
            .set(entity.getRelationId() != null, MessageToUsers::getRelationId, entity.getRelationId())
            .set(MessageToUsers::getReadStatus, entity.getReadStatus());

        return updateChainWrapper.update();
    }

}
