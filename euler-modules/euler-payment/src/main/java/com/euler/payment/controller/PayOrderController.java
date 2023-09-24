package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.domain.dto.PayOrderPageDto;
import com.euler.payment.service.IPayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 支付订单Controller
 * 前端访问路由地址为:/payment/order
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "支付订单控制器", tags = {"支付订单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/payorder")
public class PayOrderController extends BaseController {

    private final IPayOrderService iPayOrderService;

    /**
     * 查询支付订单列表
     */
    @ApiOperation("查询支付订单列表")
    @SaCheckPermission("payment:order:list")
    @PostMapping("/list")
    public TableDataInfo<PayOrderVo> list(@RequestBody PayOrderPageDto pageQuery) {
        return iPayOrderService.queryPageList(pageQuery);
    }

    /**
     * 导出支付订单列表
     */
    @ApiOperation("导出支付订单列表")
    @SaCheckPermission("payment:order:export")
    @Log(title = "支付订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody PayOrderPageDto pageQuery, HttpServletResponse response) {
        List<PayOrderVo> list = iPayOrderService.queryList(pageQuery);
        ExcelUtil.exportExcel(list, "支付订单", PayOrderVo.class, response);
    }

    /**
     * 获取支付订单详细信息
     */
    @ApiOperation("获取支付订单详细信息")
    @SaCheckPermission("payment:order:query")
    @PostMapping("/{id}")
    public R<PayOrderVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") String id) {
        return R.ok(iPayOrderService.queryById(id));
    }



    /**
     * 删除支付订单
     */
    @ApiOperation("删除支付订单")
    @SaCheckPermission("payment:order:remove")
    @Log(title = "支付订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(iPayOrderService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
