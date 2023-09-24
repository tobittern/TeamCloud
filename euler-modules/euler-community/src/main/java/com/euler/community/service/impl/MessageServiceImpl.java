package com.euler.community.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.dto.MessageDto;
import com.euler.community.domain.dto.MessageTypeDto;
import com.euler.community.domain.entity.Message;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.MessageVo;
import com.euler.community.mapper.MessageMapper;
import com.euler.community.mapper.MessageToUsersMapper;
import com.euler.community.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    private final MessageMapper baseMapper;

    private final MessageToUsersMapper messageToUsersMapper;

    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    @Override
    public MessageVo queryById(Long id) {
        MessageVo vo = baseMapper.selectVoById(id);
        if (vo != null && ObjectUtil.isNotNull(vo.getContent())) {
            // 消息内容，去除标签,并且截取字符串
            vo.setContent(StringUtils.substring(HtmlUtil.cleanHtmlTag(vo.getContent()), 0, 100));
        }
        return vo;
    }

    /**
     * 查询消息列表
     *
     * @param dto 消息
     * @return 消息
     */
    @Override
    public TableDataInfo<MessageVo> queryPageList(MessageDto dto) {
        QueryWrapper<Message> lqw = Wrappers.query();
        lqw.eq(dto.getId() != null, "m.id", dto.getId());
        lqw.eq(StringUtils.isNotBlank(dto.getPlatformType()), "m.platform_type", dto.getPlatformType());
        lqw.eq(StringUtils.isNotBlank(dto.getPushUserType()), "m.push_user_type", dto.getPushUserType());
        lqw.eq(StringUtils.isNotBlank(dto.getAutoPush()), "m.auto_push", dto.getAutoPush());
        lqw.eq(StringUtils.isNotBlank(dto.getPushStatus()), "m.push_status", dto.getPushStatus());
        if (dto.getUserId() != null) {
            lqw.and(a -> a.eq("mu.to_user_id", dto.getUserId())
                .or(b -> b.eq("m.push_users", "全部用户")));
        }
        if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType())) {
            lqw.eq(StringUtils.isNotBlank(dto.getType()), "mu.type", dto.getType());
        } else {
            lqw.and(a -> a.eq("m.type", Constants.MESSAGE_TYPE_4)
                .or(b -> b.eq("m.type", "0")));
        }
        lqw.orderByDesc(StringUtils.isNotBlank(dto.getIsTop()), "m.is_top");
        lqw.orderByDesc("m.push_time");
        Page<MessageVo> result = baseMapper.selectMessageList(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(message -> {
                // 新增的时候，消息内容，去除标签,并且截取字符串
                if (ObjectUtil.isNotNull(message.getContent())) {
                    message.setContent(StringUtils.substring(HtmlUtil.cleanHtmlTag(message.getContent()), 0, 100));
                }
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 前台查询消息列表
     *
     * @param dto 消息
     * @return 消息
     */
    @Override
    public TableDataInfo<MessageVo> queryFontPageList(MessageDto dto) {
        QueryWrapper<Message> lqw = buildQueryWrapper(dto);
        Page<MessageVo> result;
        try {
            // 登录场合
            result = baseMapper.selectFontMessageList(dto.build(), lqw, dto.getType(), LoginHelper.getUserId());
        } catch (NotLoginException e) {
            // 未登录场合
            result = baseMapper.selectFontMessageListNoLogin(dto.build(), lqw, dto.getType());
        }
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(message -> {
                // 新增的时候，消息内容，去除标签,并且截取字符串
                if (ObjectUtil.isNotNull(message.getContent())) {
                    message.setContent(StringUtils.substring(HtmlUtil.cleanHtmlTag(message.getContent()), 0, 100));
                }
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询消息列表
     *
     * @param dto 消息
     * @return 消息
     */
    @Override
    public List<MessageVo> queryList(MessageDto dto) {
        QueryWrapper<Message> lqw = buildQueryWrapper(dto);
        return baseMapper.selectVoList(lqw);
    }

    private QueryWrapper<Message> buildQueryWrapper(MessageDto dto) {
        QueryWrapper<Message> lqw = Wrappers.query();
        lqw.eq(StringUtils.isNotBlank(dto.getPlatformType()), "m.platform_type", dto.getPlatformType());
        lqw.eq("m.push_status", Constants.IS_PUSH_YES);
        lqw.eq(StringUtils.isNotBlank(dto.getAutoPush()), "m.auto_push", dto.getAutoPush());
        //未读，已读数据筛选
        if (Constants.UNREAD.equals(dto.getReadStatus())) {
            lqw.and(a -> a.eq("mu.read_status", Constants.UNREAD)
                .or(b -> b.isNull("mu.read_status")));
        } else if (Constants.READ.equals(dto.getReadStatus())) {
            lqw.eq("mu.read_status", Constants.READ);
        }

        // 消息类型 1:点赞 2:评论 3:新粉丝 4:系统消息
        if (!StringUtils.equals(Constants.MESSAGE_TYPE_4, dto.getType()) && !StringUtils.equals("0", dto.getType())) {
            //推送用户为部分用户的数据
            lqw.eq("m.push_user_type", Constants.PUSH_USER_PART);
            lqw.eq("m.type", dto.getType());
            lqw.eq("mu.to_user_id", LoginHelper.getUserId());
        } else {
            try {
                lqw.and(a -> a.eq("m.type", Constants.MESSAGE_TYPE_4)
                    .or(b -> b.eq("m.type", "0")));
                lqw.and(a -> a.eq("mu.to_user_id", LoginHelper.getUserId())
                    .or(b -> b.eq("m.push_users", "全部用户")));
            } catch (NotLoginException e) {
                lqw.eq("m.push_user_type", Constants.PUSH_USER_ALL);
            }
        }

        //消息类型，推送用户为全部用户
        if (StringUtils.equals(Constants.PUSH_USER_ALL, dto.getPushUserType())) {
            lqw.eq("m.push_user_type", Constants.PUSH_USER_ALL);
        }

        // 根据已读/未读升序排列
        lqw.orderByAsc("read_status");
        // 根据置顶和推送时间来排序
        lqw.orderByDesc("m.is_top").orderByDesc("m.create_time");

        return lqw;
    }

    /**
     * 新增消息
     *
     * @param bo 消息
     * @return 结果
     */
    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(MessageBo bo) {
        Message add = BeanUtil.toBean(bo, Message.class);
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        // 发布人
        add.setPublishUser(LoginHelper.getUsername());
        boolean flag = baseMapper.insert(add) > 0;

        if (flag) {
            bo.setId(add.getId());
            // APP: 从管理后台追加的系统消息，才需要追加关联数据
            if (StringUtils.equals(Constants.MESSAGE_TYPE_4, bo.getType())
                && StringUtils.equals(Constants.IS_PUSH_NO, bo.getPushStatus())) {
                Boolean relationFlag = updateRelationData(add);
                if (relationFlag) {
                    return R.ok("新增成功");
                }
            } else {
                return R.ok("新增成功");
            }
        }
        return R.fail("新增失败");
    }


    /**
     * 修改消息
     *
     * @param bo 消息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(MessageBo bo) {
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        Message update = BeanUtil.toBean(bo, Message.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // APP: 从管理后台追加的系统消息，才需要追加关联数据
            if (StringUtils.equals(Constants.MESSAGE_TYPE_4, bo.getType())
                && StringUtils.equals(Constants.IS_PUSH_NO, bo.getPushStatus())) {
                Boolean flag = updateRelationData(update);
                if (flag) {
                    return R.ok("更新成功");
                }
            } else {
                return R.ok("更新成功");
            }
        }
        return R.fail("更新失败");
    }

    /**
     * 关联数据修正
     *
     * @param entity 消息
     */
    @SneakyThrows
    private Boolean updateRelationData(Message entity) {
        // 关联数据修正
        String pushUsers = "";
        // 推送用户是部分用户, 可以手动录入用户ID或者上传excel文件；
        if (StringUtils.equals(Constants.PUSH_USER_PART, entity.getPushUserType())) {
            List<MessageToUsers> entityList = new ArrayList<>();
            // 推送用户列表
            // 如果上传的文件为空，也没有手动入力用户id, 就会报错
            if (ObjectUtil.isNull(entity.getPushUsers())) {
                throw new ServiceException("请输入部分用户ID，至少一个");
            } else {
                // 手动录入用户ID
                pushUsers = entity.getPushUsers();
                List<String> userlist = Convert.convert(List.class, pushUsers);

                for (int i = 0; i < userlist.size(); i++) {
                    MessageToUsers users = new MessageToUsers();
                    users.setMessageId(entity.getId());
                    users.setToUserId(Convert.toLong(userlist.get(i)));
                    // 阅读状态，默认是未读
                    users.setReadStatus(Constants.UNREAD);

                    // 判断是否存在
                    LambdaQueryWrapper<MessageToUsers> eq = Wrappers.<MessageToUsers>lambdaQuery()
                        .eq(MessageToUsers::getMessageId, entity.getId())
                        .eq(MessageToUsers::getToUserId, Convert.toLong(userlist.get(i)));

                    boolean flag = messageToUsersMapper.exists(eq);
                    if (!flag) {
                        entityList.add(users);
                    }
                }
            }

            // 新增或更新到关联表
            messageToUsersMapper.insertOrUpdateBatch(entityList);
        } else {
            pushUsers = "全部用户";
        }

        // 画面展示的推送用户信息更新
        entity.setPushUsers(pushUsers);
        // 推送状态
        entity.setPushStatus(Constants.IS_PUSH_NO);

        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(Message entity) {
        //TODO 做一些数据校验,如唯一约束
        return "success";
    }

    /**
     * 批量删除消息
     *
     * @param dto 需要删除的消息dto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteMessageById(MessageTypeDto dto) {
        List<Long> ids = Convert.convert(List.class, dto.getId());

        // 消息表
        int i = baseMapper.delete(new LambdaQueryWrapper<Message>().in(Message::getId, ids).eq(StringUtils.isNotBlank(dto.getPlatformType()), Message::getPlatformType, dto.getPlatformType()));

        // 判断推送用户是否是部分用户
        if (StringUtils.equals(Constants.PUSH_USER_PART, dto.getPushUserType())) {
            // 删除关联表的信息
            int j = messageToUsersMapper.delete(
                new LambdaQueryWrapper<MessageToUsers>()
                    .in(MessageToUsers::getMessageId, ids));
        } else {
            // 全部用户
            int j = messageToUsersMapper.delete(
                new LambdaQueryWrapper<MessageToUsers>()
                    .in(MessageToUsers::getMessageId, ids)
                    .eq(MessageToUsers::getToUserId, LoginHelper.getUserId()));
        }
        return R.ok("删除成功");
    }

    /**
     * 批量置顶消息
     *
     * @param idTypeDto
     * @return
     */
    @Override
    public Boolean toTopMessageByIds(IdTypeDto<String, String> idTypeDto) {
        List<Integer> ids = Convert.convert(List.class, idTypeDto.getId());

        //idTypeDto.getType(),1:置顶，0：取消置顶
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper).in(Message::getId, ids)
            .set(Message::getIsTop, idTypeDto.getType().equals("1") ? Constants.COMMON_STATUS_YES : Constants.COMMON_STATUS_NO);
        return updateChainWrapper.update();
    }

    /**
     * 批量已读通知
     *
     * @param idTypeDto
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean toRead(IdTypeDto<String, String> idTypeDto, Long userId) {

        List<MessageToUsers> list = new ArrayList<MessageToUsers>();
        // 获取id
        List<Integer> ids = Convert.toList(Integer.class, idTypeDto.getId());
        // 全部已读场合
        if (Constants.READ.equals(idTypeDto.getType())) {
            // 检索推送给全部用户的消息列表
            var messageWrapper = new LambdaQueryWrapper<Message>();
            messageWrapper.eq(Message::getPushUserType, Constants.PUSH_USER_ALL);
            List<Message> messageList = baseMapper.selectList(messageWrapper);

            for (Message m : messageList) {
                if (!ids.contains(Convert.toInt(m.getId()))) {
                    ids.add(Convert.toInt(m.getId()));
                }

                MessageToUsers rm = new MessageToUsers();
                rm.setMessageId(Convert.toLong(m.getId()));
                rm.setReadStatus(Constants.UNREAD);
                rm.setToUserId(userId);
                list.add(rm);
            }
        } else {
            // 批量已读
            var messageWrapper = new LambdaQueryWrapper<Message>();
            messageWrapper.in(Message::getId, ids);
            List<Message> pushMessage = baseMapper.selectList(messageWrapper);
            // 判断是否是推送给全部用户的消息
            for (Message m : pushMessage) {
                if (m != null && Constants.PUSH_USER_ALL.equals(m.getPushUserType())) {
                    if (!ids.contains(Convert.toInt(m.getId()))) {
                        ids.add(Convert.toInt(m.getId()));
                    }
                    MessageToUsers rm = new MessageToUsers();
                    rm.setMessageId(Convert.toLong(m.getId()));
                    rm.setReadStatus(Constants.UNREAD);
                    rm.setToUserId(userId);
                    list.add(rm);
                }
            }
        }
        var selectWrapper = new LambdaQueryWrapper<MessageToUsers>();
        selectWrapper.eq(MessageToUsers::getToUserId, userId);

        // idTypeDto.getType，0：批量已读，1：全部已读
        if (Constants.UNREAD.equals(idTypeDto.getType())) {
            selectWrapper.in(MessageToUsers::getMessageId, ids);
        }
        // 查询那些消息我们没有入库
        List<MessageToUsers> messageToUsers = messageToUsersMapper.selectList(selectWrapper);
        // 判断那些已经存在数据库中
        List<Long> mysqlHave = new ArrayList<>();
        if (messageToUsers != null && messageToUsers.size() > 0) {
            mysqlHave = messageToUsers.stream().map(MessageToUsers::getMessageId).collect(Collectors.toList());
        }

        // 循环判断当前消息那些不存在数据库中
        for (var id : ids) {
            // 判断那些在数据库中不存在的
            if (mysqlHave.size() > 0 && !mysqlHave.contains(Convert.toLong(id))) {
                MessageToUsers rm = new MessageToUsers();
                rm.setMessageId(Convert.toLong(id));
                rm.setReadStatus(Constants.UNREAD);
                rm.setToUserId(userId);
                list.add(rm);
            }
        }

        // 判断是否存在需要入库的数据
        Integer rows = 1;
        if (list.size() > 0) {
            rows = messageToUsersMapper.insertBatch(list) ? list.size() : 0;
        }
        // 开始进行更新数据
        var updateWrapper = new LambdaUpdateChainWrapper<>(messageToUsersMapper);
        updateWrapper.eq(MessageToUsers::getToUserId, userId);
        // idTypeDto.getType，0：批量已读，1：全部已读
        if (Constants.UNREAD.equals(idTypeDto.getType())) {
            updateWrapper.in(MessageToUsers::getMessageId, ids);
        }
        updateWrapper.set(MessageToUsers::getReadStatus, Constants.READ);
        return updateWrapper.update();
    }

    /**
     * 批量推送
     *
     * @param dto
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R toPush(MessageTypeDto dto, Long userId) {
        List<Integer> ids = Convert.convert(List.class, dto.getId());
        // 消息表
        var baseWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        baseWrapper.eq(StringUtils.isNotBlank(dto.getPlatformType()), Message::getPlatformType, dto.getPlatformType());
        baseWrapper.eq(StringUtils.isNotBlank(dto.getPushUserType()), Message::getPushUserType, dto.getPushUserType());
        baseWrapper.eq(Message::getPushStatus, Constants.IS_PUSH_NO);
        baseWrapper.in(Message::getId, ids);
        baseWrapper.set(Message::getPushStatus, Constants.IS_PUSH_YES);
        baseWrapper.set(Message::getPushTime, DateUtil.date());

        baseWrapper.update();
        return R.ok("推送成功");
    }

    /**
     * 获取用户未读消息数
     *
     * @param dto
     * @param type 消息类型 '1':点赞 '2':评论 '3':新粉丝 '4':系统消息
     * @return
     */
    @Override
    public Integer getUnReadCount(MessageDto dto, String type) {
        dto.setType(type);
        dto.setReadStatus(Constants.UNREAD);
        var lqw = buildQueryWrapper(dto);
        Integer unReadCount = 0;
        try {
            // 登录场合
            unReadCount = baseMapper.getUnReadCount(lqw, LoginHelper.getUserId());
        } catch (NotLoginException e) {
        }

        if (unReadCount > 99) {
            // 消息菜单上显示具体的数字，最多显示到99
            unReadCount = 99;
        }
        return unReadCount;
    }

    /**
     * 获取用户未读消息数
     *
     * @param dto
     * @return
     */
    @Override
    public Integer getUnReadMessageCount(MessageDto dto) {
        dto.setReadStatus(Constants.UNREAD);
        var lqw = buildQueryWrapper(dto);
        Integer unReadCount = 0;
        try {
            // 登录场合
            unReadCount = baseMapper.getUnReadCount(lqw, LoginHelper.getUserId());
        } catch (NotLoginException e) {
        }

        if (unReadCount > 99) {
            // 消息菜单上显示具体的数字，最多显示到99
            unReadCount = 99;
        }
        return unReadCount;
    }
}
