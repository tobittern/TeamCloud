package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.AdvertBo;
import com.euler.community.domain.dto.AdvertDto;
import com.euler.community.domain.dto.AdvertDynamicEsDto;
import com.euler.community.domain.vo.AdvertVo;
import com.euler.community.service.IAdvertService;
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
 * 广告Controller
 * 前端访问路由地址为:/system/advert
 *
 * @author euler
 *  2022-06-06
 */
@Validated
@Api(value = "广告控制器", tags = {"广告管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/advert")
public class AdvertController extends BaseController {

    private final IAdvertService iAdvertService;

    /**
     * 查询广告列表
     */
    @ApiOperation("查询广告列表")
    @SaCheckPermission("community:advert:list")
    @PostMapping("/list")
    public TableDataInfo<AdvertVo> list(@RequestBody AdvertDto advertDto) {
        return iAdvertService.queryPageList(advertDto);
    }

    /**
     * 导出广告列表
     */
    @ApiOperation("导出广告列表")
    @SaCheckPermission("community:advert:export")
    @PostMapping("/export")
    public void export(@Validated AdvertDto bo, HttpServletResponse response) {
        List<AdvertVo> list = iAdvertService.queryList(bo);
        ExcelUtil.exportExcel(list, "广告", AdvertVo.class, response);
    }

    /**
     * 获取广告详细信息
     */
    @ApiOperation("获取广告详细信息")
    @SaCheckPermission("community:advert:query")
    @PostMapping("/getInfo")
    public R getInfo(@RequestBody AdvertDto dto) {
        return R.ok(iAdvertService.queryById(dto.getId()));
    }

    /**
     * 新增广告
     */
    @ApiOperation("新增广告")
    @SaCheckPermission("community:advert:add")
    @Log(title = "广告", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody AdvertBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iAdvertService.insertByBo(bo);
    }

    /**
     * 修改广告
     */
    @ApiOperation("修改广告")
    @SaCheckPermission("community:advert:edit")
    @Log(title = "广告", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R edit(@Validated(EditGroup.class) @RequestBody AdvertBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return iAdvertService.updateByBo(bo);
    }


    /**
     * 单个删除广告
     */
    @ApiOperation("单个删除广告")
    @SaCheckPermission("community:advert:remove")
    @Log(title = "广告", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R delete(@RequestBody AdvertBo bo) {
        try {
            return toAjax(iAdvertService.logicRemove(bo) ? 1 : 0);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }


    /**
     * 批量 删除广告
     */
    @ApiOperation("删除广告")
    @SaCheckPermission("community:advert:remove")
    @Log(title = "广告", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody AdvertDto dto ) {
        dto.setMemberId(LoginHelper.getUserId());
        return toAjax(iAdvertService.deleteWithValidByIds(Arrays.asList(dto.getIds()), true) ? 1 : 0);
    }

    /**
     * 根据所在页数、当前登录人 获取广告
     * @param advertBo 查询页数
     */
    @ApiOperation("根据所在页数获取广告")
    @PostMapping("/queryAdvert")
    public R<List<AdvertDynamicEsDto>> queryAdvert(@RequestBody AdvertBo advertBo) {
        return R.ok(iAdvertService.queryByPageNum(advertBo));
    }

    /**
     * 修改广告状态
     * @param bo 广告
     */
    @ApiOperation("修改广告状态")
    @SaCheckPermission("community:advert:updateStatus")
    @PostMapping("/updateStatus")
    public R updateStatus(@RequestBody AdvertBo bo) {
        return iAdvertService.updateStatus(bo);
    }
}
