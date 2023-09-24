package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.ResourceBo;
import com.euler.community.domain.dto.ResourceDto;
import com.euler.community.domain.vo.ResourceVo;
import com.euler.community.service.IResourceService;
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
 * 动态所有资源Controller
 * 前端访问路由地址为:/community/resource
 *
 * @author euler
 * @date 2022-06-09
 */
@Validated
@Api(value = "动态所有资源控制器", tags = {"动态所有资源管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    private final IResourceService iResourceService;

    /**
     * 查询动态所有资源列表
     */
    @ApiOperation("查询动态所有资源列表")
    @SaCheckPermission("community:resource:list")
    @PostMapping("/list")
    public TableDataInfo<ResourceVo> list(@RequestBody ResourceDto dto) {
        return iResourceService.queryPageList(dto);
    }

    /**
     * 获取动态所有资源详细信息
     */
    @ApiOperation("获取动态所有资源详细信息")
    @SaCheckPermission("community:resource:query")
    @PostMapping("/getInfo")
    public R<ResourceVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iResourceService.queryById(idDto.getId()));
    }

    /**
     * 新增动态所有资源
     */
    @ApiOperation("新增动态所有资源")
    @SaCheckPermission("community:resource:add")
    @Log(title = "动态所有资源", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody ResourceBo bo) {
        return iResourceService.insertByBo(bo);
    }

    /**
     * 删除动态所有资源
     */
    @ApiOperation("删除动态所有资源")
    @SaCheckPermission("community:resource:remove")
    @Log(title = "删除动态所有资源", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(iResourceService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
