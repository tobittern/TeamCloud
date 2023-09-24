package com.euler.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.system.domain.bo.SysAuditKeywordBo;
import com.euler.system.domain.dto.SysAuditKeywordDto;
import com.euler.system.domain.vo.SysAuditKeywordVo;
import com.euler.system.service.ISysAuditKeywordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 审核关键词 - 敏感词Controller
 * 前端访问路由地址为:/system/keyword
 *
 * @author euler
 * @date 2022-03-21
 */
@Slf4j
@Validated
@Api(value = "审核关键词 - 敏感词控制器", tags = {"审核关键词 - 敏感词管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/keyword")
public class SysAuditKeywordController extends BaseController {

    private final ISysAuditKeywordService iSysAuditKeywordService;

    /**
     * 查询审核关键词 - 敏感词列表
     */
    @ApiOperation("查询审核关键词 - 敏感词列表")
    @SaCheckPermission("system:keyword:list")
    @PostMapping("/list")
    public TableDataInfo<SysAuditKeywordVo> list(@RequestBody SysAuditKeywordDto sysAuditKeywordDto) {
        return iSysAuditKeywordService.queryPageList(sysAuditKeywordDto);
    }

    /**
     * 新增审核关键词 - 敏感词
     */
    @ApiOperation("新增审核关键词 - 敏感词")
    @SaCheckPermission("system:keyword:add")
    @Log(title = "审核关键词 - 敏感词", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody SysAuditKeywordBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iSysAuditKeywordService.insertByBo(bo);
    }

    /**
     * 修改审核关键词 - 敏感词
     */
    @ApiOperation("修改审核关键词 - 敏感词")
    @SaCheckPermission("system:keyword:edit")
    @Log(title = "审核关键词 - 敏感词", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody SysAuditKeywordBo bo) {
        return iSysAuditKeywordService.updateByBo(bo);
    }

    /**
     * 删除审核关键词 - 敏感词
     */
    @ApiOperation("删除审核关键词 - 敏感词")
    @SaCheckPermission("system:keyword:remove")
    @Log(title = "审核关键词 - 敏感词", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        // 将数组中的数据变成 int 类型
        return toAjax(iSysAuditKeywordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 修改审核关键词 - 敏感词
     */
    @ApiOperation("检查当前关键词是否是敏感词")
    @SaCheckPermission("system:keyword:check")
    @Log(title = "检查当前关键词是否是敏感词")
    @PostMapping("/check")
    public R check(String checkString) {
        return iSysAuditKeywordService.check(checkString, 1);
    }


}
