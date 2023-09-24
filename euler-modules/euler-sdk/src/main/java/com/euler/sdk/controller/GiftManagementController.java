package com.euler.sdk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.sdk.domain.dto.GiftManagementDetailDto;
import com.euler.sdk.domain.dto.GiftManagementDto;
import com.euler.sdk.domain.vo.GiftInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.domain.vo.GiftManagementVo;
import com.euler.sdk.domain.bo.GiftManagementBo;
import com.euler.sdk.service.IGiftManagementService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Arrays;

/**
 * 礼包组管理Controller
 * 前端访问路由地址为:/sdk/giftManagement
 *
 * @author euler
 * @date 2022-03-22
 */
@Validated
@Api(value = "礼包组管理控制器", tags = {"礼包组管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/giftManagement")
public class GiftManagementController extends BaseController {

    private final IGiftManagementService iGiftManagementService;

    /**
     * 查询礼包组管理列表
     */
    @ApiOperation("查询礼包组管理列表")
    @PostMapping("/list")
    public TableDataInfo<GiftManagementVo> list(@RequestBody GiftManagementDto dto) {
        return iGiftManagementService.queryPageList(dto);
    }

    /**
     * 获取礼包组管理详细信息
     */
    @ApiOperation("获取礼包组管理详细信息")
    @PostMapping("/getInfo")
    public R<GiftManagementVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iGiftManagementService.queryById(idDto.getId()));
    }

    /**
     * 获取礼包组管理详细信息
     */
    @ApiOperation("获取礼包组管理详细信息")
    @PostMapping("/getInfoByCodeKey")
    public R<GiftManagementVo> getInfoByCodeKey() {
        return R.ok(iGiftManagementService.getInfoByCodeKey());
    }

    /**
     * 新增礼包组
     */
    @ApiOperation("新增礼包组")
    @Log(title = "新增礼包组", businessType = BusinessType.INSERT)
    @SaCheckPermission("sdk:giftManagement:add")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftManagementBo bo) {
        return iGiftManagementService.insertByBo(bo);
    }

    /**
     * 修改礼包管理
     */
    @ApiOperation("修改礼包管理")
    @Log(title = "礼包管理", businessType = BusinessType.UPDATE)
    @SaCheckPermission("sdk:giftManagement:edit")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody GiftManagementBo bo) {
        return iGiftManagementService.updateByBo(bo);
    }

    /**
     * 删除礼包组管理的同时，删除对应的礼包详情
     */
    @ApiOperation("删除礼包管理")
    @Log(title = "礼包管理", businessType = BusinessType.DELETE)
    @SaCheckPermission("sdk:giftManagement:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return iGiftManagementService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @Log(title = "上下架")
    @SaCheckPermission("sdk:giftManagement:operation")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iGiftManagementService.operation(idNameTypeDicDto, userId);
    }

    /**
     * 查看礼包组内容详情
     */
    @ApiOperation("查看礼包组内容详情")
    @Log(title = "查看礼包组内容详情")
    @PostMapping("/getGiftContentsList")
    public TableDataInfo<GiftInfoVo> getGiftContentsList(@RequestBody GiftManagementDetailDto dto) {
        if(dto.getGiftGroupId() == null){
            throw new ServiceException("礼包组id的参数缺失");
        }
        return iGiftManagementService.getGiftContentsList(dto);
    }

}
