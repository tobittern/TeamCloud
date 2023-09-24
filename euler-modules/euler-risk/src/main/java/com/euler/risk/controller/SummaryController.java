package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.dto.DeviceSearchDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.vo.TfDeviceSummaryVo;
import com.euler.risk.domain.vo.TfIpSummaryVo;
import com.euler.risk.service.ITfDeviceSummaryService;
import com.euler.risk.service.ITfIpSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备数据汇总Controller
 * 前端访问路由地址为:/risk/summary
 * @author euler
 * @date 2022-08-24
 */
@Validated
@Api(value = "设备数据汇总控制器" , tags = {"设备数据汇总管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/summary")
public class SummaryController extends BaseController {

    @Autowired
    private ITfDeviceSummaryService deviceSummaryService;

    @Autowired
    private ITfIpSummaryService ipSummaryService;

    /**
     * 查询设备行为异常预警列表
     */
    @ApiOperation("查询设备行为异常预警列表")
    @SaCheckPermission("risk:summary:deviceList")
    @PostMapping("/deviceList")
    public TableDataInfo<TfDeviceSummaryVo> deviceList(@RequestBody DeviceSearchDto dto) {
        return deviceSummaryService.queryPageList(dto);
    }

    /**
     * 查询ip行为异常预警列表
     */
    @ApiOperation("查询ip行为异常预警列表")
    @SaCheckPermission("risk:summary:ipList")
    @PostMapping("/ipList")
    public TableDataInfo<TfIpSummaryVo> ipList(@RequestBody IpSearchDto dto) {
        return ipSummaryService.queryPageList(dto);
    }

}
