package com.euler.community.controller;

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
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.AdvertViewRecordBo;
import com.euler.community.domain.dto.AdvertViewRecordDto;
import com.euler.community.domain.vo.AdvertViewRecordVo;
import com.euler.community.service.IAdvertViewRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Controller
 * 前端访问路由地址为:/system/record
 * @author euler
 *  2022-06-17
 */
@Validated
@Api(value = "控制器", tags = {"管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/advertRecord")
public class AdvertViewRecordController extends BaseController {

    private final IAdvertViewRecordService iAdvertViewRecordService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @SaCheckPermission("community:record:list")
    @PostMapping("/list")
    public TableDataInfo<AdvertViewRecordVo> list(@Validated(QueryGroup.class) @RequestBody AdvertViewRecordDto advertViewRecordDto) {
        return iAdvertViewRecordService.queryPageList(advertViewRecordDto);
    }

    /**
     * 导出列表
     */
    @ApiOperation("导出列表")
    @SaCheckPermission("community:record:export")
    @PostMapping("/export")
    public void export(@Validated AdvertViewRecordDto advertViewRecordDto, HttpServletResponse response) {
        List<AdvertViewRecordVo> list = iAdvertViewRecordService.queryList(advertViewRecordDto);
        ExcelUtil.exportExcel(list, "", AdvertViewRecordVo.class, response);
    }

    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @SaCheckPermission("community:record:query")
    @PostMapping("/getInfo")
    public R<AdvertViewRecordVo> getInfo(@RequestBody AdvertViewRecordDto advertViewRecordDto) {
        return R.ok(iAdvertViewRecordService.queryById(advertViewRecordDto.getId()));
    }

    /**
     * 新增
     */
    @ApiOperation("新增")
    @SaCheckPermission("community:record:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AdvertViewRecordBo bo) {
        return toAjax(iAdvertViewRecordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @SaCheckPermission("community:record:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AdvertViewRecordBo bo) {
        return toAjax(iAdvertViewRecordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @SaCheckPermission("community:record:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @PostMapping("/{ids}")
    public R<Void> remove( @RequestBody AdvertViewRecordDto advertViewRecordDto) {
        return toAjax(iAdvertViewRecordService.deleteWithValidByIds(Arrays.asList(advertViewRecordDto.getIds()), true) ? 1 : 0);
    }


    /**
     * 广告浏览-记录浏览人
     *
     * @param bo 广告记录
     */
    @ApiOperation("广告浏览")
    @PostMapping("/view")
    public R view(@RequestBody AdvertViewRecordBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iAdvertViewRecordService.view(bo);
    }
}
