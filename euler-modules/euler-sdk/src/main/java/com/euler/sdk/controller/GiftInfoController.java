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
import com.euler.sdk.domain.dto.GiftInfoDelDto;
import com.euler.sdk.domain.dto.GiftInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.sdk.domain.vo.GiftInfoVo;
import com.euler.sdk.domain.bo.GiftInfoBo;
import com.euler.sdk.service.IGiftInfoService;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Arrays;

/**
 * 礼包Controller
 * 前端访问路由地址为:/sdk/giftInfo
 * @author euler
 * @date 2022-03-24
 */
@Validated
@Api(value = "礼包控制器", tags = {"礼包管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/giftInfo")
public class GiftInfoController extends BaseController {

    private final IGiftInfoService iGiftInfoService;

    /**
     * 查询礼包列表
     */
    @ApiOperation("查询礼包列表")
    @PostMapping("/list")
    public TableDataInfo<GiftInfoVo> list(@RequestBody GiftInfoDto dto) {
        return iGiftInfoService.queryPageList(dto);
    }

    /**
     * 获取礼包详细信息
     */
    @ApiOperation("获取礼包详细信息")
    @PostMapping("/getInfo")
    public R<GiftInfoVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iGiftInfoService.queryById(idDto.getId()));
    }

    /**
     * 新增礼包
     */
    @ApiOperation("新增礼包")
    @Log(title = "新增礼包", businessType = BusinessType.INSERT)
    @SaCheckPermission("sdk:giftInfo:add")
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody GiftInfoBo bo) {

        return iGiftInfoService.insertByBo(bo);
    }

    /**
     * 修改礼包
     */
    @ApiOperation("修改礼包")
    @Log(title = "修改礼包", businessType = BusinessType.UPDATE)
    @SaCheckPermission("sdk:giftInfo:edit")
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody GiftInfoBo bo) {
        return iGiftInfoService.updateByBo(bo);
    }

    /**
     * 删除礼包
     */
    @ApiOperation("删除礼包")
    @Log(title = "删除礼包", businessType = BusinessType.DELETE)
    @SaCheckPermission("sdk:giftInfo:remove")
    @PostMapping("/remove")
    public R remove(@RequestBody GiftInfoDelDto dto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = dto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return iGiftInfoService.deleteWithValidByIds(Arrays.asList(ids), true, dto.getGiftGroupId());
    }

}
