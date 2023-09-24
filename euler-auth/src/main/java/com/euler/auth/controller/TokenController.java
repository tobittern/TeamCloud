package com.euler.auth.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.euler.auth.domain.WxLoginDto;
import com.euler.auth.form.LoginBody;
import com.euler.auth.form.RegisterBody;
import com.euler.auth.form.UpdatePwdDto;
import com.euler.auth.service.SysLoginService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.enums.LoginTypeEnum;
import com.euler.common.core.enums.UserTypeEnum;
import com.euler.common.satoken.utils.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * token 控制
 *
 * @author euler
 */
@Slf4j
@Validated
@Api(value = "认证鉴权控制器", tags = {"认证鉴权管理"})
@RequiredArgsConstructor
@RestController
public class TokenController {

    @Autowired
    private SysLoginService sysLoginService;

    @ApiOperation("多用户登录")
    @PostMapping("/loginFirst")
    public R loginFirst(@RequestBody LoginBody form) {
        StpUtil.logout();
        return sysLoginService.loginFirst(form);
    }


    @ApiOperation("登录方法")
    @PostMapping("/login")
    public R login(@RequestBody LoginBody form) {
        StpUtil.logout();
        return sysLoginService.login(form);
    }

    @ApiOperation("一键登录获取手机号快捷登录")
    @PostMapping("/quickLogin")
    public R quickLogin(@RequestBody IdDto<String> idDto) {
        log.info("用户一键登录开始启动");
        LoginBody loginBody = new LoginBody();
        loginBody.setUserName(idDto.getId());
        loginBody.setUserType(UserTypeEnum.SDK_USER.getUserTypeNum());
        loginBody.setLoginType(LoginTypeEnum.MOBILESIGN.getLoginTypeNum());
        return sysLoginService.login(loginBody);
    }

    /**
     * 微信一键登录
     */
    @ApiOperation("微信一键登录")
    @PostMapping("/wxLogin")
    public R wxLogin(@RequestBody WxLoginDto dto) {
        return sysLoginService.wxLogin(dto.getMobile());
    }

    @ApiOperation("登出方法")
    @PostMapping("/logout")
    public R logout() {
        try {
            sysLoginService.logout(LoginHelper.getUsername());
        } catch (NotLoginException e) {
        }
        return R.ok();
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok("", 1);
    }

    @ApiOperation("用户自动注册")
    @PostMapping("/autoRegister")
    public R autoRegister() {
        return R.ok(sysLoginService.autoRegister());
    }

    /**
     * 忘记密码，修改密码
     */
    @ApiOperation("忘记密码，修改密码")
    @PostMapping("/forgetPwd")
    public R forgetPwd(@Validated @RequestBody RegisterBody body) {
        // 验证数据的合理性
        sysLoginService.forgetPassword(body);
        return R.ok("密码修改成功！");
    }


    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @PostMapping("/updatePwd")
    public R updatePwd(@RequestBody UpdatePwdDto updateBody) {
        sysLoginService.updatePwd(updateBody);
        return R.ok("密码修改成功！");
    }

}
