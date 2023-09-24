package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.api.domain.UserExtend;
import com.euler.community.domain.dto.IndexDto;
import com.euler.community.domain.dto.WxUserDto;
import com.euler.community.domain.entity.DynamicFrontEs;
import com.euler.community.domain.vo.RegionVo;
import com.euler.community.domain.vo.UserVo;
import com.euler.community.domain.vo.WxUserVo;
import com.euler.community.enums.DynamicOperationEnum;
import com.euler.community.esMapper.DynamicElasticsearch;
import com.euler.community.service.*;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.MemberProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户在APP的行为操作模块
 * 前端访问路由地址为:/community/user
 *
 * @author euler
 * @date 2022-06-01
 */
@Slf4j
@Validated
@Api(value = "首页控制器", tags = {"首页管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IDynamicService iDynamicService;
    @Autowired
    private DynamicElasticsearch dynamicElasticsearch;
    @Autowired
    private IRegionService regionService;
    @Autowired
    private IAttentionService iAttentionService;
    @Autowired
    private ICollectService iCollectService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @Autowired
    private IPraiseService iPraiseService;
    @Autowired
    private IUserBehaviorService iUserBehaviorService;
    @Autowired
    private IUserExtendService userExtendService;
    @Autowired
    private ICommentService iCommentService;

    /**
     * 列表功能查询
     *
     * @param indexDto
     * @return
     */
    @ApiOperation("用户动态列表页面")
    @PostMapping("/dynamic")
    public TableDataInfo<DynamicFrontEs> dynamic(@RequestBody IndexDto indexDto) {
        Long userId = 0L;
        if (LoginHelper.isLogin()) {
            userId = LoginHelper.getUserId();
        }
        indexDto.setPosition(3);
        indexDto.setMemberId(indexDto.getMemberId() != null ? indexDto.getMemberId() : userId);
        try {
            TableDataInfo<DynamicFrontEs> documentByPage = dynamicElasticsearch.getDocumentByPage(indexDto);
            List<Long> dynamicIds = documentByPage.getRows().stream().map(DynamicFrontEs::getId).collect(Collectors.toList());
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
            // 循环过滤开始进行赋值操作
            for (DynamicFrontEs dynamicFrontEs : documentByPage.getRows()) {
                // 数据获取完毕之后需要判断当前用户是否针对与这些文章进行了点赞和收藏
                // 查询出当前用户针对与这个稿件那些存在点赞行为
                if (praiseOperationList != null && praiseOperationList.contains(dynamicFrontEs.getId())) {
                    dynamicFrontEs.setIsPraise(1);
                }
                if (collectOperationList != null && collectOperationList.contains(dynamicFrontEs.getId())) {
                    dynamicFrontEs.setIsCollect(1);
                }
                if (attentionOperationList != null && attentionOperationList.contains(dynamicFrontEs.getMemberId())) {
                    dynamicFrontEs.setIsAttention(1);
                }
                // 设置昵称和头像
                Optional<MemberProfile> first = memberByUserIds.stream().filter(b -> b.getMemberId().equals(dynamicFrontEs.getMemberId())).findFirst();
                first.ifPresent(memberProfile -> {
                    dynamicFrontEs.setNickname(memberProfile.getNickName());
                    dynamicFrontEs.setAvatar(iUserBehaviorService.getAvatar(memberProfile.getSex(), memberProfile.getAvatar()));
                    dynamicFrontEs.setIsOfficial(memberProfile.getIsOfficial());
                });
            }
            return documentByPage;
        } catch (Exception e) {
            log.info("用户个人列表出现了错误:", e);
        }
        TableDataInfo<DynamicFrontEs> error = new TableDataInfo<>();
        error.setCode(200);
        error.setRows(new ArrayList<>());
        error.setMsg("数据获取成功");
        return error;
    }


    /**
     * 删除动态
     */
    @ApiOperation("删除动态")
    @Log(title = "删除动态", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        // 首先修改ES中的状态
        try {
            for (Long id : ids) {
                IdNameTypeDicDto<Long> idNameTypeDicDto = new IdNameTypeDicDto();
                idNameTypeDicDto.setId(id);
                idNameTypeDicDto.setType(DynamicOperationEnum.DEL.getCode());
                dynamicElasticsearch.operationDataToElasticSearch(idNameTypeDicDto);
            }
        } catch (Exception e) {
        }
        Long userId = LoginHelper.getUserId();
        return toAjax(iDynamicService.operationWithValidByIds(Arrays.asList(ids), userId, DynamicOperationEnum.DEL.getCode(), "") ? 1 : 0);
    }

    @ApiOperation("获取指定类型的地区信息")
    @PostMapping("/getAppointRegion")
    public R<List<RegionVo>> getAppointRegion(@RequestBody IdTypeDto<Integer, Integer> idTypeDto) {
        List<RegionVo> appointRegion = regionService.getAppointRegion(idTypeDto.getType(), idTypeDto.getId());

        return R.ok(appointRegion);
    }


    private Long getUserId(IdDto<Long> idDto) {
        Long userId = 0L;
        if (idDto == null || idDto.getId() == null || idDto.getId() == 0L) {
            if (LoginHelper.isLogin()) {
                userId = LoginHelper.getUserId();
            }
        } else {
            userId = idDto.getId();
        }
        return userId;
    }


    @ApiOperation("获取用户基础数据")
    @PostMapping("/getUserBaseData")
    public R getUserBaseData(@RequestBody IdDto<Long> idDto) {
        // 获取用户的ID
        Long userId = getUserId(idDto);
        Long loginUserId = LoginHelper.isLogin() ? LoginHelper.getUserId() : 0L;
        Boolean isOneSelf = userId.equals(loginUserId);
        if (userId.equals(0L))
            return R.fail("用户信息错误");


        UserVo userVo = new UserVo();
        userVo.setDynamicCount(Convert.toInt(iDynamicService.count(userId)));
        userVo.setAttentionCount(iAttentionService.queryAttentionCountById(userId));
        userVo.setFanCount(iAttentionService.queryFansCountById(userId));
        // 判断用户是否关注过指定用户
        if (loginUserId > 0) {
            if (!isOneSelf) {
                Boolean attention = iAttentionService.isAttention(loginUserId, userId);
                userVo.setIsAttention(attention ? 1 : 0);
            } else {
                // 只有获取自己的时候我们需要展示出收藏量
                userVo.setCollectCount(iCollectService.count(userId));
            }
        }

        // 获取用户的基础信息
        MemberProfile memberProfile = remoteMemberService.getMemberByUserId(userId);
        if (memberProfile != null) {
            var userExtend = userExtendService.getById(userId);
            userVo.setUserId(memberProfile.getMemberId());
            userVo.setNickName(memberProfile.getNickName());
            userVo.setAvatar(iUserBehaviorService.getAvatar(memberProfile.getSex(), memberProfile.getAvatar()));
            userVo.setSex(memberProfile.getSex());
            userVo.setDescription(memberProfile.getDescription());
            userVo.setIsOfficial(memberProfile.getIsOfficial());
            userVo.setProvinceId(memberProfile.getProvinceId());
            userVo.setProvince(memberProfile.getProvince());
            userVo.setCityId(memberProfile.getCityId());
            userVo.setCity(memberProfile.getCity());
            userVo.setAreaId(memberProfile.getAreaId());
            userVo.setArea(memberProfile.getArea());
            userVo.setMobile(memberProfile.getMobile());
            userVo.setIdCardNo(memberProfile.getIdCardNo());
            userVo.setVerifyStatus(memberProfile.getVerifyStatus());
            if (userExtend == null) {
                userExtend = new UserExtend();
                userExtend.setAvatar(memberProfile.getAvatar());
                userExtend.setIsOfficial(memberProfile.getIsOfficial());
                userExtend.setSex(memberProfile.getSex());
                userExtend.setNickName(memberProfile.getNickName());
                userExtend.setMemberId(userId);
                userExtendService.save(userExtend);
            }
        }
        return R.ok(userVo);
    }

    @ApiOperation("获取微信用户动态数据")
    @PostMapping("/getWxUserBaseData")
    public R getWxUserBaseData(@RequestBody WxUserDto dto) {
        Long userId = dto.getUserId();
        Long loginUserId = LoginHelper.isLogin() ? LoginHelper.getUserId() : 0L;
        Boolean isOneSelf = userId.equals(loginUserId);
        if (userId.equals(0L))
            return R.fail("用户信息错误");

        WxUserVo vo = new WxUserVo();
        vo.setUserId(userId);
        // 用户某天的动态数量
        vo.setDynamicCount(Convert.toInt(iDynamicService.dynamicCountForDay(userId, dto.getDate())));

        // 用户某天的评论数量
        Integer dynamicCommentCount = iCommentService.commentCountForDay(userId, dto.getDate(), "1");
        Integer commentComCount = iCommentService.commentCountForDay(userId, dto.getDate(), "2");
        Integer commentCount = dynamicCommentCount + commentComCount;
        vo.setCommentCount(commentCount);

        // 用户某天的点赞数量
        Integer dynamicPraiseCount = iPraiseService.praiseCountForDay(userId, dto.getDate(), "1");
        Integer commentPraiseCount = iPraiseService.praiseCountForDay(userId, dto.getDate(), "2");
        Integer praiseCount = dynamicPraiseCount + commentPraiseCount;
        vo.setPraiseCount(praiseCount);

        if(loginUserId > 0) {
            if (isOneSelf) {
                // 自己的时候，才展示收藏数量
                vo.setCollectCount(iCollectService.collectCountForDay(userId, dto.getDate()));
            }
        }

        return R.ok(vo);
    }
}
