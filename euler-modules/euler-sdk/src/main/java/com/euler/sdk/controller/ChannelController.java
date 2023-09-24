package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.SelectGameDto;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.excel.utils.ExcelUtil;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.domain.ChannelPackageVo;
import com.euler.sdk.domain.bo.ChannelBo;
import com.euler.sdk.domain.bo.ChannelPackageBo;
import com.euler.sdk.domain.dto.ChannelDto;
import com.euler.sdk.domain.dto.ChannelPackageDto;
import com.euler.sdk.domain.vo.ChannelVo;
import com.euler.sdk.enums.GameUseRecordTypeEnum;
import com.euler.sdk.service.IChannelService;
import com.euler.sdk.service.IGameUseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 主渠道Controller
 * 前端访问路由地址为:/system/channel
 *
 * @author euler
 * @date 2022-04-01
 */
@Validated
@Api(value = "主渠道控制器", tags = {"主渠道管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/channel")
public class ChannelController extends BaseController {

    private final IChannelService iChannelService;
    @Autowired
    private IGameUseRecordService iGameUseRecordService;

    /**
     * 查询主渠道列表
     */
    @ApiOperation("查询主渠道列表")
    @SaCheckPermission("sdk:channel:list")
    @PostMapping("/list")
    public TableDataInfo<ChannelVo> list(@RequestBody ChannelDto dto) {
        return iChannelService.queryPageList(dto);
    }

    /**
     * 查询主渠道列表, 不分页
     */
    @ApiOperation("查询主渠道列表")
    @SaCheckPermission("sdk:channel:channelList")
    @PostMapping("/channelList")
    public List<ChannelVo> channelList(@RequestBody ChannelDto dto) {
        return iChannelService.queryList(dto);
    }

    /**
     * 获取主渠道详细信息
     */
    @ApiOperation("获取主渠道详细信息")
    @SaCheckPermission("sdk:channel:query")
    @PostMapping("/getInfo")
    public R<ChannelVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iChannelService.queryById(idDto.getId()));
    }

    /**
     * 渠道对应的游戏列表
     */
    @ApiOperation("渠道对应的游戏列表")
    @SaCheckPermission("sdk:channel:getChannelGameList")
    @Log(title = "渠道对应的游戏列表")
    @PostMapping("/getChannelGameList")
    public TableDataInfoCoreDto<OpenGameDubboVo> getChannelGameList(@RequestBody SelectGameDto dto) {
        dto.setType(GameUseRecordTypeEnum.CHANNEL_ASSOCIATION.getCode());
        return iGameUseRecordService.selectGameNameByParams(dto);
    }

    /**
     * 渠道对应的游戏列表, 不分页
     */
    @ApiOperation("渠道对应的游戏列表, 不分页")
//    @SaCheckPermission("sdk:channel:getGameListByChannel")
    @Log(title = "渠道对应的游戏列表, 不分页")
    @PostMapping("/getGameListByChannel")
    public List<OpenGameDubboVo> getGameListByChannel(@RequestBody SelectGameDto dto) {
        dto.setType(GameUseRecordTypeEnum.CHANNEL_ASSOCIATION.getCode());
        return iGameUseRecordService.selectGameNameByChannel(dto);
    }

    /**
     * 新增主渠道
     */
    @ApiOperation("新增主渠道")
    @SaCheckPermission("sdk:channel:add")
    @Log(title = "主渠道", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody ChannelBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iChannelService.insertByBo(bo);
    }

    /**
     * 修改主渠道
     */
    @ApiOperation("修改主渠道")
    @SaCheckPermission("sdk:channel:edit")
    @Log(title = "主渠道", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody ChannelBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iChannelService.updateByBo(bo);
    }


    /**
     * 渠道开启停用
     */
    @ApiOperation("渠道开启停用")
    @SaCheckPermission("sdk:channel:operation")
    @Log(title = "渠道开启停用", businessType = BusinessType.UPDATE)
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto dto) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iChannelService.operation(dto, userId);
    }


    /**
     * 分包列表
     */
    @ApiOperation("分包列表")
    @SaCheckPermission("sdk:channel:package:list")
    @Log(title = "分包列表", businessType = BusinessType.INSERT)
    @PostMapping("/groupList")
    public TableDataInfo<ChannelPackageVo> groupList(@RequestBody ChannelPackageDto dto) {
        return iChannelService.groupList(dto);
    }

    /**
     * 导出渠道分组列表
     */
    @ApiOperation("导出渠道分组列表")
    @SaCheckPermission("sdk:channel:package:export")
    @Log(title = "渠道分组", businessType = BusinessType.EXPORT)
    @PostMapping("/packageExport")
    public void packageExport(ChannelPackageDto dto, HttpServletResponse response) {
        List<ChannelPackageVo> list = iChannelService.queryExportList(dto);
        ExcelUtil.exportExcel(list, "渠道分组", ChannelPackageVo.class, response);
    }

    /**
     * 创建分包
     */
    @ApiOperation("创建分包")
    @SaCheckPermission("sdk:channel:add:group")
    @Log(title = "创建分包", businessType = BusinessType.INSERT)
    @PostMapping("/addGroup")
    public R addGroup(@Validated(AddGroup.class) @RequestBody ChannelPackageBo bo) {
        return iChannelService.addGroup(bo);
    }

    /**
     * 修正分包
     */
    @ApiOperation("修正分包")
    @SaCheckPermission("sdk:channel:edit:group")
    @Log(title = "修正分包", businessType = BusinessType.UPDATE)
    @PostMapping("/editGroup")
    public R editGroup(@Validated(EditGroup.class) @RequestBody ChannelPackageBo bo) {
        return iChannelService.editGroup(bo);
    }

}
