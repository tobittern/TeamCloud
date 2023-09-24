package com.euler.community.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.bo.PraiseBo;
import com.euler.community.domain.dto.PraiseDto;
import com.euler.community.domain.entity.*;
import com.euler.community.domain.vo.NewPraiseVo;
import com.euler.community.domain.vo.PraiseVo;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.mapper.*;
import com.euler.community.service.*;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 点赞Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class PraiseServiceImpl extends ServiceImpl<PraiseMapper, Praise> implements IPraiseService {

    private final PraiseMapper baseMapper;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private final IMessageToUsersService messageToUsersService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private IDynamicService iDynamicService;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DynamicMapper dynamicMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageToUsersMapper messageToUsersMapper;

    /**
     * 查询点赞列表
     *
     * @return 点赞
     */
    @Override
    public TableDataInfo<PraiseVo> queryPageList(PraiseDto dto) {
        QueryWrapper<Praise> lqw = Wrappers.query();
        lqw.eq(dto.getRelationId() != null, "p.relation_id", dto.getRelationId());
        lqw.eq(dto.getMemberId() != null, "p.member_id", dto.getMemberId());
        lqw.eq(dto.getType() != null, "p.type", dto.getType());
        lqw.eq("p.status", "1");
        Page<PraiseVo> result = null;
        if (dto.getType().equals(1)) {
            lqw.eq("d.del_flag", "0");
            lqw.orderByAsc("p.id");
            result = baseMapper.getUserPraiseDynamicList(dto.build(), lqw);
        } else {
            lqw.eq("c.del_flag", "0");
            lqw.orderByAsc("p.id");
            result = baseMapper.getUserPraiseCommentList(dto.build(), lqw);
        }
        return TableDataInfo.build(result);
    }


    /**
     * 新增点赞
     *
     * @return 结果
     */
    @Override
    public R insertByBo(PraiseBo bo) {
        // 判断一下动态类型是否正确
        Integer[] favTypeList = new Integer[]{1, 2};
        Optional<Integer> favTypeAny = Arrays.stream(favTypeList).filter(a -> a.equals(bo.getClickType())).findAny();
        if (!favTypeAny.isPresent()) {
            return R.fail("点赞状态错误");
        }
        if (bo.getPraiseUserId() == null) {
            return R.fail("点赞所属用户的id未输入");
        }
        Long userId = LoginHelper.getUserId();
        // 首先判断是否存在
        LambdaQueryWrapper<Praise> eq = Wrappers.<Praise>lambdaQuery()
            .eq(Praise::getMemberId, userId)
            .eq(Praise::getRelationId, bo.getRelationId())
            .eq(Praise::getType, bo.getType());
        Praise praise = baseMapper.selectOne(eq);
        int row = 0;
        Long praiseId = 0L;
        if (praise == null) {
            // 为空的时候代表我们之前没有点赞
            Praise insertEntity = new Praise();
            insertEntity.setMemberId(userId);
            insertEntity.setRelationId(bo.getRelationId());
            insertEntity.setType(bo.getType());
            insertEntity.setStatus(bo.getClickType().toString());
            row = baseMapper.insert(insertEntity);
            praiseId = insertEntity.getId();
        } else {
            // 不为空的时候我们需要判断当前执行的值是否和数据库存储的值一致
            if (praise.getStatus().equals(bo.getClickType().toString())) {
                return R.fail("您已经操作成功");
            }
            Praise updateEntity = new Praise();
            updateEntity.setId(praise.getId());
            updateEntity.setStatus(bo.getClickType().toString());
            row = baseMapper.updateById(updateEntity);
            praiseId = praise.getId();
        }
        if (row > 0) {
            if (bo.getType().equals(1)) {
                // 更新动态表和Es中的数据
                IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
                idTypeDto.setId(bo.getRelationId().toString());
                idTypeDto.setType(bo.getClickType().equals(1) ? DynamicFieldIncrEnum.FAV.getCode() : DynamicFieldIncrEnum.CANCEL_FAV.getCode());
                iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            } else {
                // 点赞评论 这个时候我们需要获取评论的信息
                // 评论信息
                Comment comment = commentMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, bo.getRelationId()));
                if (comment != null) {
                    Integer needNums = bo.getClickType().equals(1) ? comment.getPraiseNum() + 1 : comment.getPraiseNum() - 1;
                    if (needNums >= 0) {
                        Comment updateComment = new Comment();
                        updateComment.setId(comment.getId());
                        updateComment.setPraiseNum(needNums);
                        commentMapper.updateById(updateComment);
                    }
                }
            }
            // 如果是点赞
            if (bo.getClickType().equals(1)) {
                // 判断是否需要添加积分
                iUserBehaviorService.checkUserBehaviorLegitimate(userId, 2, praiseId);
            }
            if (bo.getClickType().equals(1)) {
                // 添加消息数据
                MessageBo messageBo = new MessageBo();
                messageBo.setTitle("点赞消息");
                messageBo.setContent("<p>点赞消息</p>");
                messageBo.setPlatformType(Constants.PLATFORM_TYPE_APP);
                messageBo.setPushUserType(Constants.PUSH_USER_PART);
                messageBo.setPushUsers(Convert.toStr(bo.getPraiseUserId()));
                messageBo.setPushTime(new Date());
                messageBo.setPushStatus(Constants.IS_PUSH_YES);
                messageBo.setType(Constants.MESSAGE_TYPE_1);
                messageService.insertByBo(messageBo);

                MessageToUsers entity = new MessageToUsers();
                entity.setMessageId(messageBo.getId());
                entity.setToUserId(bo.getPraiseUserId());
                entity.setType(Constants.MESSAGE_TYPE_1);
                entity.setRelationId(bo.getRelationId());
                entity.setReadStatus(Constants.UNREAD);
                messageToUsersService.insertMessage(entity);
            } else if (bo.getClickType().equals(2)) {
                // 取消点赞
                LambdaQueryWrapper<MessageToUsers> usersWrapper = Wrappers.<MessageToUsers>lambdaQuery()
                    .eq(MessageToUsers::getType, Constants.MESSAGE_TYPE_1)
                    .eq(MessageToUsers::getRelationId, bo.getRelationId())
                    .eq(MessageToUsers::getToUserId, bo.getPraiseUserId())
                    .eq(MessageToUsers::getCreateBy, userId);
                List<MessageToUsers> list = messageToUsersMapper.selectList(usersWrapper);

                // 删除消息关联表里的点赞信息
                messageToUsersMapper.delete(Wrappers.<MessageToUsers>lambdaQuery()
                    .eq(MessageToUsers::getType, Constants.MESSAGE_TYPE_1)
                    .eq(MessageToUsers::getRelationId, bo.getRelationId())
                    .eq(MessageToUsers::getToUserId, bo.getPraiseUserId())
                    .eq(MessageToUsers::getCreateBy, userId));

                for (MessageToUsers m : list) {
                    messageMapper.delete(Wrappers.<Message>lambdaQuery()
                        .eq(Message::getId, m.getMessageId()));
                }
            }
            return R.ok("操作成功");
        }
        return R.fail("操作失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Praise entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除点赞
     *
     * @param ids 需要删除的点赞主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 检测用户是否针对一些动态进行的点赞
     *
     * @param userId
     * @param type
     * @param relationId
     * @return
     */
    @Override
    public List<Long> checkUserIsPraise(Long userId, Integer type, List<Long> relationId) {
        if (relationId.size() <= 0 || userId == 0L) {
            return new ArrayList<>();
        }
        // 首先查询出用户针对这些动态是否存在指定行为操作
        LambdaQueryWrapper<Praise> in = Wrappers.<Praise>lambdaQuery()
            .in(Praise::getRelationId, relationId)
            .eq(Praise::getType, type)
            .eq(Praise::getMemberId, userId)
            .eq(Praise::getStatus, "1");
        List<PraiseVo> praiseVos = baseMapper.selectVoList(in);
        if (praiseVos != null && praiseVos.size() > 0) {
            // 返回查询到的数据
            return praiseVos.stream().map(PraiseVo::getRelationId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 查询新点赞消息列表
     *
     * @return 点赞
     */
    @Override
    public TableDataInfo<NewPraiseVo> queryNewPageList(PraiseDto dto) {
        QueryWrapper<Praise> lqw = buildNewQueryWrapper(dto);
        Page<NewPraiseVo> result = baseMapper.getNewPraiseList(dto.build(), lqw);

        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {

            List<Long> collect = result.getRecords().stream().distinct().map(NewPraiseVo::getMemberId).collect(Collectors.toList());
            // 用户获取完毕之后拼接起来一次性查询
            List<Long> searchUserIds = collect.stream().distinct().collect(Collectors.toList());
            if (searchUserIds.size() > 0) {
                // 查询出这些用户的昵称和头像
                List<MemberProfile> memberByUserIds = remoteMemberService.getMemberByUserIds(searchUserIds);
                if (memberByUserIds != null && memberByUserIds.size() > 0) {
                    // 用户的基础信息获取完毕之后我们累加到返回列表中
                    if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
                        result.getRecords().forEach(praise -> {
                            // 查询出这些用户的昵称和头像
                            Optional<MemberProfile> second = memberByUserIds.stream().filter(b -> b.getMemberId().equals(praise.getMemberId())).findFirst();
                            if (second.isPresent()) {

                                praise.setPraiseNickName(second.get().getNickName());
                                praise.setPraiseUserAvatar((iUserBehaviorService.getAvatar(second.get().getSex(), second.get().getAvatar())));
                                praise.setPraiseUserOfficial(second.get().getIsOfficial());
                            }

                            // 查询我的动态/评论信息
                            if (praise.getType() == 1) {
                                // 动态信息
                                Dynamic dynamic = dynamicMapper.selectOne(Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getId, praise.getRelationId()));
                                if (ObjectUtil.isNotNull(dynamic)) {
                                    String content;
                                    if (dynamic.getType() == 3) {
                                        // 攻略, 内容清除标签后，如果为空的话，就设置标题
                                        content = StringUtils.isBlank(HtmlUtil.cleanHtmlTag(dynamic.getContent())) ? dynamic.getTitle() : HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                    } else {
                                        content = HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                    }
                                    // 动态的内容
                                    praise.setContent(content);
                                    praise.setDynamicContent(content);
                                    praise.setDynamicId(dynamic.getId());

                                    // 查询出我的昵称和头像
                                    try {
                                        MemberProfile myMemberInfo = remoteMemberService.getMemberByUserId(dynamic.getMemberId());
                                        if (ObjectUtil.isNotNull(myMemberInfo)) {
                                            praise.setNickName(myMemberInfo.getNickName());
                                            praise.setAvatar(iUserBehaviorService.getAvatar(myMemberInfo.getSex(), myMemberInfo.getAvatar()));
                                            praise.setIsOfficial(myMemberInfo.getIsOfficial());
                                        }
                                    } catch (UserException e) {
                                    }
                                }
                            } else {
                                // 评论信息
                                Comment comment = commentMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, praise.getRelationId()));
                                if (comment != null) {
                                    praise.setContent(comment.getCommentsRaw());
                                    // 该评论归属的动态id
                                    praise.setDynamicId(comment.getAscriptionDynamicId());
                                    try {
                                        // 查询出我的昵称和头像
                                        MemberProfile myMemberInfo = remoteMemberService.getMemberByUserId(comment.getMemberId());
                                        if (ObjectUtil.isNotNull(myMemberInfo)) {
                                            praise.setNickName(myMemberInfo.getNickName());
                                            praise.setAvatar(iUserBehaviorService.getAvatar(myMemberInfo.getSex(), myMemberInfo.getAvatar()));
                                            praise.setIsOfficial(myMemberInfo.getIsOfficial());
                                        }

                                        // 动态信息
                                        Dynamic dynamic = dynamicMapper.selectOne(Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getId, comment.getAscriptionDynamicId()));
                                        if (ObjectUtil.isNotNull(dynamic)) {
                                            String content;
                                            if (dynamic.getType() == 3) {
                                                // 攻略, 内容清除标签后，如果为空的话，就设置标题
                                                content = StringUtils.isBlank(HtmlUtil.cleanHtmlTag(dynamic.getContent())) ? dynamic.getTitle() : HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                            } else {
                                                content = HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                            }
                                            // 动态的内容
                                            praise.setDynamicContent(content);

                                            MemberProfile member = remoteMemberService.getMemberByUserId(dynamic.getMemberId());
                                            if (ObjectUtil.isNotNull(member)) {
                                                // 动态的昵称
                                                praise.setDynamicNickName(member.getNickName());
                                            }
                                        }
                                    } catch (UserException e) {
                                    }
                                }
                            }

                            // 点击点赞消息，进入点赞列表，视为已读所有点赞消息；
                            MessageToUsers entity = new MessageToUsers();
                            entity.setToUserId(dto.getMemberId());
                            entity.setType(Constants.MESSAGE_TYPE_1);
                            entity.setReadStatus(Constants.READ);
                            messageToUsersService.updateMessage(entity);
                        });
                    }
                }
            }

        }

        return TableDataInfo.build(result);
    }

    private QueryWrapper<Praise> buildNewQueryWrapper(PraiseDto dto) {
        QueryWrapper<Praise> lqw = Wrappers.query();
        lqw.eq(dto.getRelationId() != null, "p.relation_id", dto.getRelationId());
        lqw.eq(dto.getMemberId() != null, "u.to_user_id", dto.getMemberId());
        lqw.eq(dto.getType() != null, "p.type", dto.getType());
        lqw.eq("p.status", "1");
        // 未读的点赞消息放在上面
        lqw.orderByAsc("u.read_status");
        lqw.orderByDesc("p.id");
        return lqw;
    }

    /**
     * 获取用户某天的点赞数量
     *
     * @param userId
     * @param date
     * @param type 类型 1动态 2评论
     * @return
     */
    @Override
    public Integer praiseCountForDay(Long userId, Date date, String type) {
        Integer praiseCount = 0;
        QueryWrapper<Praise> lqw = Wrappers.query();
        lqw.ge("p.create_time", DateUtils.getBeginOfDay(date));
        lqw.le("p.create_time", DateUtils.getEndOfDay(date));
        lqw.eq(type != null, "p.type", type);
        lqw.eq("p.status", "1");
        lqw.eq("p.del_flag", "0");

        if(StringUtils.equals("1", type)) {
            lqw.eq(userId != null, "d.member_id", userId);
            praiseCount = baseMapper.getUseDynamicPraiseCount(lqw);
        } else if(StringUtils.equals("2", type)) {
            lqw.eq(userId != null, "c.member_id", userId);
            praiseCount = baseMapper.getUseCommentPraiseCount(lqw);
        }
        return praiseCount;
    }

}
