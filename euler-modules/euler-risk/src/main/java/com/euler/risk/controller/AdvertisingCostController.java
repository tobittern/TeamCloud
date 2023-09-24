package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.AdvertisingCostBo;
import com.euler.risk.domain.bo.AdvertisingMediaBo;
import com.euler.risk.domain.dto.AdvertisingCostDto;
import com.euler.risk.domain.dto.AdvertisingMediaPageDto;
import com.euler.risk.domain.vo.AdvertisingCostVo;
import com.euler.risk.domain.vo.AdvertisingMediaVo;
import com.euler.risk.service.IAdvertisingCostService;
import com.euler.risk.service.IAdvertisingMediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 广告成本管理Controller
 * 前端访问路由地址为:/risk/cost
 *
 * @author euler
 * @date 2022-08-24
 */
@Validated
@Api(value = "广告成本管理控制器", tags = {"广告成本管理管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/cost")
public class AdvertisingCostController extends BaseController {

    @Autowired
    private IAdvertisingCostService advertisingCostService;
    @Autowired
    private IAdvertisingMediaService advertisingMediaService;

    //region 广告成本管理

    /**
     * 查询广告成本管理列表
     */
    @ApiOperation("查询广告成本管理列表")
    @SaCheckPermission("risk:cost:list")
    @PostMapping("/list")
    public TableDataInfo<AdvertisingCostVo> list(@RequestBody AdvertisingCostDto dto) {
        return advertisingCostService.queryPageList(dto);
    }

    /**
     * 导出广告成本管理列表
     */
    @ApiOperation("导出广告成本管理列表")
    @SaCheckPermission("risk:cost:export")
    @PostMapping("/export")
    public void export(@RequestBody AdvertisingCostDto dto, HttpServletResponse response) {
        List<AdvertisingCostVo> list = advertisingCostService.queryList(dto);
        ExcelUtil.exportExcel(list, "广告成本管理", AdvertisingCostVo.class, response);
    }

    /**
     * 获取广告成本管理详细信息
     */
    @ApiOperation("获取广告成本管理详细信息")
    @SaCheckPermission("risk:cost:getInfo")
    @PostMapping("/getInfo")
    public R<AdvertisingCostVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(advertisingCostService.queryById(idDto.getId()));
    }

    /**
     * 新增广告成本管理
     */
    @ApiOperation("新增广告成本管理")
    @SaCheckPermission("risk:cost:add")
    @Log(title = "广告成本管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody AdvertisingCostBo bo) {
        return advertisingCostService.insertByBo(bo);
    }

    /**
     * 修改广告成本管理
     */
    @ApiOperation("修改广告成本管理")
    @SaCheckPermission("risk:cost:edit")
    @Log(title = "广告成本管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody AdvertisingCostBo bo) {
        return advertisingCostService.updateByBo(bo);
    }

    /**
     * 删除广告成本管理
     */
    @ApiOperation("删除广告成本管理")
    @SaCheckPermission("risk:cost:remove")
    @Log(title = "广告成本管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return advertisingCostService.deleteWithValidByIds(Arrays.asList(ids), true);

    }
    //endregion 广告成本管理


    //region 广告媒体

    /**
     * 查询广告媒体列表
     */
    @ApiOperation("查询广告媒体列表")
    @SaCheckPermission("risk:media:list")
    @PostMapping("/mediaList")
    public TableDataInfo<AdvertisingMediaVo> mediaList(@RequestBody AdvertisingMediaPageDto pageDto) {
        return advertisingMediaService.queryPageList(pageDto);
    }


    /**
     * 导出广告媒体列表
     */
    @ApiOperation("导出广告媒体列表")
    @SaCheckPermission("risk:media:export")
    @PostMapping("/mediaExport")
    public void mediaExport(@RequestBody AdvertisingMediaPageDto pageDto, HttpServletResponse response) {
        List<AdvertisingMediaVo> list = advertisingMediaService.queryList(pageDto);
        ExcelUtil.exportExcel(list, "广告媒体", AdvertisingMediaVo.class, response);
    }


    /**
     * 获取广告媒体详细信息
     */
    @ApiOperation("获取广告媒体详细信息")
    @SaCheckPermission("risk:media:query")
    @PostMapping("/getMediaInfo")
    public R<AdvertisingMediaVo> getMediaInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(advertisingMediaService.queryById(idDto.getId()));
    }

    /**
     * 新增广告媒体
     */
    @ApiOperation("新增广告媒体")
    @SaCheckPermission("risk:media:add")
    @Log(title = "广告媒体", businessType = BusinessType.INSERT)
    @PostMapping("/mediaAdd")
    public R mediaAdd(@Validated(AddGroup.class) @RequestBody AdvertisingMediaBo bo) {
        return toAjax(advertisingMediaService.insertByBo(bo));
    }

    @ApiOperation("根据媒体平台获取广告媒体")
    @PostMapping("/getMediaByPlatform")
    public R<List<AdvertisingMediaVo>> getMediaByPlatform(@RequestBody IdNameDto<String> idNameDto) {
        return R.ok(advertisingMediaService.getMediaByPlatform(idNameDto));
    }


    /**
     * 修改广告媒体
     */
    @ApiOperation("修改广告媒体")
    @SaCheckPermission("risk:media:edit")
    @Log(title = "广告媒体", businessType = BusinessType.UPDATE)
    @PostMapping("/mediaEdit")
    public R mediaEdit(@Validated(EditGroup.class) @RequestBody AdvertisingMediaBo bo) {
        return toAjax(advertisingMediaService.updateByBo(bo));
    }

    /**
     * 删除广告媒体
     */
    @ApiOperation("删除广告媒体")
    @SaCheckPermission("risk:media:remove")
    @Log(title = "广告媒体", businessType = BusinessType.DELETE)
    @PostMapping("/mediaRemove")
    public R mediaRemove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(advertisingMediaService.deleteWithValidByIds(Arrays.asList(ids)));

    }


    //endregion 广告媒体


}



