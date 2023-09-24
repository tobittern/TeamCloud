package com.euler.community.controller;


import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.AdvertBo;
import com.euler.community.domain.bo.HistorySearchBo;
import com.euler.community.domain.dto.AdvertDynamicEsDto;
import com.euler.community.domain.dto.CollectDto;
import com.euler.community.domain.dto.IndexDto;
import com.euler.community.domain.dto.PraiseDto;
import com.euler.community.domain.entity.DynamicFrontEs;
import com.euler.community.domain.vo.*;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.enums.DynamicStatusEnum;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.service.*;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 首页Controller
 * 前端访问路由地址为:/community/index
 *
 * @author euler
 * @date 2022-06-01
 */
@Slf4j
@Validated
@Api(value = "首页控制器", tags = {"首页管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private DynamicElasticsearch dynamicElasticsearch;
    @Autowired
    private IPraiseService iPraiseService;
    @Autowired
    private ICollectService iCollectService;
    @Autowired
    private IAttentionService iAttentionService;
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private IDynamicService iDynamicService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private IAdvertService iAdvertService;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private IHistorySearchService iHistorySearchService;

    /**
     * 列表功能查询
     *
     * @param indexDto
     * @return
     */
    @ApiOperation("首页列表页面")
    @PostMapping("/list")
    public TableDataInfo<DynamicFrontEs> list(@RequestBody IndexDto indexDto) {
        // 异常捕获 获取当前用户的用户ID
        Long userId = 0L;
        List<Long> searchMemberIds = null;
        List<Long> collectDynamicIds = null;
        // 用户没有登录的时候 获取用户相关的数据都是空的
        if (!LoginHelper.isLogin() && (indexDto.getPosition() == 2 || indexDto.getPosition() == 4 || indexDto.getPosition() == 5)) {
            TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
            error.setCode(200);
            error.setRows(new ArrayList<>());
            error.setMsg("数据获取成功");
            return error;
        }
        if (indexDto.getPosition().equals(6)) {
            // 判断是否存在需要剔除的动态ID
            if (StringUtils.isNotBlank(indexDto.getExcludeDynamicIdString())) {
                List<Long> excludeIds = Arrays.stream(indexDto.getExcludeDynamicIdString().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
                // 设置排除的ids
                if (excludeIds.size() > 0) {
                    indexDto.setExcludeDynamicIds(excludeIds);
                }
            }
        }
        if (LoginHelper.isLogin()) {
            userId = LoginHelper.getUserId();
            if (indexDto.getPosition() == 2) {
                // 查询当前用户关注的一些用户的动态
                // 查询当前用户关注了那些用户
                List<AttentionVo> attentionVos = iAttentionService.queryMyAttentionList();
                if (attentionVos.size() > 0) {
                    searchMemberIds = attentionVos.stream().map(AttentionVo::getAttentionUserId).collect(Collectors.toList());
                } else {
                    TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
                    error.setCode(200);
                    error.setRows(new ArrayList<>());
                    error.setMsg("数据获取成功");
                    return error;
                }
                // 设置一下查询条件
                indexDto.setSearchMemberIds(searchMemberIds);
            } else if (indexDto.getPosition() == 4) {
                // 查询出当前用户收藏的列表
                CollectDto collectDto = new CollectDto();
                collectDto.setPageNum(indexDto.getPageNum());
                collectDto.setPageSize(indexDto.getPageSize());
                collectDto.setMemberId(userId);
                TableDataInfo<CollectVo> collectVoTableDataInfo = iCollectService.queryPageList(collectDto);
                if (collectVoTableDataInfo.getRows().size() > 0) {
                    collectDynamicIds = collectVoTableDataInfo.getRows().stream().map(CollectVo::getDynamicId).collect(Collectors.toList());
                } else {
                    TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
                    error.setCode(200);
                    error.setRows(new ArrayList<>());
                    error.setMsg("数据获取成功");
                    return error;
                }
                indexDto.setSearchDynamicIds(collectDynamicIds);
            } else if (indexDto.getPosition() == 5) {
                PraiseDto praiseDto = new PraiseDto();
                praiseDto.setPageNum(indexDto.getPageNum());
                praiseDto.setPageSize(indexDto.getPageSize());
                praiseDto.setMemberId(userId);
                praiseDto.setType(1);
                TableDataInfo<PraiseVo> praiseVoTableDataInfo = iPraiseService.queryPageList(praiseDto);
                if (praiseVoTableDataInfo.getRows().size() > 0) {
                    collectDynamicIds = praiseVoTableDataInfo.getRows().stream().map(PraiseVo::getRelationId).collect(Collectors.toList());
                } else {
                    TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
                    error.setCode(200);
                    error.setRows(new ArrayList<>());
                    error.setMsg("数据获取成功");
                    return error;
                }
                indexDto.setSearchDynamicIds(collectDynamicIds);
            }
        }
        try {
            List<AdvertDynamicEsDto> advertDynamicEsDtos = null;
            if (indexDto.getPosition().equals(1)) {
                // 获取一下当前也是否存在广告
                AdvertBo advertBo = new AdvertBo();
                advertBo.setPage(indexDto.getPageNum());
                advertBo.setMemberId(userId);
                advertDynamicEsDtos = iAdvertService.queryByPageNum(advertBo);
            }
            // 从ES中获取搜索数据
            TableDataInfo<DynamicFrontEs> documentByPage = dynamicElasticsearch.getDocumentByPage(indexDto);
            List<Long> dynamicIds = documentByPage.getRows().stream().map(DynamicFrontEs::getId).collect(Collectors.toList());
            List<CommentVo> commentVos = iCommentService.selectCommentByMemberIds(dynamicIds);
            List<Long> memberIds = documentByPage.getRows().stream().map(DynamicFrontEs::getMemberId).collect(Collectors.toList());
            // 查询出这些用户的昵称和头像
            List<MemberProfile> memberByUserIds = remoteMemberService.getMemberByUserIds(memberIds);
            // 进行对象的拷贝
            List<Long> praiseOperationList = null;
            List<Long> collectOperationList = null;
            List<Long> attentionOperationList = null;
            if (userId != 0L) {
                praiseOperationList = iPraiseService.checkUserIsPraise(userId, 1, dynamicIds);
                collectOperationList = iCollectService.checkUserIsCollect(userId, dynamicIds);
                attentionOperationList = iAttentionService.checkUserIsAttention(userId, memberIds);
            }
            // 循环对数据进行处理
            List<DynamicFrontEs> newDynamicList = new ArrayList<>();
            for (int i = 0; i < documentByPage.getRows().size(); i++) {
                int tempI = i;
                // 首先循环一下追加神评
                Long dynamicId = documentByPage.getRows().get(tempI).getId();
                Long dynamicMemberId = documentByPage.getRows().get(tempI).getMemberId();
                Optional<CommentVo> first = commentVos.stream().filter(b -> b.getAscriptionDynamicId().equals(dynamicId)).findFirst();
                first.ifPresent(commentVo -> documentByPage.getRows().get(tempI).setCommentFrontVo(commentVo));
                // 追加是否点赞
                if (praiseOperationList != null && praiseOperationList.contains(dynamicId)) {
                    documentByPage.getRows().get(tempI).setIsPraise(1);
                }
                // 追加是否收藏
                if (collectOperationList != null && collectOperationList.contains(dynamicId)) {
                    documentByPage.getRows().get(tempI).setIsCollect(1);
                }
                // 追加是否关注
                if (attentionOperationList != null && attentionOperationList.contains(dynamicMemberId)) {
                    documentByPage.getRows().get(tempI).setIsAttention(1);
                }
                // 设置昵称和头像
                Optional<MemberProfile> second = memberByUserIds.stream().filter(b -> b.getMemberId().equals(dynamicMemberId)).findFirst();
                if (second.isPresent()) {
                    documentByPage.getRows().get(tempI).setNickname(second.get().getNickName());
                    documentByPage.getRows().get(tempI).setAvatar((iUserBehaviorService.getAvatar(second.get().getSex(), second.get().getAvatar())));
                    documentByPage.getRows().get(tempI).setIsOfficial(second.get().getIsOfficial());
                }
                // 基础数据设置完毕之后我们需要将数据添加到新的数组中
                newDynamicList.add(documentByPage.getRows().get(tempI));
                // 判断是否存在广告
                if (advertDynamicEsDtos != null && advertDynamicEsDtos.size() > 0) {
                    // 过滤一下当前页数是否存在广告
                    List<AdvertDynamicEsDto> searchAdvCollect = advertDynamicEsDtos.stream().filter(page -> page.getRow().equals(tempI)).collect(Collectors.toList());
                    if (searchAdvCollect.size() > 0) {
                        // 一个位置存在多个动态 所以我们需要循环追加
                        for (int i1 = 0; i1 < searchAdvCollect.size(); i1++) {
                            newDynamicList.add(searchAdvCollect.get(i1).getDynamicEs());
                        }
                    }
                }
            }
            // 将新的数据进行一下替换
            documentByPage.setRows(newDynamicList);
            // 判断一下类型将搜索词添加的用户历史搜索记录表中
            if (indexDto.getPosition().equals(1)) {
                HistorySearchBo historySearchBo = new HistorySearchBo();
                historySearchBo.setMemberId(userId);
                historySearchBo.setKey(indexDto.getKeyword());
                historySearchBo.setPosition(0);
                iHistorySearchService.insertByBo(historySearchBo);
            }
            return documentByPage;
        } catch (Exception e) {
            log.info("首页列表出现了错误:", e);
        }
        TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
        error.setCode(200);
        error.setRows(new ArrayList<>());
        error.setMsg("数据获取成功");
        return error;
    }

    /**
     * 详情获取
     *
     * @param idDto
     * @return
     */
    @ApiOperation("首页列表页面")
    @PostMapping("/detail")
    public R detail(@RequestBody IdDto<Long> idDto) {
        if (idDto.getId() == null) {
            return R.fail("动态已下架或者删除");
        }
        // 异常捕获 获取当前用户的用户ID
        Long userId = 0L;
        if (LoginHelper.isLogin()) {
            userId = LoginHelper.getUserId();
        }
        try {
            DynamicFrontEs document = dynamicElasticsearch.getDocument(idDto.getId().toString());
            // 删除了将无法访问到
            if (!document.getDelFlag().equals("0")) {
                return R.fail("动态已下架或者删除");
            }
            if ((!document.getStatus().equals(DynamicStatusEnum.RELEASE.getCode()) || document.getIsUp().equals("1"))
                && !document.getMemberId().equals(userId)) {
                // 下架状态 如果当前动态不是当前用户的 将无法访问
                return R.fail("动态已下架或者删除");
            }
            List<Long> praiseOperationList = null;
            List<Long> collectOperationList = null;
            Boolean attention = false;
            if (userId != 0L) {
                // 数据获取完毕之后需要判断当前用户是否针对与这些文章进行了点赞和收藏
                List<Long> dynamicIds = new ArrayList<>();
                dynamicIds.add(document.getId());
                // 查询出当前用户针对与这个稿件那些存在点赞行为
                praiseOperationList = iPraiseService.checkUserIsPraise(userId, 1, dynamicIds);
                collectOperationList = iCollectService.checkUserIsCollect(userId, dynamicIds);
                attention = iAttentionService.isAttention(userId, document.getMemberId());
            }
            // 循环过滤开始进行赋值操作
            if (praiseOperationList != null && praiseOperationList.contains(document.getId())) {
                document.setIsPraise(1);
            }
            if (collectOperationList != null && collectOperationList.contains(document.getId())) {
                document.setIsCollect(1);
            }
            if (attention) {
                document.setIsAttention(1);
            }
            // 查询出这些用户的昵称和头像
            MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(document.getMemberId());
            // 设置用户昵称和头像
            if (memberByUserId != null) {
                document.setNickname(memberByUserId.getNickName());
                document.setAvatar(iUserBehaviorService.getAvatar(memberByUserId.getSex(), memberByUserId.getAvatar()));
                document.setIsOfficial(memberByUserId.getIsOfficial());
            }
            // 需要获取动态的真实内容 没有去掉html标签的
            DynamicVo dynamicVo = iDynamicService.queryById(document.getId());
            document.setContent(dynamicVo.getContent());
            // 更新动态表和Es中的数据
            IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
            idTypeDto.setId(idDto.getId().toString());
            idTypeDto.setType(DynamicFieldIncrEnum.CLICK.getCode());
            iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            return R.ok(document);
        } catch (Exception e) {
            log.info("首页详情出现了错误:", e);
        }
        return R.fail("动态已下架或者删除");
    }


    /**
     * 分享页面详情获取
     *
     * @return
     */
    @ApiOperation("分享页面详情获取")
    @PostMapping("/shareDetail")
    public R shareDetail(@RequestBody IdDto<Long> idDto) {
        if (idDto.getId() == null) {
            return R.fail();
        }
        try {
            DynamicFrontEs document = dynamicElasticsearch.getDocument(idDto.getId().toString());
            // 没有审核通过的+下架的不能访问
            if (!document.getStatus().equals(DynamicStatusEnum.RELEASE.getCode())
                || document.getIsUp().equals("1")
                || !document.getDelFlag().equals("0")) {
                return R.fail();
            }
            // 查询出这些用户的昵称和头像
            try {
                MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(document.getMemberId());
                // 设置用户昵称和头像
                if (memberByUserId != null) {
                    document.setNickname(memberByUserId.getNickName());
                    document.setAvatar(iUserBehaviorService.getAvatar(memberByUserId.getSex(), memberByUserId.getAvatar()));
                    document.setIsOfficial(memberByUserId.getIsOfficial());
                }
            } catch (Exception e) {
            }
            // 需要获取动态的真实内容 没有去掉html标签的
            DynamicVo dynamicVo = iDynamicService.queryById(document.getId());
            document.setContent(dynamicVo.getContent());
            // 更新动态表和Es中的数据
            IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
            idTypeDto.setId(idDto.getId().toString());
            idTypeDto.setType(DynamicFieldIncrEnum.CLICK.getCode());
            iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            return R.ok(document);
        } catch (Exception e) {
            log.info("分享页面详情获取错误:", e);
        }
        return R.fail("数据不存在");
    }


}
