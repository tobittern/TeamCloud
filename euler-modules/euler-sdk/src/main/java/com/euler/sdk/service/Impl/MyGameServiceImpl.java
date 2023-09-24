package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.domain.R;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.MyGameBo;
import com.euler.sdk.domain.dto.MemberBlacklistDto;
import com.euler.sdk.domain.dto.MyGameDto;
import com.euler.sdk.domain.entity.MyGame;
import com.euler.sdk.domain.vo.MemberBlacklistVo;
import com.euler.sdk.mapper.MyGameMapper;
import com.euler.sdk.service.IMemberBlacklistService;
import com.euler.sdk.service.IMyGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 我的游戏Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MyGameServiceImpl implements IMyGameService {

    private final MyGameMapper baseMapper;
    @Autowired
    private IMemberBlacklistService iMemberBlacklistService;

    /**
     * 查询我的游戏
     *
     * @param id 我的游戏主键
     * @return 我的游戏
     */
    @Override
    public MyGameVo queryById(Integer id) {
        Long userId = LoginHelper.getUserId();
        QueryWrapper<MyGame> wrapper = Wrappers.query();
        wrapper.eq("mg.id", id).eq("mg.user_id", userId);
        MyGameVo myGameVo = baseMapper.getMyGameVo(wrapper);

        if (myGameVo==null)
            throw  new ServiceException("已下架或不存在");

        // 查询一下当前用户的封禁情况
        MemberBlacklistDto memberBlacklistDto = new MemberBlacklistDto();
        memberBlacklistDto.setMemberId(userId);
        memberBlacklistDto.setPlatform(1);
        List<MemberBlacklistVo> memberBlacklistVos = iMemberBlacklistService.queryList(memberBlacklistDto);
        // 先判断是否存在不针对与游戏的封禁
        Optional<MemberBlacklistVo> noGameId = memberBlacklistVos.stream().filter(
            b -> b.getGameId().equals(0)
                && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
            .findFirst();
        if (noGameId.isPresent()) {
            myGameVo.setMemberStatus(1);
            myGameVo.setIsPermanent(noGameId.get().getIsPermanent());
            myGameVo.setBlackTime(noGameId.get().getEndTime());
        } else {
            // 判断是否是指定游戏封禁了
            Optional<MemberBlacklistVo> havaGame = memberBlacklistVos.stream().filter(
                b -> b.getGameId().equals(myGameVo.getGameId())
                    && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
                .findFirst();
            if (havaGame.isPresent()) {
                // 存在 代表当前游戏进行了封禁
                myGameVo.setMemberStatus(1);
                myGameVo.setIsPermanent(havaGame.get().getIsPermanent());
                myGameVo.setBlackTime(havaGame.get().getEndTime());
            }
        }
        // 设置一下玩的时间
        Float gameDuration = myGameVo.getGameDuration();
        if (gameDuration != null) {
            float v = (gameDuration / 60);
            DecimalFormat df = new DecimalFormat("0.0");
            String format = df.format(v);
            myGameVo.setGameDuration(Convert.toFloat(format));
        }
        return myGameVo;
    }

    /**
     * 后台 - 查询用户游戏列表
     *
     * @param
     * @return 游戏列表
     */
    @Override
    public TableDataInfo<MyGameVo> backstagePageList(MyGameDto dto) {
        LambdaQueryWrapper<MyGame> lqw = buildQueryWrapper(dto);
        IPage<MyGameVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        // 查询一下当前用户的封禁情况
        MemberBlacklistDto memberBlacklistDto = new MemberBlacklistDto();
        memberBlacklistDto.setMemberId(dto.getUserId());
        memberBlacklistDto.setPlatform(1);
        List<MemberBlacklistVo> memberBlacklistVos = iMemberBlacklistService.queryList(memberBlacklistDto);
        // 数据查询出来之后我们需要计算一下当前距离开服时间还有多久
        result.getRecords().forEach(a -> {
            // 先判断是否存在不针对与游戏的封禁
            Optional<MemberBlacklistVo> noGameId = memberBlacklistVos.stream().filter(
                b -> b.getGameId().equals(0)
                    && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
                .findFirst();
            if (noGameId.isPresent()) {
                a.setMemberStatus(1);
                a.setIsPermanent(noGameId.get().getIsPermanent());
                a.setBlackTime(noGameId.get().getEndTime());
            } else {
                // 判断是否是指定游戏封禁了
                Optional<MemberBlacklistVo> havaGame = memberBlacklistVos.stream().filter(
                    b -> b.getGameId().equals(a.getGameId())
                        && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1)))
                    .findFirst();
                if (havaGame.isPresent()) {
                    // 存在 代表当前游戏进行了封禁
                    a.setMemberStatus(1);
                    a.setIsPermanent(havaGame.get().getIsPermanent());
                    a.setBlackTime(havaGame.get().getEndTime());
                }
            }
            // 设置一下玩的时间
            Float gameDuration = a.getGameDuration();
            if (gameDuration != null) {
                float v = (gameDuration / 60);
                DecimalFormat df = new DecimalFormat("0.0");
                String format = df.format(v);
                a.setGameDuration(Convert.toFloat(format));
            }
        });

        return TableDataInfo.build(result);
    }


    /**
     * 查询我的游戏列表
     *
     * @param
     * @return 我的游戏
     */
    @Override
    public TableDataInfo<MyGameVo> queryPageList(MyGameDto dto) {
        QueryWrapper<MyGame> wrapper = Wrappers.query();
        wrapper.eq(dto.getUserId() != null, "mg.user_id", dto.getUserId());
        wrapper.eq(dto.getGameId() != null, "mg.game_id", dto.getGameId());
        wrapper.eq(dto.getChannelId() != null, "mg.channel_id", dto.getChannelId());
        wrapper.likeRight(StringUtils.isNotBlank(dto.getGameName()), "mg.game_name", dto.getGameName());
        wrapper.eq(dto.getPlatformServices() != null, "mg.platform_services", dto.getPlatformServices());
        wrapper.eq(dto.getGameType() != null, "mg.game_type", dto.getGameType());
        wrapper.likeRight(StringUtils.isNotBlank(dto.getNickname()), "mg.nickname", dto.getNickname());
        wrapper.orderByDesc("mg.visit_time");
        Page<MyGameVo> result = baseMapper.getMyGameVoPageList(dto.build(), wrapper);
        // 数据查询出来之后我们需要计算一下当前距离开服时间还有多久
        result.getRecords().forEach(a -> {
            if (a.getOpeningServiceTime() != null) {
                long between = DateUtil.between(a.getOpeningServiceTime(), new Date(), DateUnit.DAY, true);
                int compare = DateUtil.compare(new Date(), a.getOpeningServiceTime());
                // -1 大于当前时间  1小于当前时间
                if (between == 0 && compare == -1) {
                    // 当天开服
                    int hours = a.getOpeningServiceTime().getHours();
                    a.setServiceTimeDay(hours + "点开服");
                } else if (between != 0 && compare == -1) {
                    a.setServiceTimeDay(between + "天后开服");
                } else {
                    a.setServiceTimeDay("已开服");
                }
            } else {
                a.setServiceTimeDay("已开服");
            }
            // 设置一下玩的时间
            Float gameDuration = a.getGameDuration();
            if (gameDuration != null) {
                float v = (gameDuration / 60);
                DecimalFormat df = new DecimalFormat("0.0");
                String format = df.format(v);
                a.setGameDuration(Convert.toFloat(format));
            }
            // 设置一下用户多少天前玩过
            if (a.getVisitTime() != null) {
                long between = DateUtil.between(a.getVisitTime(), new Date(), DateUnit.DAY);
                if (between == 0L) {
                    a.setHowDaysAgo("今日玩过");
                } else {
                    a.setHowDaysAgo(between + "天前玩过");
                }
            }
        });

        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<MyGame> buildQueryWrapper(MyGameDto dto) {
        LambdaQueryWrapper<MyGame> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getUserId() != null, MyGame::getUserId, dto.getUserId());
        lqw.eq(dto.getGameId() != null, MyGame::getGameId, dto.getGameId());
        lqw.eq(dto.getChannelId() != null, MyGame::getChannelId, dto.getChannelId());
        lqw.likeRight(StringUtils.isNotBlank(dto.getGameName()), MyGame::getGameName, dto.getGameName());
        lqw.eq(dto.getPlatformServices() != null, MyGame::getPlatformServices, dto.getPlatformServices());
        lqw.eq(dto.getGameType() != null, MyGame::getGameType, dto.getGameType());
        lqw.likeRight(StringUtils.isNotBlank(dto.getNickname()), MyGame::getNickname, dto.getNickname());
        lqw.orderByDesc(MyGame::getVisitTime);
        return lqw;
    }


    /**
     * 获取登录的游戏信息
     *
     * @param myGameDto
     * @return
     */
    public MyGameVo getCurrentGameInfo(MyGameDto myGameDto) {
        LambdaQueryWrapper<MyGame> lqw = buildQueryWrapper(myGameDto);
        lqw.last("limit 1");
        return baseMapper.selectVoOne(lqw);
    }


    /**
     * 新增我的游戏
     *
     * @return 结果
     */
    @Override
    public R insertByBo(MyGameBo bo) {
        MyGame add = BeanUtil.toBean(bo, MyGame.class);
        String s = validEntityBeforeSave(add);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改我的游戏
     *
     * @return 结果
     */
    @Override
    public R updateByBo(MyGameBo bo) {
        MyGame update = BeanUtil.toBean(bo, MyGame.class);
        String s = validEntityBeforeSave(update);
        if (!s.equals("success")) {
            return R.fail(s);
        }
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(MyGame entity) {
        //TODO 做一些数据校验,如唯一约束
        return "success";
    }

    /**
     * 批量删除我的游戏
     *
     * @param ids 需要删除的我的游戏 主键
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
     * 查询用户指定渠道的游戏ID
     *
     * @param userId
     * @param packageCode
     */
    @Override
    public MyGameVo selectUserGameByParams(Long userId, Integer gameId, String packageCode) {
        LambdaQueryWrapper<MyGame> eq = Wrappers.<MyGame>lambdaQuery().eq(MyGame::getUserId, userId)
            .eq(MyGame::getGameId, gameId)
            .eq(MyGame::getPackageCode, packageCode)
            .orderByDesc(MyGame::getId)
            .last("limit 1");
        MyGameVo myGameVo = baseMapper.selectVoOne(eq);
        if (myGameVo == null) {
            return new MyGameVo();
        } else {
            return myGameVo;
        }
    }

}
