package com.euler.sdk.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.sdk.api.domain.MemberBanVo;
import com.euler.sdk.api.domain.MyGameVo;
import com.euler.sdk.domain.bo.MemberBlacklistBo;
import com.euler.sdk.domain.dto.CommonIdPageDto;
import com.euler.sdk.domain.dto.MemberBlacklistDto;
import com.euler.sdk.domain.entity.MemberBlacklist;
import com.euler.sdk.domain.entity.MemberBlacklistRecord;
import com.euler.sdk.domain.entity.MyGame;
import com.euler.sdk.domain.vo.MemberBlacklistRecordVo;
import com.euler.sdk.domain.vo.MemberBlacklistVo;
import com.euler.sdk.mapper.MemberBlacklistMapper;
import com.euler.sdk.mapper.MemberBlacklistRecordMapper;
import com.euler.sdk.mapper.MyGameMapper;
import com.euler.sdk.service.IMemberBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户黑名单列Service业务层处理
 *
 * @author euler
 * @date 2022-06-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberBlacklistServiceImpl implements IMemberBlacklistService {

    private final MemberBlacklistMapper baseMapper;
    @Autowired
    private MyGameMapper myGameMapper;
    @Autowired
    private MemberBlacklistRecordMapper memberBlacklistRecordMapper;


    /**
     * 查询用户黑名单列列表
     *
     * @return 用户黑名单列
     */
    @Override
    public TableDataInfo<MemberBlacklistRecordVo> queryPageList(CommonIdPageDto<Long> dto) {
        LambdaQueryWrapper<MemberBlacklistRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberBlacklistRecord::getMemberId, dto.getId());
        lqw.orderByDesc(MemberBlacklistRecord::getId);
        Page<MemberBlacklistRecordVo> result = memberBlacklistRecordMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户黑名单列列表
     *
     * @return 用户黑名单列
     */
    @Override
    public List<MemberBlacklistVo> queryList(MemberBlacklistDto dto) {
        LambdaQueryWrapper<MemberBlacklist> lqw = Wrappers.lambdaQuery();
        List<Integer> platformParams = new ArrayList<>();
        platformParams.add(0);
        platformParams.add(dto.getPlatform() != null ? dto.getPlatform() : 1);
        lqw.in(MemberBlacklist::getPlatform, platformParams);
        lqw.eq(dto.getMemberId() != null, MemberBlacklist::getMemberId, dto.getMemberId());
        lqw.in((dto.getMemberIds() != null && dto.getMemberIds().size() > 0), MemberBlacklist::getMemberId, dto.getMemberIds());
        lqw.eq(dto.getGameId() != null, MemberBlacklist::getGameId, dto.getGameId());
        lqw.eq(dto.getIsPermanent() != null, MemberBlacklist::getIsPermanent, dto.getIsPermanent());
        lqw.orderByDesc(MemberBlacklist::getId).orderByDesc(MemberBlacklist::getIsPermanent);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增用户黑名单列
     *
     * @param bo 用户黑名单列
     * @return 结果
     */
    @Override
    public R insertByBo(MemberBlacklistBo bo) {
        // 基础判断一下
        if ((bo.getIsPermanent().equals(0) || bo.getIsPermanent() == null) && bo.getEndTime() == null) {
            return R.fail("封禁结束时间必须填写");
        }
        if (bo.getRecord() == null || bo.getRecord().equals("")) {
            return R.fail("封号原因必须填写");
        }
        // 如果针对游戏进行封禁可能存在多个游戏一块封
        int rows = 0;
        String description = "";
        if (bo.getGameId() != null && !bo.getGameId().equals("")) {
            String[] strArr = bo.getGameId().split(",");
            Integer[] ids = Convert.toIntArray(strArr);
            List<Integer> useGameIds = Arrays.asList(ids);
            // 针对游戏进行封禁 我们需要查询出游戏的游戏名
            if (useGameIds.size() > 0 && useGameIds != null) {
                // 查询游戏基础信息
                LambdaQueryWrapper<MyGame> in = Wrappers.<MyGame>lambdaQuery().eq(MyGame::getUserId, bo.getMemberId())
                    .in(MyGame::getGameId, useGameIds);
                List<MyGameVo> myGameVos = myGameMapper.selectVoList(in);
                // 游戏名获取完毕之后
                if (myGameVos.size() > 0) {
                    List<String> gameNameList = myGameVos.stream().map(MyGameVo::getGameName).distinct().collect(Collectors.toList());
                    description = StringUtils.join(gameNameList, " , ");
                }
            }
            List<MemberBlacklist> list = new ArrayList<MemberBlacklist>();
            for (Integer id : ids) {
                MemberBlacklist memberBlacklist = new MemberBlacklist();
                memberBlacklist.setPlatform(bo.getPlatform());
                memberBlacklist.setMemberId(bo.getMemberId());
                memberBlacklist.setGameId(id);
                memberBlacklist.setEndTime(bo.getEndTime());
                memberBlacklist.setIsPermanent(bo.getIsPermanent());
                memberBlacklist.setRecord(bo.getRecord());
                memberBlacklist.setType(bo.getType());
                list.add(memberBlacklist);
            }
            if (list.size() > 0) {
                rows = baseMapper.insertBatch(list) ? list.size() : 0;
            }
        } else {
            // 不对游戏进行添加
            MemberBlacklist memberBlacklist = new MemberBlacklist();
            memberBlacklist.setPlatform(bo.getPlatform());
            memberBlacklist.setMemberId(bo.getMemberId());
            memberBlacklist.setGameId(0);
            memberBlacklist.setEndTime(bo.getEndTime());
            memberBlacklist.setIsPermanent(bo.getIsPermanent());
            memberBlacklist.setRecord(bo.getRecord());
            memberBlacklist.setType(bo.getType());
            rows = baseMapper.insert(memberBlacklist);
            if (bo.getPlatform().equals(0)) {
                description = "全平台";
            } else if (bo.getPlatform().equals(1)) {
                description = "SDK";
            } else {
                description = "APP";
            }
        }
        if (rows > 0) {
            // 每次修改之后我们删除掉一些不是永久封禁的同事封禁时间到了的
            LambdaQueryWrapper<MemberBlacklist> gt = Wrappers.<MemberBlacklist>lambdaQuery().eq(MemberBlacklist::getIsPermanent, 0)
                .lt(MemberBlacklist::getEndTime, new Date());
            List<MemberBlacklistVo> memberBlacklistVos = baseMapper.selectVoList(gt);
            if (memberBlacklistVos.size() > 0) {
                List<Integer> collect = memberBlacklistVos.stream().map(MemberBlacklistVo::getId).collect(Collectors.toList());
                deleteWithValidByIds(collect, false);
            }
            // 删除缓存值
            String blocKey = Constants.MEMBER_BLACK_KEY + bo.getMemberId();
            RedisUtils.deleteObject(blocKey);
            // 平台封禁时候我们需要添加一下操作记录
            MemberBlacklistRecord memberBlacklistRecord = new MemberBlacklistRecord();
            memberBlacklistRecord.setMemberId(bo.getMemberId());
            memberBlacklistRecord.setDescription(description);
            if (bo.getIsPermanent().equals(1)) {
                memberBlacklistRecord.setEndTime("永久封号");
            } else {
                memberBlacklistRecord.setEndTime(DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", bo.getEndTime()));
            }
            memberBlacklistRecord.setRecord(bo.getRecord());
            memberBlacklistRecord.setType(1);
            memberBlacklistRecordMapper.insert(memberBlacklistRecord);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 修改用户黑名单列
     *
     * @param bo 用户黑名单列
     * @return 结果
     */
    @Override
    public R updateByBo(MemberBlacklistBo bo) {
        // 基础判断一下
        if ((bo.getIsPermanent().equals(0) || bo.getIsPermanent() == null) && bo.getEndTime() == null) {
            return R.fail("封禁结束时间必须填写");
        }
        if (bo.getRecord() == null || bo.getRecord().equals("")) {
            return R.fail("封号原因必须填写");
        }
        MemberBlacklist update = BeanUtil.toBean(bo, MemberBlacklist.class);
        // 设置gameId的类型
        update.setGameId(Convert.toInt(bo.getGameId()));
        int i = baseMapper.updateById(update);
        if (i > 0) {
            // 每次修改之后我们删除掉一些不是永久封禁的同事封禁时间到了的
            LambdaQueryWrapper<MemberBlacklist> gt = Wrappers.<MemberBlacklist>lambdaQuery().eq(MemberBlacklist::getIsPermanent, 0)
                .lt(MemberBlacklist::getEndTime, new Date());
            List<MemberBlacklistVo> memberBlacklistVos = baseMapper.selectVoList(gt);
            if (memberBlacklistVos.size() > 0) {
                List<Integer> collect = memberBlacklistVos.stream().map(MemberBlacklistVo::getId).collect(Collectors.toList());
                deleteWithValidByIds(collect, false);
            }
            // 删除缓存值
            String blocKey = Constants.MEMBER_BLACK_KEY + bo.getMemberId();
            RedisUtils.deleteObject(blocKey);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 批量删除用户黑名单列
     *
     * @param ids 需要删除的用户黑名单列主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 批量删除用户黑名单列
     *
     * @return 结果
     */
    @Override
    public Boolean remove(KeyValueDto<Long> dto) {
        String[] strArr = dto.getValue().split(",");
        Integer[] gameIds = Convert.toIntArray(strArr);
        List<Integer> useGameIds = Arrays.asList(gameIds);
        if (useGameIds.size() <= 0) {
            return false;
        }
        LambdaUpdateChainWrapper<MemberBlacklist> set = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(MemberBlacklist::getMemberId, dto.getKey())
            .in(MemberBlacklist::getGameId, useGameIds)
            .set(MemberBlacklist::getDelFlag, "2");
        set.update();
        // 删除缓存值
        String blocKey = Constants.MEMBER_BLACK_KEY + dto.getKey();
        RedisUtils.deleteObject(blocKey);
        // 添加删除封禁记录
        String description = "";
        // 针对游戏进行封禁 我们需要查询出游戏的游戏名
        if (useGameIds.size() > 0) {
            // 查询游戏基础信息
            LambdaQueryWrapper<MyGame> in = Wrappers.<MyGame>lambdaQuery().eq(MyGame::getUserId, dto.getKey())
                .in(MyGame::getGameId, useGameIds);
            List<MyGameVo> myGameVos = myGameMapper.selectVoList(in);
            // 游戏名获取完毕之后
            if (myGameVos.size() > 0) {
                List<String> gameNameList = myGameVos.stream().map(MyGameVo::getGameName).distinct().collect(Collectors.toList());
                description = StringUtils.join(gameNameList, " , ");
            }
        }
        // 平台封禁时候我们需要添加一下操作记录
        MemberBlacklistRecord memberBlacklistRecord = new MemberBlacklistRecord();
        memberBlacklistRecord.setMemberId(dto.getKey());
        memberBlacklistRecord.setDescription(description);
        memberBlacklistRecord.setEndTime("");
        memberBlacklistRecord.setRecord("");
        memberBlacklistRecord.setType(2);
        memberBlacklistRecordMapper.insert(memberBlacklistRecord);
        return true;
    }


    /**
     * 一键解封
     *
     * @return 结果
     */
    public R unseal(IdTypeDto<Long, Integer> dto) {
        LambdaUpdateChainWrapper<MemberBlacklist> set = new LambdaUpdateChainWrapper<>(baseMapper)
            .eq(MemberBlacklist::getMemberId, dto.getId())
            .eq((dto.getType() != null && !dto.getType().equals(0)), MemberBlacklist::getPlatform, dto.getType())
            .set(MemberBlacklist::getDelFlag, "2");
        set.update();
        // 删除缓存值
        String blocKey = Constants.MEMBER_BLACK_KEY + dto.getId();
        RedisUtils.deleteObject(blocKey);
        // 平台封禁时候我们需要添加一下操作记录
        String description = "";
        if ((dto.getType() == null || dto.getType().equals(0))) {
            description = "全平台";
        } else if (dto.getType().equals(1)) {
            description = "SDK";
        } else {
            description = "APP";
        }
        MemberBlacklistRecord memberBlacklistRecord = new MemberBlacklistRecord();
        memberBlacklistRecord.setMemberId(dto.getId());
        memberBlacklistRecord.setDescription(description);
        memberBlacklistRecord.setEndTime("");
        memberBlacklistRecord.setRecord("");
        memberBlacklistRecord.setType(2);
        memberBlacklistRecordMapper.insert(memberBlacklistRecord);
        return R.ok();
    }

    /**
     * 检测用户是否被封禁
     *
     * @return
     */
    @Override
    public MemberBanVo checkUserIsBlacklist(Long userId, String username, Integer platform, Integer gameId) {
        // 先从缓存中获取当前用户封禁的情况
        String blocKey = Constants.MEMBER_BLACK_KEY + userId;
        List<MemberBlacklistVo> cacheList = RedisUtils.getCacheObject(blocKey);
        if (cacheList == null || cacheList.size() <= 0) {
            // 不存在的时候我们从数据库中获取我们想要的数据
            MemberBlacklistDto memberBlacklistDto = new MemberBlacklistDto();
            memberBlacklistDto.setMemberId(userId);
            memberBlacklistDto.setPlatform(1);
            cacheList = queryList(memberBlacklistDto);
            //  不管我们查询出的数据是否为空 我们都直接强制添加一个空实体村放进去 用户下面的判断
            MemberBlacklistVo memberBlacklistVo = new MemberBlacklistVo();
            memberBlacklistDto.setMemberId(0L);
            memberBlacklistDto.setType(0);
            cacheList.add(memberBlacklistVo);
            // 数据存储到缓存中
            RedisUtils.setCacheObject(blocKey, cacheList, Duration.ofDays(7));
        }
        // 循环判断当前用户是否被封禁状态
        MemberBanVo memberBanVo = new MemberBanVo();
        if (cacheList.size() > 1) {
            // 首先过滤掉一些假数据
            List<MemberBlacklistVo> collect = cacheList.stream().filter(row -> row.getId() != null).collect(Collectors.toList());
            for (int i = 0; i < collect.size(); i++) {
                // 首先将一些没用的数据过滤掉
                if (collect.get(i).getId() != null) {
                    // 首先判断当前用户是否存在全平台封禁的
                    Optional<MemberBlacklistVo> first = collect.stream().filter(
                        b -> b.getPlatform().equals(0)
                            && ((b.getEndTime() != null && b.getEndTime().getTime() > new Date().getTime()) || b.getIsPermanent().equals(1))
                    ).findFirst();
                    if (first.isPresent()) {
                        memberBanVo.setMemberId(userId);
                        memberBanVo.setMemberName(username);
                        memberBanVo.setGameId(first.get().getGameId());
                        memberBanVo.setIsPermanent(first.get().getIsPermanent());
                        memberBanVo.setEndTime(first.get().getEndTime());
                        memberBanVo.setRecord(first.get().getRecord());
                        return memberBanVo;
                    }
                    // 查询是否封指定平台的全部
                    Optional<MemberBlacklistVo> second = collect.stream().filter(
                        c -> c.getPlatform().equals(platform)
                            && c.getGameId().equals(0) && ((c.getEndTime() != null && c.getEndTime().getTime() > new Date().getTime()) || c.getIsPermanent().equals(1))
                    ).findFirst();
                    if (second.isPresent()) {
                        memberBanVo.setMemberId(userId);
                        memberBanVo.setMemberName(username);
                        memberBanVo.setGameId(second.get().getGameId());
                        memberBanVo.setIsPermanent(second.get().getIsPermanent());
                        memberBanVo.setEndTime(second.get().getEndTime());
                        memberBanVo.setRecord(second.get().getRecord());
                        return memberBanVo;
                    }
                    // 判断是否封指定平台指定游戏
                    Optional<MemberBlacklistVo> third = collect.stream().filter(
                        d -> d.getPlatform().equals(platform)
                            && d.getGameId().equals(gameId) && ((d.getEndTime() != null && d.getEndTime().getTime() > new Date().getTime()) || d.getIsPermanent().equals(1))
                    ).findFirst();
                    if (third.isPresent()) {
                        memberBanVo.setMemberId(userId);
                        memberBanVo.setMemberName(username);
                        memberBanVo.setGameId(third.get().getGameId());
                        memberBanVo.setIsPermanent(third.get().getIsPermanent());
                        memberBanVo.setEndTime(third.get().getEndTime());
                        memberBanVo.setRecord(third.get().getRecord());
                        return memberBanVo;
                    }
                }
            }
        }
        return memberBanVo;
    }
}
