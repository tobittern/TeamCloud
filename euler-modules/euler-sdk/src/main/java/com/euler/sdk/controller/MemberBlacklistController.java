package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.MemberBlacklistBo;
import com.euler.sdk.domain.dto.CommonIdPageDto;
import com.euler.sdk.domain.vo.MemberBlacklistRecordVo;
import com.euler.sdk.service.IMemberBlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 用户黑名单列Controller
 * 前端访问路由地址为:/system/blacklist
 *
 * @author euler
 * @date 2022-06-13
 */
@Validated
@Api(value = "用户黑名单列控制器", tags = {"用户黑名单列管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/blacklist")
public class MemberBlacklistController extends BaseController {

    private final IMemberBlacklistService iMemberBlacklistService;

    /**
     * 查询用户黑名单列列表
     */
    @ApiOperation("查询用户黑名单列列表")
    @SaCheckPermission("sdk:blacklist:list")
    @PostMapping("/list")
    public TableDataInfo<MemberBlacklistRecordVo> list(@RequestBody CommonIdPageDto<Long> dto) {
        return iMemberBlacklistService.queryPageList(dto);
    }

    /**
     * 新增用户黑名单列
     */
    @ApiOperation("新增用户黑名单列")
    @SaCheckPermission("sdk:blacklist:add")
    @Log(title = "用户黑名单列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@RequestBody MemberBlacklistBo bo) {
        return iMemberBlacklistService.insertByBo(bo);
    }

    /**
     * 修改用户黑名单列
     */
    @ApiOperation("修改用户黑名单列")
    @SaCheckPermission("sdk:blacklist:edit")
    @Log(title = "用户黑名单列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@RequestBody MemberBlacklistBo bo) {
        return iMemberBlacklistService.updateByBo(bo);
    }

    /**
     * 删除用户黑名单列
     */
    @ApiOperation("删除用户黑名单列")
    @SaCheckPermission("sdk:blacklist:remove")
    @Log(title = "用户黑名单列", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody KeyValueDto<Long> dto) {
        return toAjax(iMemberBlacklistService.remove(dto) ? 1 : 0);
    }

    /**
     * 一键解封
     */
    @ApiOperation("一键解封")
    @SaCheckPermission("sdk:blacklist:unseal")
    @Log(title = "一键解封", businessType = BusinessType.UPDATE)
    @PostMapping("/unseal")
    public R unseal(@RequestBody IdTypeDto<Long, Integer> idTypeDto) {
        return iMemberBlacklistService.unseal(idTypeDto);
    }

}
