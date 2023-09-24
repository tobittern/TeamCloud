package com.euler.auth.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.lock.LockInfo;
import com.euler.auth.config.RegisterMqConfig;
import com.euler.auth.domain.LoginResultDto;
import com.euler.auth.domain.WxLoginUser;
import com.euler.auth.form.LoginBody;
import com.euler.auth.form.RegisterBody;
import com.euler.auth.form.UpdatePwdDto;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.domain.dto.SdkChannelPackageDto;
import com.euler.common.core.enums.LoginPlatformEnum;
import com.euler.common.core.enums.LoginTypeEnum;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.exception.user.UserException;
import com.euler.common.core.utils.*;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.redis.utils.LockHelper;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.RemoteCaptchaCodeService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.CaptchaCode;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.risk.api.RemoteBehaviorService;
import com.euler.risk.api.RemoteLoginConfigService;
import com.euler.risk.api.domain.BehaviorType;
import com.euler.risk.api.domain.TdDeviceInfoVo;
import com.euler.risk.api.enums.BehaviorTypeEnum;
import com.euler.sdk.api.RemoteChannelService;
import com.euler.sdk.api.RemoteGameConfigService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.RemoteMyGameService;
import com.euler.sdk.api.domain.*;
import com.euler.sdk.api.domain.dto.NewLoginResultDto;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.RemoteLogService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysDictData;
import com.euler.system.api.domain.SysLogininfor;
import com.euler.system.api.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 登录校验方法
 *
 * @author euler
 */
@Service
@Slf4j
public class SysLoginService {

    //region 初始化
    @DubboReference
    private RemoteLogService remoteLogService;
    @DubboReference
    private RemoteUserService remoteUserService;
    @DubboReference
    private RemoteCaptchaCodeService remoteCaptchaCodeService;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @DubboReference
    private RemoteChannelService remoteChannelService;
    @DubboReference
    private RemoteMyGameService remoteMyGameService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;
    @DubboReference
    private RemoteMyGameService myGameService;
    @DubboReference
    private RemoteBehaviorService remoteBehaviorService;
    @DubboReference
    private RemoteLoginConfigService remoteLoginConfigService;
    @DubboReference
    private RemoteGameConfigService remoteGameConfigService;
    @DubboReference
    private RemoteDictService remoteDictService;
    @Autowired
    private LockHelper lockHelper;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private RegisterMqConfig registerMqConfig;

    //endregion

    //region 统一登录
    public R loginFirst(LoginBody loginBody) {
        UserTypeEnum userTypeEnum = UserTypeEnum.find(loginBody.getUserType());
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.find(loginBody.getLoginType());

        String account = loginBody.getUserName();
        if (StringUtils.isEmpty(loginBody.getUserName()) && !LoginTypeEnum.MOBILESIGN.equals(loginTypeEnum)) {
            throw new UserException("user.username.not.blank");
        }

        switch (loginTypeEnum) {
            //1、校验验证码、密码等
            case CAPTCHA:
                captchaCheck(loginBody, userTypeEnum, loginTypeEnum);
                break;
            case PASSWORD:
                passwordLogin(loginBody, userTypeEnum, loginTypeEnum);
                break;
            case MOBILESIGN:
                loginBody.setUserName(loginBody.getId());
                account = getQuickLoginMobile(loginBody);
                loginBody.setUserName(account);
                break;
        }
        //2、校验成功，获取用户信息
        Integer gameId = 0;
        LoginPlatformEnum loginPlatformEnum = getCurrentLoginPlatform(loginBody.getPlatform());
        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();


        if (UserTypeEnum.SDK_USER.equals(userTypeEnum) && LoginPlatformEnum.SDK.equals(loginPlatformEnum)) {
            OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
            if (openGameDubboVo == null) {
                log.info("获取游戏信息失败--{}", JsonHelper.toJson(headerDto));
                throw new ServiceException("获取游戏信息失败");
            }
            gameId = openGameDubboVo.getId();
        }

        List<NewLoginResultDto> list = remoteMemberService.getMemberByAccount(account, gameId);
        if (list == null || list.isEmpty()) {
            if (PhoneUtil.isMobile(account) && UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
                mobileLogin(loginBody, userTypeEnum);
                list = remoteMemberService.getMemberByAccount(account, gameId);
            }
            if (list == null || list.isEmpty())
                throw new ServiceException("用户不存在");
        }
        String ip = ServletUtils.getClientIP();

        List<NewLoginResultDto> it = JsonHelper.copyList(list, NewLoginResultDto.class);
        for (var loginResult : it) {
            R r = null;
            try {
                r = remoteLoginConfigService.checkUserStatus(loginResult.getId(), "", loginResult.getMobile(), ip, headerDto);
                if (Integer.valueOf(4003).equals(r.getCode())) {
                    list.removeIf(a -> a.getId().equals(loginResult.getId()));
                    if (list.size() == 0) {
                        loginResult.setMsg(r.getMsg());
                        loginResult.setStatus(r.getCode());
                        list.add(loginResult);
                    }
                }
            } catch (Exception e) {
                log.error("校验用户登录--异常", e);
            }
        }

        List<Long> memberIds = list.stream().map(a -> a.getId()).collect(Collectors.toList());

        String code = IdUtil.nanoId();
        String codeKey = StringUtils.format("{}memberIdLogin:code:{}", Constants.BASE_KEY, code);
        //添加二次登录的code缓存
        RedisUtils.setCacheObject(codeKey, JsonHelper.toJson(memberIds), Duration.ofMinutes(10));

        Map<String, Object> map = new HashMap<>();
        map.put("userList", list);
        map.put("authCode", code);
        if (PhoneUtil.isMobile(account))
            map.put("mobile", account);

        return R.ok(map);

    }

    private LoginPlatformEnum getCurrentLoginPlatform(Integer platform) {
        Integer headerPlatform = Convert.toInt(ServletUtils.getHeader(ServletUtils.getRequest(), "platform"), 0);
        if (headerPlatform == null || headerPlatform == 0)
            headerPlatform = platform;
        return LoginPlatformEnum.find(headerPlatform);

    }

    public R login(LoginBody form) {

        if (StringUtils.isEmpty(form.getUserName())) {
            throw new UserException("user.username.not.blank");
        }

        UserTypeEnum userTypeEnum = UserTypeEnum.find(form.getUserType());
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.find(form.getLoginType());


        LoginPlatformEnum loginPlatformEnum = getCurrentLoginPlatform(form.getPlatform());
        LoginUser userInfo = null;
        switch (loginTypeEnum) {
            case CAPTCHA:
                captchaCheck(form, userTypeEnum, loginTypeEnum);
                userInfo = mobileLogin(form, userTypeEnum);
                break;
            case PASSWORD:
                userInfo = passwordLogin(form, userTypeEnum, loginTypeEnum);
                break;
            case MOBILESIGN:
                String mobile = getQuickLoginMobile(form);
                form.setUserName(mobile);
                userInfo = mobileLogin(form, UserTypeEnum.SDK_USER);
                break;

            case IDLOGIN:
                checkIdLoginCode(form);
                userInfo = getLoginUserByUserType(null, null, userTypeEnum, false, Convert.toLong(form.getUserName(), 0L));
                break;

        }

        //sdk用户实名认证过，不可玩则跳退出登录
        if (userTypeEnum.equals(UserTypeEnum.SDK_USER) && userInfo.getVerifyStatus().equals(Constants.COMMON_STATUS_YES) && !userInfo.getIsPlay().equals(Constants.COMMON_STATUS_YES)) {
            throw new ServiceException("根据法律规定，未成年人限制游戏登录");
        }
        //设置sdk登录信息
        userInfo = setSdkLoginUser(userInfo, userTypeEnum, loginPlatformEnum.getLoginPlatformNum());
        // 由于上面都没有从memberProfile表中获取数据 所以我们这边需要远程掉一下 获取到当前用户的session_num 就是会话次数
        if (UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
            // 只有SDK用户的时候才需要调用
            MemberProfile memberByUserId = remoteMemberService.getMemberByUserId(userInfo.getUserId());
            if (memberByUserId != null && (memberByUserId.getSessionNum() == null || memberByUserId.getSessionNum() < 1)) {
                userInfo.setIsReg(1);
            }
        }

        LoginHelper.loginByDevice(userInfo, loginPlatformEnum);

        LoginResultDto loginResultDto = new LoginResultDto(StpUtil.getTokenValue(), userInfo.getFillPassword(), userInfo.getVerifyStatus(), userInfo.getUserId(), 0, userInfo.getIsReg(), userInfo.getIsNewGame(), null);

        return R.ok(loginResultDto);
    }

    private LoginUser setSdkLoginUser(LoginUser userInfo, UserTypeEnum userTypeEnum, Integer platform) {
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        String ip = ServletUtils.getClientIP();
        R r = remoteLoginConfigService.checkUserStatus(userInfo.getUserId(), "", userInfo.getMobile(), ip, headerDto);

        if (Integer.valueOf(4003).equals(r.getCode())) {
            throw new ServiceException(r.getMsg(), r.getCode());
        }

        userInfo.setAppId(headerDto.getAppId());
        userInfo.setPlatform(platform);
        userInfo.setDevice(Convert.toInt(headerDto.getDevice(), 0));
        if (userTypeEnum.equals(UserTypeEnum.SDK_USER)) {
            SdkChannelPackageDto sdkChannelPackage = new SdkChannelPackageDto();

            if (LoginPlatformEnum.SDK.getLoginPlatformNum().equals(platform)) {

                if (StringUtils.isEmpty(headerDto.getPackageCode()))
                    throw new ServiceException("登录游戏必须提供分包号");

                userInfo.setPackageCode(headerDto.getPackageCode());
                var channelPackageVo = remoteChannelService.selectChannelPackageByCode(headerDto.getPackageCode(), headerDto.getAppId());
                if (channelPackageVo == null)
                    throw new ServiceException("登录游戏必须提供正确的分包号");

                sdkChannelPackage.setChannelId(channelPackageVo.getChannelId())
                    .setChannelName(channelPackageVo.getChannelName())
                    .setPackageCode(headerDto.getPackageCode())
                    .setGameId(channelPackageVo.getGameId())
                    .setGameName(channelPackageVo.getNewGameName())
                    .setVersionId(channelPackageVo.getVersionId())
                    .setVersion(channelPackageVo.getVersion());
                List<Long> memberIds = new ArrayList<>();
                memberIds.add(userInfo.getUserId());
                List<GameUserManagement> gameUserInfoList = myGameService.getGameUserInfoList(memberIds, channelPackageVo.getGameId());
                if (gameUserInfoList == null || gameUserInfoList.isEmpty())
                    userInfo.setIsNewGame(1);

                userInfo.setSdkChannelPackage(sdkChannelPackage);
            }

            MemberProfile memberProfile = new MemberProfile();
            memberProfile.setMemberId(userInfo.getUserId());
            memberProfile.setLoginDate(new Date());
            memberProfile.setLoginIp(ip);
            try {
                TdDeviceInfoVo tdDeviceInfoVo = remoteBehaviorService.getDeviceInfoByHeader(headerDto);
                if (tdDeviceInfoVo != null)
                    memberProfile.setLoginDeviceId(tdDeviceInfoVo.getId());
            } catch (Exception e) {

            }

            if (userInfo.getIsReg().equals(1)) {
                memberProfile.setRegisterIp(memberProfile.getLoginIp());
                memberProfile.setChannelId(sdkChannelPackage.getChannelId());
                memberProfile.setChannelName(sdkChannelPackage.getChannelName());
                memberProfile.setPackageCode(sdkChannelPackage.getPackageCode());
                memberProfile.setGameId(sdkChannelPackage.getGameId());
                memberProfile.setGameName(sdkChannelPackage.getGameName());
            }
            remoteMemberService.updateMemberDetail(memberProfile);
        }
        return userInfo;

    }

    //endregion 统一登录

    //region 注册


    public RegisterBody autoRegister() {
        RegisterBody registerBody = new RegisterBody();
        registerBody.setAccount(getAccount(0));
        registerBody.setPassword(IdUtil.nanoId(6));
        registerBody.setUserType(UserTypeEnum.SDK_USER.getUserTypeNum());
        registerBody.setPlatform(1);

        Long userId = registerByUserType(registerBody);
        if (userId <= 0L) {
            throw new UserException("user.register.error");
        }

        // 校验游戏配置里是否配置了事件广播
        if(checkIsOpenBroadcast()) {
            var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
            DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
            String msgId = "auto_register" + userId;
            Map<String, Object> map = new HashMap<>();
            map.put("member_id", userId);
            map.put("device_info", deviceInfo);
            map.put("status", "success");
            // 用户自动注册成功，在消息队列里发送一条消息
            rabbitMqHelper.sendObj(registerMqConfig.getRegisterExchange(), registerMqConfig.getRegisterRoutingKey(), msgId, map);
        }

        return registerBody;

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


    private String getAccount(int num) {
        num = num + 1;
        String account = IdUtil.nanoId(9);
        var member = new Member();
        member.setAccount(account);
        if (UserConstants.NOT_UNIQUE.equals(remoteMemberService.checkUnique(member)) && num < 5) {
            return getAccount(num);
        }
        return account;
    }


    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {

        // 获取缓存里注册请求的次数
        String registerTimesKey = Constants.REGISTER_REQUEST_TIME + registerBody.getUserName();

        Integer registerTime = RedisUtils.getCacheObject(registerTimesKey);

        // 五分钟只能连续3次请求
        if (ObjectUtil.isNotNull(registerTime) && registerTime > Constants.REGISTER_REQUEST_NUMBER) {
            // 用户注册次数过多，帐户锁定5分钟
            throw new UserException("user.register.limit.exceed", Constants.REGISTER_LIMIT_TIME);
        }

        // 是否是第一次
        registerTime = Convert.toInt(registerTime, 1) + 1;
        // 达到规定次数3次 则锁定5分钟, 不能再注册了
        if (registerTime > Constants.REGISTER_REQUEST_NUMBER) {
            RedisUtils.setCacheObject(registerTimesKey, registerTime, Duration.ofMinutes(Constants.REGISTER_LIMIT_TIME));
            throw new UserException("user.register.limit.exceed", Constants.REGISTER_LIMIT_TIME);
        } else {
            // 未达到规定次数
            RedisUtils.setCacheObject(registerTimesKey, registerTime, Duration.ofMinutes(Constants.REGISTER_LIMIT_TIME));
        }

        // 密码
        if (!StringUtils.equals(registerBody.getPassword(), registerBody.getConfirmPwd())) {
            throw new ServiceException("两次密码不一致");
        }

        if (StringUtils.isEmpty(registerBody.getCode()))
            throw new ServiceException("请输入正确的验证码");


        remoteCaptchaCodeService.checkCode(registerBody.getUserName(), registerBody.getCode());
        Long userId = registerByUserType(registerBody);
        if (userId <= 0L) {
            throw new UserException("user.register.error");
        }
        // 注册成功 清空请求次数
        RedisUtils.deleteObject(registerTimesKey);

        // 校验游戏配置里是否配置了事件广播
        if(checkIsOpenBroadcast()) {
            var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
            DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
            String msgId = "register" + userId;
            Map<String, Object> map = new HashMap<>();
            map.put("member_id", userId);
            map.put("device_info", deviceInfo);
            map.put("status", "success");
            // 用户注册成功，在消息队列里发送一条消息
            rabbitMqHelper.sendObj(registerMqConfig.getRegisterExchange(), registerMqConfig.getRegisterRoutingKey(), msgId, map);
        }

        //recordLogininfor(registerBody.getUserName(), Constants.REGISTER, MessageUtils.message("user.register.success"));

    }
    //endregion 注册

    //region 忘记密码

    /**
     * 忘记密码,通过手机号或邮箱找回密码
     *
     * @param body
     */
    public void forgetPassword(RegisterBody body) {

        if (!StringUtils.equals(body.getPassword(), body.getConfirmPwd())) {
            throw new ServiceException("两次输入密码不一致");
        }

        remoteCaptchaCodeService.checkCode(body.getUserName(), body.getCode());

        UserTypeEnum userTypeEnum = UserTypeEnum.find(body.getUserType());
        //获取用户，判断用户状态和是否存在
        if (UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
            var memberList = remoteMemberService.getMutiMemberByMobile(body.getUserName());

            if (memberList == null || memberList.isEmpty())
                throw new ServiceException("用户不存在或已删除");

            for (var member : memberList) {
                boolean flag = changePassword(member.getId(), body.getPassword(), userTypeEnum);
                if (!flag)
                    throw new ServiceException("密码设置失败");

            }


        } else {
            LoginUser loginUser = getLoginUserByUserType(body.getUserName(), null, userTypeEnum, true, null);
            if (!changePassword(loginUser.getUserId(), body.getPassword(), userTypeEnum)) {
                throw new ServiceException("密码设置失败");
            }

        }


    }
    //endregion

    //region 修改密码

    /**
     * 修改密码
     *
     * @param updateBody
     */
    public void updatePwd(UpdatePwdDto updateBody) {

        // 基础验证
        if (!updateBody.getNewPassword().equals(updateBody.getConfirmPassword())) {
            throw new ServiceException("新密码和确定密码不一致");
        }

        UserTypeEnum userTypeEnum = UserTypeEnum.find(updateBody.getUserType());

        if (!updateBody.getFillPassword()) {
            LoginUser userInfo = getLoginUserByUserType(LoginHelper.getUsername(), null, userTypeEnum, true, LoginHelper.getUserId());

            if (!BCrypt.checkpw(updateBody.getOldPassword(), userInfo.getPassword())) {
                throw new ServiceException("修改密码失败，旧密码错误");
            }
            if (BCrypt.checkpw(updateBody.getNewPassword(), userInfo.getPassword())) {
                throw new ServiceException("新密码不能与旧密码相同");
            }
        }
        if (!changePassword(LoginHelper.getUserId(), updateBody.getNewPassword(), userTypeEnum)) {
            throw new ServiceException("修改密码异常，请联系管理员");
        }

    }
    //endregion 修改密码


    //region 退出登录

    /**
     * 退出登录
     *
     * @param loginName
     */
    public void logout(String loginName) {
        StpUtil.logout();
        //recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
    }
    //endregion 退出登录

    //region 验证码登录

    /**
     * 验证码登录
     *
     * @param form
     * @param userTypeEnum
     * @param loginTypeEnum
     * @return
     */
    public void captchaCheck(LoginBody form, UserTypeEnum userTypeEnum, LoginTypeEnum loginTypeEnum) {
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)，key例：euler:cloud:sdk_user:password:langlang
        if (StringUtils.isEmpty(form.getCode())) {
            // 验证码已失效
            throw new UserException("user.jcaptcha.not.blank");
        }

        String loginErrorKey = StringUtils.format("{}login_error:{}:{}:{}", Constants.BASE_KEY, userTypeEnum.getUserType(), loginTypeEnum.getLoginType(), form.getUserName());
        Integer errorNumber = Convert.toInt(RedisUtils.getCacheObject(loginErrorKey), 0);
        // 锁定时间内登录 则踢出
        if (errorNumber >= CacheConstants.LOGIN_ERROR_NUMBER) {
            //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, StringUtils.format("验证码多次输入错误，请{}分钟后再登录", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
            throw new ServiceException(StringUtils.format("验证码多次输入错误，请{}分钟后再登录", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
        }
        String codeExpireTimeKey = Constants.CAPTCHA_CODE_EXPIRE_TIME + form.getUserName();

        String checkCode = RedisUtils.getCacheObject(codeExpireTimeKey);
        if (StringUtils.isEmpty(checkCode)) {
            // 验证码已失效
            throw new UserException("user.jcaptcha.expire");

        }
        if (!StringUtils.equals(form.getCode(), checkCode)) {
            errorNumber = errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber >= CacheConstants.LOGIN_ERROR_NUMBER) {
                RedisUtils.setCacheObject(loginErrorKey, errorNumber, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LIMIT_TIME));
                //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, StringUtils.format("验证码多次输入错误，请{}分钟后再登录", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
                throw new ServiceException(StringUtils.format("验证码多次输入错误，请{}分钟后再登录", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(loginErrorKey, errorNumber, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LIMIT_TIME));
                //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, StringUtils.format("验证码输入错误{}次", errorNumber));
                throw new ServiceException(StringUtils.format("验证码输入错误{}次", errorNumber));
            }
        }
        // 清空验证码的缓存
        RedisUtils.deleteObject(codeExpireTimeKey);
        // 清空缓存
        RedisUtils.deleteObject(loginErrorKey);


    }
    //endregion 验证码登录

    public void checkIdLoginCode(LoginBody loginBody) {
        String codeKey = StringUtils.format("{}memberIdLogin:code:{}", Constants.BASE_KEY, loginBody.getCode());

        List<Long> memberIds = JsonHelper.toList(RedisUtils.getCacheObject(codeKey), Long.class);

        if (memberIds == null || memberIds.isEmpty())
            throw new ServiceException("非法请求或已过期，请重新登录");

        if (!memberIds.contains(Convert.toLong(loginBody.getUserName(), 0L)))
            throw new ServiceException("用户有误或已过期，请重新登录");

        RedisUtils.deleteObject(codeKey);
    }

    //region 手机一键登录
    /**
     * 手机一键登录
     *
     * @return
     */
    private String getQuickLoginMobile(LoginBody form) {

        String token = form.getUserName();
        String substring = StringUtils.substring(token, 0, 32);
        String lockKey = StringUtils.format("{}lock:getphonecode:{}", Constants.BASE_KEY, substring);

        LockInfo lockInfo = lockHelper.lock(lockKey);
        try {
            if (null == lockInfo) {
                throw new ServiceException("业务处理中,请稍后再试");
            }
            // 调用api获取手机号码
            R phoneCode = remoteCaptchaCodeService.getPhoneCode(token);
            if (phoneCode.getCode() == 200) {
                return phoneCode.getMsg();
            }
            throw new ServiceException("一键登录失败");
        } finally {
            //释放锁
            lockHelper.unLock(lockInfo);
        }
    }


    private LoginUser mobileLogin(LoginBody form, UserTypeEnum userTypeEnum) {
        //获取用户信息
        LoginUser userInfo = getLoginUserByUserType(form.getUserName(), null, userTypeEnum, true, null);

        //用户信息不存在，注册
        if (userInfo == null) {
            RegisterBody registerBody = new RegisterBody();
            registerBody.setUserName(form.getUserName());
            registerBody.setUserType(form.getUserType());
            registerBody.setPlatform(form.getPlatform());
            Long userId = registerByUserType(registerBody);
            userInfo = new LoginUser();
            userInfo.setUserId(userId);
            userInfo.setUsername(form.getUserName());
            userInfo.setMobile(form.getUserName());
            userInfo.setUserType(UserTypeEnum.SDK_USER.getUserType());
            userInfo.setFillPassword(true);
            userInfo.setVerifyStatus(Constants.COMMON_STATUS_NO);
            userInfo.setIsReg(1);

            // 校验游戏配置里是否配置了事件广播
            if(checkIsOpenBroadcast()) {
                var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
                DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
                String msgId = "mobile_register" + userId;
                Map<String, Object> map = new HashMap<>();
                map.put("member_id", userId);
                map.put("device_info", deviceInfo);
                map.put("status", "success");
                // 用户注册成功，在消息队列里发送一条消息
                rabbitMqHelper.sendObj(registerMqConfig.getRegisterExchange(), registerMqConfig.getRegisterRoutingKey(), msgId, map);
            }
        } else {
            userInfo.setIsReg(form.getIsReg());

        }
        return userInfo;

    }
    //endregion 手机一键登录

    //region 密码登录
    /**
     * 密码登录
     *
     * @return 结果
     */
    private LoginUser passwordLogin(LoginBody form, UserTypeEnum userTypeEnum, LoginTypeEnum loginTypeEnum) {

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)，key例：euler:cloud:sdk_user:password:langlang

        String loginErrorKey = StringUtils.format("{}login_error:{}:{}:{}", Constants.BASE_KEY, userTypeEnum.getUserType(), loginTypeEnum.getLoginType(), form.getUserName());
        Integer errorNumber = RedisUtils.getCacheObject(loginErrorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(CacheConstants.LOGIN_ERROR_NUMBER)) {
            //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
            throw new UserException("user.password.retry.limit.exceed", CacheConstants.LOGIN_ERROR_LIMIT_TIME);
        }
        //获取用户信息
        LoginUser userInfo = getLoginUserByUserType(form.getUserName(), form.getPassword(), userTypeEnum, false, null);
        //校验密码
        if (!BCrypt.checkpw(form.getPassword(), userInfo.getPassword())) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(CacheConstants.LOGIN_ERROR_NUMBER)) {
                RedisUtils.setCacheObject(loginErrorKey, errorNumber, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LIMIT_TIME));
                //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", CacheConstants.LOGIN_ERROR_LIMIT_TIME));
                throw new UserException("user.password.retry.limit.exceed", CacheConstants.LOGIN_ERROR_LIMIT_TIME);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(loginErrorKey, errorNumber, Duration.ofMinutes(CacheConstants.LOGIN_ERROR_LIMIT_TIME));


                //recordLogininfor(form.getUserName(), Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", errorNumber));
                throw new UserException("user.password.retry.limit.count", errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(loginErrorKey);
        return userInfo;
    }
    //endregion 密码登录验证

    //region 微信一键登录
    /**
     * 微信一键登录
     */
    public R wxLogin(String mobile) {
        Map<String, Object> map = new HashMap<>();
        LoginUser userInfo = new LoginUser();
        WxLoginUser wxLoginUser = new WxLoginUser();
        // 验证是否是手机号
        if (!PhoneUtil.isMobile(mobile))
            return R.fail("手机格式不正确！");
        // 根据手机号获取用户信息
        LoginMemberVo member = remoteMemberService.getMemberByMobile(mobile);
        String userName = RandomUtil.randomString(8);
        // 手机号是否存在，不存在即注册，存在即登录
        if (member == null) {
            // 不存在即注册
            RegisterBody registerBody = new RegisterBody();
            // 微信登录，这里设置的是手机号
            registerBody.setUserName(mobile);
            registerBody.setUserType(UserTypeEnum.SDK_USER.getUserTypeNum());
            registerBody.setPlatform(4);
            Long userId = registerByUserType(registerBody);

            userInfo.setUserId(userId);
            userInfo.setToken(IdUtil.getSnowflake().nextIdStr());
            userInfo.setUsername(userName);
            userInfo.setMobile(mobile);
            userInfo.setUserType(UserTypeEnum.SDK_USER.getUserType());
            userInfo.setFillPassword(true);
            userInfo.setVerifyStatus(Constants.COMMON_STATUS_NO);
            userInfo.setIsReg(1);

            wxLoginUser.setUserId(userId);
            wxLoginUser.setNickName(userName);
            wxLoginUser.setAvatar(remoteMemberService.getAvatar("2", ""));
            wxLoginUser.setSex("2");

            // 校验游戏配置里是否配置了事件广播
            if(checkIsOpenBroadcast()) {
                var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
                DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
                String msgId = "wxLogin_register" + userId;
                Map<String, Object> retMap = new HashMap<>();
                retMap.put("member_id", userId);
                retMap.put("device_info", deviceInfo);
                retMap.put("status", "success");
                // 用户注册成功，在消息队列里发送一条消息
                rabbitMqHelper.sendObj(registerMqConfig.getRegisterExchange(), registerMqConfig.getRegisterRoutingKey(), msgId, retMap);
            }
        } else {
            // 判断该手机号是否被封禁
            MemberBanVo memberBanVo = remoteMemberService.checkUserBlacklist(member.getId(), member.getNickName(), 1, 0);
            if (memberBanVo.getMemberId() != null) {
                return R.fail("该账号已经被封禁！");
            }

            userInfo.setUserId(member.getId());
            userInfo.setToken(member.getUniqueId());
            userInfo.setUsername(StringUtils.isNotBlank(member.getAccount()) ? member.getAccount() : member.getMobile());
            userInfo.setMobile(member.getMobile());
            userInfo.setPassword(member.getPassword());
            userInfo.setUserType(UserTypeEnum.SDK_USER.getUserType());
            userInfo.setFillPassword(StringUtils.isEmpty(member.getPassword()));
            userInfo.setVerifyStatus(member.getVerifyStatus());
            Boolean aBoolean = remoteMemberService.checkUserPlay(member.getIdCardNo());
            userInfo.setIsPlay(aBoolean ? Constants.COMMON_STATUS_YES : Constants.COMMON_STATUS_NO);

            wxLoginUser.setUserId(member.getId());
            wxLoginUser.setNickName(member.getNickName());
            wxLoginUser.setAvatar(remoteMemberService.getAvatar(member.getSex(), member.getAvatar()));
            wxLoginUser.setSex(member.getSex());
        }

        userInfo = setSdkLoginUser(userInfo, UserTypeEnum.SDK_USER, 4);
        LoginHelper.loginByDevice(userInfo, LoginPlatformEnum.SDK);
        map.put("userInfo", wxLoginUser);
        String token = StpUtil.getTokenSession().set("loginUser", userInfo).getId().replace(CacheConstants.TOKEN_SESSION_KEY,"");
        map.put("accessToken", token);
        map.put("uid", wxLoginUser.getUserId());
        return R.ok(map);
    }

    //endregion 微信一键登录

    //region 私有公用方法

    /**
     * 注册用户，不同平台注册
     *
     * @param registerBody
     * @return
     */
    private Long registerByUserType(RegisterBody registerBody) {
        UserTypeEnum userTypeEnum = UserTypeEnum.find(registerBody.getUserType());
        Long userId = 0L;

        //sdk平台注册
        if (UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
            var member = new Member();
            member.setMobile(registerBody.getUserName());
            member.setAccount(registerBody.getAccount());
            member.setPlatform(registerBody.getPlatform());

            if (!PhoneUtil.isMobile(member.getMobile())) {
                if (UserConstants.NOT_UNIQUE.equals(remoteMemberService.checkUnique(member))) {
                    throw new ServiceException("此账号已被注册");
                }
            }

            //验证码登录时不存在账户时，不设置密码
            if (StringUtils.isNotEmpty(registerBody.getPassword())) {
                member.setPassword(BCrypt.hashpw(registerBody.getPassword()));
            }

            member.setUniqueId(IdUtil.getSnowflake().nextIdStr());


            userId = remoteMemberService.registerMember(member);
            // 用户注册完毕之后需要修改一下当前用户是从那个游戏过来的  设置sdk登录信息
            LoginUser userInfo = new LoginUser();
            userInfo.setUserId(userId);
            userInfo.setIsReg(1);

            LoginPlatformEnum loginPlatformEnum = getCurrentLoginPlatform(registerBody.getPlatform());


            try {
                //注册行为
                RequestHeaderDto requestHeaderDto = HttpRequestHeaderUtils.getFromHttpRequest();
                if (requestHeaderDto != null && StringUtils.isNotBlank(requestHeaderDto.getPlatform()) && StringUtils.isNotBlank(requestHeaderDto.getDevice())) {
                    BehaviorType behaviorType = getBehaviorTypeByCode(requestHeaderDto.getPlatform(), requestHeaderDto.getDevice(), BehaviorTypeEnum.register.getCode());
                    if (behaviorType != null) {
                        remoteBehaviorService.submitUserBehavior(behaviorType, requestHeaderDto, JsonHelper.toJson(registerBody), userId, ServletUtils.getClientIP());
                    }
                }
            } catch (Exception e) {
                log.error("游客注册上报行为--异常，userId：{}", userId, e);
            }


            setSdkLoginUser(userInfo, userTypeEnum, loginPlatformEnum.getLoginPlatformNum());
        } else {
            // 添加邮箱注册
            var sysUser = new SysUser();
            sysUser.setEmail(registerBody.getUserName());
            if (UserConstants.NOT_UNIQUE.equals(remoteUserService.checkEmailUnique(registerBody.getUserName()))) {
                throw new UserException("user.register.save.email.error");
            }
            sysUser.setPassword(BCrypt.hashpw(registerBody.getPassword()));

            sysUser.setUserName(registerBody.getUserName());
            sysUser.setNickName("");
            sysUser.setUserType(UserTypeEnum.OPEN_USER.getUserType());
            sysUser.setRoleIds(new Long[]{UserConstants.DEFAULT_FRONT_ROLE_ID});
            sysUser.setPlatform(registerBody.getPlatform());
            userId = remoteUserService.registerUserInfo(sysUser);


        }
        if (StringUtils.isNotEmpty(registerBody.getCode())) {
            CaptchaCode captchaCode = new CaptchaCode();
            captchaCode.setUserId(userId);
            captchaCode.setReceiver(registerBody.getUserName());
            captchaCode.setIsUse(UserConstants.ISUSE_1);
            remoteCaptchaCodeService.updateByReceiver(captchaCode);
        }

        return Convert.toLong(userId, 0L);
    }

    /**
     * 用户名和用户类型获取用户信息
     *
     * @param userName
     * @param userTypeEnum
     * @return
     */
    private LoginUser getLoginUserByUserType(String userName, String password, UserTypeEnum userTypeEnum, boolean isCheckMemberExists, Long userId) {
        LoginUser userInfo = null;
        //不通登录类型获取用户方式不一样
        if (UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
            LoginMemberVo member = null;
            //手机号登录，isCheckMemberExists为true，不存在用户时会抛出异常，账户名密码登录、修改密码操作，isCheckMemberExists为false，不存在用户时不会抛出异常

            if (userId != null && userId > 0L)
                member = remoteMemberService.getMemberById(userId);
            else
                member = remoteMemberService.loginByUserName(userName, password);

            if (!isCheckMemberExists && member == null) {
                throw new UserException("user.not.exists", userName);
            }

            if (member != null) {
                userInfo = new LoginUser();
                userInfo.setUserId(member.getId());
                userInfo.setUsername(StringUtils.isNotBlank(member.getAccount()) ? member.getAccount() : member.getMobile());
                userInfo.setMobile(member.getMobile());
                userInfo.setPassword(member.getPassword());
                userInfo.setUserType(UserTypeEnum.SDK_USER.getUserType());
                userInfo.setFillPassword(StringUtils.isEmpty(member.getPassword()));
                userInfo.setVerifyStatus(member.getVerifyStatus());
                Boolean aBoolean = remoteMemberService.checkUserPlay(member.getIdCardNo());
                userInfo.setIsPlay(aBoolean ? Constants.COMMON_STATUS_YES : Constants.COMMON_STATUS_NO);
            }

        } else {
            userInfo = remoteUserService.getUserInfo(userName);
        }
        return userInfo;
    }

    /**
     * 不同平台的修改密码
     *
     * @param userId
     * @param password
     * @param userTypeEnum
     * @return
     */
    private boolean changePassword(Long userId, String password, UserTypeEnum userTypeEnum) {
        if (UserTypeEnum.SDK_USER.equals(userTypeEnum)) {
            return remoteMemberService.resetUserPwd(userId, BCrypt.hashpw(password));
        } else {
            return remoteUserService.resetUserPwd(userId, BCrypt.hashpw(password));
        }
    }


    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private void recordLogininfor(String username, String status, String message) {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ServletUtils.getClientIP());
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus("0");
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus("1");
        }
        remoteLogService.saveLogininfor(logininfor);
    }
    //endregion

    /**
     * 校验游戏配置里是否配置了事件广播
     */
    private Boolean checkIsOpenBroadcast() {
        AtomicReference<Boolean> isOpenRegister = new AtomicReference<>(false);
        // 事件广播，默认false
        Boolean hasEventBroadcast = false;
        JSONObject jsonObject = new JSONObject();
        var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        // 通过appid获取游戏信息
        if (headerDto != null && headerDto.getAppId() != null) {
            // 通过appid查询游戏信息
            OpenGameDubboVo openGameDubboVo = remoteGameManagerService.selectOpenGameInfo(headerDto.getAppId());
            if (openGameDubboVo != null && openGameDubboVo.getId() > 0) {
                // 查询是否有单游戏配置了事件广播
                // 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6: 事件广播
                GameConfigVo vo = remoteGameConfigService.selectGameConfigByParam(openGameDubboVo.getId(), "6", Convert.toStr(openGameDubboVo.getOperationPlatform()));
                if (vo != null) {
                    hasEventBroadcast = true;
                    jsonObject = JSONUtil.parseObj(vo.getData());
                }
            }
        }

        // 如果有单个游戏配置，返回游戏配置信息
        if(hasEventBroadcast) {
            JSONObject finalJsonObject = jsonObject;
            isOpenRegister.set(Convert.toBool(finalJsonObject.get("注册")));
        } else {
            // 查询全局字典
            List<SysDictData> data = remoteDictService.selectDictDataByType("event_broadcast");
            if (data != null && !data.isEmpty()) {
                data.forEach(a -> {
                    // 判断开关是否开启
                    if (a.getDictLabel().equals("注册") && StringUtils.equals("0", a.getStatus())) {
                        isOpenRegister.set(true);
                    }
                });
            }
        }
        return isOpenRegister.get();
    }

}
