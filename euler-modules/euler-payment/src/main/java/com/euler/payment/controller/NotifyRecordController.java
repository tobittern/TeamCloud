package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.dto.NotifyRecordPageDto;
import com.euler.payment.api.domain.NotifyRecordVo;
import com.euler.payment.service.INotifyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通知记录Controller
 * 前端访问路由地址为:/payment/record
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "通知记录控制器", tags = {"通知记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifyrecord")
public class NotifyRecordController extends BaseController {

    private final INotifyRecordService iNotifyRecordService;

    /**
     * 查询通知记录列表
     */
    @ApiOperation("查询通知记录列表")
    @SaCheckPermission("payment:record:list")
    @PostMapping("/list")
    public TableDataInfo<NotifyRecordVo> list(@RequestBody NotifyRecordPageDto notifyRecordPageDto) {
        return iNotifyRecordService.queryPageList(notifyRecordPageDto);
    }

    /**
     * 导出通知记录列表
     */
    @ApiOperation("导出通知记录列表")
    @SaCheckPermission("payment:record:export")
    @Log(title = "通知记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody NotifyRecordPageDto notifyRecordPageDto, HttpServletResponse response) {
        List<NotifyRecordVo> list = iNotifyRecordService.queryList(notifyRecordPageDto);
        ExcelUtil.exportExcel(list, "通知记录", NotifyRecordVo.class, response);
    }

}
