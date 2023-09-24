package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.DiscoverBo;
import com.euler.community.domain.dto.DiscoverDto;
import com.euler.community.domain.vo.DiscoverVo;
import com.euler.community.domain.vo.OpenGameForAppVo;
import com.euler.community.service.IDiscoverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 发现配置Controller
 * 前端访问路由地址为:/community/discover
 *
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "发现配置控制器", tags = {"发现配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/discover")
public class DiscoverController extends BaseController {

    private final IDiscoverService iDiscoverService;

    /**
     * 查询发现配置列表
     */
    @ApiOperation("查询发现配置列表")
    @SaCheckPermission("community:discover:list")
    @PostMapping("/list")
    public TableDataInfo<DiscoverVo> list(@RequestBody DiscoverDto dto) {
        return iDiscoverService.queryPageList(dto);
    }

    /**
     * 获取发现配置详细信息
     */
    @ApiOperation("获取发现配置详细信息")
    @SaCheckPermission("community:discover:getInfo")
    @PostMapping("/getInfo")
    public R<DiscoverVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iDiscoverService.queryById(idDto.getId()));
    }

    /**
     * 前台专用发现配置列表
     */
    @ApiOperation("前台专用发现配置列表")
    @PostMapping("/search")
    public R search(@RequestBody DiscoverDto dto) {
        dto.setStatus("1");
        if (dto.getApplicationSystem() == null) {
            dto.setApplicationSystem("1");
        }
        List<DiscoverVo> list = iDiscoverService.queryList(dto);
        return R.ok(list);
    }

    /**
     * 新增发现配置
     */
    @ApiOperation("新增发现配置")
    @SaCheckPermission("community:discover:add")
    @Log(title = "发现配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody DiscoverBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return (iDiscoverService.insertByBo(bo));
    }

    /**
     * 修改发现配置
     */
    @ApiOperation("修改发现配置")
    @SaCheckPermission("community:discover:edit")
    @Log(title = "发现配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody DiscoverBo bo) {
        return (iDiscoverService.updateByBo(bo));
    }

    /**
     * 删除发现配置
     */
    @ApiOperation("删除发现配置")
    @SaCheckPermission("community:discover:remove")
    @Log(title = "删除发现配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iDiscoverService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("community:discover:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iDiscoverService.operation(idNameTypeDicDto, userId);
    }


    /**
     * app端查询游戏详情
     *
     * @return 游戏列表集合
     */
    @ApiOperation("获取【App游戏】详细信息")
    @PostMapping("/getGameInfo")
    public R<OpenGameForAppVo> getGameInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iDiscoverService.getGameInfo(idDto));
    }


    /**
     * 清空发现页面缓存
     */
    @ApiOperation("清空发现页面缓存")
    @SaCheckPermission("community:discover:clearCache")
    @PostMapping("/clearCache")
    public R clearCache() {
        iDiscoverService.clearCache();
        return R.ok();
    }

    /**
     * 根据游戏名查询出指定游戏列表
     */
    @ApiOperation("根据游戏名查询出指定游戏列表")
    @PostMapping("/getGameListByName")
    public R getGameListByName(@RequestBody DiscoverDto dto) {
        return iDiscoverService.getGameListByName(dto);
    }

}

