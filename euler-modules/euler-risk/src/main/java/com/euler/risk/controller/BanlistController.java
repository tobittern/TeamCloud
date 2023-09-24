package com.euler.risk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.risk.domain.bo.BanlistBo;
import com.euler.risk.domain.dto.BanlistDto;
import com.euler.risk.domain.dto.TdDeviceMemberPageDto;
import com.euler.risk.domain.vo.BanlistVo;
import com.euler.risk.domain.vo.TdDeviceMemberVo;
import com.euler.risk.service.IBanlistService;
import com.euler.risk.service.ITdDeviceInfoService;
import com.euler.risk.service.ITdDeviceMemberService;
import com.euler.sdk.api.domain.MemberProfileBasics;
import com.euler.sdk.api.domain.dto.SearchBanDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 封号列Controller
 * 前端访问路由地址为:/risk/banlist
 *
 * @author euler
 * @date 2022-08-23
 */
@Validated
@Api(value = "封号列控制器", tags = {"封号列管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/banlist")
public class BanlistController extends BaseController {

    private final IBanlistService banlistService;
    @Autowired
    private ITdDeviceInfoService iTdDeviceInfoService;
    @Autowired
    private ITdDeviceMemberService iTdDeviceMemberService;

    /**
     * 按照指定条件查询用户和设备关联列表
     */
    @ApiOperation("按照指定条件查询用户和设备关联列表")
    @SaCheckPermission("risk:banlist:search")
    @PostMapping("/search")
    public TableDataInfo<MemberProfileBasics> search(@RequestBody SearchBanDto dto) {
        return banlistService.search(dto);
    }

    /**
     * 查询封号列列表
     */
    @ApiOperation("查询封号列列表")
    @SaCheckPermission("risk:banlist:list")
    @PostMapping("/list")
    public TableDataInfo<BanlistVo> list(@RequestBody BanlistDto dto) {
        return banlistService.queryPageList(dto);
    }

    /**
     * 获取封号列详细信息
     */
    @ApiOperation("获取封号列详细信息")
    @SaCheckPermission("risk:banlist:query")
    @PostMapping("/getInfo")
    public R<BanlistVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(banlistService.queryById(idDto.getId()));
    }

    /**
     * 新增封号列
     */
    @ApiOperation("新增封号列")
    @SaCheckPermission("risk:banlist:add")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody BanlistBo bo) {
        // 设置平台
        bo.setPlatform(ServletUtils.getHeader(ServletUtils.getRequest(), "platform"));
        return banlistService.insertByBo(bo);
    }

    /**
     * 修改封号列
     */
    @ApiOperation("修改封号列")
    @SaCheckPermission("risk:banlist:edit")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody BanlistBo bo) {
        return banlistService.updateByBo(bo);
    }


    /**
     * 删除封号列
     */
    @ApiOperation("删除封号列")
    @SaCheckPermission("risk:banlist:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        //主键为其他类型的时候，修改这个数组类型
        Long[] ids = Convert.toLongArray(strArr);
        return toAjax(banlistService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 根据设备ID查询出设备的基础信息
     */
    @ApiOperation("根据设备ID查询出设备的基础信息")
    @SaCheckPermission("risk:banlist:getDeviceInfoById")
    @PostMapping("/getDeviceInfoById")
    public R getDeviceInfoById(@RequestBody TdDeviceMemberPageDto dto) {
        return R.ok(iTdDeviceInfoService.queryById(dto.getDeviceId()));
    }


    /**
     * 根据设备ID查询出设备使用过的用户
     */
    @ApiOperation("根据设备ID查询出设备使用过的用户")
    @SaCheckPermission("risk:banlist:deviceMember")
    @PostMapping("/deviceMember")
    public TableDataInfo<TdDeviceMemberVo> deviceMember(@RequestBody TdDeviceMemberPageDto dto) {
        return iTdDeviceMemberService.queryPageList(dto);
    }

}
