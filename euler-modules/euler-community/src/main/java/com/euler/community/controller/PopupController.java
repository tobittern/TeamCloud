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
import com.euler.community.domain.bo.PopupBo;
import com.euler.community.domain.dto.PopupDto;
import com.euler.community.domain.vo.PopupVo;
import com.euler.community.service.IPopupService;
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
 * 弹窗管理Controller
 * 前端访问路由地址为:/community/popup
 *
 * @author euler
 * @date 2022-06-02
 */
@Validated
@Api(value = "弹窗管理控制器", tags = {"弹窗管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/popup")
public class PopupController extends BaseController {

    private final IPopupService iPopupService;

    /**
     * 查询弹窗管理列表
     */
    @ApiOperation("查询弹窗管理列表")
    @SaCheckPermission("community:popup:list")
    @PostMapping("/list")
    public TableDataInfo<PopupVo> list(@RequestBody PopupDto dto) {
        return iPopupService.queryPageList(dto);
    }

    /**
     * 前台查询弹窗管理列表
     */
    @ApiOperation("前台查询弹窗管理列表")
    @PostMapping("/search")
    public R search(@RequestBody PopupDto dto) {
        List<PopupVo> list = iPopupService.queryList(dto);
        return R.ok(list);
    }

    /**
     * 获取弹窗管理详细信息
     */
    @ApiOperation("获取弹窗管理详细信息")
    @SaCheckPermission("community:popup:getInfo")
    @PostMapping("/getInfo")
    public R<PopupVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iPopupService.queryById(idDto.getId()));
    }

    /**
     * 新增弹窗管理
     */
    @ApiOperation("新增弹窗管理")
    @SaCheckPermission("community:popup:add")
    @Log(title = "弹窗管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody PopupBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iPopupService.insertByBo(bo);
    }

    /**
     * 修改弹窗管理
     */
    @ApiOperation("修改弹窗管理")
    @SaCheckPermission("community:popup:edit")
    @Log(title = "弹窗管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody PopupBo bo) {
        return iPopupService.updateByBo(bo);
    }

    /**
     * 删除弹窗管理
     */
    @ApiOperation("删除弹窗管理")
    @SaCheckPermission("community:popup:remove")
    @Log(title = "删除发现配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iPopupService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("community:popup:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iPopupService.operation(idNameTypeDicDto, userId);
    }
}

