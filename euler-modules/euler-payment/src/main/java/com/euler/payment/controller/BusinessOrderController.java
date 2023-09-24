package com.euler.payment.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.*;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.domain.dto.*;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 订单Controller
 * 前端访问路由地址为:/payment/order
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "订单控制器", tags = {"订单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
@Slf4j
public class BusinessOrderController extends BaseController {

    private final IBusinessOrderService orderService;

    @DubboReference
    private RemoteUserService remoteUserService;

    private Integer getChannelIdByUser() {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            return sysUser.getRegisterChannelId();
        }
        return 0;
    }

    //region 后台方法
    /**
     * 查询订单列表
     */
    @ApiOperation("查询订单列表")
    @SaCheckPermission("payment:order:list")
    @PostMapping("/list")
    public TableDataInfo<BusinessOrderVo> list(@RequestBody BusinessOrderPageDto businessOrderPageDto) {
        Integer channelIdByUser = getChannelIdByUser();
        businessOrderPageDto.setChannelId(channelIdByUser);
        return orderService.queryPageList(businessOrderPageDto);
    }

    /**
     * 导出订单列表
     */
    @ApiOperation("导出订单列表")
    @SaCheckPermission("payment:order:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody BusinessOrderPageDto businessOrderPageDto, HttpServletResponse response) {
        Integer channelIdByUser = getChannelIdByUser();
        businessOrderPageDto.setChannelId(channelIdByUser);
        List<BusinessOrderVo> list = orderService.queryList(businessOrderPageDto);
        ExcelUtil.exportExcel(list, "订单", BusinessOrderVo.class, response);
    }

    /**
     * 获取订单详细信息
     */
    @ApiOperation("获取订单详细信息")
    @SaCheckPermission("payment:order:query")
    @PostMapping("/getInfo")
    public R<BusinessOrderVo> getInfo(@RequestBody IdDto<String> idDto) {
        return R.ok(orderService.queryById(idDto.getId()));
    }

    /**
     * 删除订单
     */
    @ApiOperation("删除订单")
    @SaCheckPermission("payment:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R<Void> remove(@RequestBody IdDto<String> idDto) {
        return null;
    }

    @ApiOperation("补单")
    @PostMapping("/supplyOrder")
    @SaCheckPermission("payment:order:supplyOrder")
    public R supplyOrder(@RequestBody IdDto<String> idDto) {
        String opUser = LoginHelper.getUsername();
        String opContent = StringUtils.format("由运营人员{}发起订单补单", opUser);
        return orderService.supplyOrder(idDto.getId(), opUser, opContent,false);
    }
    //endregion 后台方法

    //region 业务方法
    @ApiOperation("我的订单列表")
    @PostMapping("/myList")
    public TableDataInfo<BusinessOrderVo> myList(@RequestBody BusinessOrderPageDto businessOrderPageDto) {
        businessOrderPageDto.setMemberId(LoginHelper.getUserId());
        return orderService.queryPageList(businessOrderPageDto);
    }
    //endregion

    //region 前端支付
    /**
     * 统一下单
     */
    @ApiOperation("统一下单")
    @PostMapping("/unifiedOrder")
    public R unifiedOrder(@RequestBody UnifiedOrderDto unifiedOrderDto) {
        var orderRes = orderService.unifiedOrder(unifiedOrderDto);
        if (orderRes != null && orderRes.success())
            return R.ok(orderRes.value());
        return R.fail("下单失败");
    }

    /**
     * 统一下单
     */
    @ApiOperation("统一下单2")
    @PostMapping("/unifiedOrder2")
    public R unifiedOrder2(@RequestBody UnifiedOrderDto unifiedOrderDto) {
        var orderRes = orderService.unifiedOrder(unifiedOrderDto);
        UnifiedOrderResultDto resultDto = new UnifiedOrderResultDto();

        if (orderRes != null) {
            resultDto.setBusinessOrderId(orderRes.businessOrderId());
            resultDto.setSuccess(orderRes.success());
            resultDto.setOrderInfo(Convert.toStr(orderRes.value(), ""));
            return R.ok(resultDto);
        }
        return R.fail("下单失败");
    }

    /**
     * 查询订单状态
     */
    @ApiOperation("查询订单状态")
    @PostMapping("/queryOrder")
    public R queryOrder(@RequestBody IdDto<String> idDto) {
        return R.ok(orderService.queryPayOrderState(idDto.getId()));
    }

    /**
     * 前端查询订单状态
     */
    @ApiOperation("前端查询订单状态")
    @PostMapping("/query")
    public R<FrontQueryResultDto> query(@RequestBody IdDto<String> idDto) {
        return R.ok(orderService.frontQueryOrderState(idDto.getId()));
    }

    /**
     * 关闭订单
     */
    @ApiOperation("关闭订单")
    @PostMapping("/close")
    public R closeOrder() {
        return R.ok();
    }

    /**
     * 同步通知
     */
    @ApiOperation("同步通知")
    @PostMapping("/aliReturn")
    public R doAliReturn(HttpServletRequest request, HttpServletResponse response) {
        return R.ok();
    }

    /**
     * 支付宝异步回调
     */
    @ApiOperation("支付宝异步回调")
    @PostMapping("/aliNotify")
    public void doAliNotify(HttpServletRequest request, HttpServletResponse response) {
        orderService.orderAliNotify(request, response);
    }

    /**
     * 支付宝异步回调
     */
    @ApiOperation("支付宝异步回调")
    @PostMapping("/testPost")
    public void testPost(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (params == null || params.isEmpty()) {
            String params2 = ServletUtils.getParas();
            log.info("支付宝异步回调原始参数2：{}", params2);
            params = HttpClientUtil.StringToMap(params2);
        }
        log.info("支付宝异步回调--参数：{}", JsonUtils.toJsonString(params));
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
        orderService.orderWxNotify(request, response);
    }

    /**
     * 苹果内购回调
     */
    @ApiOperation("苹果内购回调")
    @PostMapping(value = "/applePayOrder")
    public R applePayOrder(@RequestBody ApplePayDto dto) {
        return orderService.orderApplePay(dto);
    }
    //endregion 前端支付

}
