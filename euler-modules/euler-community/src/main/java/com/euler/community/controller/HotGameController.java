package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.HotGameBo;
import com.euler.community.domain.dto.HotGameDto;
import com.euler.community.domain.vo.HotGameVo;
import com.euler.community.service.IHotGameService;
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
 * 前端访问路由地址为:/system/game
 *
 * @author euler
 * 2022-06-17
 */
@Validated
@Api(value = "控制器", tags = {"管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/hotGame")
public class HotGameController extends BaseController {

    private final IHotGameService iHotGameService;

    /**
     * 查询列表
     */
    @ApiOperation("查询列表")
    @SaCheckPermission("community:game:list")
    @PostMapping("/list")
    public TableDataInfo<HotGameVo> list(@Validated(QueryGroup.class) @RequestBody HotGameDto hotGameDto) {
        return iHotGameService.queryPageList(hotGameDto);
    }

    /**
     * 导出列表
     */
    @ApiOperation("导出列表")
    @SaCheckPermission("community:game:export")
    @Log(title = "导出列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated HotGameDto hotGameDto, HttpServletResponse response) {
        List<HotGameVo> list = iHotGameService.queryList(hotGameDto);
        ExcelUtil.exportExcel(list, "", HotGameVo.class, response);
    }

    /**
     * 获取详细信息
     */
    @ApiOperation("获取详细信息")
    @SaCheckPermission("community:game:query")
    @PostMapping("/getInfo")
    public R<HotGameVo> getInfo(@RequestBody HotGameDto hotGameDto) {
        return R.ok(iHotGameService.queryById(hotGameDto.getId()));
    }

    /**
     * 新增
     */
    @ApiOperation("新增")
    @SaCheckPermission("community:game:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@RequestBody HotGameBo bo) {
        bo.setMemberId(LoginHelper.getUserId());
        return toAjax(iHotGameService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @SaCheckPermission("community:game:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R edit(@Validated(EditGroup.class) @RequestBody HotGameBo bo) {
        return toAjax(iHotGameService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @SaCheckPermission("community:game:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public R remove(@RequestBody HotGameDto hotGameDto) {
        return toAjax(iHotGameService.deleteWithValidByIds(Arrays.asList(hotGameDto.getIds()), true) ? 1 : 0);
    }

    /**
     * 获取热门游戏
     * 取出10条数据
     */
    @ApiOperation("获取热门游戏")
    @PostMapping("/getHotGames")
    public R getHotGames(@RequestBody HotGameBo hotGameBo) {
        String device = ServletUtils.getHeader(ServletUtils.getRequest(), "device");
        if (StringUtils.isEmpty(device)) {
            hotGameBo.setOperationPlatform(1);
        } else {
            hotGameBo.setOperationPlatform(Convert.toInt(device));
        }
        return iHotGameService.getHotGames(hotGameBo);
    }
}
