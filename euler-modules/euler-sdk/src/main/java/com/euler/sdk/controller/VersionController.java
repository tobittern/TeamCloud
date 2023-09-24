package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.sdk.domain.bo.VersionBo;
import com.euler.sdk.domain.dto.VersionDto;
import com.euler.sdk.domain.vo.VersionVo;
import com.euler.sdk.service.IVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * sdk版本管理Controller
 * 前端访问路由地址为:/system/version
 *
 * @author euler
 * 2022-07-08
 */
@Validated
@Api(value = "sdk版本管理控制器", tags = {"sdk版本管理管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/version")
public class VersionController extends BaseController {

    private final IVersionService iVersionService;

    /**
     * 查询sdk版本管理列表
     */
    @ApiOperation("查询sdk版本管理列表")
    //@SaCheckPermission("system:version:list")
    @PostMapping("/list")
    public TableDataInfo<VersionVo> list(@Validated(QueryGroup.class) @RequestBody VersionDto versionDto) {
        return iVersionService.queryPageList(versionDto);
    }

    /**
     * 导出sdk版本管理列表
     */
    @ApiOperation("导出sdk版本管理列表")
    @SaCheckPermission("system:version:export")
    @Log(title = "sdk版本管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated @RequestBody VersionDto versionDto, HttpServletResponse response) {
        List<VersionVo> list = iVersionService.queryList(versionDto);
        ExcelUtil.exportExcel(list, "sdk版本管理", VersionVo.class, response);
    }

    /**
     * 获取sdk版本管理详细信息
     */
    @ApiOperation("获取sdk版本管理详细信息")
    @SaCheckPermission("system:version:query")
    @PostMapping("/getInfo")
    public R<VersionVo> getInfo(@RequestBody VersionBo bo) {
        return R.ok(iVersionService.queryById(bo.getId()));
    }

    /**
     * 新增sdk版本管理
     */
    @ApiOperation("新增sdk版本管理")
    @SaCheckPermission("system:version:add")
    @Log(title = "sdk版本管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody VersionBo bo) {
        return iVersionService.insertByBo(bo);
    }

    /**
     * 修改sdk版本管理
     */
    @ApiOperation("修改sdk版本管理")
    @SaCheckPermission("system:version:edit")
    @Log(title = "sdk版本管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody VersionBo bo) {
        return iVersionService.updateByBo(bo);
    }

    /**
     * 删除sdk版本管理
     */
    @ApiOperation("批量删除sdk版本管理")
    @SaCheckPermission("system:version:remove")
    @Log(title = "sdk版本管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R<Void> remove(@RequestBody VersionDto versionDto) {
        return toAjax(iVersionService.deleteWithValidByIds(Arrays.asList(versionDto.getIds()), true) ? 1 : 0);
    }

    /**
     * 删除sdk版本管理
     */
    @ApiOperation("删除sdk版本管理")
    @SaCheckPermission("system:version:delete")
    @Log(title = "sdk版本管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R<Void> delete(@RequestBody VersionDto versionDto) {
        return iVersionService.delete(versionDto.getId());
    }



    /**
     * 获取sdk版本管理新版本列表
     */
    @ApiOperation("获取sdk版本管理新版本列表")
    @SaCheckPermission("system:version:list")
    @PostMapping("/getNewVersions")
    public R<Object> getNewVersions() {
        return R.ok(iVersionService.getNewVersions());
    }


    /**
     * 官网Sdk版本下载数据，返回特定格式
     */
    @ApiOperation("获取sdk下载版本")
    @PostMapping("/getSdkVersions")
    public R<Object> getSdkVersions() {
        return R.ok(iVersionService.getSdkVersions());
    }

}
