package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.sdk.api.domain.GameConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.euler.sdk.domain.bo.GameConfigBo;
import com.euler.sdk.domain.dto.GameConfigDto;
import com.euler.sdk.service.IGameConfigService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Arrays;

/**
 * 游戏配置Controller
 * 前端访问路由地址为:/sdk/gameConfig
 * @author euler
 * @date 2023-03-23
 */
@Validated
@Api(value = "游戏配置控制器", tags = {"游戏配置管理" })
@RequiredArgsConstructor
@RestController
@RequestMapping("/gameConfig")
public class GameConfigController extends BaseController {

    @Autowired
    private IGameConfigService gameConfigService;

    /**
     * 查询游戏配置列表
     */
    @ApiOperation("查询游戏配置列表")
    @PostMapping("/list")
    public TableDataInfo<GameConfigVo> list(@RequestBody GameConfigDto dto) {
        return gameConfigService.queryPageList(dto);
    }

    /**
     * 获取游戏配置详细信息
     */
    @ApiOperation("获取游戏配置详细信息")
    @PostMapping("/getInfo")
    public R<GameConfigVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(gameConfigService.queryById(idDto.getId()));
    }

    /**
     * 新增游戏配置
     */
    @ApiOperation("新增游戏配置")
    @SaCheckPermission("sdk:gameConfig:add")
    @Log(title = "游戏配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GameConfigBo bo) {
        return gameConfigService.insertByBo(bo);
    }

    /**
     * 修改游戏配置
     */
    @ApiOperation("修改游戏配置")
    @SaCheckPermission("sdk:gameConfig:edit")
    @Log(title = "游戏配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody GameConfigBo bo) {
        return gameConfigService.updateByBo(bo);
    }

    /**
     * 删除游戏配置
     */
    @ApiOperation("删除游戏配置")
    @SaCheckPermission("sdk:gameConfig:remove")
    @Log(title = "游戏配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split("," );
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return gameConfigService.deleteWithValidByIds(Arrays.asList(ids));
    }

}
