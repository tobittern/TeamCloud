package com.euler.statistics.controller;

import com.euler.common.core.domain.R;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.statistics.domain.dto.SummaryQueryDto;
import com.euler.statistics.domain.vo.LineChartVo;
import com.euler.statistics.domain.vo.SummaryIndexVo;
import com.euler.statistics.service.ITdMeasureService;
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

import java.util.List;

@Validated
@Api(value = "数据统计 - 指维度", tags = {"数据统计 - 指维度"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/measure")
public class MeasureController {

    @Autowired
    private ITdMeasureService measureService;
    @DubboReference
    private RemoteUserService remoteUserService;

    private SummaryQueryDto setStatChannelDto(SummaryQueryDto dto) {
        // 根据用户判断是否需要给他设置查询指定渠道的数据
        Long userId = LoginHelper.getUserId();
        SysUser sysUser = remoteUserService.selectUserById(userId);
        if (sysUser != null && sysUser.getRegisterChannelId() != null && sysUser.getRegisterChannelId() != 0) {
            dto.setChannelId(sysUser.getRegisterChannelId());
        }
        return dto;
    }

    /**
     * 数据统计 - 后台首页统计
     */
    @ApiOperation("数据统计 - 后台首页统计")
    @PostMapping("/indexSummary")
    public R<SummaryIndexVo> indexSummary(@RequestBody SummaryQueryDto dto) {
        var vo = measureService.getSummaryIndex(setStatChannelDto(dto));
        return R.ok(vo);
    }


    /**
     * 数据统计 - 后台首页统计折线
     */
    @ApiOperation("数据统计 - 后台首页统计折线")
    @PostMapping("/indexLineChart")
    public R<List<LineChartVo>> indexLineChart(@RequestBody SummaryQueryDto dto) {
        var vo = measureService.getIndexLineChart(setStatChannelDto(dto));
        return R.ok(vo);
    }


}
