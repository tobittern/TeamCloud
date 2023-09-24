package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.bo.StatisticsChargeBo;
import com.euler.sdk.domain.dto.StatisticsChargeDto;
import com.euler.sdk.domain.vo.StatisticsChargeVo;
import com.euler.sdk.service.IStatisticsChargeService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Controller
 * 前端访问路由地址为:/system/charge
 *
 * @author euler
 *  2022-07-06
 */
@Slf4j
@Validated
@Api(value = "控制器", tags = {"管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    private final IStatisticsChargeService iStatisticsChargeService;

    @DubboReference
    private RemoteUserService remoteUserService;


    /**
     * 初始化指定日期数据
     * @param bo 入参，指定日期
     * @return 数据
     */
    @ApiOperation("初始化指定日期数据")
    @PostMapping("/insertDataSummary")
    public R<Object> getStatisticsData(@RequestBody StatisticsChargeDto bo) {
        String date = bo.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            iStatisticsChargeService.insertDataSummary(StringUtils.isBlank(date)?null:dateFormat.parse(date));
            return R.ok();
        } catch (Exception e) {
            log.error("获取数据异常:{}",e.getMessage());
            return R.fail("获取数据异常");
        }
    }


    /**
     * 初始化近30天数据
     * @return 数据
     */
    @ApiOperation("初始化指定日期数据")
    @PostMapping("/init30DayData")
    public R<Object> init30DayData() {
            return R.ok(iStatisticsChargeService.init30DayData());
    }

    /**
     *
     * @return 汇总数据
     */
    @ApiOperation("查询汇总后数据")
    @PostMapping("/getStatisticsInfo")
    public R<Object> getStatisticsInfo() {
        Integer channelId=0;
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
             channelId=sysUser.getRegisterChannelId();
        }
        return R.ok(iStatisticsChargeService.getStatisticsInfo(channelId));
    }
}
