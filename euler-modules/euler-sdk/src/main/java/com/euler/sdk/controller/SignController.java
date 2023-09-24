package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.bo.SignConfigBo;
import com.euler.sdk.api.domain.dto.SignInDto;
import com.euler.sdk.api.domain.SignInVo;
import com.euler.sdk.service.ISignConfigService;
import com.euler.sdk.service.ISignInService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 签到
 * @author euler
 * @date 2022-03-21
 */
@Validated
@Api(value = "签到", tags = {"签到"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sign")
public class SignController extends BaseController {

    @Autowired
    private ISignInService iSignInService;
    @Autowired
    private ISignConfigService iSignConfigService;

    /**
     * 签到列表
     */
    @ApiOperation("签到列表")
    @PostMapping("/list")
    public TableDataInfo<SignInVo> list(@RequestBody SignInDto signInDto) {
        return iSignInService.queryPageList(signInDto);
    }

    /**
     * 签到
     */
    @ApiOperation("签到")
    @RepeatSubmit()
    @PostMapping("/click")
    public R click() {
        Long userId = LoginHelper.getUserId();
        return iSignInService.insertByBo(userId);
    }

    /**
     * 修改签到配置
     */
    @ApiOperation("修改签到配置")
    @Log(title = "修改签到配置", businessType = BusinessType.UPDATE)
    @SaCheckPermission("sdk:sign:config:edit")
    @RepeatSubmit()
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody SignConfigBo bo) {
        return toAjax(iSignConfigService.updateByBo(bo) ? 1 : 0);
    }
}
