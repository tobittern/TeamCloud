package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.BlacklistBo;
import com.euler.risk.domain.dto.BlacklistDto;
import com.euler.risk.domain.vo.BlacklistVo;
import com.euler.risk.service.IBlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 黑名单Controller
 * 前端访问路由地址为:/risk/blacklist
 *
 * @author euler
 * @date 2022-08-23
 */
@Validated
@Api(value = "黑名单控制器", tags = {"黑名单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/blacklist")
public class BlacklistController extends BaseController {

    private final IBlacklistService blacklistService;

    /**
     * 查询黑名单列表
     */
    @ApiOperation("查询黑名单列表")
    @SaCheckPermission("risk:blacklist:list")
    @PostMapping("/list")
    public TableDataInfo<BlacklistVo> list(@RequestBody BlacklistDto dto) {
        return blacklistService.queryPageList(dto);
    }

    /**
     * 加入黑名单
     */
    @ApiOperation("加入黑名单")
    @SaCheckPermission("risk:blacklist:add")
    @Log(title = "黑名单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody BlacklistBo bo) {
        return blacklistService.insertByBo(bo);
    }

    /**
     * 解除黑名单
     */
    @ApiOperation("解除黑名单")
    @SaCheckPermission("risk:blacklist:remove")
    @Log(title = "黑名单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Integer[] ids = Convert.toIntArray(strArr);
        return blacklistService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

}
