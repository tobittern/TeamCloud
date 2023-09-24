package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.dto.OrderLogPageDto;
import com.euler.payment.domain.vo.OrderLogVo;
import com.euler.payment.service.IOrderLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单日志Controller
 * 前端访问路由地址为:/payment/log
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "订单日志控制器", tags = {"订单日志管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
public class OrderLogController extends BaseController {

    private final IOrderLogService iOrderLogService;

    /**
     * 查询订单日志列表
     */
    @ApiOperation("查询订单日志列表")
    @SaCheckPermission("payment:log:list")
    @PostMapping("/list")
    public TableDataInfo<OrderLogVo> list(@RequestBody OrderLogPageDto orderLogPageDto) {
        return iOrderLogService.queryPageList(orderLogPageDto);
    }


    /**
     * 查询订单的所有日志列表
     */
    @ApiOperation("查询订单的所有日志列表")
    @SaCheckPermission("payment:log:list")
    @PostMapping("/listall")
    public R<List<OrderLogVo>> listAll(@RequestBody IdDto<String> idDto) {
        List<OrderLogVo> list = iOrderLogService.queryListByOrderId(idDto.getId());
        return R.ok(list);
    }


    /**
     * 导出订单日志列表
     */
    @ApiOperation("导出订单日志列表")
    @SaCheckPermission("payment:log:export")
    @Log(title = "订单日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody OrderLogPageDto orderLogPageDto, HttpServletResponse response) {
        List<OrderLogVo> list = iOrderLogService.queryList(orderLogPageDto);
        ExcelUtil.exportExcel(list, "订单日志", OrderLogVo.class, response);
    }

    /**
     * 获取订单日志详细信息
     */
    @ApiOperation("获取订单日志详细信息")
    @SaCheckPermission("payment:log:query")
    @PostMapping("/getInfo")
    public R<OrderLogVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iOrderLogService.queryById(idDto.getId()));
    }

}
