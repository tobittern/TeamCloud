package com.euler.platform.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.validate.QueryGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.idempotent.annotation.RepeatSubmit;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.platform.api.domain.OpenGameVo;
import com.euler.platform.domain.bo.OpenGameAuditRecordBo;
import com.euler.platform.domain.bo.OpenGameBo;
import com.euler.platform.domain.bo.OpenGameVersionHistoryBo;
import com.euler.platform.domain.dto.*;
import com.euler.platform.domain.vo.OpenGameAuditRecordVo;
import com.euler.platform.domain.vo.OpenGameDataCountVo;
import com.euler.platform.domain.vo.OpenGameTransferLogVo;
import com.euler.platform.domain.vo.OpenGameVersionHistoryVo;
import com.euler.platform.service.IOpenGameAuditRecordService;
import com.euler.platform.service.IOpenGameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 开放平台 - 游戏管理
 */
@Api(value = "开放平台 - 游戏管理", tags = {"开放平台 - 游戏管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/gameManagement")
public class GameManagementController extends BaseController {

    @Autowired
    private IOpenGameService iOpenGameService;
    @Autowired
    private IOpenGameAuditRecordService iOpenGameAuditRecordService;

    /**
     * 游戏申请记录数据查询
     */
    @ApiOperation("游戏申请记录数据查询")
    @SaCheckPermission("platform:gameManagement:count")
    @PostMapping("/count")
    public OpenGameDataCountVo count() {
        return iOpenGameService.selectCount();
    }

    /**
     * 查询【游戏管理】列表
     */
    @ApiOperation("查询【游戏管理】列表")
    @SaCheckPermission("platform:gameManagement:list")
    @PostMapping("/list")
    public TableDataInfo<OpenGameVo> list(@RequestBody OpenGamePageDto openGamePageDto) {
        Long userId = LoginHelper.getUserId();
        openGamePageDto.setUserId(userId);
        return iOpenGameService.queryPageList(openGamePageDto);
    }

    /**
     * 获取【游戏管理】详细信息
     */
    @ApiOperation("获取【游戏管理】详细信息")
    @SaCheckPermission("platform:gameManagement:getInfo")
    @PostMapping("/getInfo")
    public R<OpenGameVo> getInfo(@RequestBody IdDto idDto) {
        Long userId = LoginHelper.getUserId();
        return R.ok(iOpenGameService.selectInfo(idDto, userId));
    }

    /**
     * 新增【游戏管理】
     */
    @ApiOperation("新增【游戏管理】")
    @SaCheckPermission("platform:gameManagement:add")
    @Log(title = "【游戏管理】", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody OpenGameBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iOpenGameService.insertByBo(bo);
    }

    /**
     * 修改【游戏管理】
     */
    @ApiOperation("修改【游戏管理】")
    @SaCheckPermission("platform:gameManagement:edit")
    @Log(title = "【游戏管理】", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody OpenGameBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iOpenGameService.updateByBo(bo);
    }

    /**
     * 删除游戏
     */
    @ApiOperation("删除游戏")
    @SaCheckPermission("platform:gameManagement:remove")
    @Log(title = "删除游戏", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] split = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(split);
        return toAjax(iOpenGameService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 游戏管理 撤销审核
     */
    @ApiOperation("撤销审核")
    @SaCheckPermission("platform:gameManagement:revokeApproval")
    @Log(title = "撤销审核", businessType = BusinessType.UPDATE)
    @PostMapping("/revokeApproval")
    public R revokeApproval(@RequestBody IdDto<Integer> idDto) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenGameService.revokeApproval(idDto, userId);
    }

    /**
     * 上架下架操作【游戏管理】
     */
    @ApiOperation("上架下架操作【游戏管理】")
    @SaCheckPermission("platform:gameManagement:operation")
    @Log(title = "【游戏管理】", businessType = BusinessType.UPDATE)
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDTO) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenGameService.operation(idNameTypeDicDTO, userId);
    }

    /**
     * 游戏审核列表
     */
    @ApiOperation("游戏审核列表【游戏管理】")
    @SaCheckPermission("platform:gameManagement:auditGameList")
    @Log(title = "【游戏管理】")
    @PostMapping("/auditGameList")
    public TableDataInfo<OpenGameAuditRecordVo> auditGameList(@RequestBody CommonIdPageDto dto) {
        return iOpenGameAuditRecordService.auditGameList(dto);
    }

    /**
     * 审核游戏【游戏管理】
     */
    @ApiOperation("审核游戏【游戏管理】")
    @SaCheckPermission("platform:gameManagement:auditGame")
    @Log(title = "【游戏管理】", businessType = BusinessType.UPDATE)
    @PostMapping("/auditGame")
    public R auditGame(@Validated(QueryGroup.class) @RequestBody OpenGameAuditRecordBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenGameService.auditGame(bo, userId);
    }


    /**
     * 新增【游戏版本管理】
     */
    @ApiOperation("新增游戏版本管理")
    @SaCheckPermission("platform:gameManagement:version:add")
    @Log(title = "【游戏版本管理】", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/addVersion")
    public R addVersion(@Validated(AddGroup.class) @RequestBody OpenGameVersionHistoryBo bo) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iOpenGameService.addVersion(bo);
    }

    /**
     * 游戏版本管理列表
     */
    @ApiOperation("游戏版本管理列表")
    @SaCheckPermission("platform:gameManagement:version:list")
    @PostMapping("/gameVersionList")
    public TableDataInfo<OpenGameVersionHistoryVo> gameVersionList(@RequestBody OpenGameVersionHistoryDto dto) {
        // 游戏版本历史查询
        return iOpenGameService.gameVersionList(dto);
    }

    /**
     * 游戏版本的上架下架操作
     */
    @ApiOperation("游戏版本的上架下架操作")
    @SaCheckPermission("platform:gameManagement:operation:version")
    @PostMapping("/operationVersion")
    public R operationVersion(@RequestBody IdNameTypeDicDto dto) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenGameService.operationVersion(dto, userId);
    }

    /**
     * 游戏历史版本删除
     */
    @ApiOperation("游戏历史版本删除")
    @Log(title = "游戏历史版本删除", businessType = BusinessType.UPDATE)
    @PostMapping("/removeVersion")
    public R removeVersion(@RequestBody KeyValueDto<String> keyValueDto) {
        return iOpenGameService.removeVersionByIds(keyValueDto);
    }

    /**
     * 审核游戏版本
     */
    @ApiOperation("审核游戏版本")
    @SaCheckPermission("platform:gameManagement:auditVersion")
    @Log(title = "审核游戏版本", businessType = BusinessType.UPDATE)
    @PostMapping("/auditVersion")
    public R auditVersion(@RequestBody IdNameTypeDicDto dto) {
        // 查询设置用户
        Long userId = LoginHelper.getUserId();
        return iOpenGameService.auditVersion(dto, userId);
    }

    /**
     * 获取支付方式
     *
     * @param keyValueDto
     * @return
     */
    @PostMapping(value = "/getGamePayTypeList")
    public R getGamePayTypeList(@RequestBody KeyValueDto<String> keyValueDto) {
        List list = iOpenGameService.getPayTypeData(keyValueDto);
        return R.ok(list);
    }

    /**
     * 游戏拥有权转移
     */
    @ApiOperation("游戏拥有权转移")
    @SaCheckPermission("platform:gameManagement:transfer")
    @PostMapping("/transfer")
    public R transfer(@RequestBody OpenGameTransferDto dto) {
        return iOpenGameService.transfer(dto);
    }

    /**
     * 游戏转移列表查询
     */
    @ApiOperation("游戏转移列表查询")
    @SaCheckPermission("platform:gameManagement:transferList")
    @PostMapping("/transferList")
    public TableDataInfo<OpenGameTransferLogVo> transferList(@RequestBody OpenGameTransferLogDto dto) {
        return iOpenGameService.transferList(dto);
    }

}
