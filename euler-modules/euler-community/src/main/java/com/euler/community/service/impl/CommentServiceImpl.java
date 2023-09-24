package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.MemberConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.domain.bo.CommentBo;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.dto.CommentDto;
import com.euler.community.domain.entity.Comment;
import com.euler.community.domain.entity.Dynamic;
import com.euler.community.domain.entity.MessageToUsers;
import com.euler.community.domain.vo.*;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.mapper.CommentMapper;
import com.euler.community.mapper.DynamicMapper;
import com.euler.community.service.*;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.system.api.RemoteAuditKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 评论Service业务层处理
 *
 * @author euler
 * @date 2022-06-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    private final CommentMapper baseMapper;
    @Autowired
    private IDynamicService iDynamicService;
    @Autowired
    private IPraiseService iPraiseService;
    @Autowired
    private CommonCommunityConfig commonCommunityConfig;
    @DubboReference
    private RemoteAuditKeywordService remoteAuditKeywordService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IMessageToUsersService messageToUsersService;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private DynamicMapper dynamicMapper;

    /**
     * 神评接口
     *
     * @param id 评论主键
     * @return 评论
     */
    @Override
    public CommentVo getCommentDivine(Long id) {
        // 首先获取当前动态的所有评论的点赞和是多少
        QueryWrapper<CommentVo> queryWrapper = Wrappers.query();
        queryWrapper.in("ascription_dynamic_id", id);
        queryWrapper.groupBy("ascription_dynamic_id");
        List<IdTypeDto<Long, Integer>> idTypeDtos = baseMapper.selectSumPraise(queryWrapper);
        Optional<IdTypeDto<Long, Integer>> first = idTypeDtos.stream().filter(a -> Convert.toLong(a.getId()).equals(id)).findFirst();
        if (first.isPresent() && Convert.toInt(first.get().getType()) > 0) {
            // 获取当前评论中点赞最大的
            LambdaQueryWrapper<Comment> last = Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getAscriptionDynamicId, id).orderByDesc(Comment::getPraiseNum)
                .gt(Comment::getPraiseNum, 19)
                .last("limit 1");
            Comment comment = baseMapper.selectOne(last);
            if (comment != null) {
                Integer maxPraise = Convert.toInt(first.get().getType());
                // 计算一下点赞率
                BigDecimal div = NumberUtil.div(comment.getPraiseNum(), maxPraise);
                if (div.compareTo(new BigDecimal("0.3")) > 0) {
                    CommentVo commentVo = BeanCopyUtils.copy(comment, CommentVo.class);
                    return commentVo;
                }
            }
        }
        return new CommentVo();
    }

    /**
     * 获取一批稿件的神评
     *
     * @param dynamicIds 动态id集合
     * @return 评论
     */
    @Override
    public List<CommentVo> selectCommentByMemberIds(List<Long> dynamicIds) {
        if (dynamicIds.size() <= 0) {
            return new ArrayList<>();
        }
        // 获取这批动态中最大点赞数据的动态数据
        QueryWrapper<CommentVo> wrapper = Wrappers.query();
        wrapper.in("a.ascription_dynamic_id", dynamicIds);
        wrapper.gt("a.praise_num", 19);
        wrapper.eq("a.type", 1);
        wrapper.groupBy("a.id, a.ascription_dynamic_id");
        wrapper.having("a.`praise_num` = MAX(b.`praise_num`)");
        List<CommentVo> commentVos = baseMapper.selectDynamicDivineEvaluation(wrapper);
        List<MemberProfile> memberByUserIds = null;
        if (commentVos.size() > 0) {
            // 获取评论这的用户基础信息
            List<Long> searchUserIds = commentVos.stream().map(CommentVo::getMemberId).collect(Collectors.toList());
            if (searchUserIds.size() > 0) {
                memberByUserIds = remoteMemberService.getMemberByUserIds(searchUserIds);
            }
            // 首先获取当前动态的所有评论的点赞和是多少
            QueryWrapper<CommentVo> queryWrapper = Wrappers.query();
            queryWrapper.in("ascription_dynamic_id", dynamicIds);
            queryWrapper.eq("type", 1);
            queryWrapper.groupBy("ascription_dynamic_id");
            List<IdTypeDto<Long, Integer>> idTypeDtos = baseMapper.selectSumPraise(queryWrapper);
            // 一次进行循环判断那些动态存在神评
            List<MemberProfile> finalMemberByUserIds = memberByUserIds;
            commentVos.forEach(a -> {
                Optional<IdTypeDto<Long, Integer>> first = idTypeDtos.stream().filter(b -> Convert.toLong(b.getId()).equals(a.getAscriptionDynamicId())).findFirst();
                if (first.isPresent() && Convert.toInt(first.get().getType()) > 0) {
                    Integer sumPraise = Convert.toInt(first.get().getType());
                    // 计算一下点赞率
                    BigDecimal div = NumberUtil.div(a.getPraiseNum(), sumPraise);
                    if (div.compareTo(new BigDecimal("0.3")) > 0) {
                        // 获取评论这的用户基础信息
                        Optional<MemberProfile> getMemberInfo = finalMemberByUserIds.stream().filter(c -> c.getMemberId().equals(a.getMemberId())).findFirst();
                        if (getMemberInfo.isPresent()) {
                            MemberVo memberVo = BeanCopyUtils.copy(getMemberInfo.get(), MemberVo.class);
                            memberVo.setAvatar(iUserBehaviorService.getAvatar(memberVo.getSex(), memberVo.getAvatar()));
                            a.setUsers(memberVo);
                        }
                    }
                }
            });
            return commentVos;
        }
        return new ArrayList<>();
    }

    /**
     * 查询评论列表
     *
     * @return 评论
     */
    @Override
    public TableDataInfo<CommentFrontVo> queryPageList(CommentDto dto) {
        if (dto.getRelationId() == null) {
            return new TableDataInfo<>();
        }
        Long userId = 0L;
        try {
            userId = LoginHelper.getUserId();
        } catch (Exception userNoLoginException) {
        }
        Page<CommentFrontVo> result = null;
        if (dto.getType().equals(1)) {
            // 获取动态的全部评论信息
            QueryWrapper<CommentFrontVo> wrapper = Wrappers.query();
            wrapper.eq("ca.ascription_dynamic_id", dto.getRelationId());
            wrapper.eq("ca.type", 1);
            // 设置分组
            wrapper.groupBy("ca.id");
            // 设置排序
            if (dto.getOrderByColumn() != null && dto.getOrderByColumn().equals("createTime")) {
                wrapper.orderByDesc("ca.create_time");
            } else if (dto.getOrderByColumn() != null && dto.getOrderByColumn().equals("hot")) {
                wrapper.orderByDesc("nums");
            } else {
                wrapper.orderByDesc("ca.create_time");
            }
            result = baseMapper.selectCommentForDynamicList(dto.build(), wrapper);
        } else {
            // 获取评论的评论的全部评论信息
            QueryWrapper<CommentFrontVo> wrapper = Wrappers.query();
            wrapper.eq("ca.ascription_comment_id", dto.getRelationId());
            wrapper.eq("ca.type", 2);
            // 设置排序
            wrapper.orderByDesc("ca.create_time");
            result = baseMapper.selectCommentForCommentList(dto.build(), wrapper);
        }
        // 数据获取完毕之后我们需要汇总一下用户ID 一次性通过dubbo服务获取用户的基础信息
        if (result.getRecords() != null) {
            List<Long> collectA = result.getRecords().stream().map(CommentFrontVo::getMemberId).collect(Collectors.toList());
            List<Long> collectB = result.getRecords().stream().map(CommentFrontVo::getCommentMemberId).collect(Collectors.toList());
            List<Long> commentIds = result.getRecords().stream().map(CommentFrontVo::getId).collect(Collectors.toList());
            // 获取当前用户那些评论他进行点赞行为
            List<Long> praiseOperationList = iPraiseService.checkUserIsPraise(userId, 2, commentIds);
            // 用户获取完毕之后拼接起来一次性查询
            collectB.addAll(collectA);
            List<Long> searchUserIds = collectB.stream().distinct().collect(Collectors.toList());
            if (searchUserIds.size() > 0) {
                List<MemberProfile> memberByUserIds = remoteMemberService.getMemberByUserIds(searchUserIds);
                CommentVo commentVo = null;
                if (dto.getPageNum() != null && dto.getPageNum() <= 1 && dto.getType().equals(1)) {
                    commentVo = getCommentDivine(dto.getRelationId());
                }
                // 用户的基础信息获取完毕之后我们累加到返回列表中
                CommentVo finalCommentVo = commentVo;
                result.getRecords().forEach(a -> {
                    // 评论所属用户的基本信息
                    Optional<MemberProfile> first = memberByUserIds.stream().filter(b -> b.getMemberId().equals(a.getMemberId())).findFirst();
                    if (first.isPresent()) {
                        MemberVo memberVo = BeanCopyUtils.copy(first.get(), MemberVo.class);
                        if (memberVo != null && memberVo.getAvatar() != null) {
                            memberVo.setAvatar(iUserBehaviorService.getAvatar(memberVo.getSex(), memberVo.getAvatar()));
                        }
                        a.setUsers(memberVo);
                    }
                    // 评论的第一个评论人的用户ID,头像,昵称
                    Optional<MemberProfile> second = memberByUserIds.stream().filter(c -> c.getMemberId().equals(a.getCommentMemberId())).findFirst();
                    if (second.isPresent()) {
                        a.setCommentMemberId(second.get().getMemberId());
                        a.setCommentMemberNickName(second.get().getNickName());
                        a.setCommentMemberAvatar(second.get().getAvatar());
                        a.setCommentMemberIsOfficial(second.get().getIsOfficial());
                    }
                    // 设置是否点赞过
                    if (praiseOperationList.contains(a.getId())) {
                        a.setIsPraise(1);
                    }
                    // 设置是否是神评标签
                    if (finalCommentVo != null && finalCommentVo.getId() != null && finalCommentVo.getId().equals(a.getId())) {
                        a.setIsDivine(1);
                    }
                });
                // 按照指定字段进行一下排序
                result.getRecords().sort(Comparator.comparing(CommentFrontVo::getIsDivine).reversed());
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 新增评论
     *
     * @param bo 评论
     * @return 结果
     */
    @Override
    public R insertByBo(CommentBo bo) {
        // 判断字符是否超出
        String clearComments = HtmlUtil.cleanHtmlTag(bo.getComments());
        if (clearComments.length() > commonCommunityConfig.getMaxCommentsLength()) {
            return R.fail("您评论的内容超出了限制");
        }
        // 判断评论内容中是否存在敏感词
        R checkKeyword = remoteAuditKeywordService.systemCheck(clearComments, 1);
        if (checkKeyword.getCode() == R.FAIL) {
            return R.fail("您评论的内容存在敏感词");
        }
        // 验证数据是否正确
        if (bo.getType().equals(1)) {
            // 查询动态是否存在
            DynamicVo dynamicVo = iDynamicService.queryById(bo.getRelationId());
            if (dynamicVo == null) {
                return R.fail("评论的动态不存在");
            }
        } else {
            LambdaQueryWrapper<Comment> eq = Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getId, bo.getRelationId());
            Comment comment = baseMapper.selectOne(eq);
            if (comment == null) {
                return R.fail("评论的评论不存在");
            }
        }
        // 对实体进行一下转换
        Comment add = BeanUtil.toBean(bo, Comment.class);
        // 我们同样需要将comments的值赋值给commentsRaw一下
        add.setCommentsRaw(add.getComments());
        // 判断一下当前评论的类型 是评论评论同事还是评论评论的评论的时候 我们需要手动拼接上@符号同时追加他评论用户的用户名上去
        if (add.getType().equals(2)) {
            // 根据关联ID查询出这条评论是属于评论动态还是评论评论  如果是二级以上的评论我们需要添加@符号 同时追加他评论用户的用户名上去
            LambdaQueryWrapper<Comment> eq = Wrappers.<Comment>lambdaQuery()
                .select(Comment::getMemberId, Comment::getType)
                .eq(Comment::getId, add.getRelationId());
            CommentVo commentVo = baseMapper.selectVoOne(eq);
            if (commentVo != null && commentVo.getType().equals(2)) {
                // 当前评论就可以属于二级或者二级以上的评论 我们需要针对评论内容进行新的追加
                // 首先获取 你针对评论用户的基础信息
                MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(commentVo.getMemberId());
                if (memberByUserId != null) {
                    String newComments = "回复<a href=" + memberByUserId.getNickName() + ">@" + memberByUserId.getNickName() + "</a>:" + bo.getComments();
                    String newCommentsRaw = "回复@" + memberByUserId.getNickName() + " :" + bo.getComments();
                    add.setComments(newComments);
                    add.setCommentsRaw(newCommentsRaw);
                }
            }
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            // 更新动态表和Es中的数据
            IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
            idTypeDto.setId(bo.getAscriptionDynamicId().toString());
            idTypeDto.setType(DynamicFieldIncrEnum.COMMENT.getCode());
            iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            // 判断是否需要添加积分
            iUserBehaviorService.checkUserBehaviorLegitimate(add.getMemberId(), 3, add.getId());
            // 添加消息数据
            MessageBo messageBo = new MessageBo();
            messageBo.setTitle("评论消息");
            messageBo.setContent("<p>评论消息</p>");
            messageBo.setPlatformType(Constants.PLATFORM_TYPE_APP);
            messageBo.setPushUserType(Constants.PUSH_USER_PART);
            messageBo.setPushUsers(add.getCommentedMemberId().toString());
            messageBo.setPushTime(new Date());
            messageBo.setPushStatus(Constants.IS_PUSH_YES);
            messageBo.setType(Constants.MESSAGE_TYPE_2);
            messageService.insertByBo(messageBo);

            MessageToUsers entity = new MessageToUsers();
            entity.setMessageId(messageBo.getId());
            entity.setToUserId(add.getCommentedMemberId());
            entity.setType(Constants.MESSAGE_TYPE_2);
            entity.setRelationId(add.getId());
            entity.setReadStatus(Constants.UNREAD);
            messageToUsersService.insertMessage(entity);

            return R.ok(add);
        }
        return R.fail("评论失败");
    }


    /**
     * 批量删除评论
     *
     * @param ids 需要删除的评论主键
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
     * 用户注销之后清空用户的动态数据
     *
     * @param userId
     * @return
     */
    public R cancellationClearComment(Long userId) {
        // 查询出用户所有的状态 将动态全部进行删除掉
        LambdaQueryWrapper<Comment> eq = Wrappers.<Comment>lambdaQuery().select(Comment::getId).eq(Comment::getMemberId, userId);
        List<Comment> comments = baseMapper.selectList(eq);
        if (comments.size() > 0) {
            LambdaUpdateChainWrapper<Comment> updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Comment::getMemberId, userId)
                .set(Comment::getDelFlag, MemberConstants.MEMBER_STATUS_DISABLE)
                .set(Comment::getUpdateTime, new Date())
                .set(Comment::getUpdateBy, userId);
            updateChainWrapper.update();
        }
        return R.ok();
    }

    /**
     * 查询新评论消息列表
     *
     * @return 评论
     */
    @Override
    public TableDataInfo<NewCommentFrontVo> queryNewPageList(CommentDto dto) {
        Long userId = 0L;
        try {
            userId = LoginHelper.getUserId();
            dto.setMemberId(userId);
        } catch (Exception userNoLoginException) {
        }
        QueryWrapper<NewCommentFrontVo> lqw = buildQueryWrapper(dto, userId);
        Page<NewCommentFrontVo> result = baseMapper.getNewCommentList(dto.build(), lqw, dto.getType());

        // 数据获取完毕之后我们需要汇总一下用户ID 一次性通过dubbo服务获取用户的基础信息
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            List<Long> collectA = result.getRecords().stream().distinct().map(NewCommentFrontVo::getMemberId).collect(Collectors.toList());
            List<Long> collectB = result.getRecords().stream().distinct().map(NewCommentFrontVo::getCommentedMemberId).collect(Collectors.toList());

            // 用户获取完毕之后拼接起来一次性查询
            collectB.addAll(collectA);
            List<Long> searchUserIds = collectB.stream().distinct().collect(Collectors.toList());
            if (searchUserIds.size() > 0) {
                List<MemberProfile> memberByUserIds = remoteMemberService.getMemberByUserIds(searchUserIds);
                if (memberByUserIds != null && memberByUserIds.size() > 0) {
                    if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
                        // 用户的基础信息获取完毕之后我们累加到返回列表中
                        result.getRecords().forEach(a -> {
                            // 评论所属用户的基本信息
                            Optional<MemberProfile> first = memberByUserIds.stream().filter(b -> b.getMemberId().equals(a.getMemberId())).findFirst();
                            if (first.isPresent()) {
                                MemberVo memberVo = BeanCopyUtils.copy(first.get(), MemberVo.class);
                                if (memberVo != null && memberVo.getAvatar() != null) {
                                    memberVo.setAvatar(iUserBehaviorService.getAvatar(memberVo.getSex(), memberVo.getAvatar()));
                                }
                                a.setUsers(memberVo);
                            }
                            try {
                                // 查询我的基本信息
                                MemberProfile myUser = remoteMemberService.getMemberByUserId(LoginHelper.getUserId());
                                if (myUser != null) {
                                    MemberVo myMemberVo = BeanCopyUtils.copy(myUser, MemberVo.class);
                                    if (myMemberVo != null && myMemberVo.getAvatar() != null) {
                                        myMemberVo.setAvatar(iUserBehaviorService.getAvatar(myMemberVo.getSex(), myMemberVo.getAvatar()));
                                    }
                                    a.setMyUser(myMemberVo);
                                }
                            } catch (UserException e) {
                            }

                            // 查询我的关联的动态/评论内容
                            if (a.getType() == 1) {
                                // 动态内容
                                Dynamic dynamic = dynamicMapper.selectOne(Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getId, a.getRelationId()));
                                if (ObjectUtil.isNotNull(dynamic)) {
                                    a.setDynamicId(dynamic.getId());
                                    String content;
                                    if (dynamic.getType() == 3) {
                                        // 攻略, 内容清除标签后，如果为空的话，就设置标题
                                        content = StringUtils.isBlank(HtmlUtil.cleanHtmlTag(dynamic.getContent())) ? dynamic.getTitle() : HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                    } else {
                                        content = HtmlUtil.cleanHtmlTag(dynamic.getContent());
                                    }
                                    a.setRelationContent(content);
                                    // 动态的内容
                                    a.setDynamicContent(content);
                                }
                            } else {
                                // 评论内容
                                Comment comment = baseMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, a.getRelationId()));
                                if (ObjectUtil.isNotNull(comment)) {
                                    a.setRelationContent(comment.getCommentsRaw());
                                    // 该评论归属的动态id
                                    a.setDynamicId(comment.getAscriptionDynamicId());

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
                                        a.setDynamicContent(content);
                                        try {
                                            MemberProfile member = remoteMemberService.getMemberByUserId(dynamic.getMemberId());
                                            if (ObjectUtil.isNotNull(member)) {
                                                // 动态的昵称
                                                a.setDynamicNickName(member.getNickName());
                                            }
                                        } catch (UserException e) {
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
            // 点击评论消息，进入评论列表，视为已读所有评论消息；
            MessageToUsers entity = new MessageToUsers();
            entity.setToUserId(dto.getMemberId());
            entity.setType(Constants.MESSAGE_TYPE_2);
            entity.setReadStatus(Constants.READ);
            messageToUsersService.updateMessage(entity);
        }
        return TableDataInfo.build(result);
    }

    private QueryWrapper<NewCommentFrontVo> buildQueryWrapper(CommentDto dto, Long userId) {

        QueryWrapper<NewCommentFrontVo> wrapper = Wrappers.query();
        wrapper.eq(userId > 0, "ca.commented_member_id", userId);
        wrapper.eq(dto.getRelationId() != null, "ca.relation_id", dto.getRelationId());
        wrapper.eq(dto.getType() != null && dto.getType() > 0, "ca.type", dto.getType());
        if (dto.getType() != null && dto.getType() == 1) {
            // 设置分组
            wrapper.groupBy("ca.id");
        }
        // 设置排序
        // 未读的放在上面
        wrapper.orderByAsc("u.read_status");
        wrapper.orderByDesc("ca.create_time");

        return wrapper;
    }

    /**
     * 获取用户某天的评论数量
     *
     * @param userId
     * @param date
     * @param type 类型 1动态 2评论
     * @return
     */
    public Integer commentCountForDay(Long userId, Date date, String type) {
        Integer commentCount = 0;
        QueryWrapper<Comment> lqw = Wrappers.<Comment>query();
        lqw.ge("c.create_time", DateUtils.getBeginOfDay(date));
        lqw.le("c.create_time", DateUtils.getEndOfDay(date));
        lqw.ge("c.type", type);
        lqw.eq("c.del_flag", "0");

        if(StringUtils.equals("1", type)) {
            lqw.eq("d.member_id", userId);
            commentCount = baseMapper.getUseDynamicCommentCount(lqw);
        } else if(StringUtils.equals("2", type)) {
            lqw.eq("c.commented_member_id", userId);
            commentCount = baseMapper.getUseCommentComCount(lqw);
        }

        return commentCount;
    }
}
