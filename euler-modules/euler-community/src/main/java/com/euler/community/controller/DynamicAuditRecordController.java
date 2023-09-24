package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicAuditRecordDto;
import com.euler.community.domain.vo.DynamicAuditRecordVo;
import com.euler.community.service.IDynamicAuditRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 审核记录Controller
 * 前端访问路由地址为:/community/audit
 *
 * @author euler
 * @date 2022-06-01
 */
@Validated
@Api(value = "审核记录控制器", tags = {"审核记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/dynamicAudit")
public class DynamicAuditRecordController extends BaseController {

    private final IDynamicAuditRecordService iDynamicAuditRecordService;

    /**
     * 查询审核记录列表
     */
    @ApiOperation("查询审核记录列表")
    @SaCheckPermission("community:dynamicAudit:list")
    @PostMapping("/list")
    public TableDataInfo<DynamicAuditRecordVo> list(@RequestBody DynamicAuditRecordDto dto) {
        return iDynamicAuditRecordService.queryPageList(dto);
    }

    /**
     * 删除审核记录
     */
    @ApiOperation("删除审核记录")
    @SaCheckPermission("community:dynamicAudit:remove")
    @Log(title = "审核记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(iDynamicAuditRecordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
