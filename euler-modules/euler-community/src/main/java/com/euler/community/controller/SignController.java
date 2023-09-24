package com.euler.community.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.RemoteSignService;
import com.euler.sdk.api.domain.SignInVo;
import com.euler.sdk.api.domain.dto.SignInDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 签到
 *
 * @author euler
 * @date 2022-03-21
 */
@Validated
@Api(value = "签到", tags = {"签到"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sign")
public class SignController extends BaseController {

    @DubboReference
    private RemoteSignService remoteSignService;

    /**
     * 签到列表
     */
    @ApiOperation("签到列表")
    @PostMapping("/list")
    public TableDataInfo<SignInVo> list(@RequestBody SignInDto signInDto) {
        return remoteSignService.queryPageList(signInDto);
    }

    /**
     * 签到
     */
    @ApiOperation("签到")
    @RepeatSubmit()
    @PostMapping("/click")
    public R click() {
        Long userId = LoginHelper.getUserId();
        return remoteSignService.insertByBo(userId);
    }

    /**
     * 判断当前是否签到过
     */
    @ApiOperation("判断当前是否签到过")
    @PostMapping("/checkClick")
    public R checkClick() {
        Long userId = LoginHelper.getUserId();
        return remoteSignService.checkClick(userId);
    }
}
