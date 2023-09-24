package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.api.domain.UserExtend;
import com.euler.community.domain.bo.AttentionBo;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.dto.AttentionDto;
import com.euler.community.domain.entity.Attention;
import com.euler.community.domain.entity.Message;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.AttentionVo;
import com.euler.community.mapper.AttentionMapper;
import com.euler.community.mapper.MessageMapper;
import com.euler.community.mapper.MessageToUsersMapper;
import com.euler.community.mapper.UserExtendMapper;
import com.euler.community.service.IAttentionService;
import com.euler.community.service.IMessageService;
import com.euler.community.service.IMessageToUsersService;
import com.euler.community.service.IUserBehaviorService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 关注Service业务层处理
 *
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
public class AttentionServiceImpl extends ServiceImpl<AttentionMapper, Attention> implements IAttentionService {

    private final AttentionMapper baseMapper;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IMessageToUsersService messageToUsersService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private UserExtendMapper attentionUserMapper;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageToUsersMapper messageToUsersMapper;

    /**
     * 查询自己的关注列表
     *
     * @return 自己的关注列表
     */
    @Override
    public List<AttentionVo> queryMyAttentionList() {
        List<AttentionVo> result = baseMapper.getMyAttentionList(LoginHelper.getUserId());
        if (result != null && !result.isEmpty()) {
            result.forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
            });
        }
        return result;
    }

    /**
     * 查询自己的粉丝列表
     *
     * @return 自己的粉丝列表
     */
    @Override
    public List<AttentionVo> queryMyFansList() {
        List<AttentionVo> result = baseMapper.getMyFansList(LoginHelper.getUserId());
        if (result != null && !result.isEmpty()) {
            result.forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
                // 查看我对该用户的关注状态
                setAttentionStatus(a, LoginHelper.getUserId(), a.getMemberId());
            });
        }
        return result;
    }

    /**
     * 查询自己的关注列表
     *
     * @return 自己的关注列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryMyAttentionList(AttentionDto dto) {
        Page<AttentionVo> result = baseMapper.getMyAttentionList(dto.build(), dto.getMemberId());
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询自己的粉丝列表
     *
     * @return 自己的粉丝列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryMyFansList(AttentionDto dto) {
        Page<AttentionVo> result = baseMapper.getMyFansList(dto.build(), dto.getAttentionUserId());
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
                // 查看我对该用户的关注状态
                setAttentionStatus(a, LoginHelper.getUserId(), a.getMemberId());
            });
        }
        // 点击粉丝消息，进入粉丝列表，视为已读所有粉丝消息；
        MessageToUsers entity = new MessageToUsers();
        entity.setToUserId(dto.getAttentionUserId());
        entity.setType(Constants.MESSAGE_TYPE_3);
        entity.setReadStatus(Constants.READ);
        messageToUsersService.updateMessage(entity);

        return TableDataInfo.build(result);
    }


    /**
     * 查询他人的关注列表
     *
     * @param dto
     * @return 他人的关注列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryOthersAttentionList(AttentionDto dto) {
        // 判断用户是否存在
        MemberProfile member = remoteMemberService.getMemberByUserId(dto.getMemberId());
        if (ObjectUtil.isNull(member)) {
            throw new ServiceException("用户不存在");
        }

        List<AttentionVo> list = baseMapper.getOthersAttentionList(dto.getMemberId());
        if (list != null && list.size() > 0) {
            list.forEach(a -> {
                a.setMemberId(dto.getMemberId());

                LambdaQueryWrapper<Attention> eq = Wrappers.<Attention>lambdaQuery()
                    .select(Attention::getId)
                    .eq(Attention::getMemberId, a.getMemberId())
                    .eq(Attention::getAttentionUserId, a.getAttentionUserId());

                AttentionVo vo = baseMapper.selectVoOne(eq);
                if (ObjectUtil.isNotNull(vo)) {
                    a.setId(vo.getId());
                }
                // 设置头像
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
                try {
                    // 查看我对该用户的关注状态
                    setAttentionStatus(a, LoginHelper.getUserId(), a.getAttentionUserId());
                } catch (Exception userNoLoginException) {
                    // 未登录场合：对该用户的关注状态, 默认是未关注
                    a.setStatus(Constants.ATTENTION_STATUS_1);
                }
            });
        }
        // 手动分页
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        if (dto.getPageNum() == null) {
            dto.setPageNum(0);
        }
        int total= list.size();
        list = getPageList(list, dto.getPageSize(), dto.getPageNum());

        TableDataInfo<AttentionVo> build = TableDataInfo.build(list);
        build.setTotal(total);
        build.setRows(list);
        return build;
    }

    /**
     * 查询他人的粉丝列表
     *
     * @param dto
     * @return 他人的粉丝列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryOthersFansList(AttentionDto dto) {
        // 判断用户是否存在
        MemberProfile member = remoteMemberService.getMemberByUserId(dto.getMemberId());
        if (ObjectUtil.isNull(member)) {
            throw new ServiceException("用户不存在");
        }

        List<AttentionVo> list = baseMapper.getOthersFansList(dto.getMemberId());
        if (list != null && list.size() > 0) {
            list.forEach(a -> {
                a.setAttentionUserId(dto.getMemberId());

                LambdaQueryWrapper<Attention> eq = Wrappers.<Attention>lambdaQuery()
                    .select(Attention::getId)
                    .eq(Attention::getMemberId, a.getMemberId())
                    .eq(Attention::getAttentionUserId, a.getAttentionUserId());

                AttentionVo vo = baseMapper.selectVoOne(eq);
                if (ObjectUtil.isNotNull(vo)) {
                    a.setId(vo.getId());
                }
                // 设置头像
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
                try {
                    // 查看我对该用户的关注状态
                    setAttentionStatus(a, LoginHelper.getUserId(), a.getMemberId());
                } catch (Exception userNoLoginException) {
                    // 未登录场合：对该用户的关注状态, 默认是未关注
                    a.setStatus(Constants.ATTENTION_STATUS_1);
                }
            });
        }

        // 手动分页
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        if (dto.getPageNum() == null) {
            dto.setPageNum(0);
        }
        int total= list.size();
        list = getPageList(list, dto.getPageSize(), dto.getPageNum());

        TableDataInfo<AttentionVo> build = TableDataInfo.build(list);
        build.setTotal(total);
        build.setRows(list);
        return build;
    }

    /**
     * 查询我的新粉丝列表
     *
     * @return 新粉丝列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryMyNewFansList(AttentionDto dto) {
        QueryWrapper<Attention> lqw = Wrappers.query();
        lqw.eq("a.attention_user_id", dto.getAttentionUserId());
        lqw.and(a -> a.eq("a.status", Constants.ATTENTION_STATUS_2)
            .or(b -> b.eq("a.status", Constants.ATTENTION_STATUS_3)));
        // 按照时间倒序排列
        lqw.orderByAsc("u.read_status");
        lqw.orderByDesc("a.create_time");

        Page<AttentionVo> result = baseMapper.getMyNewAttentionList(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 新增关注
     *
     * @param bo 关注
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(AttentionBo bo) {
        Attention add = BeanUtil.toBean(bo, Attention.class);

        String result = validEntityBeforeSave(add, true);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        // 查询该用户是否被关注
        Boolean attentionFlag = isAttention(add.getAttentionUserId(), add.getMemberId());

        if (attentionFlag) {
            add.setStatus(Constants.ATTENTION_STATUS_3);
        } else {
            add.setStatus(Constants.ATTENTION_STATUS_2);
        }

        // 判断用户和他人的关系
        AttentionDto dto = new AttentionDto();
        dto.setMemberId(add.getMemberId());
        dto.setAttentionUserId(add.getAttentionUserId());
        AttentionVo vo = getUserFansIfExist(dto);

        boolean flag;
        if (ObjectUtil.isNull(vo)) {
            // 不存在就插入
            flag = baseMapper.insert(add) > 0;
        } else {
            add.setId(vo.getId());
            // 如果存在就更新
            var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Attention::getMemberId, add.getMemberId())
                .eq(Attention::getAttentionUserId, add.getAttentionUserId())
                .set(Attention::getStatus, add.getStatus())
                .set(Attention::getUpdateTime, new Date())
                .set(Attention::getUpdateBy, add.getMemberId());

            flag = updateChainWrapper.update();
        }

        if (flag) {
            bo.setId(add.getId());

            // 更新状态(已关注-->互粉)
            if (attentionFlag) {

                var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                    .eq(Attention::getMemberId, add.getAttentionUserId())
                    .eq(Attention::getAttentionUserId, add.getMemberId())
                    .set(Attention::getStatus, Constants.ATTENTION_STATUS_3)
                    .set(Attention::getUpdateTime, new Date())
                    .set(Attention::getUpdateBy, add.getAttentionUserId());

                updateChainWrapper.update();
            }

            // 判断用户关联表里是否有关注用户
            boolean memberExist = attentionUserMapper.exists(new LambdaQueryWrapper<UserExtend>()
                .eq(UserExtend::getMemberId, bo.getMemberId()));

            if (!memberExist) {
                // 把用户id和昵称新增到关联表里
                UserExtend entity = new UserExtend();
                entity.setMemberId(bo.getMemberId());
                // 查询关注用户的昵称和头像
                MemberProfile memberInfo = remoteMemberService.getMemberByUserId(add.getMemberId());
                if (ObjectUtil.isNotNull(memberInfo)) {
                    // 昵称，理论上不可能为空
                    entity.setNickName(memberInfo.getNickName());
                    // 性别
                    entity.setSex(memberInfo.getSex());
                    // 设置关注头像
                    entity.setAvatar(iUserBehaviorService.getAvatar(memberInfo.getSex(), memberInfo.getAvatar()));
                }
                attentionUserMapper.insert(entity);
            }

            // 判断用户关联表里是否有被关注用户
            boolean attentionExist = attentionUserMapper.exists(new LambdaQueryWrapper<UserExtend>()
                .eq(UserExtend::getMemberId, bo.getAttentionUserId()));

            if (!attentionExist) {
                // 把用户id和昵称新增到关联表里
                UserExtend entity = new UserExtend();
                entity.setMemberId(bo.getAttentionUserId());
                // 查询关注用户的昵称和头像
                MemberProfile memberInfo = remoteMemberService.getMemberByUserId(add.getAttentionUserId());
                if (ObjectUtil.isNotNull(memberInfo)) {
                    // 昵称，理论上不可能为空
                    entity.setNickName(memberInfo.getNickName());
                    // 性别
                    entity.setSex(memberInfo.getSex());
                    // 设置关注头像
                    entity.setAvatar(iUserBehaviorService.getAvatar(memberInfo.getSex(), memberInfo.getAvatar()));
                }
                attentionUserMapper.insert(entity);
            }
            // 更新消息数据
            MessageBo messageBo = new MessageBo();
            messageBo.setTitle("新粉丝关注消息");
            messageBo.setContent("<p>新粉丝消息</p>");
            messageBo.setPlatformType(Constants.PLATFORM_TYPE_APP);
            messageBo.setPushUserType(Constants.PUSH_USER_PART);
            messageBo.setPushUsers(bo.getAttentionUserId().toString());
            messageBo.setPushTime(new Date());
            messageBo.setPushStatus(Constants.IS_PUSH_YES);
            messageBo.setType(Constants.MESSAGE_TYPE_3);
            messageService.insertByBo(messageBo);

            MessageToUsers entity = new MessageToUsers();
            entity.setMessageId(messageBo.getId());
            entity.setToUserId(bo.getAttentionUserId());
            entity.setRelationId(add.getId());
            entity.setType(Constants.MESSAGE_TYPE_3);
            entity.setReadStatus(Constants.UNREAD);
            messageToUsersService.insertMessage(entity);
        }
        return R.ok("关注成功");
    }

    /**
     * 取消关注
     *
     * @param bo 关注
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByBo(AttentionBo bo) {

        Attention update = BeanUtil.toBean(bo, Attention.class);
        String result = validEntityBeforeSave(update, false);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        var updateWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Attention::getMemberId, bo.getMemberId())
            .eq(Attention::getAttentionUserId, bo.getAttentionUserId())
            .set(Attention::getStatus, Constants.ATTENTION_STATUS_1)
            .set(Attention::getUpdateTime, new Date())
            .set(Attention::getUpdateBy, bo.getMemberId());
        updateWrapper.update();

        // 查询该用户是否被关注
        Boolean attentionFlag = isAttention(update.getAttentionUserId(), update.getMemberId());
        // 更新状态(互粉-->已关注)
        if (attentionFlag) {

            var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Attention::getMemberId, update.getAttentionUserId())
                .eq(Attention::getAttentionUserId, update.getMemberId())
                .set(Attention::getStatus, Constants.ATTENTION_STATUS_2)
                .set(Attention::getUpdateTime, new Date())
                .set(Attention::getUpdateBy, update.getAttentionUserId());

            updateChainWrapper.update();
        }

        LambdaQueryWrapper<Attention> lqw = Wrappers.<Attention>lambdaQuery().select(Attention::getId)
        .eq(Attention::getMemberId, bo.getMemberId())
        .eq(Attention::getAttentionUserId, bo.getAttentionUserId())
        .last(" limit 1");
        AttentionVo vo = baseMapper.selectVoOne(lqw);

        // 取消关注
        LambdaQueryWrapper<MessageToUsers> usersWrapper = Wrappers.<MessageToUsers>lambdaQuery()
            .eq(MessageToUsers::getType, Constants.MESSAGE_TYPE_3)
            .eq(MessageToUsers::getRelationId, vo.getId())
            .eq(MessageToUsers::getToUserId, bo.getAttentionUserId())
            .eq(MessageToUsers::getCreateBy, bo.getMemberId());
        List<MessageToUsers> list = messageToUsersMapper.selectList(usersWrapper);

        // 删除消息关联表里的关注信息
        messageToUsersMapper.delete(Wrappers.<MessageToUsers>lambdaQuery()
            .eq(MessageToUsers::getType, Constants.MESSAGE_TYPE_3)
            .eq(MessageToUsers::getRelationId, vo.getId())
            .eq(MessageToUsers::getToUserId, bo.getAttentionUserId())
            .eq(MessageToUsers::getCreateBy, bo.getMemberId()));

        for (MessageToUsers m : list) {
            messageMapper.delete(Wrappers.<Message>lambdaQuery()
                .eq(Message::getId, m.getMessageId()));
        }

        return R.ok("取消关注成功");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @param isAdd  是否是新增
     */
    private String validEntityBeforeSave(Attention entity, Boolean isAdd) {

        // 判断用户是否已关注
        Boolean flag = isAttention(entity.getMemberId(), entity.getAttentionUserId());

        if (isAdd) {
            // 判断用户是否存在
            MemberProfile member = remoteMemberService.getMemberByUserId(entity.getAttentionUserId());
            if (ObjectUtil.isNull(member)) {
                return "关注的用户不存在";
            }
            if (entity.getMemberId().equals(entity.getAttentionUserId())) {
                return "不能自己关注自己";
            }
            if (flag) {
                return "该用户已关注，不能再关注了";
            }
        } else {
            if (!flag) {
                return "该用户还没有关注，不能取消关注";
            }
        }
        return "success";
    }

    /**
     * 判断用户和他人的关系
     */
    private AttentionVo getUserFansIfExist(AttentionDto dto) {
        LambdaQueryWrapper<Attention> lqw = Wrappers.lambdaQuery();
        lqw.eq(Attention::getMemberId, dto.getMemberId());
        lqw.eq(Attention::getAttentionUserId, dto.getAttentionUserId());
        lqw.orderByDesc(Attention::getId).last("limit 1");
        return baseMapper.selectVoOne(lqw);
    }

    /**
     * 查询用户是否已关注
     *
     * @param memberId        用户id
     * @param attentionUserId 被关注用户id
     * @return true: 已关注 false: 未关注
     */
    public Boolean isAttention(Long memberId, Long attentionUserId) {
        // 传输一样的时候 直接返回false
        if (memberId.equals(attentionUserId)) {
            return false;
        }
        LambdaQueryWrapper<Attention> lqw = Wrappers.lambdaQuery();
        lqw.eq(Attention::getMemberId, memberId);
        lqw.eq(Attention::getAttentionUserId, attentionUserId);
        lqw.and(a -> a.eq(Attention::getStatus, Constants.ATTENTION_STATUS_2)
            .or(b -> b.eq(Attention::getStatus, Constants.ATTENTION_STATUS_3)));
        lqw.orderByDesc(Attention::getId).last("limit 1");
        // 查询用户是否已关注
        AttentionVo vo = baseMapper.selectVoOne(lqw);

        if (ObjectUtil.isNotNull(vo)) {
            return true;
        }
        return false;
    }

    /**
     * 查询关注的数量
     *
     * @param memberId 用户id
     * @return
     */
    @Override
    public Integer queryAttentionCountById(Long memberId) {

        return baseMapper.getAttentionCount(memberId);
    }

    /**
     * 查询粉丝的数量
     *
     * @param memberId 用户id
     * @return
     */
    @Override
    public Integer queryFansCountById(Long memberId) {

        return baseMapper.getFansCount(memberId);
    }

    @Override
    public List<Long> checkUserIsAttention(Long userId, List<Long> relationId) {
        if (relationId.size() <= 0 || userId == 0L) {
            return new ArrayList<>();
        }
        // 首先查询出用户针对这些动态是否存在指定行为操作
        LambdaQueryWrapper<Attention> in = Wrappers.<Attention>lambdaQuery().eq(Attention::getMemberId, userId)
            .and(a -> a.eq(Attention::getStatus, Constants.ATTENTION_STATUS_2)
                .or(b -> b.eq(Attention::getStatus, Constants.ATTENTION_STATUS_3)))
            .in(Attention::getAttentionUserId, relationId);
        List<AttentionVo> attentionVos = baseMapper.selectVoList(in);
        if (attentionVos != null && attentionVos.size() > 0) {
            // 返回查询到的数据
            return attentionVos.stream().map(AttentionVo::getAttentionUserId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 根据昵称检索我的关注列表
     *
     * @return 我的关注列表
     */
    public TableDataInfo<AttentionVo> queryMyAttentionListByName(AttentionDto dto) {
        // 昵称校验
        if (ObjectUtil.isNull(dto.getNickName()) || StringUtils.isEmpty(dto.getNickName())) {
            throw new ServiceException("昵称不能为空");
        }
        QueryWrapper<Attention> lqw = Wrappers.query();
        lqw.eq("a.member_id", dto.getMemberId());
        lqw.and(a -> a.eq("a.status", Constants.ATTENTION_STATUS_2)
            .or(b -> b.eq("a.status", Constants.ATTENTION_STATUS_3)));
        lqw.like("u.nick_name", dto.getNickName());
        // 按照时间倒序排列
        lqw.orderByDesc("a.create_time");

        Page<AttentionVo> result = baseMapper.getMyAttentionListByName(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 根据昵称检索我的粉丝列表
     *
     * @return 我的粉丝列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryMyFansListByName(AttentionDto dto) {
        // 昵称校验
        if (ObjectUtil.isNull(dto.getNickName()) || StringUtils.isEmpty(dto.getNickName())) {
            throw new ServiceException("昵称不能为空");
        }
        QueryWrapper<Attention> lqw = Wrappers.query();
        lqw.eq("a.attention_user_id", dto.getAttentionUserId());
        lqw.and(a -> a.eq("a.status", Constants.ATTENTION_STATUS_2)
            .or(b -> b.eq("a.status", Constants.ATTENTION_STATUS_3)));
        lqw.like("u.nick_name", dto.getNickName());
        // 按照时间倒序排列
        lqw.orderByDesc("a.create_time");

        Page<AttentionVo> result = baseMapper.getMyFansListByName(dto.build(), lqw);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            result.getRecords().forEach(a -> {
                a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
                // 查看我对该用户的关注状态
                setAttentionStatus(a, LoginHelper.getUserId(), a.getMemberId());
            });
        }
        return TableDataInfo.build(result);
    }

    /**
     * 根据昵称检索他人的关注列表
     *
     * @param dto
     * @return 他人的关注列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryOthersAttentionListByName(AttentionDto dto) {
        // 昵称校验
        if (ObjectUtil.isNull(dto.getNickName()) || StringUtils.isEmpty(dto.getNickName())) {
            throw new ServiceException("昵称不能为空");
        }
        // 判断用户是否存在
        MemberProfile member = remoteMemberService.getMemberByUserId(dto.getMemberId());
        if (ObjectUtil.isNull(member)) {
            throw new ServiceException("用户不存在");
        }

        List<AttentionVo> list = baseMapper.getOthersAttentionListByName(dto);
        list.forEach(a -> {
            a.setMemberId(dto.getMemberId());

            LambdaQueryWrapper<Attention> eq = Wrappers.<Attention>lambdaQuery()
                .select(Attention::getId)
                .eq(Attention::getMemberId, a.getMemberId())
                .eq(Attention::getAttentionUserId, a.getAttentionUserId());

            AttentionVo vo = baseMapper.selectVoOne(eq);
            if (ObjectUtil.isNotNull(vo)) {
                a.setId(vo.getId());
            }
            // 设置头像
            a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
            try {
                // 查看我对该用户的关注状态
                setAttentionStatus(a, LoginHelper.getUserId(), a.getAttentionUserId());
            } catch (Exception userNoLoginException) {
                // 未登录场合：对该用户的关注状态, 默认是未关注
                a.setStatus(Constants.ATTENTION_STATUS_1);
            }
        });

        // 手动分页
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        if (dto.getPageNum() == null) {
            dto.setPageNum(0);
        }
        int total= list.size();
        list = getPageList(list, dto.getPageSize(), dto.getPageNum());

        TableDataInfo<AttentionVo> build = TableDataInfo.build(list);
        build.setTotal(total);
        build.setRows(list);
        return build;
    }

    /**
     * 根据昵称检索他人的粉丝列表
     *
     * @param dto
     * @return 他人的粉丝列表
     */
    @Override
    public TableDataInfo<AttentionVo> queryOthersFansListByName(AttentionDto dto) {
        // 昵称校验
        if (ObjectUtil.isNull(dto.getNickName()) || StringUtils.isEmpty(dto.getNickName())) {
            throw new ServiceException("昵称不能为空");
        }
        // 判断用户是否存在
        MemberProfile member = remoteMemberService.getMemberByUserId(dto.getMemberId());
        if (ObjectUtil.isNull(member)) {
            throw new ServiceException("用户不存在");
        }
        List<AttentionVo> list = baseMapper.getOthersFansListByName(dto);
        list.forEach(a -> {
            a.setAttentionUserId(dto.getMemberId());
            LambdaQueryWrapper<Attention> eq = Wrappers.<Attention>lambdaQuery()
                .select(Attention::getId)
                .eq(Attention::getMemberId, a.getMemberId())
                .eq(Attention::getAttentionUserId, a.getAttentionUserId());

            AttentionVo vo = baseMapper.selectVoOne(eq);
            if (ObjectUtil.isNotNull(vo)) {
                a.setId(vo.getId());
            }
            // 设置头像
            a.setAvatar(iUserBehaviorService.getAvatar(a.getSex(), a.getAvatar()));
            try {
                // 查看我对该用户的关注状态
                setAttentionStatus(a, LoginHelper.getUserId(), a.getMemberId());
            } catch (Exception userNoLoginException) {
                // 未登录场合：对该用户的关注状态, 默认是未关注
                a.setStatus(Constants.ATTENTION_STATUS_1);
            }
        });

        // 手动分页
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        if (dto.getPageNum() == null) {
            dto.setPageNum(0);
        }
        int total= list.size();
        list = getPageList(list, dto.getPageSize(), dto.getPageNum());

        TableDataInfo<AttentionVo> build = TableDataInfo.build(list);
        build.setTotal(total);
        build.setRows(list);
        return build;
    }

    /**
     * 查看我对该用户的关注状态
     *
     * @param vo
     * @return
     */
    private AttentionVo setAttentionStatus(AttentionVo vo, Long userId, Long memberId) {
        // 判断我是否关注这个人
        Boolean flag = isAttention(userId, memberId);
        if (flag) {
            // 判断是否互粉
            Boolean flag2 = isAttention(memberId, userId);
            if (flag2) {
                vo.setStatus(Constants.ATTENTION_STATUS_3);
            } else {
                vo.setStatus(Constants.ATTENTION_STATUS_2);
            }
        } else {
            vo.setStatus(Constants.ATTENTION_STATUS_1);
        }
        return vo;
    }

    /**
     * 手动分页
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @return
     */
    private List<AttentionVo> getPageList(List<AttentionVo> list, int pageSize, int pageNum) {
        int pageCount = 0;
        int listSize = list.size();

        if (listSize > pageSize) {
            pageCount = listSize / pageSize + 1;

            if (pageNum <= 1) {
                // 第一页
                list = list.subList(0, pageSize);
            } else if (pageNum * pageSize > list.size()) {
                if((pageNum - 1) * pageSize > list.size()){
                    list = new ArrayList<>();
                } else {
                    // 最后一页
                    list = list.subList((pageCount - 1) * pageSize, listSize);
                }
            } else {
                // 中间页
                list = list.subList((pageNum - 1) * pageSize, pageNum * pageSize);
            }
        }
        return list;
    }

}
