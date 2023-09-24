package com.euler.risk.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.risk.domain.dto.HeartBeatDto;
import com.euler.risk.service.IHeartBeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设心跳包控制器Controller
 * 前端访问路由地址为:/risk/heartbeat
 *
 * @author euler
 * @date 2022-08-24
 */
@Validated
@Api(value = "心跳包控制器", tags = {"心跳包控制器"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/heartbeat")
public class HeartBeatController extends BaseController {

    private final IHeartBeatService heartBeatService;

    /**
     * 心跳检测数据收集
     */
    @ApiOperation("心跳检测数据收集")
    @PostMapping("/check")
    public R check(@RequestBody HeartBeatDto dto) {
        return heartBeatService.heartJumpCollection(dto);
    }

}
