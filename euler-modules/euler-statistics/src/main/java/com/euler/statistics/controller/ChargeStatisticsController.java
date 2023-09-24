package com.euler.statistics.controller;

import cn.hutool.core.util.ObjectUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameDto;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.ChargeStatisticsDto;
import com.euler.statistics.domain.vo.ChargeStatisticsVo;
import com.euler.statistics.service.IChargeStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 开放平台充值金额统计Controller
 * 前端访问路由地址为:/statistics/charge
 * @author euler
 *  2022-07-13
 */
@Slf4j
@Validated
@Api(value = "开放平台充值金额统计控制器", tags = {"开放平台充值金额统计管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/charge")
public class ChargeStatisticsController extends BaseController {

    private final IChargeStatisticsService iChargeStatisticsService;

    /**
     * 查询开放平台充值金额统计列表
     */
    @ApiOperation("查询开放平台充值金额统计列表")
    @PostMapping("/list")
    public TableDataInfo<ChargeStatisticsVo> list(@RequestBody ChargeStatisticsDto dto) {
        return iChargeStatisticsService.queryChargeStatPageList(dto);
    }

    /**
     * 导出开放平台充值金额统计列表
     */
    @ApiOperation("导出开放平台充值金额统计列表")
    @Log(title = "开放平台充值金额统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody  ChargeStatisticsDto bo, HttpServletResponse response) {
        List<ChargeStatisticsVo> list = iChargeStatisticsService.queryChargeStatList(bo);
        ExcelUtil.exportExcel(list, "开放平台充值金额统计", ChargeStatisticsVo.class, response);
    }

    /**
     * 开放平台充值金额的变化率详情
     */
    @ApiOperation("开放平台充值金额的变化率详情")
    @PostMapping("/rateInfo")
    public R rateInfo(@RequestBody ChargeStatisticsDto dto){
        return iChargeStatisticsService.queryChargeStatInfo(dto);
    }

    /**
     * 根据用户id查询出审核过，在线的游戏列表
     */
    @ApiOperation("根据游戏名查询出指定游戏列表")
    @PostMapping("/getGameListByUserId")
    public R getGameListByUserId() {
        IdDto<Long> idDto = new IdDto<>();
        if (LoginHelper.isLogin()) {
            idDto.setId(LoginHelper.getUserId());
            return iChargeStatisticsService.getGameListByUserId(idDto);
        } else {
            return R.fail("用户还没有登录");
        }
    }

    /**
     * 根据游戏id，区服名称查询游戏区服列表
     */
    @ApiOperation("根据区服名称查询游戏区服列表")
    @PostMapping("/getServerListByName")
    public R getServerListByName(@RequestBody IdNameDto<Long> idNameDto) {
        if (ObjectUtil.isNull(idNameDto.getId())) {
            return R.fail("游戏id不能为空");
        }
        return iChargeStatisticsService.getServerListByName(idNameDto);
    }

}
