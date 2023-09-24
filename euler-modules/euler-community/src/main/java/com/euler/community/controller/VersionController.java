package com.euler.community.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.controller.BaseController;
import com.euler.common.log.annotation.Log;
import com.euler.common.log.enums.BusinessType;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.community.domain.bo.VersionBo;
import com.euler.community.domain.dto.VersionDto;
import com.euler.community.domain.dto.VersionPublishDto;
import com.euler.community.domain.entity.Version;
import com.euler.community.domain.vo.VersionVo;
import com.euler.community.service.IVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 版本管理Controller
 * 前端访问路由地址为:/community/version
 *
 * @author euler
 * @date 2022-06-01
 */
@Validated
@Api(value = "版本管理控制器", tags = {"版本管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/version")
public class VersionController extends BaseController {

    private final IVersionService iVersionService;

    /**
     * 查询版本管理列表
     */
    @ApiOperation("查询版本管理列表")
    @SaCheckPermission("community:version:list")
    @PostMapping("/list")
    public TableDataInfo<VersionVo> list(@RequestBody VersionDto dto) {

        return iVersionService.queryPageList(dto);
    }

    /**
     * 查看版本详情
     */
    @ApiOperation("查看版本详情")
    @SaCheckPermission("community:version:getInfo")
    @PostMapping("/getInfo")
    public R<VersionVo> getInfo(@RequestBody IdDto<Long> idDto) {
        return R.ok(iVersionService.queryById(idDto.getId()));
    }

    /**
     * 新增版本管理
     */
    @ApiOperation("新增版本管理")
    @SaCheckPermission("community:version:add")
    @Log(title = "版本管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R add(@Validated(AddGroup.class) @RequestBody VersionBo bo) {
        return iVersionService.insertByBo(bo);
    }

    /**
     * 修改版本管理
     */
    @ApiOperation("修改版本管理")
    @SaCheckPermission("community:version:edit")
    @Log(title = "版本管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public R edit(@Validated(EditGroup.class) @RequestBody VersionBo bo) {

        return iVersionService.updateByBo(bo);
    }

    /**
     * 发布版本
     */
    @ApiOperation("发布版本")
    @Log(title = "版本管理")
    @SaCheckPermission("community:version:publish")
    @PostMapping("/publish")
    public R publish(@RequestBody VersionPublishDto dto) {
        return iVersionService.publish(dto);
    }

    /**
     * 删除版本管理
     */
    @ApiOperation("删除版本管理")
    @SaCheckPermission("community:version:remove")
    @Log(title = "版本管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public R remove(@RequestBody IdDto<String> idDto) {
        // 判断传输过来的数据是否包含多个
        String[] strArr = idDto.getId().split(",");
        Long[] ids = Convert.toLongArray(strArr);
        return iVersionService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

    /**
     * 判断用户是否需要更新版本
     */
    @ApiOperation("用户是否需要更新版本")
    @PostMapping("/isUpdate")
    public R isUpdate(@RequestBody VersionDto dto) {
        return iVersionService.isUpdate(dto);
    }


    /**
     * 获取当前版本用户
     */
    @ApiOperation("用户是否需要更新版本")
    @PostMapping("/getCurrentVersion")
    public R<String> getCurrentVersion() {
        VersionDto dto = new VersionDto();
        dto.setVersionNo(ServletUtils.getHeader(ServletUtils.getRequest(), "version"));
        dto.setSystemType(ServletUtils.getHeader(ServletUtils.getRequest(), "device"));
        String key = StringUtils.format("{}app_version:{}:{}", Constants.BASE_KEY, dto.getSystemType(), dto.getVersionNo());
        Version version = RedisUtils.getCacheObject(key);
        if (version == null) {
            var res = iVersionService.getCurrentVersion(dto);
            if (res != null) {
                RedisUtils.setCacheObject(key, res);
                return R.ok("", res.getDiscoverSwitch());
            }
            return R.ok("","0");

        }

        return R.ok("", version.getDiscoverSwitch());

    }

}
