package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
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
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.config.CommonCommunityConfig;
import com.euler.community.config.DynamicRabbitMqProducer;
import com.euler.community.domain.bo.DynamicBo;
import com.euler.community.domain.bo.DynamicTopicBo;
import com.euler.community.domain.bo.MessageBo;
import com.euler.community.domain.bo.ResourceBo;
import com.euler.community.domain.dto.DynamicDto;
import com.euler.community.domain.dto.IdTypeCommunityDto;
import com.euler.community.domain.entity.*;
import com.euler.community.domain.vo.DynamicVo;
import com.euler.community.enums.DynamicOperationEnum;
import com.euler.community.enums.DynamicStatusEnum;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.mapper.*;
import com.euler.community.service.*;
import com.euler.community.utils.CommonForCommunityUtils;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.system.api.RemoteAuditKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态Service业务层处理
 *
 * @author euler
 * @date 2022-06-01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {

    private final DynamicMapper baseMapper;
    @Autowired
    private IDynamicTopicService iDynamicTopicService;
    @Autowired
    private IResourceService iResourceService;
    @Autowired
    private DynamicAuditRecordMapper dynamicAuditRecordMapper;
    @DubboReference
    private RemoteAuditKeywordService remoteAuditKeywordService;
    @Autowired
    private DynamicRabbitMqProducer dynamicRabbitMqProducer;
    @Autowired
    private CommonCommunityConfig commonCommunityConfig;
    @Autowired
    private DynamicElasticsearch dynamicElasticsearch;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private DynamicTopicMapper dynamicTopicMapper;
    @Autowired
    private DynamicOperationLogMapper dynamicOperationLogMapper;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IMessageToUsersService messageToUsersService;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 查询动态
     *
     * @param id 动态主键
     * @return 动态
     */
    @Override
    public DynamicVo queryById(Long id) {
        DynamicVo dynamicVo = baseMapper.selectVoById(id);
        // 设置又拍云域名前缀
        if (dynamicVo.getCover() != null && StringUtils.isNotBlank(dynamicVo.getCover())) {
            dynamicVo.setCover(commonCommunityConfig.getYunDomain() + dynamicVo.getCover());
        }
        if (dynamicVo.getResourceUrl() != null && StringUtils.isNotBlank(dynamicVo.getResourceUrl())) {
            String[] split = dynamicVo.getResourceUrl().split(",");
            List<String> tempResource = new ArrayList<>();
            for (String s : split) {
                tempResource.add(commonCommunityConfig.getYunDomain() + s);
            }
            String join = StringUtils.join(tempResource, ",");
            dynamicVo.setResourceUrl(join);
        }
        MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(dynamicVo.getMemberId());
        // 设置头像
        dynamicVo.setMemberNickName(memberByUserId.getNickName());
        // 获取专题的基础信息
        QueryWrapper<IdNameDto<Long>> orderWrapper = Wrappers.query();
        // 这个结束时间可能需要获取当前时间
        orderWrapper.in("dt.dynamic_id", id)
            .eq("t.status", 1);
        List<IdNameDto<Long>> topicList = dynamicTopicMapper.selectTopicNameByDynamicId(orderWrapper);
        if (topicList.size() > 0) {
            List<String> collect = topicList.stream().filter(b -> b.getId().equals(id)).map(IdNameDto::getName).collect(Collectors.toList());
            String topicName = StringUtils.join(collect, ",");
            dynamicVo.setTopic(topicName);
        }
        // 查询出这些动态下面的评论的汇总举报次数
        QueryWrapper<IdNameDto<Long>> commentWrappers = Wrappers.query();
        commentWrappers.in("ascription_dynamic_id", id);
        commentWrappers.groupBy("ascription_dynamic_id");
        List<IdTypeDto<Long, Integer>> dynamicCommentReportNum = commentMapper.getDynamicCommentReportNum(commentWrappers);
        // 设置动态关联评论总举报次数
        if (dynamicCommentReportNum.size() > 0) {
            Optional<IdTypeDto<Long, Integer>> first = dynamicCommentReportNum.stream().filter(c -> c.getId().equals(id)).findFirst();
            first.ifPresent(longIntegerIdTypeDto -> dynamicVo.setCommentReportNum(Convert.toInt(longIntegerIdTypeDto.getType())));
        }
        return dynamicVo;
    }

    /**
     * 查询动态列表
     *
     * @return 动态
     */
    @Override
    public TableDataInfo<DynamicVo> queryPageList(DynamicDto dto) {
        // 获取动态信息
        QueryWrapper<Dynamic> dynamicWrapper = Wrappers.query();
        // 这个结束时间可能需要获取当前时间
        dynamicWrapper.eq(dto.getId() != null, "d.id", dto.getId());
        dynamicWrapper.eq(dto.getCategoryId() != null, "d.category_id", dto.getCategoryId());
        dynamicWrapper.eq(dto.getMemberId() != null, "d.member_id", dto.getMemberId());
        dynamicWrapper.eq(dto.getType() != null, "d.type", dto.getType());
        dynamicWrapper.like(StringUtils.isNotBlank(dto.getKeyword()), "d.content", dto.getKeyword());
        dynamicWrapper.eq(dto.getStatus() != null, "d.status", dto.getStatus());
        dynamicWrapper.eq(dto.getReportNum() != null, "d.report_num", dto.getReportNum());
        if (StringUtils.isNotBlank(dto.getIsReport()) && dto.getIsReport().equals("0")) {
            dynamicWrapper.eq("d.report_num", 0);
        } else if (StringUtils.isNotBlank(dto.getIsReport()) && dto.getIsReport().equals("1")) {
            dynamicWrapper.gt("d.report_num", 0);
        }
        dynamicWrapper.eq(StringUtils.isNotBlank(dto.getIsUp()), "d.is_up", dto.getIsUp());
        dynamicWrapper.eq(StringUtils.isNotBlank(dto.getIsTop()), "d.is_top", dto.getIsTop());
        dynamicWrapper.eq(StringUtils.isNotBlank(dto.getIsCanFav()), "d.is_can_fav", dto.getIsCanFav());
        dynamicWrapper.eq(dto.getAuditUserId() != null, "d.audit_user_id", dto.getAuditUserId());
        dynamicWrapper.between((dto.getStartTime() != null && dto.getEndTime() != null), "d.create_time", dto.getStartTime(), dto.getEndTime());
        dynamicWrapper.likeRight(StringUtils.isNotBlank(dto.getTopic()), "t.topic_name", dto.getTopic());
        dynamicWrapper.eq("d.del_flag", "0");
        dynamicWrapper.orderByDesc("d.create_time");
        dynamicWrapper.groupBy("d.id");
        Page<DynamicVo> result = baseMapper.selectDynamic(dto.build(), dynamicWrapper);
        // 数据获取完毕之后获取这些用户的昵称
        List<Long> getMemberIds = result.getRecords().stream().map(DynamicVo::getMemberId).distinct().collect(Collectors.toList());
        List<MemberProfile> memberByUserIds = new ArrayList<>();
        if (getMemberIds.size() > 0) {
            memberByUserIds = remoteMemberService.getMemberByUserIds(getMemberIds);
        }
        List<Long> getDynamicIds = result.getRecords().stream().map(DynamicVo::getId).distinct().collect(Collectors.toList());
        // 获取专题的基础信息
        QueryWrapper<IdNameDto<Long>> topicWrapper = Wrappers.query();
        // 这个结束时间可能需要获取当前时间
        topicWrapper.in(getDynamicIds.size() > 0, "dt.dynamic_id", getDynamicIds)
            .eq("t.status", 1);
        List<IdNameDto<Long>> topicList = dynamicTopicMapper.selectTopicNameByDynamicId(topicWrapper);
        List<MemberProfile> finalMemberByUserIds = memberByUserIds;
        // 查询出这些动态下面的评论的汇总举报次数
        QueryWrapper<IdNameDto<Long>> commentWrappers = Wrappers.query();
        commentWrappers.in(getDynamicIds.size() > 0, "ascription_dynamic_id", getDynamicIds);
        commentWrappers.groupBy("ascription_dynamic_id");
        List<IdTypeDto<Long, Integer>> dynamicCommentReportNum = commentMapper.getDynamicCommentReportNum(commentWrappers);
        result.getRecords().forEach(a -> {
            if (finalMemberByUserIds.size() > 0) {
                Optional<MemberProfile> first = finalMemberByUserIds.stream().filter(b -> b.getMemberId().equals(a.getMemberId())).findFirst();
                first.ifPresent(memberProfile -> a.setMemberNickName(memberProfile.getNickName()));
            }
            if (topicList.size() > 0) {
                List<String> collect = topicList.stream().filter(b -> b.getId().equals(a.getId())).map(IdNameDto::getName).collect(Collectors.toList());
                String topicName = StringUtils.join(collect, ",");
                a.setTopic(topicName);
            }
            // 设置动态关联评论总举报次数
            if (dynamicCommentReportNum.size() > 0) {
                Optional<IdTypeDto<Long, Integer>> first = dynamicCommentReportNum.stream().filter(c -> c.getId().equals(a.getId())).findFirst();
                first.ifPresent(longIntegerIdTypeDto -> a.setCommentReportNum(Convert.toInt(longIntegerIdTypeDto.getType())));
            }
            // 设置又拍云域名前缀
            if (a.getCover() != null && StringUtils.isNotBlank(a.getCover())) {
                a.setCover(commonCommunityConfig.getYunDomain() + a.getCover());
            }
            if (a.getResourceUrl() != null && StringUtils.isNotBlank(a.getResourceUrl())) {
                String[] split = a.getResourceUrl().split(",");
                List<String> tempResource = new ArrayList<>();
                for (String s : split) {
                    tempResource.add(commonCommunityConfig.getYunDomain() + s);
                }
                String join = StringUtils.join(tempResource, ",");
                a.setResourceUrl(join);
            }
        });

        return TableDataInfo.build(result);
    }

    /**
     * 审核动态
     *
     * @param dto
     * @return
     */
    @Override
    public R auditDynamic(IdNameTypeDicDto<Long> dto) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(dto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        // 判断当前动态是否存在
        DynamicVo dynamicVo = baseMapper.selectVoById(dto.getId());
        if (dynamicVo == null) {
            return R.fail("当前动态不存在");
        }
        // 执行更新操作
        Integer auditStatus = DynamicStatusEnum.RELEASE.getCode();
        if (dto.getType().equals(2)) {
            auditStatus = DynamicStatusEnum.PEOPLE_FAIL.getCode();
        }
        Long auditUserId = LoginHelper.getUserIdOther();
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Dynamic::getId, dto.getId())
            .set(Dynamic::getStatus, auditStatus)
            .set(auditStatus.equals(DynamicStatusEnum.RELEASE.getCode()), Dynamic::getOnlineTime, new Date())
            .set(Dynamic::getAuditUserId, auditUserId)
            .set(Dynamic::getAuditTime, new Date());
        boolean update = updateChainWrapper.update();
        if (update) {
            // 如果审核成功 数据进到消息队列中
            if (auditStatus.equals(DynamicStatusEnum.RELEASE.getCode())) {
                // 审核通过的时候需要执行更新状态操作
                try {
                    IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                    idNameTypeDicDto.setId(dto.getId());
                    idNameTypeDicDto.setType(DynamicOperationEnum.ONLINE.getCode());
                    dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
                } catch (Exception e) {
                }
                // 判断是否需要添加积分
                iUserBehaviorService.checkUserBehaviorLegitimate(dynamicVo.getMemberId(), 1, dynamicVo.getId());
            } else {
                try {
                    // 审核拒绝的时候我们需要将动态下线
                    IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                    idNameTypeDicDto.setId(dto.getId());
                    idNameTypeDicDto.setType(DynamicOperationEnum.PEOPLE_FAIL.getCode());
                    dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
                } catch (Exception e) {
                }
            }
            // 添加审核记录
            DynamicAuditRecord dynamicAuditRecord = new DynamicAuditRecord();
            dynamicAuditRecord.setDynamicId(dto.getId());
            dynamicAuditRecord.setAuditId(auditUserId);
            dynamicAuditRecord.setAuditStatus(dto.getType());
            dynamicAuditRecord.setAuditContent(dto.getName());
            dynamicAuditRecord.setAuditTime(new Date());
            dynamicAuditRecordMapper.insert(dynamicAuditRecord);

            // 发送消息
            String title = StringUtils.isBlank(dynamicVo.getTitle()) || dynamicVo.getTitle() == null ? dynamicVo.getContent() : dynamicVo.getTitle();
            String content = "";
            if (auditStatus.equals(DynamicStatusEnum.RELEASE.getCode())) {
                // 审核通
                content = "<p>审核成功：尊敬的用户您好，您的动态【" + title + "】审核通过。</p>";
                insertMessageInfo(Arrays.asList(dto.getId()), "系统消息", content, dynamicVo.getMemberId());
            } else if (auditStatus.equals(DynamicStatusEnum.PEOPLE_FAIL.getCode())) {
                // 审核拒绝
                content = "<p>审核失败：尊敬的用户您好，您的动态【" + title + "】审核失败，失败原因：" + dto.getName() + "</p>";
                insertMessageInfo(Arrays.asList(dto.getId()), "系统消息", content, dynamicVo.getMemberId());
            }
            return R.ok("审核成功");
        }
        return R.fail("审核失败");
    }

    /**
     * 新增动态
     *
     * @param bo 动态
     * @return 结果
     */
    @Override
    public R insertByBo(DynamicBo bo) {
        // 判断一下动态类型是否正确
        Integer[] dynamicTypeList = new Integer[]{1, 2, 3};
        Optional<Integer> dynamicTypeAny = Arrays.stream(dynamicTypeList).filter(a -> a.equals(bo.getType())).findAny();
        if (!dynamicTypeAny.isPresent()) {
            return R.fail("动态类型错误");
        }
        // 设置短 Title
        bo.setShortTitle(StringUtils.substring(bo.getTitle(), 0, 50));
        Dynamic add = BeanUtil.toBean(bo, Dynamic.class);
        // 调用dubbo服务检测title和内容中是否存在违禁词
        String clearContent = HtmlUtil.cleanHtmlTag(bo.getContent());
        R checkKeyword = remoteAuditKeywordService.systemCheck((bo.getTitle() + clearContent), 1);
        if (checkKeyword.getCode() == R.FAIL) {
            return R.fail("您发布的动态存在敏感词");
        }
        if (bo.getTitle().length() > commonCommunityConfig.getMaxDynamicTitleLength()) {
            return R.fail("您发布的标题长度超出了限制");
        }
        // 视频和图文类型的数据判断
        if ((bo.getType().equals(1) || bo.getType().equals(2))
            && (StringUtils.isEmpty(bo.getContent()) && StringUtils.isEmpty(bo.getResourceUrl()))) {
            return R.fail("请输入动态内容");
        }
        // 攻略数据判断
        if (bo.getType().equals(3)) {
            if (StringUtils.isEmpty(bo.getTitle())) {
                return R.fail("请输入标题");
            }
            if (StringUtils.isEmpty(bo.getCover())) {
                return R.fail("请上传封面");
            }
            if (StringUtils.isEmpty(bo.getContent())) {
                return R.fail("请输入攻略内容");
            }
            // 添加攻略标题长度限制
            if (bo.getTitle().length() > 50) {
                return R.fail("标题最多50个文字");
            }
        }
        // 根据资源的URL判断上传的是什么 是视频还是图片
        if (bo.getResourceUrl() != null) {
            // 判断资源url一次传输的是多个还是一个
            String[] split = bo.getResourceUrl().split(",");
            String path = split[0];
            Integer resourceType = CommonForCommunityUtils.getResourceType(path);
            if (resourceType == 1 && StringUtils.isEmpty(bo.getContent())) {
                add.setTitle(commonCommunityConfig.getDefaultDynamicPictureTitle());
                add.setContent(commonCommunityConfig.getDefaultDynamicPictureTitle());
            } else if (resourceType == 2 && StringUtils.isEmpty(bo.getContent())) {
                add.setTitle(commonCommunityConfig.getDefaultDynamicVideoTitle());
                add.setContent(commonCommunityConfig.getDefaultDynamicVideoTitle());
            }
            Integer coverType = CommonForCommunityUtils.getCoverType(bo.getCoverWidth(), bo.getCoverHeight(), bo.getResourceUrl());
            add.setCoverType(coverType);
        }
        // 设置短标题
        if (bo.getTitle() != null) {
            bo.setShortTitle(StringUtils.substring(bo.getTitle(), 0, 30));
        }
        // 设置审核状态
        add.setStatus(DynamicStatusEnum.AUDITING.getCode());
        if (bo.getIsSubmit().equals(1)) {
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                // 数据添加成功之后 我们将动态关联的话题添加到指定数据库中
                DynamicTopicBo dynamicTopicBo = new DynamicTopicBo();
                dynamicTopicBo.setDynamicId(add.getId());
                dynamicTopicBo.setTopicIds(bo.getTopicIds());
                iDynamicTopicService.insertDynamicTopic(dynamicTopicBo);
                // 动态的资源需要存放到资源库中
                if (bo.getResourceUrl() != null) {
                    ResourceBo resourceBo = new ResourceBo();
                    resourceBo.setMemberId(bo.getMemberId());
                    resourceBo.setDynamicId(add.getId());
                    resourceBo.setFilePath(bo.getResourceUrl());
                    iResourceService.insertByBo(resourceBo);
                }
                // 将数据添加到消息队列里面 先存储到ES中
                String msgId = "add_dynamic_into_es_" + add.getId();
                IdTypeCommunityDto idTypeCommunityDto = new IdTypeCommunityDto();
                idTypeCommunityDto.setId(add.getId().toString());
                // 数据发送到消息队列中
                dynamicRabbitMqProducer.dynamiInsertIntoEs(idTypeCommunityDto, msgId);
                return R.ok("发布成功");
            }
            return R.fail("发布失败");
        }
        return R.ok("发布成功");
    }

    /**
     * 批量操作动态
     *
     * @param ids 需要删除的动态主键
     * @return 结果
     */
    @Override
    public Boolean operationWithValidByIds(Collection<Long> ids, Long userId, Integer operationType, String name) {
        try {
            DynamicOperationEnum.find(operationType);
        } catch (RuntimeException runtimeException) {
            return false;
        }
        if (ids.size() <= 0) {
            return false;
        }
        // 执行更新操作
        LambdaUpdateChainWrapper<Dynamic> udpateSet = new LambdaUpdateChainWrapper<>(baseMapper)
            .in(Dynamic::getId, ids);
        // 用户ID为0的时候代表着是后台删除操作
        if (userId != 0L) {
            udpateSet.eq(Dynamic::getMemberId, userId);
        }
        // 根据条件设置不通的字段属性
        if (operationType.equals(DynamicOperationEnum.AI_FAIL.getCode())) {
            // AI审核拒绝
            udpateSet.set(Dynamic::getStatus, DynamicStatusEnum.AI_FAIL.getCode());
        } else if (operationType.equals(DynamicOperationEnum.PEOPLE_FAIL.getCode())) {
            // 人工审核拒绝
            udpateSet.set(Dynamic::getStatus, DynamicStatusEnum.PEOPLE_FAIL.getCode());
        } else if (operationType.equals(DynamicOperationEnum.ONLINE.getCode())) {
            // 上线
            udpateSet.set(Dynamic::getIsUp, "0");
            udpateSet.set(Dynamic::getStatus, DynamicStatusEnum.RELEASE.getCode());
        } else if (operationType.equals(DynamicOperationEnum.DOWN.getCode())) {
            // 下线
            udpateSet.set(Dynamic::getIsUp, "1");
            udpateSet.set(Dynamic::getStatus, DynamicStatusEnum.DOWN.getCode());
        } else if (operationType.equals(DynamicOperationEnum.DEL.getCode())) {
            // 删除
            udpateSet.set(Dynamic::getDelFlag, "2");
        } else if (operationType.equals(DynamicOperationEnum.RECOVERY.getCode())) {
            // 恢复删除
            udpateSet.set(Dynamic::getDelFlag, "0");
        } else if (operationType.equals(DynamicOperationEnum.ALL_SEE_ME.getCode())) {
            // 全部可见
            udpateSet.set(Dynamic::getIsOnlyMeSee, "0");
        } else if (operationType.equals(DynamicOperationEnum.ONLY_SEE_ME.getCode())) {
            // 仅我可见
            udpateSet.set(Dynamic::getIsOnlyMeSee, "1");
        } else if (operationType.equals(DynamicOperationEnum.CAN_FAV.getCode())) {
            // 可以点赞
            udpateSet.set(Dynamic::getIsCanFav, "0");
        } else if (operationType.equals(DynamicOperationEnum.NO_CAN_FAV.getCode())) {
            // 不能点赞
            udpateSet.set(Dynamic::getIsCanFav, "1");
        } else if (operationType.equals(DynamicOperationEnum.TOP.getCode())) {
            // 置顶
            udpateSet.set(Dynamic::getIsTop, "1");
        } else if (operationType.equals(DynamicOperationEnum.NO_TOP.getCode())) {
            // 取消置顶
            udpateSet.set(Dynamic::getIsTop, "0");
        }
        Long auditUserId = LoginHelper.getUserIdOther();
        // 添加一下操作的记录
        int rows = 0;
        List<DynamicOperationLog> list = new ArrayList<DynamicOperationLog>();
        for (Long id : ids) {
            DynamicOperationLog dynamicOperationLog = new DynamicOperationLog();
            dynamicOperationLog.setMemberId(auditUserId);
            dynamicOperationLog.setDynamicId(id);
            dynamicOperationLog.setOperationType(operationType);
            dynamicOperationLog.setOperationContent(name);
            list.add(dynamicOperationLog);
        }
        if (list.size() > 0) {
            rows = dynamicOperationLogMapper.insertBatch(list) ? list.size() : 0;
        }

        Boolean result = udpateSet.update();
        if (result) {
            LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery()
                .select(Dynamic::getTitle, Dynamic::getContent, Dynamic::getMemberId, Dynamic::getStatus)
                .in(Dynamic::getId, ids);

            List<DynamicVo> voList = baseMapper.selectVoList(eq);
            if (voList != null && voList.size() > 0) {
                voList.forEach(vo -> {
                    String title = StringUtils.isBlank(vo.getTitle()) || vo.getTitle() == null ? vo.getContent() : vo.getTitle();
                    String content;
                    if (operationType.equals(DynamicOperationEnum.DOWN.getCode())) {
                        // 动态被下架: type =4, 下线
                        content = "<p>动态下架：尊敬的用户您好，您的动态【" + title + "】已下架，下架原因：" + name + "</p>";
                        insertMessageInfo(ids, "系统消息", content, vo.getMemberId());
                    }
                });
            }
        }
        return result;
    }

    /**
     * 动态下架/审核未通过/审核通过的场合，发送系统消息
     *
     * @param ids      主键ids
     * @param title    标题
     * @param content  内容
     * @param memberId 用户id
     * @return
     */
    private Boolean insertMessageInfo(Collection<Long> ids, String title, String content, Long memberId) {

        MessageBo messageBo = new MessageBo();
        messageBo.setTitle(title);
        messageBo.setContent(content);
        messageBo.setPlatformType(Constants.PLATFORM_TYPE_APP);
        messageBo.setPushUserType(Constants.PUSH_USER_PART);
        messageBo.setPushUsers(Convert.toStr(memberId));
        messageBo.setPushTime(new Date());
        messageBo.setPushStatus(Constants.IS_PUSH_YES);
        messageBo.setType(Constants.MESSAGE_TYPE_4);
        messageService.insertByBo(messageBo);

        MessageToUsers entity = new MessageToUsers();
        entity.setMessageId(messageBo.getId());
        entity.setToUserId(memberId);
        entity.setType(Constants.MESSAGE_TYPE_4);
        entity.setReadStatus(Constants.UNREAD);
        for (Long id : ids) {
            entity.setRelationId(id);
            messageToUsersService.insertMessage(entity);
        }
        return true;
    }

    /**
     * 动态的一些基础数据进行自加操作
     *
     * @param idTypeDto
     * @return
     */
    @Override
    public Boolean incrDynamicSomeFieldValue(IdTypeDto<String, Integer> idTypeDto) {
        String[] strArr = idTypeDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        // 循环将数据存储到消息队列中
        for (Long id : ids) {
            String msgId = "operation_dynamic_some_field_value_update_es_" + id;
            IdTypeCommunityDto idTypeCommunityDto = new IdTypeCommunityDto();
            idTypeCommunityDto.setId(id.toString());
            idTypeCommunityDto.setType(idTypeDto.getType().toString());
            dynamicRabbitMqProducer.operationDynamic(idTypeCommunityDto, msgId);
        }
        return true;
    }

    /**
     * 用户注销之后清空用户的动态数据
     *
     * @param userId
     * @return
     */
    @Override
    public R cancellationClearDynamic(Long userId) {
        // 查询出用户所有的状态 将动态全部进行删除掉
        LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery().select(Dynamic::getId).eq(Dynamic::getMemberId, userId);
        List<Dynamic> dynamics = baseMapper.selectList(eq);
        if (dynamics.size() > 0) {
            // 首先更新es
            dynamics.forEach(a -> {
                IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                idNameTypeDicDto.setId(a.getId());
                idNameTypeDicDto.setType(DynamicOperationEnum.DEL.getCode());
                try {
                    dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
                } catch (Exception e) {
                }
            });
            LambdaUpdateChainWrapper<Dynamic> updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Dynamic::getMemberId, userId)
                .set(Dynamic::getDelFlag, MemberConstants.MEMBER_STATUS_DISABLE)
                .set(Dynamic::getUpdateTime, new Date())
                .set(Dynamic::getUpdateBy, userId);
            updateChainWrapper.update();
        }
        return R.ok();
    }

    /**
     * 统计动态数量
     *
     * @return
     */
    @Override
    public Long count(Long userId) {
        LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery()
            .eq(Dynamic::getMemberId, userId)
            .eq(Dynamic::getStatus, DynamicStatusEnum.RELEASE.getCode())
            .eq(Dynamic::getDelFlag, "0");
        return baseMapper.selectCount(eq);
    }

    /**
     * 统计用户某天的动态数量
     */
    public Long dynamicCountForDay(Long userId, Date date) {
        LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery()
            .eq(Dynamic::getMemberId, userId)
            .ge(Dynamic::getOnlineTime, DateUtils.getBeginOfDay(date))
            .le(Dynamic::getOnlineTime, DateUtils.getEndOfDay(date))
            .eq(Dynamic::getStatus, DynamicStatusEnum.RELEASE.getCode())
            .eq(Dynamic::getDelFlag, "0");
        return baseMapper.selectCount(eq);
    }

    /**
     * 删除动态
     *
     * @param idDto
     * @return 结果
     */
    @Override
    public R deleteWithValidByIds(IdDto<String> idDto) {
        Long userId = LoginHelper.getUserId();
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        LambdaQueryWrapper<Dynamic> eq = Wrappers.<Dynamic>lambdaQuery()
            .select(Dynamic::getId)
            .eq(Dynamic::getMemberId, userId)
            .in(Dynamic::getId, ids);
        List<Dynamic> dynamics = baseMapper.selectList(eq);
        if (dynamics.size() > 0) {
            // 首先更新es
            dynamics.forEach(a -> {
                IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                idNameTypeDicDto.setId(a.getId());
                idNameTypeDicDto.setType(DynamicOperationEnum.DEL.getCode());
                try {
                    dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
                } catch (Exception e) {
                }
            });
            LambdaUpdateChainWrapper<Dynamic> updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Dynamic::getMemberId, userId)
                .in(Dynamic::getId, ids)
                .set(Dynamic::getDelFlag, MemberConstants.MEMBER_STATUS_DISABLE)
                .set(Dynamic::getUpdateTime, new Date())
                .set(Dynamic::getUpdateBy, userId);
            updateChainWrapper.update();
        }
        return R.ok();
    }

}
