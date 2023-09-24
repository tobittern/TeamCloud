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
import com.euler.community.domain.bo.BannerGroupBo;
import com.euler.community.domain.dto.BannerGroupDto;
import com.euler.community.domain.vo.BannerGroupVo;
import com.euler.community.service.IBannerGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * banner组Controller
 * 前端访问路由地址为:/system/group
 * @author euler
 * @date 2022-06-06
 */
@Validated
@Api(value = "banner组控制器", tags = {"banner组管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/bannerGroup")
public class BannerGroupController extends BaseController {

    private final IBannerGroupService iBannerGroupService;

    /**
     * 查询banner组列表
     */
    @ApiOperation("查询banner组列表")
    @PostMapping("/list")
    public TableDataInfo<BannerGroupVo> list(@RequestBody BannerGroupDto dto) {
        return iBannerGroupService.queryPageList(dto);
    }

    /**
     * 获取banner组详细信息
     */
    @ApiOperation("获取banner组详细信息")
    @SaCheckPermission("community:bannerGroup:getInfo")
    @PostMapping("/getInfo")
    public R<BannerGroupVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iBannerGroupService.queryById(idDto.getId()));
    }

    /**
     * 新增banner组
     */
    @ApiOperation("新增banner组")
    @SaCheckPermission("community:bannerGroup:add")
    @Log(title = "banner组", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody BannerGroupBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iBannerGroupService.insertByBo(bo) ;
    }

    /**
     * 修改banner组
     */
    @ApiOperation("修改banner组")
    @SaCheckPermission("community:bannerGroup:edit")
    @Log(title = "banner组", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody BannerGroupBo bo) {
        return iBannerGroupService.updateByBo(bo) ;
    }

    /**
     * 删除bannerGroup配置
     */
    @ApiOperation("删除bannerGroup配置")
    @SaCheckPermission("community:bannerGroup:remove")
    @Log(title = "删除bannerGroup配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iBannerGroupService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 操作上下架
     */
    @ApiOperation("操作上下架")
    @SaCheckPermission("community:bannerGroup:operation")
    @Log(title = "上下架")
    @PostMapping("/operation")
    public R operation(@RequestBody IdNameTypeDicDto<Long> idNameTypeDicDto) {
        Long userId = LoginHelper.getUserId();
        return iBannerGroupService.operation(idNameTypeDicDto, userId);
    }
}
