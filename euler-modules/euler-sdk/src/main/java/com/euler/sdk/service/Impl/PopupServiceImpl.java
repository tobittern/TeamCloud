package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.RemoteOrderService;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.sdk.api.domain.*;
import com.euler.sdk.api.domain.dto.SdkPopupDto;
import com.euler.sdk.domain.bo.PopupBo;
import com.euler.sdk.domain.bo.PopupGameRelationBo;
import com.euler.sdk.domain.dto.PopupPageDto;
import com.euler.sdk.domain.entity.GiftActivity;
import com.euler.sdk.domain.entity.Popup;
import com.euler.sdk.domain.entity.PopupGameRelation;
import com.euler.sdk.domain.vo.GiftActivityMiddlerVo;
import com.euler.sdk.domain.vo.GiftActivityVo;
import com.euler.sdk.domain.vo.MemberProfileVo;
import com.euler.sdk.domain.vo.PopupMemberShowTimeVo;
import com.euler.sdk.mapper.GameUserManagementMapper;
import com.euler.sdk.mapper.GiftActivityMapper;
import com.euler.sdk.mapper.PopupMapper;
import com.euler.sdk.service.*;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 弹窗管理Service业务层处理
 *
 * @author euler
 * @date 2022-09-05
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PopupServiceImpl extends ServiceImpl<PopupMapper, Popup> implements IPopupService {

    @Autowired
    private PopupMapper baseMapper;
    @Autowired
    private GiftActivityMapper giftActivityMapper;
    @Autowired
    private IPopupGameRelationService iPopupGameRelationService;
    @Autowired
    private IPopupMemberShowTimeService iPopupMemberShowTimeService;
    @Autowired
    private IGiftActivityService iGiftActivityService;
    @Autowired
    private GameUserManagementMapper gameUserManagementMapper;
    @Autowired
    private IMemberProfileService iMemberProfileService;
    @Autowired
    private IMyGameService iMyGameService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @DubboReference
    private RemoteOrderService remoteOrderService;

    private String key = StringUtils.format("{}popup:code:{}", Constants.RISK_KEY, IdUtil.nanoId());

    /**
     * 查询弹窗管理
     *
     * @param id 弹窗管理主键
     * @return 弹窗管理
     */
    @Override
    public SdkPopupVo queryById(Integer id) {
        SdkPopupVo popupVo = baseMapper.selectVoById(id);
        // 查询对应的游戏
        PopupGameRelationBo popupGameRelationBo = new PopupGameRelationBo();
        popupGameRelationBo.setPopupId(popupVo.getId());
        List<PopupGameRelation> popupGameRelations = iPopupGameRelationService.selectInfoByParams(popupGameRelationBo);
        if (popupGameRelations.size() > 0) {
            List<Integer> gameIds = popupGameRelations.stream().map(PopupGameRelation::getGameId).collect(Collectors.toList());
            popupVo.setGameIds(gameIds);
        }
        // 设置展示时间
        String sTime = DateUtil.format(popupVo.getStartTime(), "yyyy/MM/dd");
        String eTime = DateUtil.format(popupVo.getEndTime(), "yyyy/MM/dd");
        if (eTime.equals("2194/03/05")) {
            popupVo.setShowTime("持续");
        } else {
            popupVo.setShowTime(sTime + "-" + eTime);
        }
        return popupVo;
    }

    /**
     * 查询弹窗管理列表
     *
     * @param pageDto 弹窗管理
     * @return 弹窗管理
     */
    @Override
    public TableDataInfo<SdkPopupVo> queryPageList(PopupPageDto pageDto) {
        LambdaQueryWrapper<Popup> lqw = buildQueryWrapper(pageDto);
        Page<SdkPopupVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        if (result.getRecords().size() > 0) {
            result.getRecords().forEach(a -> {
                String sTime = DateUtil.format(a.getStartTime(), "yyyy/MM/dd");
                String eTime = DateUtil.format(a.getEndTime(), "yyyy/MM/dd");
                if (eTime.equals("2194/03/05")) {
                    a.setShowTime("持续");
                } else {
                    a.setShowTime(sTime + "-" + eTime);
                }
            });
        }
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Popup> buildQueryWrapper(PopupPageDto bo) {
        LambdaQueryWrapper<Popup> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, Popup::getId, bo.getId());
        lqw.eq(bo.getMemberId() != null, Popup::getMemberId, bo.getMemberId());
        lqw.likeRight(StringUtils.isNotBlank(bo.getTitle()), Popup::getTitle, bo.getTitle());
        lqw.eq(bo.getType() != null, Popup::getType, bo.getType());
        lqw.eq(bo.getShowType() != null, Popup::getShowType, bo.getShowType());
        lqw.ge((bo.getStartTimes() != null && bo.getStartTimes() > 0), Popup::getTimes, bo.getStartTimes());
        lqw.le((bo.getEndTimes() != null && bo.getEndTimes() > 0), Popup::getTimes, bo.getEndTimes());
        lqw.le(StringUtils.isNotBlank(bo.getStartTime()), Popup::getStartTime, DateUtils.getBeginOfDay(bo.getStartTime()));
        lqw.ge(StringUtils.isNotBlank(bo.getEndTime()), Popup::getEndTime, DateUtils.getEndOfDay(bo.getEndTime()));
        lqw.eq(StringUtils.isNotBlank(bo.getJumpUrl()), Popup::getJumpUrl, bo.getJumpUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Popup::getStatus, bo.getStatus());
        lqw.eq(bo.getGiftBagId() != null, Popup::getGiftBagId, bo.getGiftBagId());
        lqw.orderByDesc(Popup::getId);
        return lqw;
    }

    /**
     * 查询出当前用户是否需要展示出指定弹框
     *
     * @return 弹窗管理
     */
    @Override
    public List<SdkPopupVo> queryList(SdkPopupDto dto) {
        List<SdkPopupVo> returnObject = new ArrayList<>();
        // 存储到Redis中的key
        String popupKey = Constants.RISK_KEY + "popup-show";
        List<SdkPopupVo> cacheObject = RedisUtils.getCacheList(popupKey);
        if (cacheObject == null || cacheObject.size() == 0) {
            List<SdkPopupVo> popupVos = new ArrayList<>();
            LambdaQueryWrapper<Popup> lqw = Wrappers.lambdaQuery();
            lqw.eq(Popup::getStatus, "1");
            lqw.lt(Popup::getStartTime, new Date());
            lqw.gt(Popup::getEndTime, new Date());
            lqw.orderByAsc(Popup::getType).orderByAsc(Popup::getLevel).orderByDesc(Popup::getId);
            popupVos = baseMapper.selectVoList(lqw);
            if (popupVos.size() <= 0) {
                // 数据存储到缓存中
                RedisUtils.setCacheList(popupKey, popupVos);
                return returnObject;
            }
            // 查询一下这个弹框对应的游戏集合
            List<Integer> popupIds = popupVos.stream().map(SdkPopupVo::getId).collect(Collectors.toList());
            PopupGameRelationBo popupGameRelationBo = new PopupGameRelationBo();
            popupGameRelationBo.setPopupIds(popupIds);
            List<PopupGameRelation> popupGameRelations = iPopupGameRelationService.selectInfoByParams(popupGameRelationBo);
            if (popupGameRelations.size() > 0) {
                popupVos.forEach(row -> {
                    List<Integer> gameIds = popupGameRelations.stream().filter(a -> a.getPopupId().equals(row.getId()))
                        .map(PopupGameRelation::getGameId).collect(Collectors.toList());
                    row.setGameIds(gameIds);
                });
            }
            // 数据存储到缓存中
            RedisUtils.setCacheList(popupKey, popupVos);
            cacheObject = popupVos;
        }

        // 数据获取完毕之后我们需要使用数据了 但是需要判断里面是否存在一个空对象
        if (cacheObject.size() == 1 && cacheObject.get(0).getId() == null) {
            return returnObject;
        }
        // 判断第一个弹框是否是强退类型 如果是强退类型的话我们不需要获取用户针对每个弹框的展示次数
        // 获取当前游戏是否存在强退类的弹框
        List<SdkPopupVo> popupForOut = cacheObject.stream().filter(a -> a.getType().equals(1)
            && ((a.getGameIds() == null || a.getGameIds().size() == 0) || a.getGameIds().contains(dto.getGameId()))).collect(Collectors.toList());
        if (popupForOut.size() > 0) {
            // 存在 哪怕存在多个我们获取的总是第一个
            returnObject.add(popupForOut.get(0));
            return returnObject;
        }

        SdkChannelPackageDto channelPackage = LoginHelper.getLoginUser().getSdkChannelPackage();
        LambdaQueryWrapper<GameUserManagement> eq = Wrappers.<GameUserManagement>lambdaQuery()
            .eq(GameUserManagement::getMemberId, dto.getMemberId())
            .eq(GameUserManagement::getGameId, dto.getGameId())
            .eq(channelPackage.getGameServerId() != null, GameUserManagement::getServerId, channelPackage.getGameServerId())
            .eq(channelPackage.getGameRoleId() != null, GameUserManagement::getRoleId, channelPackage.getGameRoleId())
            .orderByDesc(GameUserManagement::getUpdateTime);
        // 是否有角色上报记录
        List<GameUserManagementVo> gameUserManagementList = gameUserManagementMapper.selectVoList(eq);

        // 只有第一次请求的时候才会获取奖励弹框
        if (dto.getIsFirst().equals(1)) {
            // 过滤出奖励的弹框 条件就是必须包含游戏
            List<SdkPopupVo> popupForBonus = cacheObject.stream()
                .filter(a -> a.getType().equals(2) && a.getGameIds().size() > 0 && a.getGameIds().contains(dto.getGameId()))
                .collect(Collectors.toList());

            // 判断弹框是否满足条件
            popupForBonus = checkIsReachConditions(dto, popupForBonus, gameUserManagementList);
            if (popupForBonus != null && popupForBonus.size() > 0) {
                // 存在奖励弹框的时候我们需要判断用户能否满足该奖励弹框的条件
                // 获取当前弹框中指定的礼包ID
                List<Integer> getGiftBagIds = popupForBonus.stream().map(SdkPopupVo::getGiftBagId).distinct().collect(Collectors.toList());
                // 获取这些礼包ID的详细信息并判断用户是否领取过这些礼包
                List<GiftActivityVo> giftActivityVos = checkUserIsShowBonusPopup(dto, getGiftBagIds);

                // 将这里礼包中没有领取过的礼包我们需要存储到返回列表中
                popupForBonus.forEach(a -> {
                    Optional<GiftActivityVo> searchGiftInfo = giftActivityVos.stream().filter(aa -> aa.getId().equals(a.getGiftBagId()) && aa.getIsReceive().equals(1)).findFirst();
                    if (searchGiftInfo.isPresent()) {
                        returnObject.add(a);

                        // 获取活动礼包数据
                        GiftActivityVo giftActivityVo = iGiftActivityService.queryById(a.getGiftBagId());

                        // 启动时机：（1:首充 5:实名认证 6:绑定手机号)
                        if(StringUtils.equals(Constants.START_OCCASION_1, a.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_5, a.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_6, a.getStartOccasion())) {
                            // 记录一下用户针对弹框的展示次数
                            iPopupMemberShowTimeService.insertMemberBrowse(a.getId(), dto.getMemberId(), a.getStartOccasion());
                        } else if (StringUtils.equals(Constants.START_OCCASION_2, a.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_3, a.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_4, a.getStartOccasion())) {
                            iPopupMemberShowTimeService.insertMemberBrowse(a.getId(), dto.getMemberId(), a.getStartOccasion() + "," + a.getConditionValue());
                        } else {
                            // 单笔充值/新人礼包
                            iPopupMemberShowTimeService.insertMemberBrowse(a.getId(), dto.getMemberId(), a.getStartOccasion() + "," + giftActivityVo.getGiftType());
                        }
                    }
                });
            }
        }

        String roleTimeKey = StringUtils.format("{}popup:report:{}:{}:{}:{}", Constants.RISK_KEY, dto.getMemberId(), dto.getGameId(), channelPackage.getGameRoleId(), channelPackage.getGameServerId());
        // 获取运营弹窗数据
        List<SdkPopupVo> popupForActivity = cacheObject.stream().filter(a -> a.getType().equals(3)
            && ((a.getGameIds() == null || a.getGameIds().size() == 0) || a.getGameIds().contains(dto.getGameId()))).collect(Collectors.toList());

        // 判断弹框是否满足条件
        popupForActivity = checkIsReachConditions(dto, popupForActivity, gameUserManagementList);
        if(popupForActivity != null && popupForActivity.size() > 0) {
            popupForActivity.forEach(b -> {
                returnObject.add(b);

                // 只有启动时机：（0每次启动）
                if(StringUtils.equals(Constants.START_OCCASION_0, b.getStartOccasion())) {
                    // 记录一下用户针对弹框的展示次数
                    iPopupMemberShowTimeService.insertMemberBrowse(b.getId(), dto.getMemberId(), b.getStartOccasion() + "," + b.getEveryStartupType());
                // 启动时机：（1:首充 5:实名认证 6:绑定手机号)
                } else if (StringUtils.equals(Constants.START_OCCASION_1, b.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_5, b.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_6, b.getStartOccasion())) {
                    iPopupMemberShowTimeService.insertMemberBrowse(b.getId(), dto.getMemberId(), b.getStartOccasion());
                // 启动时机：（2:游戏到达等级 3:累计充值 4:累计在线时长(分))
                } else if (StringUtils.equals(Constants.START_OCCASION_2, b.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_3, b.getStartOccasion()) || StringUtils.equals(Constants.START_OCCASION_4, b.getStartOccasion())) {
                    iPopupMemberShowTimeService.insertMemberBrowse(b.getId(), dto.getMemberId(), b.getStartOccasion() + "," + b.getConditionValue());
                }
            });

            if (RedisUtils.hasKey(roleTimeKey)) {
                RedisUtils.deleteObject(roleTimeKey);
                log.info("-->>>>>>>>>弹窗弹了");
            }
        }

        return returnObject;
    }

    /**
     * 弹框是否满足条件校验
     *
     * @param dto
     * @param popupVoList
     * @param gameUserManagementList
     * @return
     */
    private List<SdkPopupVo> checkIsReachConditions(SdkPopupDto dto, List<SdkPopupVo> popupVoList, List<GameUserManagementVo> gameUserManagementList) {
        if(popupVoList != null && popupVoList.size() > 0) {
            // 获取用户信息
            MemberProfileVo memberProfileVo = iMemberProfileService.getVoByMemberId(dto.getMemberId());
            // 获取游戏基础信息
            MyGameVo myGameVo = iMyGameService.selectUserGameByParams(dto.getMemberId(), dto.getGameId(), dto.getPackageCode());
            for (int i= 0; i< popupVoList.size(); i++) {
                SdkPopupVo popupVo = popupVoList.get(i);
                // 默认不满足条件
                Boolean isReachConditions = false;
                GiftActivityVo giftActivityVo = new GiftActivityVo();
                // 奖励弹窗
                if (popupVo.getType() == Constants.POPUP_TYPE_2) {
                    // 获取活动礼包数据
                    giftActivityVo = iGiftActivityService.queryById(popupVo.getGiftBagId());
                }
                BigDecimal bigDecimal = Convert.toBigDecimal(popupVo.getConditionValue());
                // 获取订单的数据
                MemberOrderStatDto memberOrderStatInfo = remoteOrderService.getMemberOrderStatInfo(dto.getMemberId(), dto.getGameId(), bigDecimal);
                if (popupVo.getStartOccasion() != null) {
                    // 判断是否满足弹窗条件（0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
                    switch (popupVo.getStartOccasion()) {
                        case Constants.START_OCCASION_2:
                            log.info("-->>>>>>>>>我的游戏等级: {},-->>>>>>>>>弹窗条件：{}", myGameVo.getGameLevel(), popupVo.getConditionValue());
                            // 判断游戏等级是否达到
                            if (myGameVo != null && myGameVo.getGameLevel() != null && Convert.toInt(myGameVo.getGameLevel()) >= Convert.toInt(popupVo.getConditionValue())) {
                                // 校验弹窗的展示次数
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion() + "," + popupVo.getConditionValue());
                            }
                            break;
                        case Constants.START_OCCASION_1:
                        case Constants.START_OCCASION_3:
                            log.info("-->>>>>>>>>充值金额: {},-->>>>>>>>>弹窗条件：{}", memberOrderStatInfo.getRechargeAmount(), popupVo.getConditionValue());
                            if (StringUtils.equals(Constants.START_OCCASION_1, popupVo.getStartOccasion()) && memberOrderStatInfo != null && memberOrderStatInfo.getRechargeNum() > 0) {
                                // 首充礼包
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion());
                            } else if (StringUtils.equals(Constants.START_OCCASION_3, popupVo.getStartOccasion())
                                && memberOrderStatInfo != null
                                && (memberOrderStatInfo.getRechargeAmount().compareTo(Convert.toBigDecimal(popupVo.getConditionValue())) == 1
                                || memberOrderStatInfo.getRechargeAmount().compareTo(Convert.toBigDecimal(popupVo.getConditionValue())) == 0)) {
                                // 累计充值
                                // 校验弹窗的展示次数
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion() + "," + popupVo.getConditionValue());
                            }
                            break;
                        case Constants.START_OCCASION_4:
                            // 累计在线时长，游戏时长，单位以小时显示，精确到小数点后1位
                            log.info("-->>>>>>>>>累计在线时长: {}小时,-->>>>>>>>>弹窗条件：{}分钟", myGameVo.getGameDuration(), popupVo.getConditionValue());
                            if (myGameVo != null && myGameVo.getGameDuration() != null && Convert.toInt(myGameVo.getGameDuration()) * 60 >= Convert.toInt(popupVo.getConditionValue())) {
                                // 校验弹窗的展示次数
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion() + "," + popupVo.getConditionValue());
                            }
                            break;
                        case Constants.START_OCCASION_5:
                            // 判断用户是否实名认证过
                            if (memberProfileVo != null && StringUtils.equals(Constants.VERIFY_STATUS_1, memberProfileVo.getVerifyStatus())) {
                                // 校验弹窗的展示次数
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion());
                            }
                            break;
                        case Constants.START_OCCASION_6:
                            // 判断用户是否绑定手机号
                            if (memberProfileVo != null && StringUtils.isNotBlank(memberProfileVo.getMobile())) {
                                // 校验弹窗的展示次数
                                isReachConditions = checkPopupShowTime(true, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion());
                            }
                            break;
                        default:
                            // 默认是每次启动
                            // 判断是否是奖励弹窗，奖励弹窗（单笔充值/新人礼包）都默认是每次启动/进入游戏，这种也仅弹一次
                            if (popupVo.getType() == Constants.POPUP_TYPE_2) {
                                if (giftActivityVo != null && giftActivityVo.getGiftType() != null) {
                                    // 礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包 8绑定手机号
                                    if (giftActivityVo.getGiftType() == 2) {
                                        // 单笔充值
                                        log.info("-->>>>>>>>>单笔充值: {},-->>>>>>>>>弹窗条件：{}", memberOrderStatInfo.getRechargeAmount(), popupVo.getConditionValue());
                                        isReachConditions = memberOrderStatInfo.getCheckRechargeAmount();
                                    } else if (giftActivityVo.getGiftType() == 6) {
                                        // 新人礼包
                                        if (myGameVo.getId() != null) {
                                            isReachConditions = true;
                                        }
                                    }
                                    // 校验弹窗的展示次数
                                    isReachConditions = checkPopupShowTime(isReachConditions, popupVo.getId(), dto.getMemberId(), popupVo.getStartOccasion() + "," + giftActivityVo.getGiftType());
                                }
                            }

                            log.info("-->>>>>>>>>登录时间: {}", memberProfileVo.getLoginDate());
                            // 最后登录时间距当前时间 <= 1分钟，就代表该用户打开App
                            if (DateUtil.between(memberProfileVo.getLoginDate(), new Date(), DateUnit.MINUTE) <= 1) {
                                if (StringUtils.equals(Constants.EVERY_STARTUP_TYPE_1, popupVo.getEveryStartupType())) {
                                    // 判断是否有角色上报信息
                                    if (gameUserManagementList != null && gameUserManagementList.size() > 0) {
                                        log.info("-->>>>>>>>>最近一次的角色上报时间: {}", gameUserManagementList.get(0).getUpdateTime());
                                        // 判断打开app后，最近一次的角色上报时间，是否是在登录时间之后，是的话，就代表进入游戏了
                                        if (!gameUserManagementList.get(0).getUpdateTime().before(memberProfileVo.getLoginDate())) {
                                            log.info("-->>>>>>>>>进入游戏了");
                                            // 运营弹窗、 判断缓存里是否包含key
                                            if (popupVo.getType() == Constants.POPUP_TYPE_3 && checkRedisKey(dto.getMemberId(), dto.getGameId())) {
                                                // 运营弹窗（每次启动/进入游戏）关于时间限制的校验
                                                isReachConditions = checkPopupByLimit(dto, popupVo, gameUserManagementList);
                                            }
                                        }
                                    }
                                } else if (StringUtils.equals(Constants.EVERY_STARTUP_TYPE_0, popupVo.getEveryStartupType())) {
                                    log.info("-->>>>>>>>>是否是第一次请求:{}", dto.getIsFirst());
                                    if (dto.getIsFirst().equals(1)) {
                                        isReachConditions = true;
                                    }
                                }
                            }

                            // 运营弹窗
                            if (popupVo.getType() == Constants.POPUP_TYPE_3) {
                                // 每次启动（打开APP/进入游戏）时、弹窗展示次数校验
                                if (isReachConditions && !popupVo.getTimes().equals(999)) {
                                    // 获取当前用户弹框展示次数
                                    List<PopupMemberShowTimeVo> popupShowTimeList = iPopupMemberShowTimeService.getMemberPopupList(dto.getMemberId(), popupVo.getStartOccasion() + "," + popupVo.getEveryStartupType());

                                    // 只有当不为永久的时候才会判断用户具体展示了多少次 获取用户指定弹框的展示次数
                                    Optional<PopupMemberShowTimeVo> first = popupShowTimeList.stream().filter(bb -> bb.getPopupId().equals(popupVo.getId())).findFirst();
                                    if (first.isPresent() && first.get().getTime() >= popupVo.getTimes()) {
                                        // 只有存在并且小于展示次数的时候我们才需要将数据返回
                                        isReachConditions = false;
                                    }
                                }
                            }
                            break;
                    }
                }
                log.info("-->>>>>>>>>弹窗id:{}, 启动时机:{}, 条件校验结果：{}", popupVo.getId(), popupVo.getStartOccasion(), isReachConditions);
                // 把不满足条件的弹框过滤掉
                if (!isReachConditions) {
                    popupVoList.remove(popupVo);
                    i--;
                }
            }
        }

        return popupVoList;
    }

    /**
     * 判断缓存里是否包含key
     */
    private Boolean checkRedisKey(Long memberId, Integer gameId) {
        SdkChannelPackageDto channelPackage = LoginHelper.getLoginUser().getSdkChannelPackage();
        if (channelPackage != null && channelPackage.getGameServerId() != null) {
            String roleTimeKey = StringUtils.format("{}popup:report:{}:{}:{}:{}", Constants.RISK_KEY, memberId, gameId, channelPackage.getGameRoleId(), channelPackage.getGameServerId());
            log.info("-->>>>>>>>>缓存roleTimeKey: {}---{}", roleTimeKey, RedisUtils.getCacheObject(roleTimeKey));
            if (RedisUtils.hasKey(roleTimeKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验弹窗的展示次数
     * @return
     */
    private Boolean checkPopupShowTime(Boolean isReachConditions, Integer popupId, Long memberId, String startOccasion) {
        // 启动时机：不是每次启动（1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)的场合，仅能弹一次
        if (isReachConditions && iPopupMemberShowTimeService.getMemberPopupTimes(popupId, memberId, startOccasion) == 0) {
            return true;
        }
        log.info("-->>>>>>>>>startOccasion: {},-->>>>>>>>>弹窗展示过了", startOccasion);
        return false;
    }

    /**
     * 运营弹窗（每次启动/进入游戏）关于时间限制的校验
     * @return
     */
    private Boolean checkPopupByLimit(SdkPopupDto dto, SdkPopupVo popupVo, List<GameUserManagementVo> gameUserManagementList) {
        Boolean isCheckResult = false;
        long diff = 0;
        long roleRegistDiff = 0;
        // 弹窗限制时间，单位：秒
        AtomicInteger limitTime = new AtomicInteger(popupVo.getPopupTime() == null? 0 : popupVo.getPopupTime());
        // 角色创建天数限制，单位：天，默认-1，不限制
        AtomicInteger roleRegistLimit = new AtomicInteger(popupVo.getRoleRegistTime() == null? -1 : popupVo.getRoleRegistTime());
        log.info("-->>>>>>>>>弹框配置里：弹框限制时间:{}， 角色创建天数限制：{}", popupVo.getPopupTime(), popupVo.getRoleRegistTime());

        // 弹窗里未设置限制的话，就取字典里的全局设置
        // 查询字典获取运营弹窗弹出时间
        List<SysDictData> data = remoteDictService.selectDictDataByType("popup_time");
        if (data != null && !data.isEmpty()) {
            data.stream().forEach(t -> {
                if (limitTime.get() == 0 && t.getDictLabel().equals("popup")) {
                    limitTime.set(Convert.toInt(t.getDictValue()));
                }
                if (roleRegistLimit.get() == -1 && t.getDictLabel().equals("role_regist_time")) {
                    roleRegistLimit.set(Convert.toInt(t.getDictValue()));
                }
            });
        }

        SdkChannelPackageDto channelPackage = LoginHelper.getLoginUser().getSdkChannelPackage();
        if (channelPackage != null && channelPackage.getGameServerId() != null) {
            String roleTimeKey = StringUtils.format("{}popup:report:{}:{}:{}:{}", Constants.RISK_KEY, dto.getMemberId(), dto.getGameId(), channelPackage.getGameRoleId(), channelPackage.getGameServerId());
            Date roleTime = Convert.toDate(RedisUtils.getCacheObject(roleTimeKey));
            // 日期时间差: 秒
            if (roleTime != null) {
                diff = DateUtil.between(roleTime, new Date(), DateUnit.SECOND);
            }
            // 角色注册时间
            Date roleRegistTime = gameUserManagementList.get(0).getCreateTime();
            roleRegistDiff = DateUtil.between(roleRegistTime, new Date(), DateUnit.DAY);

            // 角色注册时间不大于字典里的限制时间，或者默认不限制的时候，运营弹窗才弹
            if (roleRegistLimit.get() == -1 || roleRegistDiff <= roleRegistLimit.get()) {
                // 判断最近一次角色上报时间和当前时间，是否大于设置时间
                if (diff >= limitTime.get()) {
                    log.info("-->>>>>>>>>最近一次角色上报时间，距当前时间: {}秒", diff);
                    isCheckResult = true;
                }
            }
        }
        return isCheckResult;
    }

    /**
     * 检测用户是否能领取当前礼包
     */
    private List<GiftActivityVo> checkUserIsShowBonusPopup(SdkPopupDto dto, List<Integer> giftBagIds) {
        // 数据查询之后 我们需要判断当前获取的活动礼包列表中那些用户是可以进行领取的 如果能领取打上标签
        LambdaQueryWrapper<GiftActivity> lqw = Wrappers.lambdaQuery();
        lqw.in(GiftActivity::getId, giftBagIds);
        lqw.eq(GiftActivity::getIsOnline, 1);
        lqw.orderByAsc(GiftActivity::getSort);
        List<GiftActivityVo> giftActivityVos = giftActivityMapper.selectVoList(lqw);
        // 获取当前礼包的一些相关判断数据
        GiftActivityMiddlerVo giftNeedData = iGiftActivityService.getGiftNeedData(dto.getMemberId(), dto.getGameId(), dto.getPackageCode());
        // 循环判断
        giftActivityVos.forEach(row -> {
            if (iGiftActivityService.checkIsReceive(row, giftNeedData.getMyGameVo(), dto.getGameId())
                && iGiftActivityService.checkIsAlreadyReceive(row.getId(), giftNeedData.getIds())) {
                row.setIsReceive(2);
            } else if (iGiftActivityService.checkIsReceive(row, giftNeedData.getMyGameVo(), dto.getGameId())
                && !iGiftActivityService.checkIsAlreadyReceive(row.getId(), giftNeedData.getIds())) {
                row.setIsReceive(1);
            }
        });
        return giftActivityVos;
    }

    /**
     * 新增弹窗管理
     *
     * @return 结果
     */
    @Override
    @Transactional
    public R insertByBo(PopupBo bo) {
        Popup add = BeanUtil.toBean(bo, Popup.class);
        add.setStartTime(DateUtil.parse(DateUtils.getBeginOfDay(bo.getStartTime())));
        add.setEndTime(DateUtil.parse(DateUtils.getEndOfDay(bo.getEndTime())));
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        if(setPopupEntity(add) == null) {
            return R.fail("活动礼包不存在");
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            // 判断是否是奖励类礼包
            if (bo.getGameIds() != null) {
                PopupGameRelationBo popupGameRelationBo = new PopupGameRelationBo();
                popupGameRelationBo.setPopupId(add.getId());
                popupGameRelationBo.setGameIds(bo.getGameIds());
                iPopupGameRelationService.insertRelation(popupGameRelationBo);
                deleteRedisKey();
            }
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改弹窗管理
     *
     * @return 结果
     */
    @Override
    public R updateByBo(PopupBo bo) {
        Popup update = BeanUtil.toBean(bo, Popup.class);
        update.setStartTime(DateUtil.parse(DateUtils.getBeginOfDay(bo.getStartTime())));
        update.setEndTime(DateUtil.parse(DateUtils.getEndOfDay(bo.getEndTime())));
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        if(setPopupEntity(update) == null) {
            return R.fail("活动礼包不存在");
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            if (bo.getGameIds() != null) {
                PopupGameRelationBo popupGameRelationBo = new PopupGameRelationBo();
                popupGameRelationBo.setPopupId(bo.getId());
                popupGameRelationBo.setGameIds(bo.getGameIds());
                iPopupGameRelationService.updateRelation(popupGameRelationBo);
            }
        }
        deleteRedisKey();
        return R.ok();
    }

    private Popup setPopupEntity(Popup entity){
        // 奖励弹窗
        if(entity.getType() == Constants.POPUP_TYPE_2){
            // 查询活动礼包，来获取游戏到达等级/累计充值/累计在线时长(分)的值
            GiftActivityVo giftActivityVo = iGiftActivityService.queryById(entity.getGiftBagId());
            if(giftActivityVo != null) {
                entity.setConditionValue(giftActivityVo.getRewardConditions().toString());
            } else {
                return null;
            }
        }
        // 格式化
        if (StringUtils.equals(Constants.START_OCCASION_3, entity.getStartOccasion())) {
            // 累计充值金额，保留2位小数
            entity.setConditionValue(String.format("%.2f", Convert.toDouble(entity.getConditionValue())));
        } else if (StringUtils.equals(Constants.START_OCCASION_2, entity.getStartOccasion())
            || StringUtils.equals(Constants.START_OCCASION_4, entity.getStartOccasion())) {
            entity.setConditionValue(Convert.toInt(entity.getConditionValue()).toString());
        }

        return entity;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     * @return 校验结果
     */
    private String validEntityBeforeSave(Popup entity) {
        // 启动时机的类型校验(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
        String[] typeList = new String[]{"0", "1", "2", "3", "4", "5", "6"};
        Optional<String> typeAny = Arrays.stream(typeList).filter(a -> a.equals(entity.getStartOccasion())).findAny();
        if (!typeAny.isPresent()) {
            return "启动时机的类型错误";
        }

        // 运营弹窗
        if(entity.getType() == Constants.POPUP_TYPE_3) {
            // 启动时机校验
            switch (entity.getStartOccasion()) {
                case Constants.START_OCCASION_2:
                case Constants.START_OCCASION_3:
                case Constants.START_OCCASION_4:
                    if (ObjectUtils.isEmpty(entity.getConditionValue())) {
                        return "选择游戏到达等级/累计充值/累计在线时长(分) 时，必须要设定对应的值";
                    }
                    if (StringUtils.equals(Constants.START_OCCASION_3, entity.getStartOccasion())) {
                        if (!Validator.isMoney(entity.getConditionValue())) {
                            return "累计充值设定的金额不正确";
                        }
                    } else {
                        if (!Validator.isNumber(entity.getConditionValue())) {
                            return "设定的游戏到达等级/累计在线时长(分)不是数值";
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return "success";
    }

    /**
     * 批量删除弹窗管理
     *
     * @param ids 需要删除的弹窗管理主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        int i = baseMapper.deleteBatchIds(ids);
        if (i > 0) {
            deleteRedisKey();
            return true;
        }
        return false;
    }

    /**
     * 操作状态
     *
     * @return 结果
     */
    @Override
    public R operation(IdNameTypeDicDto idNameTypeDicDto) {
        // 数据存在 我们开始进行数据更新
        Integer[] typeList = new Integer[]{1, 2};
        Optional<Integer> operationTypeAny = Arrays.stream(typeList).filter(a -> a.equals(idNameTypeDicDto.getType())).findAny();
        if (!operationTypeAny.isPresent()) {
            return R.fail("参数错误");
        }
        // 执行更新操作
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(Popup::getId, idNameTypeDicDto.getId())
            .set(Popup::getStatus, idNameTypeDicDto.getType());
        boolean update = updateChainWrapper.update();
        if (update) {
            deleteRedisKey();
            return R.ok("修改成功");
        }
        return R.fail("数据修改失败");
    }

    /**
     * 删除redis的key
     */
    private void deleteRedisKey() {
        String popupKey = Constants.RISK_KEY + "popup-show";
        RedisUtils.deleteObject(popupKey);
    }

}
