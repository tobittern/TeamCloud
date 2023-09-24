package com.euler.statistics.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.BasicStatisticsDto;
import com.euler.statistics.domain.vo.BasicStatisticsReturnDataVo;
import com.euler.statistics.domain.vo.BasicStatisticsReturnVo;
import com.euler.statistics.domain.vo.UserRegisterBaseDataVo;
import com.euler.statistics.domain.vo.UserRegisterBaseReturnDataVo;
import com.euler.statistics.service.IBasicStatisticsService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据统计 - 每日的ltv基础数据统计Controller
 *
 * @author euler
 * @date 2022-04-27
 */
@Slf4j
@Validated
@Api(value = "数据统计 - 每日的ltv基础数据统计控制器", tags = {"数据统计 - 每日的ltv基础数据统计管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/ltvBaseData")
public class BasicStatisticsController extends BaseController {
    @Autowired
    private IBasicStatisticsService iBasicStatisticsService;

    @DubboReference
    private RemoteUserService remoteUserService;

    private BasicStatisticsDto setBasicStatisticsDto(BasicStatisticsDto dto) {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            dto.setChannelId(sysUser.getRegisterChannelId());
        }
        return dto;
    }

    /**
     * 查询数据统计 - 每日的ltv基础数据统计列表
     */
    @ApiOperation("汇总数据查询")
    @PostMapping("/summary")
    public R summary(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        log.info("newDto:{}", JsonUtils.toJsonString(newDto));
        List<BasicStatisticsReturnVo> summary = iBasicStatisticsService.Summary(newDto, 1);
        return R.ok(summary);
    }

    /**
     * 单日LTV数据查询
     */
    @ApiOperation("单日LTV数据查询")
    @SaCheckPermission("basic:ltvBaseData:summaryList")
    @PostMapping("/summaryList")
    public R summaryList(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.summaryList(newDto);
        return R.ok(basicStatisticsReturnDataVo);
    }

    /**
     * 导出单日LTV数据查询列表
     */
    @ApiOperation("导出单日LTV数据查询列表")
    @Log(title = "导出单日LTV数据查询列表", businessType = BusinessType.EXPORT)
    @SaCheckPermission("basic:ltvBaseData:summaryListExport")
    @PostMapping("/summaryListExport")
    public void summaryListExport(@RequestBody BasicStatisticsDto dto, HttpServletResponse response) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        // 设置分页 设置时间
        dto.setPageNum(1);
        dto.setPageSize(50000);
        List<BasicStatisticsReturnVo> list = iBasicStatisticsService.Summary(newDto, 1);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.summaryList(newDto);
        List<BasicStatisticsReturnVo> rows = basicStatisticsReturnDataVo.getRows();
        // 数据累加到一块
        list.addAll(rows);
        ExcelUtil.exportExcel(list, "单日LTV数据查询列表", BasicStatisticsReturnVo.class, response);
    }

    /**
     * 查询数据统计 - 每日的ltv基础数据统计列表
     */
    @ApiOperation("查询数据统计 - 每日的ltv基础数据统计列表")
    @SaCheckPermission("basic:ltvBaseData:getList")
    @PostMapping("/getList")
    public R getList(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.queryPageList(newDto, 1);
        return R.ok(basicStatisticsReturnDataVo);
    }

    /**
     * 导出LTV数据查询列表
     */
    @ApiOperation("导出LTV数据查询列表")
    @Log(title = "导出LTV数据查询列表", businessType = BusinessType.EXPORT)
    @SaCheckPermission("basic:ltvBaseData:getListExport")
    @PostMapping("/getListExport")
    public void getListExport(@RequestBody BasicStatisticsDto dto, HttpServletResponse response) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        // 设置分页 设置时间
        dto.setPageNum(1);
        dto.setPageSize(50000);
        List<BasicStatisticsReturnVo> list = iBasicStatisticsService.Summary(newDto, 1);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.queryPageList(newDto, 1);
        List<BasicStatisticsReturnVo> rows = basicStatisticsReturnDataVo.getRows();
        // 数据累加到一块
        list.addAll(rows);
        ExcelUtil.exportExcel(list, "LTV数据查询列表", BasicStatisticsReturnVo.class, response);
    }

    /**
     * 查询数据统计 - 每日的ltv基础数据统计列表
     */
    @ApiOperation("统计昨日数据添加到mysql中")
    @PostMapping("/into")
    public void into() {
        iBasicStatisticsService.getDataIntoMysql();
    }

    /**
     * 当天数据的汇总
     */
    @ApiOperation("汇总数据查询")
    @PostMapping("/todaySummary")
    public R todaySummary(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        List<BasicStatisticsReturnVo> summary = iBasicStatisticsService.Summary(newDto, 2);
        return R.ok(summary);
    }

    /**
     * 每日的
     */
    @ApiOperation("查询数据统计 - 只查询当天的")
    @SaCheckPermission("basic:ltvBaseData:todayList")
    @PostMapping("/todayList")
    public R todayList(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.queryPageList(newDto, 2);
        return R.ok(basicStatisticsReturnDataVo);
    }

    /**
     * 导出每日LTV数据查询列表
     */
    @ApiOperation("导出每日LTV数据查询列表")
    @Log(title = "导出每日LTV数据查询列表", businessType = BusinessType.EXPORT)
    @SaCheckPermission("basic:ltvBaseData:todayListExport")
    @PostMapping("/todayListExport")
    public void todayListExport(@RequestBody BasicStatisticsDto dto, HttpServletResponse response) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        // 设置分页 设置时间
        dto.setPageNum(1);
        dto.setPageSize(50000);
        List<BasicStatisticsReturnVo> list = iBasicStatisticsService.Summary(newDto, 2);
        BasicStatisticsReturnDataVo basicStatisticsReturnDataVo = iBasicStatisticsService.queryPageList(newDto, 2);
        List<BasicStatisticsReturnVo> rows = basicStatisticsReturnDataVo.getRows();
        // 数据累加到一块
        list.addAll(rows);
        ExcelUtil.exportExcel(list, "每日LTV数据查询列表", BasicStatisticsReturnVo.class, response);
    }


    /**
     * 注册数据
     */
    @ApiOperation("注册数据")
    @SaCheckPermission("basic:ltvBaseData:registerList")
    @PostMapping("/registerList")
    public R registerList(@RequestBody BasicStatisticsDto dto) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        UserRegisterBaseReturnDataVo userRegisterBaseReturnDataVo = iBasicStatisticsService.registerList(newDto);
        return R.ok(userRegisterBaseReturnDataVo);
    }

    /**
     * 导出注册数据列表
     */
    @ApiOperation("导出注册数据列表")
    @Log(title = "导出注册数据列表", businessType = BusinessType.EXPORT)
    @SaCheckPermission("basic:ltvBaseData:registerListExport")
    @PostMapping("/registerListExport")
    public void registerListExport(@RequestBody BasicStatisticsDto dto, HttpServletResponse response) {
        BasicStatisticsDto newDto = setBasicStatisticsDto(dto);
        // 设置分页 设置时间
        dto.setPageNum(1);
        dto.setPageSize(50000);
        UserRegisterBaseReturnDataVo userRegisterBaseReturnDataVo = iBasicStatisticsService.registerList(newDto);
        List<UserRegisterBaseDataVo> list = userRegisterBaseReturnDataVo.getRows();
        ExcelUtil.exportExcel(list, "注册数据列表", UserRegisterBaseDataVo.class, response);
    }

    /**
     * 根据游戏名查询出指定游戏列表
     */
    @ApiOperation("根据游戏名查询出指定游戏列表")
    @PostMapping("/getGameListByName")
    public R getGameListByName(@RequestBody IdNameTypeDicDto dto) {
        return iBasicStatisticsService.getGameListByName(dto);
    }

}
