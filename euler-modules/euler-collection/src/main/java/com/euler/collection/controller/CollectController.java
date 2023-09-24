package com.euler.collection.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.collection.domain.dto.BehaviorDto;
import com.euler.collection.domain.bo.BehaviorBo;
import com.euler.collection.domain.vo.BehaviorVo;
import com.euler.collection.service.IBehaviorService;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Api(value = "收集", tags = {"收集管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/collect")
public class CollectController {

    private final IBehaviorService iBehaviorService;

    /**
     * 查询收集列表
     */
    @ApiOperation("查询收集列表")
    @SaCheckPermission("sdk:behavior:list")
    @PostMapping("/list")
    public TableDataInfo<BehaviorVo> list(@RequestBody BehaviorDto dto) {
        return iBehaviorService.queryPageList(dto);
    }

    /**
     * 信息收集
     */
    @ApiOperation("信息收集")
    @RequestMapping("/collect")
    public R listPage(@RequestBody BehaviorBo bo) {
        log.info("数据上报内容：" + JsonUtils.toJsonString(bo));
        String ipAddress = ServletUtils.getClientIP();
        log.info("通过 request 获取 IP：" + ipAddress + "，前端上传的 IP：" + bo.getIp());
        bo.setClientIp(ipAddress);
        // 将数据进行存储
        return iBehaviorService.insertByBo(bo);
    }
}
