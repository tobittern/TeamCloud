package com.euler.sdk.service.Impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.MemberConstants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.api.RemoteCommonService;
import com.euler.sdk.api.domain.LoginMemberVo;
import com.euler.sdk.api.domain.Member;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.config.Authentication;
import com.euler.sdk.config.WebConfig;
import com.euler.sdk.domain.vo.MemberVo;
import com.euler.sdk.mapper.MemberMapper;
import com.euler.sdk.service.ICancellationLogService;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户信息Service业务层处理
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    private final MemberMapper baseMapper;
    @Autowired
    private IMemberProfileService memberProfileService;

    @Autowired
    private Authentication authentication;
    @Autowired
    private WebConfig webConfig;

    @Autowired
    private ICancellationLogService cancellationLogService;
    @DubboReference
    private RemoteCommonService remoteCommonService;

    /**
     * 查询用户信息
     *
     * @param id 用户信息主键
     * @return 用户信息
     */
    @Override
    public MemberVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long register(Member member) {
        String createBy = StringUtils.isNotBlank(member.getAccount()) ? member.getAccount() : member.getMobile();
        member.setCreateBy(createBy);
        if (baseMapper.insert(member) > 0) {
            MemberProfile memberProfile = new MemberProfile();
            memberProfile.setMemberId(member.getId());
            memberProfile.setMobile(member.getMobile());
            memberProfile.setCreateBy(createBy);
            memberProfile.setNickName("n9p_" + createBy.substring(createBy.length() - 6));
            memberProfileService.save(memberProfile);

            return member.getId();
        }

        return 0L;
    }

    /**
     * 校验手机号是否唯一
     *
     * @param member 用户信息
     * @return
     */
    @Override
    public String checkUnique(Member member) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<Member>()
            .and(a -> a.eq(StringUtils.isNotBlank(member.getMobile()), Member::getMobile, member.getMobile()).or(StringUtils.isNotBlank(member.getAccount()), b -> b.eq(Member::getAccount, member.getAccount())))
            .ne(ObjectUtil.isNotNull(member.getId()), Member::getId, member.getId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 登录
     *
     * @param account
     * @return
     */
    @Override
    public LoginMemberVo login(String account, String password) {
        List<LoginMemberVo> list = getMutiMemberByMobile(account);

        if (list == null || list.isEmpty())
            return null;

        if (StringUtils.isNotBlank(password)) {
            for (var memberVo : list) {
                if (BCrypt.checkpw(password, memberVo.getPassword()))
                    return memberVo;
            }

        }
        return list.get(0);

    }

    @Override
    public LoginMemberVo getMemberById(Long memberId) {
        var wrapper = Wrappers.<Member>query().eq("m.id", memberId).last("limit 1");
        return baseMapper.login(wrapper);
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public LoginMemberVo getMemberByMobile(String mobile) {
        var wrapper = Wrappers.<Member>query().eq("m.mobile", mobile).last("limit 1");
        return baseMapper.login(wrapper);
    }

    @Override
    public List<LoginMemberVo> getMutiMemberByMobile(String account) {

        List<LoginMemberVo> member = null;
        if (PhoneUtil.isMobile(account)) {
            var wrapper = Wrappers.<Member>query().eq("m.mobile", account).orderByDesc("mp.login_date");
            member = baseMapper.loginList(wrapper);
        }

        if (member == null || member.isEmpty()) {
            var queryWrapper = Wrappers.<Member>query().eq("m.account", account).last("limit 1");
            member = baseMapper.loginList(queryWrapper);
        }

        return member;
    }


    /**
     * 检测用户是否可以玩
     *
     * @param idCardNo
     * @return
     */
    @Override
    public Boolean checkUserPlay(String idCardNo) {
        if (StringUtils.isEmpty(idCardNo)) {
            return Boolean.FALSE;
        }

        // 随机生成密钥
        byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);
        // 解密
        String checkIdCardNo = aes.decryptStr(idCardNo);
        //log.info("解密之后的身份证号:{}", checkIdCardNo);
        // 校验身份证号
        int age = IdcardUtil.getAgeByIdCard(checkIdCardNo);
        if (age < webConfig.getCanPlayAge()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 注销会员
     *
     * @return
     */
    @Transactional
    @Override
    public Boolean cannellation(Integer id, Long memberId) {
        log.info("会员注销：id:{},memberId:{}", id, memberId);

        String memberProfileStr = "";
        if (webConfig.getIsOpenCancellation()) {
            var member = this.getById(memberId);
            if (member == null) {
                log.error("执行注销用户：{}，id:{}，不存在或已删除", memberId, id);
                return false;
            }

            var memberlw = Wrappers.<Member>lambdaUpdate().eq(Member::getId, memberId)
                .set(Member::getAccount, null)
                .set(Member::getMobile, null)
                .set(Member::getDelFlag, MemberConstants.MEMBER_STATUS_DISABLE)
                .set(Member::getUpdateTime, new Date())
                .set(Member::getUpdateBy, memberId);
            this.update(memberlw);

            var memberProfile = memberProfileService.getByMemberId(memberId);
            if (memberProfile == null) {
                log.error("执行注销用户：{}，id:{}，用户详细信息不存在或已删除", memberId, id);
                return false;
            }
            memberProfileStr = JsonHelper.toJson(memberProfile);
            var memberProfilelw = Wrappers.<MemberProfile>lambdaUpdate().eq(MemberProfile::getId, memberProfile.getId())
                .set(MemberProfile::getMobile, null)
//                .set(MemberProfile::getIdCardNo, null)
//                .set(MemberProfile::getRealName, null)
//                .set(MemberProfile::getVerifyStatus, null)
//                .set(MemberProfile::getNickName, null)
//                .set(MemberProfile::getProvince, null)
//                .set(MemberProfile::getProvinceId, null)
//                .set(MemberProfile::getCity, null)
//                .set(MemberProfile::getCityId, null)
//                .set(MemberProfile::getSex, null)
//                .set(MemberProfile::getDescription, null)
//                .set(MemberProfile::getAvatar, null)
                .set(MemberProfile::getDelFlag, MemberConstants.MEMBER_STATUS_DISABLE)
                .set(MemberProfile::getUpdateTime, new Date())
                .set(MemberProfile::getUpdateBy, memberId);

            memberProfileService.update(memberProfilelw);
            //下架动态、评论,用户基础信息
            remoteCommonService.userCancellationClear(memberId);

        }
        //保存日志
        var cancellationLog = cancellationLogService.getById(id);
        if (cancellationLog != null) {
            cancellationLog.setStatus(1);
            cancellationLog.setUpdateTime(new Date());
            cancellationLog.setMemberInfo(JsonHelper.toJson(memberProfileStr));
            cancellationLog.setOpNums(Convert
                .toInt(cancellationLog.getOpNums(), 0) + 1);
            return cancellationLogService.updateById(cancellationLog);
        } else {
            log.error("执行注销用户：{}，id:{}，注销申请不存在或已删除", memberId, id);
            return false;
        }

    }

    @Override
    public Integer downLineUser(Long memberId, Integer gameId, Integer platform) {
        Integer num = 0;
        String userTokenKey = StringUtils.format("{}sdk_user:{}", CacheConstants.SESSION_KEY, memberId);
        SaSession saSession = RedisUtils.getCacheObject(userTokenKey);

        if (saSession != null) {
            var tokenList = saSession.getTokenSignList();

            if (tokenList != null && !tokenList.isEmpty()) {

                List<String> reTokenList = new ArrayList<>();
                for (var token : tokenList) {
                    String currentToken = token.getValue();
                    String tokenSession = StringUtils.format("{}{}", CacheConstants.TOKEN_SESSION_KEY, currentToken);
                    SaSession sec = RedisUtils.getCacheObject(tokenSession);
                    if (sec != null) {
                        LoginUser loginUser = sec.getModel("loginUser", LoginUser.class);

                        Integer currentGameId = loginUser.getSdkChannelPackage() != null ? loginUser.getSdkChannelPackage().getGameId() : 0;
                        if (loginUser != null) {

                            if (!loginUser.getUserType().equals(UserTypeEnum.SDK_USER.getUserType())) {
                                log.info("下线游戏：{}，用户：{}，当前角色游戏id：{}，用户类型：{}，不需要下线", gameId, memberId, currentGameId, loginUser.getUserType());
                                continue;
                            }

                            if (platform > 0 && !platform.equals(loginUser.getPlatform())) {
                                log.info("下线游戏：{}，用户：{}，当前角色游戏id：{}，用户登录平台：{}，不需要下线", gameId, memberId, currentGameId, loginUser.getUserType());
                                continue;
                            }

                            if (!currentGameId.equals(gameId) && gameId != -1) {
                                log.info("下线游戏：{}，用户：{}，当前角色游戏id：{}不需要下线", gameId, memberId, currentGameId);
                                continue;
                            }
                            //删除token-session
                            RedisUtils.deleteObject(tokenSession);

                            //删除token
                            String loginTokenKey = StringUtils.format("{}{}", CacheConstants.LOGIN_TOKEN_KEY, currentToken);
                            RedisUtils.deleteObject(loginTokenKey);

                            //删除last-activity
                            String activeTokenKey = StringUtils.format("{}{}", CacheConstants.LAST_ACTIVITY_KEY, currentToken);
                            RedisUtils.deleteObject(activeTokenKey);

                            //删除onlineToken
                            String onlineTokenKey = StringUtils.format("{}{}", CacheConstants.ONLINE_TOKEN_KEY, currentToken);
                            RedisUtils.deleteObject(onlineTokenKey);

                            reTokenList.add(currentToken);


                            log.info("下线游戏：{}，用户：{}下线成功", gameId, memberId);
                            ++num;
                        } else {
                            log.info("下线游戏：{}，用户：{}，sdk不在线", gameId, memberId);

                        }


                    } else {
                        reTokenList.add(currentToken);

                    }

                }

                if (!reTokenList.isEmpty()) {
                    for (String reToken : reTokenList) {
                        saSession.getTokenSignList().removeIf(a -> a.getValue().equals(reToken));
                    }
                    SaManager.getSaTokenDao().updateObject(userTokenKey, saSession);

                }


            }


        } else {
            log.info("下线用户：{}，用户：{}不在线", gameId, memberId);
        }
        return num;

    }


    @Override
    public String getAvatar(String sex, String avatar) {
        if (StringUtils.isNotBlank(avatar)) {
            if (avatar.startsWith(Constants.HTTP) || avatar.startsWith(Constants.HTTPS))
                return avatar;
            return webConfig.getYunDomain() + avatar;
        }
        if (UserConstants.SEX_MAN.equals(sex))
            return webConfig.getYunDomain() + "/image/boys.png";
        else if (UserConstants.SEX_WOMAN.equals(sex))
            return webConfig.getYunDomain() + "/image/girls.png";
        else
            return webConfig.getYunDomain() + "/image/avatar.png";

    }

}
