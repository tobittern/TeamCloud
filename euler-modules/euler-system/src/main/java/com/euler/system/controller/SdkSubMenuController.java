package com.euler.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.system.domain.bo.SdkSubMenuBo;
import com.euler.system.domain.dto.SdkSubMenuDto;
import com.euler.system.domain.vo.SdkSubMenuVo;
import com.euler.system.service.ISdkSubMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * SDK子菜单Controller
 * 前端访问路由地址为:/system/subMenu
 * @author euler
 * @date 2023-03-20
 */
@Validated
@Api(value = "SDK子菜单控制器" , tags = {"SDK子菜单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/subMenu")
public class SdkSubMenuController extends BaseController {

    @Autowired
    private ISdkSubMenuService sysSdkSubMenuService;

    /**
     * 查询SDK子菜单列表
     */
    @ApiOperation("查询SDK子菜单列表")
    @PostMapping("/list")
    public TableDataInfo<SdkSubMenuVo> list(@RequestBody SdkSubMenuDto dto) {
        return sysSdkSubMenuService.queryPageList(dto);
    }

    /**
     * 获取SDK子菜单详细信息
     */
    @ApiOperation("获取SDK子菜单详细信息")
    @PostMapping("/getInfo")
    public R<SdkSubMenuVo> getInfo(@RequestBody SdkSubMenuDto dto) {
        return R.ok(sysSdkSubMenuService.queryByDto(dto));
    }

    /**
     * 新增SDK子菜单
     */
    @ApiOperation("新增SDK子菜单")
    @SaCheckPermission("system:subMenu:add")
    @Log(title = "SDK子菜单" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody SdkSubMenuBo bo) {
        return sysSdkSubMenuService.insertByBo(bo);
    }

    /**
     * 修改SDK子菜单
     */
    @ApiOperation("修改SDK子菜单")
    @SaCheckPermission("system:subMenu:edit")
    @Log(title = "SDK子菜单" , businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody SdkSubMenuBo bo) {
        return sysSdkSubMenuService.updateByBo(bo);
    }

    /**
     * 删除SDK子菜单
     */
    @ApiOperation("删除SDK子菜单")
    @SaCheckPermission("system:subMenu:remove")
    @Log(title = "SDK子菜单" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody SdkSubMenuDto dto) {
        return sysSdkSubMenuService.deleteSubMenu(dto);
    }

}
