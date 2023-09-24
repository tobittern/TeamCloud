package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.vo.TdDeviceBehaviorVo;
import com.euler.risk.domain.vo.TdIpBehaviorVo;
import com.euler.risk.service.IBehaviorTypeService;
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
import java.util.List;

/**
 * 后端用户行为历史Controller
 * 前端访问路由地址为:/risk/history
 *
 * @author euler
 * @date 2022-08-24
 */
@Validated
@Api(value = "后端用户行为历史控制器", tags = {"后端用户行为历史"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/history")
public class BehaviorHistoryController extends BaseController {

    @Autowired
    private IBehaviorTypeService iBehaviorTypeService;

    /**
     * 查询设备行为历史列表
     */
    @ApiOperation("查询设备行为历史列表")
    @SaCheckPermission("risk:history:deviceList")
    @PostMapping("/deviceList")
    public TableDataInfo<TdDeviceBehaviorVo> deviceList(@RequestBody DeviceSearchDto dto) {
        return iBehaviorTypeService.queryDevicePageList(dto);
    }

    /**
     * 查询ip行为历史列表
     */
    @ApiOperation("查询ip行为历史列表")
    @SaCheckPermission("risk:history:ipList")
    @PostMapping("/ipList")
    public TableDataInfo<TdIpBehaviorVo> ipList(@RequestBody IpSearchDto dto) {
        return iBehaviorTypeService.queryIpPageList(dto);
    }

    /**
     * 导出设备行为历史列表
     */
    @ApiOperation("导出设备行为历史列表")
    @SaCheckPermission("risk:history:deviceListExport")
    @PostMapping("/deviceListExport")
    public void deviceListExport(@Validated DeviceSearchDto dto, HttpServletResponse response) {
        List<TdDeviceBehaviorVo> list = iBehaviorTypeService.queryDeviceList(dto);
        ExcelUtil.exportExcel(list, "设备行为历史列表", TdDeviceBehaviorVo.class, response);
    }

    /**
     * 导出ip行为历史列表
     */
    @ApiOperation("导出ip行为历史列表")
    @SaCheckPermission("risk:history:ipListExport")
    @PostMapping("/ipListExport")
    public void ipListExport(@Validated IpSearchDto dto, HttpServletResponse response) {
        List<TdIpBehaviorVo> list = iBehaviorTypeService.queryIpList(dto);
        ExcelUtil.exportExcel(list, "ip行为历史列表", TdIpBehaviorVo.class, response);
    }

}
