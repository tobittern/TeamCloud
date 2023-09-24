package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.HistorySearchBo;
import com.euler.community.domain.dto.HistorySearchDto;
import com.euler.community.domain.vo.HistorySearchVo;
import com.euler.community.service.IHistorySearchService;
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
 * 搜索历史Controller
 * 前端访问路由地址为:/system/search
 * @author euler
 *  2022-06-07
 */
@Validated
@Api(value = "搜索历史控制器", tags = {"搜索历史管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class HistorySearchController extends BaseController {

    private final IHistorySearchService iHistorySearchService;

    /**
     * 查询搜索历史列表
     */
    @ApiOperation("查询搜索历史列表")
    @PostMapping("/list")
    public TableDataInfo<HistorySearchVo> list(@RequestBody HistorySearchDto historySearchDto) {
        historySearchDto.setMemberId(LoginHelper.getUserId());
        return iHistorySearchService.queryPageList(historySearchDto);
    }

    /**
     * 导出搜索历史列表
     */
    @ApiOperation("导出搜索历史列表")
    @SaCheckPermission("community:search:export")
    @Log(title = "搜索历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated HistorySearchDto historySearchDto, HttpServletResponse response) {
        List<HistorySearchVo> list = iHistorySearchService.queryList(historySearchDto);
        ExcelUtil.exportExcel(list, "搜索历史", HistorySearchVo.class, response);
    }

    /**
     * 获取搜索历史详细信息
     */
    @ApiOperation("获取搜索历史详细信息")
    @SaCheckPermission("community:search:query")
    @PostMapping("/query")
    public R<HistorySearchVo> getInfo(@RequestBody  HistorySearchDto historySearchDto) {
        return R.ok(iHistorySearchService.queryById(historySearchDto.getId()));
    }

    /**
     * 新增搜索历史
     */
    @ApiOperation("新增搜索历史")
    @Log(title = "搜索历史", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@RequestBody HistorySearchBo bo) {
        return toAjax(iHistorySearchService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改搜索历史
     */
    @ApiOperation("修改搜索历史")
    @SaCheckPermission("community:search:edit")
    @Log(title = "搜索历史", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HistorySearchBo bo) {
        return toAjax(iHistorySearchService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除搜索历史
     */
    @ApiOperation("删除搜索历史")
    @SaCheckPermission("community:search:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iHistorySearchService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 清空搜索历史
     */
    @ApiOperation("清空搜索历史")
    @PostMapping("/clear")
    public R clear(@RequestBody IdTypeDto<Integer, Integer> dto) {
        return toAjax(iHistorySearchService.clearByUserId(dto));
    }

    /**
     * 搜索内容（首页、发现页功能）
     * @param bo 历史搜索
     */
    @ApiOperation("搜索具体内容")
    @PostMapping("/search")
    public R<Object> search(@RequestBody HistorySearchDto bo) {
        try{
            bo.setMemberId(LoginHelper.getUserIdOther());
            return iHistorySearchService.search(bo);
        }catch (Exception e){
            return R.fail(e.getMessage());
        }
    }

}
