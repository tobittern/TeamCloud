package com.euler.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.domain.bo.SdkMainMenuBo;
import com.euler.system.domain.dto.SdkMainMenuDto;
import com.euler.system.domain.vo.SdkMainMenuVo;
import com.euler.system.service.ISdkMainMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Arrays;

/**
 * SDK菜单管理
 *
 * @author euler
 */
@Validated
@Api(value = "SDK菜单管理控制器", tags = {"SDK菜单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sdkMenu")
public class SdkMenuController extends BaseController {

    @Autowired
    private ISdkMainMenuService sysSdkMainMenuService;

    /**
     * 查询SDK菜单列表
     */
    @ApiOperation("查询SDK菜单列表")
    @PostMapping("/list")
    public TableDataInfo<SdkMainMenuVo> list(@RequestBody SdkMainMenuDto pageDto) {
        return sysSdkMainMenuService.queryPageList(pageDto);
    }

    /**
     * 获取SDK菜单详细信息
     */
    @ApiOperation("获取SDK菜单详细信息")
    @PostMapping("/getInfo")
    public R<SdkMainMenuVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(sysSdkMainMenuService.queryById(idDto.getId()));
    }

    /**
     * 新增SDK菜单
     */
    @ApiOperation("新增SDK菜单")
    @SaCheckPermission("system:sdkMenu:add")
    @Log(title = "SDK菜单" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody SdkMainMenuBo bo) {
        return sysSdkMainMenuService.insertByBo(bo);
    }

    /**
     * 修改SDK菜单
     */
    @ApiOperation("修改SDK菜单")
    @SaCheckPermission("system:sdkMenu:edit")
    @Log(title = "SDK菜单" , businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody SdkMainMenuBo bo) {
        return sysSdkMainMenuService.updateByBo(bo);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @Log(title = "上下架")
    @SaCheckPermission("system:sdkMenu:operation")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return sysSdkMainMenuService.operation(idNameTypeDicDto, userId);
    }

    /**
     * 删除SDK菜单
     */
    @ApiOperation("删除SDK菜单")
    @SaCheckPermission("system:sdkMenu:remove")
    @Log(title = "SDK菜单" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split("," );
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return sysSdkMainMenuService.deleteWithValidByIds(Arrays.asList(ids));
    }

}
