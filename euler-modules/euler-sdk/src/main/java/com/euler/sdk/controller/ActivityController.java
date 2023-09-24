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
import com.euler.sdk.domain.bo.ActivityBo;
import com.euler.sdk.domain.dto.ActivityDto;
import com.euler.sdk.domain.vo.ActivityVo;
import com.euler.sdk.service.IActivityService;
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
 * 活动Controller
 * 前端访问路由地址为:/sdk/activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Validated
@Api(value = "活动控制器", tags = {"活动管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    private final IActivityService iActivityService;

    /**
     * 查询活动列表
     */
    @ApiOperation("查询活动列表")
    @PostMapping("/list")
    public TableDataInfo<ActivityVo> list(@RequestBody ActivityDto dto) {
        return iActivityService.queryPageList(dto);
    }


    /**
     * 获取活动详细信息
     */
    @ApiOperation("获取活动详细信息")
    @PostMapping("/getInfo")
    public R<ActivityVo> getInfo(@RequestBody IdDto<Integer> idDto) {
        return R.ok(iActivityService.queryById(idDto.getId()));
    }

    /**
     * 新增活动
     */
    @ApiOperation("新增活动")
    @SaCheckPermission("sdk:activity:add")
    @Log(title = "新增活动", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody ActivityBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        return iActivityService.insertByBo(bo);
    }

    /**
     * 修改活动
     */
    @ApiOperation("修改活动")
    @SaCheckPermission("sdk:activity:edit")
    @Log(title = "修改活动", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody ActivityBo bo) {
        return iActivityService.updateByBo(bo);
    }

    /**
     * 删除活动
     */
    @ApiOperation("删除活动")
    @SaCheckPermission("sdk:activity:remove")
    @Log(title = "删除活动", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        String[] strArr = idDto.getId().split(",");
        Integer[] ids = Convert.toIntArray(strArr);
        return toAjax(iActivityService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("sdk:activity:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iActivityService.operation(idNameTypeDicDto, userId);
    }
}
