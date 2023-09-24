package com.euler.sdk.controller;

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
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.api.domain.SdkPopupVo;
import com.euler.sdk.api.domain.dto.SdkPopupDto;
import com.euler.sdk.domain.bo.PopupBo;
import com.euler.sdk.domain.dto.PopupPageDto;
import com.euler.sdk.service.IPopupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 弹窗管理Controller
 * 前端访问路由地址为:/sdk/popup
 *
 * @author euler
 * @date 2022-09-05
 */
@Validated
@Api(value = "弹窗管理控制器", tags = {"弹窗管理管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/popup")
public class PopupController extends BaseController {

    @Autowired
    private IPopupService popupService;

    /**
     * 查询弹窗管理列表
     */
    @ApiOperation("查询弹窗管理列表")
    @SaCheckPermission("sdk:popup:list")
    @PostMapping("/list")
    public TableDataInfo<SdkPopupVo> list(@RequestBody PopupPageDto pageDto) {
        return popupService.queryPageList(pageDto);
    }

    /**
     * 获取弹窗管理详细信息
     */
    @ApiOperation("获取弹窗管理详细信息")
    @SaCheckPermission("sdk:popup:query")
    @PostMapping("/getInfo")
    public R<SdkPopupVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(popupService.queryById(idDto.getId()));
    }

    /**
     * 新增弹窗管理
     */
    @ApiOperation("新增弹窗管理")
    @SaCheckPermission("sdk:popup:add")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody PopupBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return popupService.insertByBo(bo);
    }

    /**
     * 修改弹窗管理
     */
    @ApiOperation("修改弹窗管理")
    @SaCheckPermission("sdk:popup:edit")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody PopupBo bo) {
        return popupService.updateByBo(bo);
    }

    /**
     * 删除弹窗管理
     */
    @ApiOperation("删除弹窗管理")
    @SaCheckPermission("sdk:popup:remove")
    @Log(title = "SDK弹窗删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(popupService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 弹窗管理操作
     */
    @ApiOperation("弹窗管理操作")
    @SaCheckPermission("sdk:popup:operation")
    @Log(title = "SDK弹窗管理操作", businessType = BusinessType.UPDATE)
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        return popupService.operation(idNameTypeDicDto);
    }

    /**
     * 前端列表
     */
    @ApiOperation("前端列表")
    @PostMapping("/frontList")
    public R frontList(@RequestBody SdkPopupDto dto) {
        return R.ok(popupService.queryList(dto));
    }
}
