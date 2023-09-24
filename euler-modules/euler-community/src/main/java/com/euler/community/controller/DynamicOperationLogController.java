package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicOperationLogDto;
import com.euler.community.domain.vo.DynamicOperationLogVo;
import com.euler.community.service.IDynamicOperationLogService;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态操作错误日志Controller
 * 前端访问路由地址为:/system/log
 *
 * @author euler
 * @date 2022-06-20
 */
@Validated
@Api(value = "动态操作错误日志控制器", tags = {"动态操作错误日志管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/dynamicOperation")
public class DynamicOperationLogController extends BaseController {

    private final IDynamicOperationLogService iDynamicOperationLogService;
    @DubboReference
    private RemoteDictService remoteDictService;

    /**
     * 查询动态操作错误日志列表
     */
    @ApiOperation("查询动态操作错误日志列表")
    @SaCheckPermission("community:dynamicOperation:list")
    @GetMapping("/list")
    public TableDataInfo<DynamicOperationLogVo> list(@RequestBody DynamicOperationLogDto dto) {
        return iDynamicOperationLogService.queryPageList(dto);
    }


    /**
     * 动态操作类型
     *
     * @return
     */
    @ApiOperation("获取钱包菜单")
    @PostMapping(value = "/getDynamicOperationType/{operationType}")
    public R getDynamicOperationType(@PathVariable String operationType) {
        List<SysDictData> data = remoteDictService.selectDictDataByType(operationType);
        List<Map> list = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            data.forEach(a -> {
                Map<String, String> map = new HashMap<>();
                map.put("id", a.getDictLabel());
                map.put("name", a.getDictValue());
                list.add(map);
            });
        }
        return R.ok(list);
    }


}
