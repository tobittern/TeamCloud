package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.RemoteOrderService;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.GameUseRecordBo;
import com.euler.sdk.domain.bo.GiftActivityBo;
import com.euler.sdk.domain.dto.GiftActivityDto;
import com.euler.sdk.domain.dto.GiftActivityFrontDto;
import com.euler.sdk.domain.entity.GameUseRecord;
import com.euler.sdk.domain.entity.GiftActivity;
import com.euler.sdk.domain.vo.GiftActivityMiddlerVo;
import com.euler.sdk.domain.vo.GiftActivityVo;
import com.euler.sdk.domain.vo.MemberProfileVo;
import com.euler.sdk.enums.GameUseRecordTypeEnum;
import com.euler.sdk.mapper.GiftActivityMapper;
import com.euler.sdk.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 礼包活动管理Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GiftActivityServiceImpl implements IGiftActivityService {
    @Autowired
    private GiftActivityMapper baseMapper;
    @Autowired
    private IGameUseRecordService iGameUseRecordService;
    @Autowired
    private IMyGameService iMyGameService;
    @Autowired
    private IGiftReceiveRecordService iGiftReceiveRecordService;
    @Autowired
    private IMemberProfileService iMemberProfileService;
    @DubboReference
    private RemoteOrderService remoteOrderService;

    /**
     * 查询礼包活动管理
     *
     * @param id 礼包活动管理主键
     * @return 礼包活动管理
     */
    @Override
    public GiftActivityVo queryById(Integer id) {
        GiftActivityVo giftActivityVo = baseMapper.selectVoById(id);
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.ACTIVITY_GIFT_BAG.getCode());
        if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
            // 判断哪一个包含了这款游戏
            List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(id)).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
            // 添加一个过滤 将获取的游戏列表按照id进行一下归类
            giftActivityVo.setGameOriented(newList);
        }
        return giftActivityVo;
    }

    /**
     * 查询礼包活动管理列表 - 增加需求: 需要判断
     *
     * @return 礼包活动管理
     */
    @Override
    public TableDataInfo<GiftActivityVo> queryPageList(GiftActivityDto dto) {
        // 判断当前活动是否包含这款游戏
        LambdaQueryWrapper<GiftActivity> lqw = buildQueryWrapper(dto);
        Page<GiftActivityVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        // 获取到了当前活动的礼包分页全部数据
        List<Integer> ids = result.getRecords().stream().map(GiftActivityVo::getId).collect(Collectors.toList());
        List<OpenGameDubboVo> openGameDubboVos = iGameUseRecordService.selectGameNameByIds(ids, GameUseRecordTypeEnum.ACTIVITY_GIFT_BAG.getCode());
        // 循环获取数据之后将游戏名累加到元数据中
        result.getRecords().forEach(row -> {
            if (openGameDubboVos != null && openGameDubboVos.size() > 0) {
                // 判断哪一个包含了这款游戏
                List<KeyValueDto> newList = openGameDubboVos.stream().filter(e -> e.getRelationList().contains(row.getId())).map(e -> new KeyValueDto(e.getId(), e.getGameName())).collect(Collectors.toList());
                // 添加一个过滤 将获取的游戏列表按照id进行一下归类
                row.setGameOriented(newList);
            }
        });
        return TableDataInfo.build(result);
    }


    /**
     * 查询礼包活动管理列表 - 增加需求: 需要判断
     *
     * @return 礼包活动管理
     */
    @Override
    public TableDataInfo<GiftActivityVo> queryFrontPageList(GiftActivityFrontDto dto) {
        // 从登陆信息中获取 游戏id和分包号
        dto.setGameId(LoginHelper.getSdkChannelPackage().getGameId());
        dto.setPackageCode(LoginHelper.getSdkChannelPackage().getPackageCode());
        // 如果无法从登陆中获取 我们就直接返回空
        if (dto.getGameId() == null || dto.getPackageCode() == null) {
            Page<GiftActivityVo> result = new Page<>();
            return TableDataInfo.build(result);
        }
        // 获取到当前游戏活动礼包的id集合 判断当前游戏绑定了那些活动礼包
        GameUseRecordBo gameUseRecordParams = new GameUseRecordBo();
        gameUseRecordParams.setPartyBId(dto.getGameId().toString());
        gameUseRecordParams.setType(GameUseRecordTypeEnum.ACTIVITY_GIFT_BAG.getCode());
        List<GameUseRecord> gameUseRecords = iGameUseRecordService.selectInfoByParams(gameUseRecordParams);
        List<Integer> activityId = new ArrayList<>();
        // 首先增加一个默认值方式in的时候报错
        activityId.add(0);
        gameUseRecords.forEach(a -> {
            activityId.add(a.getPartyAId());
        });
        // 数据查询之后 我们需要判断当前获取的活动礼包列表中那些用户是可以进行领取的 如果能领取打上标签
        LambdaQueryWrapper<GiftActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getGiftType()), GiftActivity::getGiftType, dto.getGiftType());
        lqw.likeRight(StringUtils.isNotBlank(dto.getGiftName()), GiftActivity::getGiftName, dto.getGiftName());
        lqw.in(GiftActivity::getId, activityId);
        lqw.eq(GiftActivity::getIsOnline, 1);
        lqw.orderByAsc(GiftActivity::getSort);
        Page<GiftActivityVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        // 获取当前礼包的一些相关判断数据
        Long userId = LoginHelper.getUserId();
        GiftActivityMiddlerVo giftNeedData = getGiftNeedData(userId, dto.getGameId(), dto.getPackageCode());
        // 循环判断
        result.getRecords().forEach(row -> {
            if (checkIsReceive(row, giftNeedData.getMyGameVo(), dto.getGameId()) && checkIsAlreadyReceive(row.getId(), giftNeedData.getIds())) {
                row.setIsReceive(2);
            } else if (checkIsReceive(row, giftNeedData.getMyGameVo(), dto.getGameId()) && !checkIsAlreadyReceive(row.getId(), giftNeedData.getIds())) {
                row.setIsReceive(1);
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 获取礼包的时候我们根据一些条件获取一些礼包相关的数据 用于判断是否可以领取 是否领取过
     *
     * @param packageCode
     * @return
     */
    public GiftActivityMiddlerVo getGiftNeedData(Long userId, Integer gameId, String packageCode) {
        // 获取游戏基础信息
        MyGameVo myGameVo = iMyGameService.selectUserGameByParams(userId, gameId, packageCode);
        log.debug("用户游戏的基础数据,{}", JsonUtils.toJsonString(myGameVo));
        ArrayList<Integer> ids = iGiftReceiveRecordService.selectUserAlreadyList("2", userId);
        log.debug("用户获取活动礼包的领取列表,{}", JsonUtils.toJsonString(ids));
        GiftActivityMiddlerVo giftActivityMiddlerVo = new GiftActivityMiddlerVo();
        giftActivityMiddlerVo.setMyGameVo(myGameVo);
        giftActivityMiddlerVo.setIds(ids);
        return giftActivityMiddlerVo;
    }

    /**
     * 判断是否已经领取过了
     *
     * @return
     */
    public Boolean checkIsAlreadyReceive(Integer activityId, ArrayList<Integer> ids) {
        // 循环判断当前活动礼包的id是否在这个集合中
        Boolean isHave = false;
        for (Integer id : ids) {
            if (id.equals(activityId)) {
                isHave = true;
                break;
            }
        }
        return isHave;
    }

    /**
     * 判断是否有资格领取
     *
     * @param vo
     * @param myGameVo
     * @param gameId
     * @return
     */
    public Boolean checkIsReceive(GiftActivityVo vo, MyGameVo myGameVo, Integer gameId) {
        // 预留 判断当前用户  指定礼包已经领取过了 那么我们不能再让用户领取了
        Boolean isReceive = false;
        switch (vo.getGiftType().toString()) {
            // TODO 游戏参数还未设定
            case Constants.GIFT_ACTIVITY_TYPE_1:
            case Constants.GIFT_ACTIVITY_TYPE_2:
            case Constants.GIFT_ACTIVITY_TYPE_3:
                // TODO 调取远程订单的dubbo服务获取订单
                BigDecimal bigDecimal = Convert.toBigDecimal(vo.getRewardConditions());
                MemberOrderStatDto memberOrderStatInfo = remoteOrderService.getMemberOrderStatInfo(LoginHelper.getUserId(), gameId, bigDecimal);
                log.debug("dubbo服务获取订单的数据,{}", JsonUtils.toJsonString(memberOrderStatInfo));
                log.debug("vo实体的数据，{}", JsonUtils.toJsonString(vo));
                if (vo.getGiftType().toString().equals(Constants.GIFT_ACTIVITY_TYPE_1) && memberOrderStatInfo.getRechargeNum() > 0) {
                    // 首充礼包
                    isReceive = true;
                } else if (vo.getGiftType().toString().equals(Constants.GIFT_ACTIVITY_TYPE_2)) {
                    // 单笔充值
                    isReceive = memberOrderStatInfo.getCheckRechargeAmount();
                } else if (vo.getGiftType().toString().equals(Constants.GIFT_ACTIVITY_TYPE_3)
                    && (memberOrderStatInfo.getRechargeAmount().compareTo(Convert.toBigDecimal(vo.getRewardConditions())) == 1
                        || memberOrderStatInfo.getRechargeAmount().compareTo(Convert.toBigDecimal(vo.getRewardConditions())) == 0)) {
                    // 累计充值
                    isReceive = true;
                }
                break;
            case Constants.GIFT_ACTIVITY_TYPE_4:
            case Constants.GIFT_ACTIVITY_TYPE_5:
            case Constants.GIFT_ACTIVITY_TYPE_6:
                if (vo.getGiftType().toString().equals(Constants.GIFT_ACTIVITY_TYPE_4)) {
                    // 判断等级是否达到
                    if (myGameVo.getGameLevel() != null
                        && Convert.toInt(myGameVo.getGameLevel()) >= vo.getRewardConditions()) {
                        isReceive = true;
                    }
                } else if (vo.getGiftType().toString().equals(Constants.GIFT_ACTIVITY_TYPE_5)) {
                    // 累计在线时长
                    if (myGameVo.getGameDuration() != null
                        && Convert.toInt(myGameVo.getGameDuration()) >= vo.getRewardConditions()) {
                        isReceive = true;
                    }
                } else {
                    // 新人礼包
                    if (myGameVo.getId() != null) {
                        isReceive = true;
                    }
                }
                break;
            case Constants.GIFT_ACTIVITY_TYPE_7:
                // 实名认证礼包
                // 判断用户是否实名认证过
                Long userId = LoginHelper.getUserId();
                MemberProfileVo voByMemberId = iMemberProfileService.getVoByMemberId(userId);
                if (voByMemberId != null && voByMemberId.getVerifyStatus().equals("1")) {
                    isReceive = true;
                }
            default:
                break;
        }
        return isReceive;
    }


    private LambdaQueryWrapper<GiftActivity> buildQueryWrapper(GiftActivityDto dto) {
        LambdaQueryWrapper<GiftActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getGiftType()), GiftActivity::getGiftType, dto.getGiftType());
        lqw.likeRight(StringUtils.isNotBlank(dto.getGiftName()), GiftActivity::getGiftName, dto.getGiftName());
        lqw.orderByAsc(GiftActivity::getSort);
        return lqw;
    }


    /**
     * 新增礼包活动管理
     *
     * @param bo 活动管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertByBo(GiftActivityBo bo) {
        GiftActivity add = BeanUtil.toBean(bo, GiftActivity.class);
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 针对绑定的游戏进行一下处理
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(add.getId());
            gameUseRecordBo.setPartyBId(bo.getGameId());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.ACTIVITY_GIFT_BAG.getCode());
            iGameUseRecordService.insertGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改礼包活动管理
     *
     * @param bo 礼包活动管理
     * @return 结果
     */
    @Override
    public R updateByBo(GiftActivityBo bo) {
        GiftActivity update = BeanUtil.toBean(bo, GiftActivity.class);
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 更新游戏关联信息数据
            GameUseRecordBo gameUseRecordBo = new GameUseRecordBo();
            gameUseRecordBo.setPartyAId(update.getId());
            gameUseRecordBo.setPartyBId(bo.getGameId());
            gameUseRecordBo.setType(GameUseRecordTypeEnum.ACTIVITY_GIFT_BAG.getCode());
            iGameUseRecordService.updateRGameUseRecord(gameUseRecordBo);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(GiftActivity entity) {
        // 判断一下礼包类型是否正确
        Integer[] giftTypeList = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        Optional<Integer> giftTypeAny = Arrays.stream(giftTypeList).filter(a -> a.equals(entity.getGiftType())).findAny();
        if (!giftTypeAny.isPresent()) {
            return "礼包类型错误";
        }
        // 判断一下礼包类型是否正确
        Integer[] giftRewardTypeList = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        Optional<Integer> giftRewardTypeAny = Arrays.stream(giftRewardTypeList).filter(a -> a.equals(entity.getGiftRewardType())).findAny();
        if (!giftRewardTypeAny.isPresent()) {
            return "礼包奖励错误";
        }
        return "success";
    }

    /**
     * 批量删除礼包活动管理
     *
     * @param ids 需要删除的礼包活动管理主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 操作上下架
     *
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto idNameTypeDicDto, Long userId) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationPlatformAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationPlatformAny.isPresent()) {
            return R.fail("参数错误");
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(GiftActivity::getId, idNameTypeDicDto.getId())
            .eq(GiftActivity::getUserId, userId)
            .set(GiftActivity::getIsOnline, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

}
