package com.euler.sdk.controller;

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
import com.euler.sdk.domain.bo.GiftActivityBo;
import com.euler.sdk.domain.dto.GiftActivityDto;
import com.euler.sdk.domain.dto.GiftActivityFrontDto;
import com.euler.sdk.domain.vo.GiftActivityVo;
import com.euler.sdk.service.IGiftActivityService;
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
 * 礼包活动管理Controller
 * 前端访问路由地址为:/sdk/activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "礼包活动管理控制器", tags = {"礼包活动管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/giftActivity")
public class GiftActivityController extends BaseController {

    private final IGiftActivityService iGiftActivityService;

    /**
     * 后端查询礼包活动管理列表
     */
    @ApiOperation("查询礼包活动管理列表")
    @PostMapping("/list")
    public TableDataInfo<GiftActivityVo> list(@RequestBody GiftActivityDto dto) {
        return iGiftActivityService.queryPageList(dto);
    }

    /**
     * 前台查询礼包活动管理列表
     */
    @ApiOperation("查询礼包活动管理列表")
    @PostMapping("/getList")
    public TableDataInfo<GiftActivityVo> getList(@RequestBody GiftActivityFrontDto dto) {
        return iGiftActivityService.queryFrontPageList(dto);
    }

    /**
     * 获取礼包活动管理详细信息
     */
    @ApiOperation("获取礼包活动管理详细信息")
    @PostMapping("/getInfo")
    public R<GiftActivityVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iGiftActivityService.queryById(idDto.getId()));
    }

    /**
     * 新增礼包活动管理
     */
    @ApiOperation("新增礼包活动管理")
    @SaCheckPermission("sdk:giftActivity:add")
    @Log(title = "新增礼包活动管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftActivityBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iGiftActivityService.insertByBo(bo);
    }

    /**
     * 修改礼包活动管理
     */
    @ApiOperation("修改礼包活动管理")
    @SaCheckPermission("sdk:giftActivity:edit")
    @Log(title = "修改礼包活动管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody GiftActivityBo bo) {
        return iGiftActivityService.updateByBo(bo);
    }

    /**
     * 删除礼包活动管理
     */
    @ApiOperation("删除礼包活动管理")
    @SaCheckPermission("sdk:giftActivity:remove")
    @Log(title = "删除礼包活动管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(iGiftActivityService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("sdk:giftActivity:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iGiftActivityService.operation(idNameTypeDicDto, userId);
    }
}
