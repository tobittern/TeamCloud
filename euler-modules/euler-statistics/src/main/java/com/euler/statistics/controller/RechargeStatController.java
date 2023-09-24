package com.euler.statistics.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.RechargeStatPageDto;
import com.euler.statistics.domain.vo.DiyConsumptionRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRechargeStatVo;
import com.euler.statistics.domain.vo.DiyRoleRechargeStatVo;
import com.euler.statistics.service.IRechargeStatService;
import com.euler.system.api.RemoteUserService;
import com.euler.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Validated
@Api(value = "数据统计 - 充值统计控制器", tags = {"数据统计 - 充值数据统计管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/recharge")
public class RechargeStatController {

    @Autowired
    private IRechargeStatService rechargeStatService;
    @DubboReference
    private RemoteUserService remoteUserService;

    private RechargeStatPageDto setRechargeStatPageDto(RechargeStatPageDto dto) {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            dto.setChannelId(sysUser.getRegisterChannelId());
        }
        return dto;
    }

    /**
     * 查询数据统计 - 充值数据
     */
    @ApiOperation("查询数据统计 - 营收数据")
    @SaCheckPermission("stat:recharge:list")
    @PostMapping("/list")
    public TableDataInfo<DiyRechargeStatVo> list(@RequestBody RechargeStatPageDto dto) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        return rechargeStatService.queryPageList(newDto);
    }

    /**
     * 导出--充值数据
     */
    @ApiOperation("导出 - 营收数据")
    @SaCheckPermission("stat:recharge:export")
    @PostMapping("/export")
    public void export(@RequestBody RechargeStatPageDto dto, HttpServletResponse response) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        dto.setPageSize(50000);
        var res = rechargeStatService.queryPageList(newDto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "充值数据", DiyRechargeStatVo.class, response);
    }


    /**
     * 查询数据统计 - 角色数据
     */
    @ApiOperation("查询数据统计 - 角色数据")
    @SaCheckPermission("stat:recharge:roleList")
    @PostMapping("/roleList")
    public TableDataInfo<DiyRoleRechargeStatVo> roleList(@RequestBody RechargeStatPageDto dto) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        return rechargeStatService.queryRolePageList(newDto);
    }

    /**
     * 导出--角色数据
     */
    @ApiOperation("导出 - 角色数据")
    @SaCheckPermission("stat:recharge:roleExport")
    @PostMapping("/roleExport")
    public void roleExport(@RequestBody RechargeStatPageDto dto, HttpServletResponse response) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        dto.setPageSize(50000);
        var res = rechargeStatService.queryRolePageList(newDto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "角色数据", DiyRoleRechargeStatVo.class, response);
    }


    @PostMapping("/genRechargeData")
    public R genRechargeData(@RequestBody IdDto<Date> date) {
        FillDataDto fillRechargeDataDto = new FillDataDto();
        fillRechargeDataDto.setBatchNo(DateUtil.format(date.getId(), DatePattern.PURE_DATETIME_MS_PATTERN))
            .setBeginTime(DateUtil.beginOfDay(date.getId())).setEndTime(DateUtil.endOfDay(date.getId()));
        rechargeStatService.fillRechargeData(fillRechargeDataDto);
        return R.ok();

    }


    /**
     * 查询数据统计 - 游戏充值数据 - 用户在游戏里的消费记录
     */
    @ApiOperation("查询数据统计 - 游戏充值数据")
    @SaCheckPermission("stat:recharge:consumption")
    @PostMapping("/consumption")
    public TableDataInfo<DiyConsumptionRechargeStatVo> consumption(@RequestBody RechargeStatPageDto dto) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        return rechargeStatService.queryConsumptionPageList(newDto);
    }

    /**
     * 导出--充值数据
     */
    @ApiOperation("导出 - 游戏充值数据")
    @SaCheckPermission("stat:recharge:consumptionExport")
    @PostMapping("/consumptionExport")
    public void consumptionExport(@RequestBody RechargeStatPageDto dto, HttpServletResponse response) {
        RechargeStatPageDto newDto = setRechargeStatPageDto(dto);
        dto.setPageSize(50000);
        var res = rechargeStatService.queryConsumptionPageList(newDto);
        if (res != null && res.getTotal() > 0 && !res.getRows().isEmpty())
            ExcelUtil.exportExcel(res.getRows(), "游戏充值数据", DiyConsumptionRechargeStatVo.class, response);
    }


}
