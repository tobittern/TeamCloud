package com.euler.risk.controller;

import com.euler.common.core.web.controller.BaseController;
import com.euler.risk.service.IBehaviorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后端用户行为上报数据Controller
 * 前端访问路由地址为:/risk/behavior
 *
 * @author euler
 * @date 2022-08-24
 */
@Validated
@Api(value = "后端用户行为上报数据控制器", tags = {"后端用户行为上报数据管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/behavior")
public class BehaviorController extends BaseController {

    @Autowired
    private  IBehaviorService behaviorService;



}
