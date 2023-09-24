package com.euler.statistics.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.BeanCopyUtils;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.KeepDataStatisticsDto;
import com.euler.statistics.domain.dto.PaymentKeepStatisticsDto;
import com.euler.statistics.domain.vo.ExportKeepDataStatisticsVo;
import com.euler.statistics.domain.vo.ExportPaymentKeepStatisticsVo;
import com.euler.statistics.domain.vo.KeepDataStatisticsVo;
import com.euler.statistics.domain.vo.PaymentKeepStatisticsVo;
import com.euler.statistics.service.IKeepDataStatisticsService;
import com.euler.statistics.service.IPaymentKeepStatisticsService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 留存数据统计Controller
 *
 * @author euler
 * @date 2022-04-27
 */
@Validated
@Api(value = "留存数据统计控制器", tags = {"留存数据统计管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/keepData")
public class KeepDataStatisticsController extends BaseController {

    private final IKeepDataStatisticsService iKeepStatisticsService;
    @DubboReference
    private RemoteUserService remoteUserService;
    @Autowired
    private IPaymentKeepStatisticsService iPaymentKeepStatService;

    private KeepDataStatisticsDto setKeepDataStatisticsDto(KeepDataStatisticsDto dto) {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            dto.setChannelId(sysUser.getRegisterChannelId());
        }
        return dto;
    }

    /**
     * 查询留存数据统计列表
     */
    @ApiOperation("查询留存数据统计列表")
    @SaCheckPermission("stat:keepData:list")
    @PostMapping("/list")
    public TableDataInfo<KeepDataStatisticsVo> list(@RequestBody KeepDataStatisticsDto dto) {
        KeepDataStatisticsDto newDto = setKeepDataStatisticsDto(dto);
        return iKeepStatisticsService.queryPageList(newDto);
    }

    /**
     * 填充留存数据到同一个基础表
     *
     * @param date 日期
     * @return
     */
    @ApiOperation("填充留存数据到同一个基础表")
    @PostMapping("/genKeepData")
    public R genKeepData(@RequestBody IdDto<Date> date) {
        FillDataDto dto = new FillDataDto();
        dto.setBatchNo(DateUtil.format(date.getId(), DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date.getId())).setEndTime(DateUtil.endOfDay(date.getId()));
        iKeepStatisticsService.fillKeepData(dto);
        return R.ok();
    }

    /**
     * 导出留存数据统计列表
     */
    @ApiOperation("导出留存数据统计列表")
    @Log(title = "留存数据统计", businessType = BusinessType.EXPORT)
    @SaCheckPermission("stat:keepData:export")
    @PostMapping("/export")
    public void export(@RequestBody KeepDataStatisticsDto dto, HttpServletResponse response) {
        KeepDataStatisticsDto newDto = setKeepDataStatisticsDto(dto);
        // 设置一页最多导出记录数
        newDto.setPageSize(50000);
        List<KeepDataStatisticsVo> list = iKeepStatisticsService.queryList(newDto);
        // 留存数据导出列表
        List<ExportKeepDataStatisticsVo> exportList = BeanCopyUtils.copyList(list, ExportKeepDataStatisticsVo.class);
        if (exportList != null && exportList.size() > 0 && !exportList.isEmpty())
            ExcelUtil.exportExcel(exportList, "留存数据统计", ExportKeepDataStatisticsVo.class, response);
    }

    /**
     * 查询单日留存数据
     */
    @ApiOperation("查询单日留存数据统计列表")
    @SaCheckPermission("stat:keepData:singleList")
    @PostMapping("/singleList")
    public TableDataInfo<KeepDataStatisticsVo> singleList(@RequestBody KeepDataStatisticsDto dto) {
        KeepDataStatisticsDto newDto = setKeepDataStatisticsDto(dto);
        newDto.setSingleFlag(true);
        return iKeepStatisticsService.queryPageList(newDto);
    }

    /**
     * 导出单日留存数据统计列表
     */
    @ApiOperation("导出单日留存数据统计列表")
    @Log(title = "单日留存数据统计", businessType = BusinessType.EXPORT)
    @SaCheckPermission("stat:keepData:singleExport")
    @PostMapping("/singleExport")
    public void singleExport(@RequestBody KeepDataStatisticsDto dto, HttpServletResponse response) {
        KeepDataStatisticsDto newDto = setKeepDataStatisticsDto(dto);
        // 设置一页最多导出记录数
        newDto.setPageSize(50000);
        newDto.setSingleFlag(true);
        List<KeepDataStatisticsVo> list = iKeepStatisticsService.queryList(newDto);
        // 留存数据导出列表
        List<ExportKeepDataStatisticsVo> exportList = BeanCopyUtils.copyList(list, ExportKeepDataStatisticsVo.class);
        if (exportList != null && exportList.size() > 0 && !exportList.isEmpty())
            ExcelUtil.exportExcel(exportList, "单日留存数据统计", ExportKeepDataStatisticsVo.class, response);
    }

    private PaymentKeepStatisticsDto setPaymentKeepStatDto(PaymentKeepStatisticsDto dto) {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            dto.setChannelId(sysUser.getRegisterChannelId());
        }
        return dto;
    }

    /**
     * 查询付费留存数据统计列表
     */
    @ApiOperation("查询付费留存数据统计列表")
    @SaCheckPermission("stat:keepData:paymentKeepList")
    @PostMapping("/paymentKeepList")
    public TableDataInfo<PaymentKeepStatisticsVo> paymentKeepList(@RequestBody PaymentKeepStatisticsDto dto) {
        PaymentKeepStatisticsDto newDto = setPaymentKeepStatDto(dto);
        return iPaymentKeepStatService.queryPaymentPageList(newDto);
    }

    /**
     * 导出付费留存数据统计列表
     */
    @ApiOperation("导出付费留存数据统计列表")
    @Log(title = "付费留存数据统计", businessType = BusinessType.EXPORT)
    @SaCheckPermission("stat:keepData:paymentKeepExport")
    @PostMapping("/paymentKeepExport")
    public void paymentKeepExport(@RequestBody PaymentKeepStatisticsDto dto, HttpServletResponse response) {
        PaymentKeepStatisticsDto newDto = setPaymentKeepStatDto(dto);
        // 设置一页最多导出记录数
        newDto.setPageSize(50000);
        List<PaymentKeepStatisticsVo> list = iPaymentKeepStatService.queryPaymentList(newDto);
        // 付费留存数据导出列表
        List<ExportPaymentKeepStatisticsVo> exportList = BeanCopyUtils.copyList(list, ExportPaymentKeepStatisticsVo.class);
        if (exportList != null && exportList.size() > 0 && !exportList.isEmpty())
            ExcelUtil.exportExcel(exportList, "付费留存数据统计", ExportPaymentKeepStatisticsVo.class, response);
    }

}
