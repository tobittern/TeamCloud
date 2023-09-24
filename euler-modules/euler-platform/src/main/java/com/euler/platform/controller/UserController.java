package com.euler.platform.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.domain.dto.FrontUserUpdateDto;
import com.euler.platform.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "用户信息管理", tags = {"用户信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;

    /**
     * 查询用户基础信息
     */
    @ApiOperation("查询用户基础信息")
    @SaCheckPermission("platform:user:getInfo")
    @PostMapping("/getInfo")
    public R getInfo() {
        Long userId = LoginHelper.getUserId();
        return iUserService.getInfo(userId);
    }


    /**
     * 编辑用户基础信息
     */
    @ApiOperation("编辑用户基础信息")
    @SaCheckPermission("platform:user:edit")
    @PostMapping("/edit")
    public R edit(@RequestBody FrontUserUpdateDto frontUserUpdateDto) {
        Long userId = LoginHelper.getUserId();
        frontUserUpdateDto.setUserId(userId);
        return iUserService.edit(frontUserUpdateDto);
    }



}
