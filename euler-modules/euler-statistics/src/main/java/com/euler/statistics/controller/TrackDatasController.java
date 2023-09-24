package com.euler.statistics.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.*;
import com.euler.statistics.domain.vo.*;
import com.euler.statistics.service.ITrackDatasService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 平台汇总统计数据Controller
 * 前端访问路由地址为:/system/datas
 *
 * @author euler
 * @date 2022-07-12
 */
@Validated
@Api(value = "平台汇总统计数据控制器", tags = {"平台汇总统计数据管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/trackDatas")
public class TrackDatasController extends BaseController {

    private final ITrackDatasService iTrackDatasService;

    @DubboReference
    private RemoteUserService remoteUserService;

    private Integer getChannelId() {
        // 根据用户判断是否需要给他设置
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            return sysUser.getRegisterChannelId();
        }
        return 0;
    }

    /**
     * 平台汇总统计数据，目前只有抖音和快手两条数据
     */
    @ApiOperation("平台汇总统计数据，目前只有抖音和快手两条数据")
    @SaCheckPermission("statistics:datas:platformList")
    @PostMapping("/platformList")
    public TableDataInfo<PlatformDatasVo> platformList(@RequestBody PlatformDatasDto dto) {
        return iTrackDatasService.queryPageList(dto);
    }

    /**
     * 平台汇总统计数据，目前只有抖音和快手两条数据
     */
    @ApiOperation("平台汇总统计数据，目前只有抖音和快手两条数据")
    @SaCheckPermission("statistics:datas:platformListExport")
    @PostMapping("/platformListExport")
    public void platformListExport(@RequestBody PlatformDatasDto dto, HttpServletResponse response) {
        dto.setPageSize(50000);
        var res = iTrackDatasService.queryPageList(dto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "平台汇总统计数据", PlatformDatasVo.class, response);
    }

    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    @ApiOperation("平台渠道计划统计数据，目前只有抖音有广告计划数据")
    @SaCheckPermission("statistics:datas:douyinChannelAidDataList")
    @PostMapping("/douyinChannelAidDataList")
    public TableDataInfo<DouyinChannelAidDatasVo> douyinChannelAidDataList(@RequestBody DouyinChannelAidDatasDto dto) {
        return iTrackDatasService.douyinChannelAidDataList(dto);
    }

    /**
     * 平台渠道计划统计数据，目前只有抖音有广告计划数据
     */
    @ApiOperation("平台渠道计划统计数据，目前只有抖音有广告计划数据")
    @SaCheckPermission("statistics:datas:douyinChannelAidDataListExport")
    @PostMapping("/douyinChannelAidDataListExport")
    public void douyinChannelAidDataListExport(@RequestBody DouyinChannelAidDatasDto dto, HttpServletResponse response) {
        dto.setPageSize(50000);
        var res = iTrackDatasService.douyinChannelAidDataList(dto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "充值数据", DouyinChannelAidDatasVo.class, response);
    }

    /**
     * 平台渠道统计数据，包含抖音和快手
     */
    @ApiOperation("平台渠道统计数据，包含抖音和快手")
    @SaCheckPermission("statistics:datas:douyinChannelDataList")
    @PostMapping("/douyinChannelDataList")
    public TableDataInfo<DouyinChannelDatasVo> douyinChannelDataList(@RequestBody DouyinChannelDatasDto dto) {
        return iTrackDatasService.douyinChannelDataList(dto);
    }


    /**
     * 平台渠道统计数据，包含抖音和快手
     */
    @ApiOperation("平台渠道统计数据，包含抖音和快手")
    @SaCheckPermission("statistics:datas:douyinChannelDataListExport")
    @PostMapping("/douyinChannelDataListExport")
    public void douyinChannelDataListExport(@RequestBody DouyinChannelDatasDto dto, HttpServletResponse response) {
        dto.setPageSize(50000);
        var res = iTrackDatasService.douyinChannelDataList(dto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "充值数据", DouyinChannelDatasVo.class, response);
    }


    /*********************** 上面的按照数据平台同学要求已经不用了 *****************************/


    /**
     * 平台渠道统计数据
     */
    @ApiOperation("平台渠道统计数据")
    @SaCheckPermission("statistics:datas:platformTongjiDatas")
    @PostMapping("/platformTongjiDatas")
    public TableDataInfo<PlatformTongjiDatasVo> platformTongjiDatas(@RequestBody PlatformTongjiDatasPageDto dto) {
        dto.setChannelId(getChannelId());
        return iTrackDatasService.platformTongjiDatas(dto);
    }

    /**
     * 平台渠道统计数据 导出
     */
    @ApiOperation("平台渠道统计数据 导出")
    @SaCheckPermission("statistics:datas:platformTongjiDatasExport")
    @PostMapping("/platformTongjiDatasExport")
    public void platformTongjiDatasExport(@RequestBody PlatformTongjiDatasPageDto dto, HttpServletResponse response) {
        dto.setPageSize(50000);
        dto.setChannelId(getChannelId());
        var res = iTrackDatasService.platformTongjiDatas(dto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "平台渠道统计数据", PlatformTongjiDatasVo.class, response);
    }


    /**
     * 广告计划统计数据
     */
    @ApiOperation("广告计划统计数据")
    @SaCheckPermission("statistics:datas:platformTongjiChannelAidDatas")
    @PostMapping("/platformTongjiChannelAidDatas")
    public TableDataInfo<PlatformTongjiChannelAidDatasVo> platformTongjiChannelAidDatas(@RequestBody PlatformTongjiChannelAidDatasPageDto dto) {
        dto.setChannelId(getChannelId());
        return iTrackDatasService.platformTongjiChannelAidDatas(dto);
    }

    /**
     * 平台渠道统计数据 导出
     */
    @ApiOperation("广告计划统计数据 导出")
    @SaCheckPermission("statistics:datas:platformTongjiChannelAidDatasExport")
    @PostMapping("/platformTongjiChannelAidDatasExport")
    public void platformTongjiChannelAidDatasExport(@RequestBody PlatformTongjiChannelAidDatasPageDto dto, HttpServletResponse response) {
        dto.setPageSize(50000);
        dto.setChannelId(getChannelId());
        var res = iTrackDatasService.platformTongjiChannelAidDatas(dto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "广告计划统计数据", PlatformTongjiChannelAidDatasVo.class, response);
    }

}
