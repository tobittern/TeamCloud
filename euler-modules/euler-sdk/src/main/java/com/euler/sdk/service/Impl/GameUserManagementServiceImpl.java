package com.euler.sdk.service.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.LockHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.risk.api.RemoteBehaviorService;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.api.enums.BehaviorTypeEnum;
import com.euler.sdk.api.domain.GameUserManagement;
import com.euler.sdk.api.domain.GameUserManagementVo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.domain.bo.GameUserManagementBo;
import com.euler.sdk.domain.dto.GameUserAddDto;
import com.euler.sdk.domain.dto.GameUserManagementDto;
import com.euler.sdk.domain.entity.MyGame;
import com.euler.sdk.mapper.GameUserManagementMapper;
import com.euler.sdk.mapper.MyGameMapper;
import com.euler.sdk.service.IGameUserManagementService;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * 游戏用户管理Service业务层处理
 *
 * @author euler
 * @date 2022-04-02
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class GameUserManagementServiceImpl extends ServiceImpl<GameUserManagementMapper, GameUserManagement> implements IGameUserManagementService {

    private final GameUserManagementMapper baseMapper;
    private final MyGameMapper myGameMapper;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteBehaviorService remoteBehaviorService;
    @Autowired
    private LockHelper lockHelper;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberProfileService iMemberProfileService;

    /**
     * 查询游戏用户管理列表
     *
     * @param dto 游戏用户管理
     * @return 游戏用户管理
     */
    @Override
    public TableDataInfo<GameUserManagementVo> getGameUserDetailPageList(GameUserManagementDto dto) {
        QueryWrapper<GameUserManagementVo> lqw = buildQueryWrapper(dto);
        Page<GameUserManagementVo> result = baseMapper.getGameUserDetailPageList(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<GameUserManagementVo> buildQueryWrapper(GameUserManagementDto dto) {
        QueryWrapper<GameUserManagementVo> lqw = Wrappers.query();
        lqw.eq("g.del_flag", UserConstants.USER_NORMAL).eq("mp.del_flag", UserConstants.USER_NORMAL)
            .eq(dto.getMemberId() != null, "g.member_id", dto.getMemberId())
            .eq(StringUtils.isNotBlank(dto.getPackageCode()), "g.package_code", dto.getPackageCode())
            .likeRight(StringUtils.isNotBlank(dto.getRoleName()), "g.role_name", dto.getRoleName())
            .eq(StringUtils.isNotBlank(dto.getRoleId()), "g.role_id", dto.getRoleId())
            .eq(StringUtils.isNotBlank(dto.getMobile()), "mp.mobile", dto.getMobile())
            .eq((dto.getChannelId() != null && dto.getChannelId() != 0), "c.id", dto.getChannelId())
            .likeRight(StringUtils.isNotBlank(dto.getGameName()), "o.game_name", dto.getGameName())
            .eq(StringUtils.isNotBlank(dto.getVipLevel()), "g.vip_level", dto.getVipLevel())
            .le(StringUtils.isNotBlank(dto.getLastLoginTime()), "g.update_time", DateUtils.getEndOfDay(dto.getLastLoginTime()));

        return lqw;
    }

    /**
     * 新增游戏用户管理
     *
     * @param gu 游戏用户管理
     * @return 结果
     */
    @Override
    public R insertByBo(GameUserAddDto gu) {
        GameUserManagementBo bo = gu.getGameUserManagementBo();
        RequestHeaderDto headerDto = gu.getHeaderDto();
        GameUserManagement entity = JsonHelper.copyObj(bo, GameUserManagement.class);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(bo.getMemberId().toString());
        // 判断游戏用户是否存在
        GameUserManagement oldEntity = getGameUserInfo(entity);

        MemberProfile memberProfile = iMemberProfileService.getByMemberId(bo.getMemberId());

        // 是否是第一次角色上报
        boolean isFirst = false;
        boolean flag = false;
        if (oldEntity != null) {
            if(oldEntity.getUpdateTime().before(memberProfile.getLoginDate())) {
                isFirst = true;
            }
            entity.setId(oldEntity.getId());

//            if (!"default".equals(oldEntity.getPackageCode()))
//                entity.setPackageCode(null);
        } else {
            isFirst = true;
            //member_id+game_id+role_id+server_id
            entity.setUniqueId(getGameUserMsgKey(bo));
            entity.setUpdateTime(new Date());
            var member = memberService.getById(bo.getMemberId());
            if (member != null)
                entity.setRegisterTime(member.getCreateTime());
        }
        // 获取当前游戏的上次游玩时间 计算游戏时长
        if (oldEntity != null && oldEntity.getUpdateTime() != null && oldEntity.getGameDuration() != null) {
            if (!DateUtil.isSameDay(oldEntity.getUpdateTime(), new Date())) {
                entity.setGameDays(Convert.toInt(oldEntity.getGameDays(), 0) + 1);
            } else {
                long between = DateUtil.between(oldEntity.getUpdateTime(), new Date(), DateUnit.MINUTE);
                float v = oldEntity.getGameDuration() + Convert.toFloat(between);
                entity.setGameDuration(v);
            }
        } else {
            // 防止上次游玩时间没有记录之后的游戏时长都不会进行累加了
            entity.setGameDuration(Convert.toFloat(0));
            entity.setGameDays(1);
        }
        //log.info("游戏用户信息上报--保存到数据库数据：{}", JsonHelper.toJson(entity));
        if (oldEntity != null) {
            flag = baseMapper.updateGameUserDataById(entity);
            log.info("-->>>>>>>>>角色上报， 更新:{}", entity);
        } else {
            flag = baseMapper.insert(entity) > 0;
            log.info("-->>>>>>>>>角色上报， 新增:{}", entity);
        }

        if (flag) {
            String roleTimeKey = StringUtils.format("{}popup:report:{}:{}:{}:{}", Constants.RISK_KEY, entity.getMemberId(), entity.getGameId(), entity.getRoleId(), entity.getServerId());
            log.info("-->>>>>>>>>角色上报， 角色上报时间:{}--{}", entity.getUpdateTime(), isFirst);
            if(isFirst) {
                RedisUtils.setCacheObject(roleTimeKey, entity.getUpdateTime());
                log.info("-->>>>>>>>>roleTimeKey: {}---{}", roleTimeKey, RedisUtils.getCacheObject(roleTimeKey));
            }
            //创建角色上报数据
            if (oldEntity == null) {
                submitUserBehavior(headerDto, entity, gu.getIp());
            }
            // 游戏数据上报成功之后需要将这款游戏添加到我的游戏列表中 - 如果已经存在的讲不需要再次进行添加了
            String key = StringUtils.format("{}game_user_{}game_id_{}", Constants.BASE_KEY, bo.getMemberId(), bo.getGameId());
            var lock = lockHelper.lock(key);
            try {
                if (null != lock) {
                    LambdaQueryWrapper<MyGame> eq = Wrappers.<MyGame>lambdaQuery().eq(MyGame::getUserId, bo.getMemberId())
                        .eq(MyGame::getGameId, bo.getGameId())
                        .orderByDesc(MyGame::getId)
                        .last("limit 1");
                    MyGame myGame = myGameMapper.selectOne(eq);
                    OpenGameVo openGameVo = remoteGameManagerService.getGameInfo(new IdDto<Integer>(bo.getGameId()));
                    if (myGame == null) {
                        // 不存在我们添加
                        MyGame addMyGame = new MyGame();
                        addMyGame.setUserId(bo.getMemberId());
                        addMyGame.setGameId(bo.getGameId());
                        addMyGame.setChannelId(bo.getChannelId());
                        addMyGame.setChannelName(bo.getChannelName());
                        addMyGame.setPackageCode(bo.getPackageCode());
                        addMyGame.setRegionalService(bo.getServerName());
                        addMyGame.setGameLevel(bo.getRoleLevel());
                        addMyGame.setNickname(bo.getRoleName());
                        addMyGame.setCreateBy(bo.getMemberId().toString());
                        addMyGame.setCreateTime(new Date());
                        // addMyGame.setGameName(bo.getGameName());
                        addMyGame.setVisitTime(new Date());
                        if (openGameVo != null) {
                            addMyGame.setGameName(openGameVo.getGameName());
                            addMyGame.setGameIcon(openGameVo.getIconUrl());
                            addMyGame.setPlatformServices(openGameVo.getOperationPlatform());
                        }
                        int insertRes = myGameMapper.insert(addMyGame);

                        if (insertRes > 0) {

                        }

                    } else {
                        // 存在了我们看看当前上报的游戏等级高还是我们存储的高
                        MyGame updateMyGame = new MyGame();
                        updateMyGame.setId(myGame.getId());
                        updateMyGame.setGameLevel(bo.getRoleLevel());
                        updateMyGame.setNickname(bo.getRoleName());
                        updateMyGame.setRegionalService(bo.getServerName());
                        updateMyGame.setUpdateBy(bo.getMemberId().toString());
                        updateMyGame.setUpdateTime(new Date());
                        if (openGameVo != null) {
                            updateMyGame.setGameName(openGameVo.getGameName());
                            updateMyGame.setGameIcon(openGameVo.getIconUrl());
                            updateMyGame.setPlatformServices(openGameVo.getOperationPlatform());
                        }
                        // 获取当前游戏的上次游玩时间 计算游戏时长
                        if (myGame.getVisitTime() != null && myGame.getGameDuration() != null) {
                            // 同一天之内才会进行数据累加
                            if (DateUtil.isSameDay(myGame.getVisitTime(), new Date())) {
                                Date visitTime = myGame.getVisitTime();
                                long between = DateUtil.between(visitTime, new Date(), DateUnit.MINUTE);
                                float v = myGame.getGameDuration() + Convert.toFloat(between);
                                updateMyGame.setGameDuration(v);
                            }
                        } else {
                            // 防止上次游玩时间没有记录之后的游戏时长都不会进行累加了
                            updateMyGame.setGameDuration(Convert.toFloat(0));
                        }
                        // 时长累加完毕之后进行设置
                        updateMyGame.setVisitTime(new Date());
                        myGameMapper.updateById(updateMyGame);
                    }
                } else {
                    log.info("分布式锁获取失败 001, 游戏id:{},操作人id:{}", bo.getGameId(), bo.getMemberId());
                }
            } catch (Exception e) {
                log.info("分布式锁获取失败 002, 游戏id:{},操作人id:{}", bo.getGameId(), bo.getMemberId());
            } finally {
                lockHelper.unLock(lock);
            }

            return R.ok();
        }
        return R.fail("上传角色信息失败");
    }


    private void submitUserBehavior(RequestHeaderDto requestHeaderDto, GameUserManagement gameUserManagement, String ip) {
        //注册行为
        try {
            if (requestHeaderDto != null && StringUtils.isNotBlank(requestHeaderDto.getPlatform()) && StringUtils.isNotBlank(requestHeaderDto.getDevice())) {
                BehaviorType behaviorType = getBehaviorTypeByCode(requestHeaderDto.getPlatform(), requestHeaderDto.getDevice(), BehaviorTypeEnum.roleCreate.getCode());
                if (behaviorType != null) {
                    remoteBehaviorService.submitUserBehavior(behaviorType, requestHeaderDto, JsonHelper.toJson(gameUserManagement), gameUserManagement.getMemberId(), ip);
                }
            }
        } catch (Exception e) {
            log.error("角色创建上报行为--异常，userId：{}", gameUserManagement.getMemberId(), e);
        }

    }


    /**
     * 获取拦截行为数据
     *
     * @return
     */
    public BehaviorType getBehaviorTypeByCode(String platform, String device, String code) {
        String key = StringUtils.format("{}behaviorType:platform_{}:device_{}:code_{}", Constants.BASE_KEY, platform, device, code);
        String cacheJson = RedisUtils.getCacheObject(key);
        if (cacheJson == null) {
            BehaviorType behaviorType = remoteBehaviorService.getBehaviorByCode(platform, device, code);
            RedisUtils.setCacheObject(key, JsonHelper.toJson(behaviorType), Duration.ofHours(24));
            return behaviorType;
        } else {
            return JsonHelper.toObject(cacheJson, BehaviorType.class);
        }

    }


    @Override
    public GameUserManagement getGameUserInfo(GameUserManagement gameUser) {
        QueryWrapper<Object> lqw = Wrappers.query()
            .eq("member_id", gameUser.getMemberId())
            .eq("game_id", gameUser.getGameId())
            .eq("role_id", gameUser.getRoleId())
            .eq("server_id", gameUser.getServerId()).last("limit 1");
        return baseMapper.getGameUserInfo(lqw);
    }

    @Override
    public List<GameUserManagement> getGameUserInfoList(List<Long> memberIds, Integer gameId) {
        if (gameId == null || gameId == 0)
            return new ArrayList<>();
        LambdaQueryWrapper<GameUserManagement> lqw = Wrappers.lambdaQuery();
        lqw.eq(GameUserManagement::getGameId, gameId)
            .in(GameUserManagement::getMemberId, memberIds).orderByDesc(GameUserManagement::getUpdateTime);
        return this.list(lqw);
    }

    @Override
    public List<GameUserManagementVo> getAllGameService() {
        //首先获取所有的游戏
        List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(null);
        if (openGameDubboVos == null || openGameDubboVos.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, List<GameUserManagementVo>> keyMap = new HashMap<>();
        for (OpenGameDubboVo game : openGameDubboVos) {
            Integer gameId = game.getId();
            String gameName = game.getGameName();
            LambdaQueryWrapper<GameUserManagement> lqw = Wrappers.lambdaQuery();
            lqw.eq(true, GameUserManagement::getGameId, gameId);
            List<GameUserManagementVo> gameUserManagementVos = baseMapper.selectVoList(lqw);
            //用来封装数据
            List<GameUserManagementVo> gameServiceList = new ArrayList<>();
            for (GameUserManagementVo v : gameUserManagementVos) {
                String key = gameId + "," + v.getServerId();
                if (keyMap.get(key) != null) {
                    keyMap.get(key).add(v);
                } else {
                    gameServiceList.add(v);
                    keyMap.put(key, gameServiceList);
                }
            }
        }

        //对分组的数据进行处理
        List<GameUserManagementVo> rList = new ArrayList<>();
        for (Map.Entry<String, List<GameUserManagementVo>> entry : keyMap.entrySet()) {
            String key = entry.getKey();
            Integer gameId = Integer.parseInt(key.split(",")[0]);
            String serviceId = key.split(",")[1];
            List<GameUserManagementVo> list = entry.getValue();
            GameUserManagementVo g = new GameUserManagementVo();
            g.setGameId(gameId);
            g.setGameName(list.get(0).getGameName());
            g.setServerId(serviceId);
            g.setServerName(list.get(0).getServerName());
            rList.add(g);
        }
        return rList;
    }

    public String getGameUserMsgKey(GameUserManagementBo bo) {
        StringBuilder sb = new StringBuilder();
        sb.append(bo.getMemberId()).append(bo.getGameId()).append(bo.getRoleId()).append(bo.getServerId());
        return SecureUtil.md5(sb.toString());
    }

    /**
     * 根据区服名称查询游戏区服列表
     *
     * @param idNameDto
     * @return
     */
    @Override
    public List<GameUserManagementVo> getServerListByName(IdNameDto<Long> idNameDto) {
        LambdaQueryWrapper<GameUserManagement> lqw = Wrappers.<GameUserManagement>lambdaQuery();
        lqw.select(GameUserManagement::getServerId, GameUserManagement::getServerName);
        lqw.eq(idNameDto.getId() != null, GameUserManagement::getGameId, idNameDto.getId());
        lqw.likeRight(StringUtils.isNotBlank(idNameDto.getName()), GameUserManagement::getServerName, idNameDto.getName());
        lqw.groupBy(GameUserManagement::getServerName);
        return baseMapper.selectVoList(lqw);
    }
}
