package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.dto.AppleRefundOrderDto;
import com.euler.payment.domain.dto.LaunchRefundDto;
import com.euler.payment.domain.dto.RefundOrderPageDto;
import com.euler.payment.api.domain.RefundOrderVo;
import com.euler.payment.service.IRefundOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 退款订单Controller
 * 前端访问路由地址为:/payment/order
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "退款订单控制器", tags = {"退款订单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/refund")
public class RefundOrderController extends BaseController {

    private final IRefundOrderService refundOrderService;

    //region 后台方法
    /**
     * 查询退款订单列表
     */
    @ApiOperation("查询退款订单列表")
    @SaCheckPermission("payment:refundorder:list")
    @PostMapping("/list")
    public TableDataInfo<RefundOrderVo> list(@RequestBody RefundOrderPageDto orderPageDto) {
        return refundOrderService.queryPageList(orderPageDto);
    }

    /**
     * 导出退款订单列表
     */
    @ApiOperation("导出退款订单列表")
    @SaCheckPermission("payment:refundorder:export")
    @Log(title = "退款订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody RefundOrderPageDto orderPageDto, HttpServletResponse response) {
        List<RefundOrderVo> list = refundOrderService.queryList(orderPageDto);
        ExcelUtil.exportExcel(list, "退款订单", RefundOrderVo.class, response);
    }

    /**
     * 获取退款订单详细信息
     */
    @ApiOperation("获取退款订单详细信息")
    @SaCheckPermission("payment:refundorder:query")
    @PostMapping("/getInfo")
    public R<RefundOrderVo> getInfo(@RequestBody IdDto<String> idDto) {
        return R.ok(refundOrderService.queryById(idDto.getId()));
    }
    //endregion 后台方法

    /**
     * 发起退款
     */
    @ApiOperation("发起退款")
    @PostMapping("/launch")
    @SaCheckPermission("payment:refundorder:launch")
    public R launch(@RequestBody LaunchRefundDto launchRefundDto) {
        return refundOrderService.launch(launchRefundDto);
    }

    /**
     * 退款查询
     */
    @ApiOperation("退款查询")
    @PostMapping("/query")
    public R query(@RequestBody IdDto<String> idDto) {
        return R.ok(refundOrderService.refundQuery(idDto.getId()));
    }

    /**
     * 微信异步回调
     *
     * @param request
     * @param response
     */
    @ApiOperation("微信异步回调")
    @PostMapping(value = "/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        refundOrderService.wxNotify(request, response);
    }

    /**
     * 苹果内购退款回调
     */
    @ApiOperation("苹果内购退款回调")
    @PostMapping(value = "/appleNotify")
    public R appleNotify(@RequestBody AppleRefundOrderDto dto) {
        return refundOrderService.applePayRefund(dto);
    }

}
