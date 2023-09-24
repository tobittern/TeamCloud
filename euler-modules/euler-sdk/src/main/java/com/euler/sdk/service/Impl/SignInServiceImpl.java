package com.euler.sdk.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.api.domain.ScoreSystemBo;
import com.euler.sdk.api.domain.SignInVo;
import com.euler.sdk.api.domain.dto.SignInDto;
import com.euler.sdk.domain.entity.SignConfig;
import com.euler.sdk.domain.entity.SignIn;
import com.euler.sdk.domain.vo.MemberProfileVo;
import com.euler.sdk.mapper.SignConfigMapper;
import com.euler.sdk.mapper.SignInMapper;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IScoreSystemService;
import com.euler.sdk.service.ISignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SignInServiceImpl implements ISignInService {
    @Autowired
    private SignInMapper baseMapper;
    @Autowired
    private SignConfigMapper signConfigMapper;
    @Autowired
    private LockTemplate lockTemplate;
    @Autowired
    private IScoreSystemService iScoreSystemService;
    @Autowired
    private IMemberProfileService iMemberProfileService;

    /**
     * 列表
     *
     * @return
     */
    @Override
    public TableDataInfo<SignInVo> queryPageList(SignInDto signInDto) {
        LambdaQueryWrapper<SignIn> lqw = buildQueryWrapper(signInDto);
        Page<SignInVo> result = baseMapper.selectVoPage(signInDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<SignIn> buildQueryWrapper(SignInDto dto) {
        LambdaQueryWrapper<SignIn> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, SignIn::getMemberId, dto.getMemberId());
        lqw.eq(dto.getActiveId() != null, SignIn::getActiveId, dto.getActiveId());
        lqw.eq(dto.getWeek() != null, SignIn::getWeek, dto.getWeek());
        lqw.orderByDesc(SignIn::getId);
        return lqw;
    }

    /**
     * 签到
     *
     * @return 结果
     */
    @Override
    @Transactional
    public R insertByBo(Long userId) {
        // 首先判断当前用户 当天是否已经签到过了
        Date startTime = DateUtil.beginOfDay(new Date());
        Date endTime = DateUtil.endOfDay(new Date());
        // 判断当天是否已经签到过了
        LambdaQueryWrapper<SignIn> eq = Wrappers.<SignIn>lambdaQuery()
            .eq(SignIn::getMemberId, userId)
            .between(SignIn::getCreateTime, startTime, endTime);
        // 查询数据是否存在
        Long count = baseMapper.selectCount(eq);
        log.info("用户：{} 查询出的签到数：{}", userId, count);
        if (count > 0) {
            return R.fail("您已经签到过了！");
        }
        // 没有签到的话 就签到
        String key = Constants.USER_SIGN_PREFIX_KEY + "LOCK:USER_" + userId;
        final LockInfo lockInfo = lockTemplate.lock(key, 30000L, 5000L, RedissonLockExecutor.class);
        // 获取锁成功，处理业务
        try {
            if (null == lockInfo) {
                log.info("用户签到 title:{}加锁失败", userId);
                throw new RuntimeException("业务处理中,请稍后再试");
            }
            // 获取当前星期几 从签到配置中获取签到的积分数
            //获取当前星期几
            Calendar calendar;
            calendar = Calendar.getInstance();
            String week;
            week = calendar.get(calendar.DAY_OF_WEEK) - 1 + "";
            if ("0".equals(week)) {
                week = "7";
            }
            // 首先获取当前签到的配置信息
            LambdaQueryWrapper<SignConfig> searchScoreConfig = Wrappers.<SignConfig>lambdaQuery()
                .eq(SignConfig::getWeek, week);
            SignConfig signConfig = signConfigMapper.selectOne(searchScoreConfig);
            if (signConfig == null) {
                return R.fail("签到失败");
            }
            // 首先进行数据库的累加
            SignIn signIn = new SignIn();
            signIn.setMemberId(userId);
            signIn.setWeek(Integer.valueOf(week));
            signIn.setScore(signConfig.getScore());
            signIn.setCreateTime(new Date());
            int insert = baseMapper.insert(signIn);
            // 判断是否添加成功
            if (insert > 0) {
                // 累计增加用户的积分数
                ScoreSystemBo scoreSystemBo = new ScoreSystemBo();
                scoreSystemBo.setUserId(userId);
                scoreSystemBo.setScore(Long.valueOf(signConfig.getScore()));
                iScoreSystemService.calculateScore(scoreSystemBo);
                // 将用户的签到时间更新一下
                MemberProfile memberProfile = new MemberProfile();
                memberProfile.setMemberId(userId);
                memberProfile.setSignDate(new Date());
                iMemberProfileService.updateMemberDetail(memberProfile);
                return R.ok("签到成功");
            }
        } catch (Exception e) {
            log.error("用户签到报错， 用户ID：{}", userId, e);
            // 错误抛出 为了能触发事务
            throw e;
        } finally {
            //释放锁
            lockTemplate.releaseLock(lockInfo);
        }
        return R.fail("签到失败");
    }


    @Override
    public R checkClick(Long userId) {
        MemberProfileVo memberProfileVo = iMemberProfileService.getVoByMemberId(userId);
        if (memberProfileVo == null) {
            return R.ok(true);
        } else {
            // 判断当天是否签到过
            if (memberProfileVo.getSignDate() != null) {
                long time = memberProfileVo.getSignDate().getTime();
                long startTime = DateUtil.beginOfDay(new Date()).getTime();
                long endTime = DateUtil.endOfDay(new Date()).getTime();
                if (time >= startTime && time <= endTime) {
                    return R.ok(false);
                }
            }
        }
        return R.ok(true);
    }
}
