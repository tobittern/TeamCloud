package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.bo.BannerBo;
import com.euler.community.domain.dto.BannerDto;
import com.euler.community.domain.vo.BannerVo;
import com.euler.community.service.IBannerService;
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
 * banner列Controller
 * 前端访问路由地址为:/community/list
 *
 * @author euler
 * @date 2022-06-07
 */
@Validated
@Api(value = "banner列控制器", tags = {"banner列管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private final IBannerService iBannerService;

    /**
     * 查询banner列列表
     */
    @ApiOperation("查询banner列列表")
    @SaCheckPermission("community:banner:list")
    @PostMapping("/list")
    public TableDataInfo<BannerVo> list(@RequestBody BannerDto dto) {
        return iBannerService.queryPageList(dto);
    }

    /**
     * 获取banner列详细信息
     */
    @ApiOperation("获取banner列详细信息")
    @SaCheckPermission("community:banner:getInfo")
    @PostMapping("/getInfo")
    public R<BannerVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iBannerService.queryById(idDto.getId()));
    }

    /**
     * 新增banner列
     */
    @ApiOperation("新增banner列")
    @SaCheckPermission("community:banner:add")
    @Log(title = "banner列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody BannerBo bo) {
        Long userId = LoginHelper.getUserId();
        bo.setMemberId(userId);
        return iBannerService.insertByBo(bo);
    }

    /**
     * 修改banner列
     */
    @ApiOperation("修改banner列")
    @SaCheckPermission("community:banner:edit")
    @Log(title = "banner列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody BannerBo bo) {
        return iBannerService.updateByBo(bo);
    }

    /**
     * 删除banner配置
     */
    @ApiOperation("删除banner配置")
    @SaCheckPermission("community:banner:remove")
    @Log(title = "删除banner配置", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iBannerService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

}
