package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.sdk.domain.dto.DistributeItemRecordDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.domain.vo.DistributeItemRecordVo;
import com.euler.sdk.domain.bo.DistributeItemRecordBo;
import com.euler.sdk.service.IDistributeItemRecordService;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 派发物品记录Controller
 * 前端访问路由地址为:/sdk/distributeItemRecord
 * @author euler
 * @date 2022-04-09
 */
@Validated
@Api(value = "派发物品记录控制器", tags = {"派发物品记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/distributeItemRecord")
public class DistributeItemRecordController extends BaseController {

    private final IDistributeItemRecordService iDistributeItemRecordService;

    /**
     * 查询派发物品记录列表
     */
    @ApiOperation("查询派发物品记录列表")
    @PostMapping("/list")
    public TableDataInfo<DistributeItemRecordVo> list(@RequestBody DistributeItemRecordDto dto) {
        return iDistributeItemRecordService.queryPageList(dto);
    }

    /**
     * 获取派发物品记录详细信息
     */
    @ApiOperation("获取派发物品记录详细信息")
    @PostMapping("/getInfo")
    public R<DistributeItemRecordVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iDistributeItemRecordService.queryById(idDto.getId()));
    }

    /**
     * 新增派发物品记录
     */
    @ApiOperation("新增派发物品记录")
    @SaCheckPermission("sdk:distributeItemRecord:add")
    @Log(title = "派发物品记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody DistributeItemRecordBo bo) {
        return iDistributeItemRecordService.insertByBo(bo);
    }

    /**
     * 修改派发物品记录
     */
    @ApiOperation("修改派发物品记录")
    @SaCheckPermission("sdk:distributeItemRecord:edit")
    @Log(title = "派发物品记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody DistributeItemRecordBo bo) {
        return iDistributeItemRecordService.updateByBo(bo);
    }

}
