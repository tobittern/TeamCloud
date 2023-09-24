package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.bo.GrowthConfigBo;
import com.euler.sdk.domain.dto.GrowthSystemDto;
import com.euler.sdk.service.IGrowthConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.domain.vo.GrowthSystemVo;
import com.euler.sdk.domain.bo.GrowthSystemBo;
import com.euler.sdk.service.IGrowthSystemService;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 成长体系Controller
 * 前端访问路由地址为:/sdk/growthSystem
 * @author euler
 * @date 2022-03-22
 */
@Validated
@Api(value = "成长体系控制器", tags = {"成长体系管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/growthSystem")
public class GrowthSystemController extends BaseController {

    private final IGrowthSystemService iGrowthSystemService;
    private final IGrowthConfigService iGrowthConfigService;

    /**
     * 查询成长体系列表
     */
    @ApiOperation("查询成长体系列表")
    @PostMapping("/list")
    public TableDataInfo<GrowthSystemVo> list(@RequestBody GrowthSystemDto dto) {
        return iGrowthSystemService.queryPageList(dto);
    }

    /**
     * 获取成长体系详细信息
     */
    @ApiOperation("获取成长体系详细信息")
    @PostMapping("/getInfo")
    public R<GrowthSystemVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iGrowthSystemService.queryById(idDto.getId()));
    }

    /**
     * 新增成长体系
     */
    @ApiOperation("新增成长体系")
    @SaCheckPermission("sdk:growthSystem:add")
    @Log(title = "成长体系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GrowthSystemBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iGrowthSystemService.insertByBo(bo);
    }

    /**
     * 成长值升级
     */
    @ApiOperation("成长值升级")
    @Log(title = "成长值升级", businessType = BusinessType.UPDATE)
    @SaCheckPermission("sdk:growthSystem:upgradeGrowth")
    @PostMapping("/upgradeGrowth")
    @RepeatSubmit()
    public R upgradeGrowth(@Validated(EditGroup.class) @RequestBody GrowthSystemBo bo) {
        return iGrowthSystemService.upgradeGrowth(bo);
    }

    /**
     * 修改成长值配置
     */
    @ApiOperation("修改成长值配置")
    @SaCheckPermission("sdk:growthSystem:edit")
    @Log(title = "成长值配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R editConfig(@Validated(EditGroup.class) @RequestBody GrowthConfigBo bo) {
        return iGrowthConfigService.updateByBo(bo);
    }
}
