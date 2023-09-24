package com.euler.sdk.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.scheme.WxMaGenerateSchemeRequest;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.euler.common.core.constant.CacheConstants;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.*;
import com.euler.common.core.enums.LoginPlatformEnum;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.enums.BusinessStatus;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.log.enums.OperatorType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.api.RemoteUserExtendService;
import com.euler.community.api.domain.UserExtend;
import com.euler.sdk.api.domain.CancellationLog;
import com.euler.sdk.api.domain.MemberDetailVo;
import com.euler.sdk.api.domain.MemberProfile;
import com.euler.sdk.config.WebConfig;
import com.euler.sdk.domain.bo.UpdateRegionDto;
import com.euler.sdk.domain.dto.*;
import com.euler.sdk.domain.vo.AuthenticationVo;
import com.euler.sdk.domain.vo.MemberFrontDetailVo;
import com.euler.sdk.domain.vo.SensitiveDto;
import com.euler.sdk.helper.AuthenticationHelper;
import com.euler.sdk.service.ICancellationLogService;
import com.euler.sdk.service.IMemberProfileService;
import com.euler.sdk.service.IMemberService;
import com.euler.system.api.RemoteAuditKeywordService;
import com.euler.system.api.RemoteLogService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysOperLog;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * 用户信息Controller
 * 前端访问路由地址为:/sdk/member
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@Validated
@Api(value = "用户信息控制器", tags = {"用户信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController extends BaseController {

    private final IMemberProfileService memberProfileService;
    @DubboReference
    private RemoteLogService remoteLogService;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private AuthenticationHelper authenticationHelper;
    @DubboReference
    private RemoteUserService remoteUserService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private ICancellationLogService cancellationLogService;
    @DubboReference
    private RemoteUserExtendService remoteUserExtendService;
    @DubboReference
    private RemoteAuditKeywordService remoteAuditKeywordService;
    ;

    private Integer getChannelIdByUser() {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            return sysUser.getRegisterChannelId();
        }
        return 0;
    }


    //region 后端接口

    /**
     * 查询会员详细信息列表
     */
    @ApiOperation("查询会员详细信息列表")
    @SaCheckPermission("sdk:profile:list")
    @PostMapping("/list")
    public TableDataInfo<MemberDetailVo> list(@RequestBody MemberPageDto memberPageDto) {
        //待添加会员账户信息
        Integer channelIdByUser = getChannelIdByUser();
        memberPageDto.setChannelId(channelIdByUser);

        TableDataInfo<MemberDetailVo> memberDetailPageList = memberProfileService.getMemberDetailPageList(memberPageDto);
        memberDetailPageList.getRows().forEach(a -> {
            a.setIdCardNo(null);
        });
        return memberDetailPageList;
    }


    /**
     * 获取会员详细信息详细信息
     */
    @ApiOperation("获取会员详细信息详细信息")
    @SaCheckPermission("sdk:profile:query")
    @PostMapping("/getInfo")
    public R<MemberDetailVo> getInfo(@RequestBody IdDto<Long> idDto) {
        MemberDetailVo memberDetailVo = memberProfileService.getMemberDetailByMemberId(idDto.getId());
        if (memberDetailVo == null)
            return R.fail("用户信息不存在");
        memberDetailVo.setAvatar(memberService.getAvatar(memberDetailVo.getSex(), memberDetailVo.getAvatar()));


        if (memberDetailVo.getIdCardNo() != null && StringUtils.isNotEmpty(memberDetailVo.getIdCardNo())) {
            try {
                // 身份证解密
                byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
                AES aes = SecureUtil.aes(key);
                // 身份证解密
                String encrypt = aes.decryptStr(memberDetailVo.getIdCardNo());
                memberDetailVo.setIdCardNo(encrypt);
            } catch (Exception e) {
            }
        }
        return R.ok(memberDetailVo);
    }

    /**
     * 获取会员详细信息详细信息
     */
    @ApiOperation("获取会员详细信息详细信息")
    @PostMapping("/getUserInfo")
    public R<MemberFrontDetailVo> getInfo() {
        Long userId = LoginHelper.getUserId();
        MemberDetailVo memberDetailVo = memberProfileService.getMemberDetailByMemberId(userId);
        if (memberDetailVo == null)
            return R.fail("用户信息不存在");

        if (StringUtils.isNotEmpty(memberDetailVo.getIdCardNo())) {
            try {
                byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
                AES aes = SecureUtil.aes(key);
                // 身份证解密
                String encrypt = aes.decryptStr(memberDetailVo.getIdCardNo());
                memberDetailVo.setIdCardNo(encrypt);
            } catch (Exception e) {
            }
        }
        memberDetailVo.setAvatar(memberService.getAvatar(memberDetailVo.getSex(), memberDetailVo.getAvatar()));


        if (StringUtils.isNotBlank(memberDetailVo.getMemberRightsLevel())) {
            if (UserConstants.MEMBER_RIGHT_LEVEL_1.equals(memberDetailVo.getMemberRightsLevel()))
                memberDetailVo.setMemberRightsLevelIcon(webConfig.getYunDomain() + "/images/account-chuji.png");
            else if (UserConstants.MEMBER_RIGHT_LEVEL_2.equals(memberDetailVo.getMemberRightsLevel()))
                memberDetailVo.setMemberRightsLevelIcon(webConfig.getYunDomain() + "/images/account-zhongji.png");
            else if (UserConstants.MEMBER_RIGHT_LEVEL_3.equals(memberDetailVo.getMemberRightsLevel()))
                memberDetailVo.setMemberRightsLevelIcon(webConfig.getYunDomain() + "/images/account-gaoji.png");
        }


        MemberFrontDetailVo getUserInfo = BeanCopyUtils.copy(memberDetailVo, MemberFrontDetailVo.class);
        if (getUserInfo != null) {
            getUserInfo.setFillPassword(StringUtils.isEmpty(memberDetailVo.getPassword()));

        }
        return R.ok(getUserInfo);
    }


    //endregion 后端接口


    /**
     * 实名认证
     */
    @ApiOperation("实名认证")
    @PostMapping("/cert")
    public R cert(@Validated @RequestBody CertDto certDto) throws Exception {

        if (!IdcardUtil.isValidCard(certDto.getIdCardNo())) {
            return R.fail("您输入的身份证号格式不正确");
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        Long userId = loginUser.getUserId();
        certDto.setMemberId(userId);
        // 随机生成密钥
        byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);
        // 加密
        String encrypt = aes.encryptBase64(certDto.getIdCardNo());
        // 调取二要素接口 判断当前用户是否满足要求
        certDto.setRealName(URLDecoder.decode(certDto.getRealName(), "UTF-8"));
        if (!webConfig.getIsDebug()) {
            AuthenticationVo check = authenticationHelper.check(certDto);
            if (!check.getErrcode().equals(0)) {
                return R.fail("认证失败！！");
            }
            if (check.getErrcode().equals(0) && !check.getData().getResult().getStatus().equals(0)) {
                return R.fail("您的认证信息不正确");
            }
        }
        // 获取用户性别
        int genderByIdCard = IdcardUtil.getGenderByIdCard(certDto.getIdCardNo());
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(userId);
        memberProfile.setIdCardNo(encrypt);
        memberProfile.setRealName(certDto.getRealName());
        memberProfile.setVerifyStatus(Constants.COMMON_STATUS_YES);
        memberProfile.setSex(String.valueOf(genderByIdCard));
        memberProfileService.updateMemberDetail(memberProfile);

        UserExtend entity = new UserExtend();
        entity.setMemberId(userId);
        entity.setSex(memberProfile.getSex());
        // 更新关联表里的性别
        remoteUserExtendService.updateUserDetail(entity);

        // 校验身份证号
        int age = IdcardUtil.getAgeByIdCard(certDto.getIdCardNo());
        if (age < webConfig.getCanPlayAge()) {
            // 调用退出 清空token
            memberService.downLineUser(userId, -1, LoginPlatformEnum.SYS.getLoginPlatformNum());
            StpUtil.logout();
            return R.fail("根据政策要求，您无法进行游戏");
        }
        //更新LoginUser数据

        loginUser.setVerifyStatus(Constants.COMMON_STATUS_YES);
        LoginHelper.setLoginUser(loginUser);


        return R.ok();
    }


    @ApiOperation("修改昵称")
    @PostMapping("/updateNickName")
    public R updateNickName(@RequestBody IdNameDto<Long> idNameDto) {
        if (StringUtils.isBlank(idNameDto.getName())) {
            return R.fail("昵称不能为空");
        }
        // 判断昵称中是否存在敏感词
        R checkKeyword = remoteAuditKeywordService.systemCheck(idNameDto.getName(), 2);
        if (checkKeyword.getCode() == R.FAIL) {
            return R.fail("昵称中不能存在敏感词");
        }
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(LoginHelper.getUserId());
        memberProfile.setNickName(idNameDto.getName());
        if (StringUtils.length(idNameDto.getName()) > 30) {
            return R.fail("昵称不能超过30位");
        }

        // 更新关联表里的昵称
        UserExtend entity = new UserExtend();
        entity.setMemberId(LoginHelper.getUserId());
        entity.setNickName(idNameDto.getName());
        // 更新关联表里的昵称
        remoteUserExtendService.updateUserDetail(entity);

        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }


    @ApiOperation("修改头像")
    @PostMapping("/avatar")
    public R avatar(@RequestBody IdNameDto<Long> idNameDto) {
        if (StringUtils.isBlank(idNameDto.getName()))
            return R.fail("头像不允许为空");
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(LoginHelper.getUserId());
        String avatar = memberService.getAvatar(UserConstants.SEX_UNKNOWN, idNameDto.getName());
        memberProfile.setAvatar(avatar);

        // 更新关联表里的头像
        UserExtend entity = new UserExtend();
        entity.setMemberId(memberProfile.getMemberId());
        entity.setAvatar(avatar);
        // 更新关联表里的头像
        remoteUserExtendService.updateUserDetail(entity);

        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }

    @ApiOperation("修改性别")
    @PostMapping("/sex")
    public R sex(@RequestBody IdDto<Integer> idDto) {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(LoginHelper.getUserId());
        memberProfile.setSex(idDto.getId().toString());

        UserExtend entity = new UserExtend();
        entity.setMemberId(memberProfile.getMemberId());
        entity.setSex(memberProfile.getSex());
        // 更新关联表里的性别
        remoteUserExtendService.updateUserDetail(entity);


        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }

    @ApiOperation("修改个人签名")
    @PostMapping("/description")
    public R description(@RequestBody IdNameDto<Integer> dto) {
        // 判断个人签名中是否存在敏感词
        R checkKeyword = remoteAuditKeywordService.systemCheck(dto.getName(), 1);
        if (checkKeyword.getCode() == R.FAIL) {
            return R.fail("个人签名中不能存在敏感词");
        }
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(LoginHelper.getUserId());
        memberProfile.setDescription(dto.getName());
        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }


    @ApiOperation("修改省市区")
    @PostMapping("/updateRegion")
    public R updateRegion(@RequestBody UpdateRegionDto dto) {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(LoginHelper.getUserId());
        memberProfile.setProvince(dto.getProvince());
        memberProfile.setProvinceId(dto.getProvinceId());
        memberProfile.setCity(dto.getCity());
        memberProfile.setCityId(dto.getCityId());
        memberProfile.setArea(dto.getArea());
        memberProfile.setAreaId(dto.getAreaId());


        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }

    @ApiOperation("校验登录")
    @PostMapping("/checkLogin")
    public R<Boolean> checkLogin(@Validated @RequestBody CheckLoginDto checkLoginDto) {
        String userTokenKey = StringUtils.format("{}{}", CacheConstants.LOGIN_TOKEN_KEY, checkLoginDto.getToken());
        String res = RedisUtils.getCacheObject(userTokenKey);
        if (StringUtils.isEmpty(res))
            return R.ok(false);
        String userId = res.substring(res.indexOf(":") + 1);
        if (checkLoginDto.getUid().equals(userId))
            return R.ok(true);

        return R.ok(false);
    }

    @ApiOperation("获取会话次数")
    @PostMapping("/getSessionNum")
    public R<Integer> getSessionNum(@RequestBody KeyValueDto<String> keyValueDto) {
        Long userId = LoginHelper.getUserId();
        String userTokenKey = StringUtils.format("{}sessionNum:{}:{}", Constants.BASE_KEY, userId, keyValueDto.getKey());
        Integer sessionNum = Convert.toInt(RedisUtils.getCacheObject(userTokenKey), 0);

        if (sessionNum == 0) {
            MemberProfile memberProfile = memberProfileService.getByMemberId(userId);
            if (memberProfile != null) {
                MemberProfile updateMemberProfile = new MemberProfile();
                updateMemberProfile.setMemberId(userId);
                updateMemberProfile.setSessionNum(Convert.toInt(memberProfile.getSessionNum(), 0) + 1);
                updateMemberProfile.setLoginDate(new Date());
                updateMemberProfile.setLoginIp(ServletUtils.getClientIP());
                memberProfileService.updateMemberDetail(updateMemberProfile);
                sessionNum = updateMemberProfile.getSessionNum();
                RedisUtils.setCacheObject(userTokenKey, sessionNum, Duration.ofHours(24));
            }
        }

        return R.ok(sessionNum);
    }

    @Autowired
    private WxMaService wxMaService;

    @SneakyThrows
    @ApiOperation("获取小程序客服链接")
    @GetMapping("/getMiniAppUrlScheme")
    public void getMiniAppUrlScheme(Long uid, String path, String query, HttpServletResponse response) {
        log.info("获取小程序客服链接--uid：{},path:{}", uid, path);
        if (uid == null || uid <= 0)
            return;
        String userTokenKey = StringUtils.format("{}miniapp:urlscheme:{}:{}", Constants.BASE_KEY, uid, SecureUtil.md5(uid + path));
        String url = RedisUtils.getCacheObject(userTokenKey);
        if (StringUtils.isEmpty(url)) {

            var builder = WxMaGenerateSchemeRequest
                .newBuilder().expireType(1).expireInterval(30);
            if (StringUtils.isNotEmpty(path))
                builder.jumpWxa(WxMaGenerateSchemeRequest.JumpWxa.newBuilder().path(path).query(query).build());
            url = wxMaService.getWxMaSchemeService().generate(builder.build());
            RedisUtils.setCacheObject(userTokenKey, url, Duration.ofDays(29));

        }
        response.sendRedirect(url);

    }

    /**
     * 绑定手机号
     */
    @ApiOperation("绑定手机号")
    @PostMapping("/bindingMobile")
    public R bindingMobile(@Validated @RequestBody MobileDto dto) {
        // 验证手机格式正确性
        if (!Validator.isMobile(dto.getMobile())) {
            return R.fail("您输入的手机号格式不正确");
        }
        dto.setMemberId(LoginHelper.getUserId());
        dto.setIsChange(false);
        return memberProfileService.updateMobileByMemberId(dto);
    }

    /**
     * 更换手机号
     */
    @ApiOperation("更换手机号")
    @PostMapping("/changeMobile")
    public R changeMobile(@Validated @RequestBody MobileDto dto) {
        // 验证手机格式正确性
        if (!Validator.isMobile(dto.getMobile())) {
            return R.fail("您输入的手机号格式不正确");
        }
        dto.setMemberId(LoginHelper.getUserId());
        dto.setIsChange(true);
        return memberProfileService.updateMobileByMemberId(dto);
    }

    /**
     * 比对用户的实名认证信息是否正确
     */
    @ApiOperation("比对用户的实名认证信息是否正确")
    @SaCheckPermission("sdk:member:checkUserCert")
    @PostMapping("/checkUserCert")
    public R checkUserCert(@RequestBody CertDto certDto) {
        if (!IdcardUtil.isValidCard(certDto.getIdCardNo())) {
            return R.fail("您输入的身份证号格式不正确");
        }
        // 随机生成密钥
        byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);
        // 加密
        String encrypt = aes.encryptBase64(certDto.getIdCardNo());
        // 查询出用户的信息
        MemberProfile byMemberId = memberProfileService.getByMemberId(certDto.getMemberId());
        if (byMemberId != null
            && byMemberId.getRealName().equals(certDto.getRealName())
            && byMemberId.getIdCardNo().equals(encrypt)) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 设置用户为官方账号
     */
    @ApiOperation("设置用户为官方账号")
    @SaCheckPermission("sdk:member:setUserOfficial")
    @PostMapping("/setUserOfficial")
    public R setUserOfficial(@RequestBody IdTypeDto<Long, Integer> idTypeDto) {
        MemberProfile memberProfile = new MemberProfile();
        memberProfile.setMemberId(idTypeDto.getId());

        // 更新关联表里的字段：是否是官方账号
        UserExtend entity = new UserExtend();
        entity.setMemberId(idTypeDto.getId());

        if (idTypeDto.getType().equals(1)) {
            memberProfile.setIsOfficial(0);
            entity.setIsOfficial(0);
        } else {
            memberProfile.setIsOfficial(1);
            entity.setIsOfficial(1);
        }

        // 更新关联表里的字段：是否是官方账号
        remoteUserExtendService.updateUserDetail(entity);

        return toAjax(memberProfileService.updateMemberDetail(memberProfile));
    }

    /**
     * 注销账号
     */
    @ApiOperation("注销账号")
    @PostMapping("/cannellation")
    public R cannellation(@RequestBody CannellationDto cannellationDto) {

        var cancellation = new CancellationLog();
        cancellation.setMemberId(LoginHelper.getUserId());
        cancellation.setReason(cannellationDto.getReason());
        cancellation.setStatus(0);
        return toAjax(cancellationLogService.save(cancellation));

    }

    @ApiOperation("查看身份证号码")
    @SaCheckPermission("sdk:member:viewSensitiveInfo")
    @PostMapping("/viewSensitiveInfo")
    public R<SensitiveDto> viewSensitiveInfo(@RequestBody IdNameDto<Long> idNameDto) {
        String password = idNameDto.getName();
        //保存日志
        saveLog(idNameDto.getId());
        MemberDetailVo memberDetailVo = memberProfileService.getMemberDetailByMemberId(idNameDto.getId());
        if (memberDetailVo == null)
            return R.fail("用户不存在或已删除");


        SysUser sysUser = remoteUserService.selectUserById(LoginHelper.getUserId());
        if (sysUser == null)
            return R.fail("当前登录用户存在异常");

        //校验密码
        if (!BCrypt.checkpw(password, sysUser.getPassword()))
            return R.fail("请输入正确的密码");
        SensitiveDto sensitiveDto = new SensitiveDto();
        //身份证号
        if (StringUtils.isNotEmpty(memberDetailVo.getIdCardNo())) {
            byte[] key = webConfig.getCommonAesKey().getBytes(StandardCharsets.UTF_8);
            AES aes = SecureUtil.aes(key);
            // 身份证解密
            String decryptStr = aes.decryptStr(memberDetailVo.getIdCardNo());
            sensitiveDto.setIdCardNo(decryptStr);
        }


        if (StringUtils.isNotEmpty(memberDetailVo.getMobile()))
            sensitiveDto.setMobile(memberDetailVo.getMobile());
        if (StringUtils.isNotEmpty(memberDetailVo.getEmail()))
            sensitiveDto.setEmail(memberDetailVo.getEmail());

        if (StringUtils.isNotEmpty(memberDetailVo.getRealName()))
            sensitiveDto.setRealName(memberDetailVo.getRealName());


        return R.ok(sensitiveDto);
    }


    @ApiOperation("获取XXX")
    @PostMapping("/getMiniAppUrlSchemeXXX")
    public R getMiniAppUrlSchemeIdCard(@RequestBody IdTypeDto<String, Map<String, Object>> idTypeDto) {
        String tempToken = webConfig.getTempToken();
        String token = idTypeDto.getId();
        Map<String, Object> type = idTypeDto.getType();
        if (token.equals(tempToken) && type.get("idCard") != null) {
            try {
                String idCard = Convert.toStr(type.get("idCard"));
                String aesKey = "aEz64VYg7LvsXrQYo7r11qoXEsTMkOq3";
                byte[] key = aesKey.getBytes(StandardCharsets.UTF_8);
                AES aes = SecureUtil.aes(key);
                // 身份证解密
                String decryptStr = aes.decryptStr(idCard);
                return R.ok(decryptStr);
            } catch (Exception e) {
                return R.fail("身份证秘串不对");
            }
        }
        return R.fail("error");
    }


    public void saveLog(Long memberId) {
        SysOperLog operLog = new SysOperLog();
        operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
        // 请求的地址
        operLog.setOperIp(ServletUtils.getClientIP());
        operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
        String username = LoginHelper.getUsername();
        if (StringUtils.isNotBlank(username)) {
            operLog.setOperName(username);
        }
        HttpServletRequest request = ServletUtils.getRequest();

        // 设置方法名称
        String className = "com.euler.sdk.controller.MemberController";
        String methodName = "viewSensitiveInfo";
        operLog.setMethod(className + "." + methodName + "()");
        // 设置请求方式
        operLog.setRequestMethod(request.getMethod());
        // 处理设置注解上的参数

        // 设置action动作
        operLog.setBusinessType(BusinessType.SENSITIVEINFO.ordinal());
        // 设置标题
        operLog.setTitle("敏感数据查看");
        // 设置操作人类别
        operLog.setOperatorType(OperatorType.MANAGE.ordinal());
        // 是否需要保存request，参数和值
        operLog.setOperParam(memberId.toString());

        // 保存数据库
        remoteLogService.saveLog(operLog);

    }


}
